package org.abubusoft.reversi.server.services;

import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Piece;
import org.abubusoft.reversi.server.events.MatchEndEvent;
import org.abubusoft.reversi.server.events.MatchMoveEvent;
import org.abubusoft.reversi.server.events.MatchStartEvent;
import org.abubusoft.reversi.server.events.MatchStatusEvent;
import org.abubusoft.reversi.server.messages.MatchEndMessage;
import org.abubusoft.reversi.server.messages.MatchMove;
import org.abubusoft.reversi.server.messages.MatchStartMessage;
import org.abubusoft.reversi.server.model.*;
import org.abubusoft.reversi.server.repositories.MatchStatusRepository;
import org.abubusoft.reversi.server.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import static org.abubusoft.reversi.server.ReversiServerApplication.MATCH_EXECUTOR;
import static org.abubusoft.reversi.server.WebSocketConfig.TOPIC_PREFIX;

@Component
public class GameServiceImpl implements GameService {
  private final static Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);
  public static final String HEADER_TYPE = "type";

  private final UserRepository userRepository;
  private final MatchStatusRepository matchStatusRepository;
  private final SimpMessageSendingOperations messagingTemplate;
  private final ObjectProvider<MatchService> gameInstanceProvider;
  private final Map<UUID, BlockingQueue<Pair<Piece, Coordinates>>> matchMovesQueues;


  public GameServiceImpl(UserRepository userRepository,
                         MatchStatusRepository matchStatusRepository,
                         @Qualifier(MATCH_EXECUTOR) Executor matchExecutor,
                         ObjectProvider<MatchService> gameInstanceProvider,
                         SimpMessageSendingOperations messagingTemplate) {
    this.userRepository = userRepository;
    this.matchStatusRepository = matchStatusRepository;
    this.matchExecutor = matchExecutor;
    this.messagingTemplate = messagingTemplate;
    this.gameInstanceProvider = gameInstanceProvider;
    this.matchMovesQueues = new ConcurrentHashMap<>();
  }

  private final Executor matchExecutor;

  @Transactional
  public void playMatch(NetworkPlayer1 player1, NetworkPlayer2 player2) {
    User user1 = userRepository.findById(player1.getUserId()).orElse(null);
    User user2 = userRepository.findById(player2.getUserId()).orElse(null);

    if (user1 != null && user2 != null) {
      BlockingQueue<Pair<Piece, Coordinates>> moveQueue = new LinkedBlockingQueue<>();
      MatchService instance = gameInstanceProvider.getObject(player1, player2, moveQueue);
      matchMovesQueues.put(instance.getId(), moveQueue);

      user1.setStatus(UserStatus.IN_GAME);
      user2.setStatus(UserStatus.IN_GAME);
      userRepository.save(user1);
      userRepository.save(user2);
      MatchStatus matchStatus = matchStatusRepository.save(MatchStatus.of(instance.getId(), null));
      matchStatus.getUsers().add(user1);
      matchStatus.getUsers().add(user2);
      matchStatusRepository.save(matchStatus);

      matchExecutor.execute(instance::play);
    }

  }

  @EventListener
  @Transactional
  public void onMatchStart(MatchStartEvent event) {
    logger.info("onMatchStart {}", event.getMatchUUID());

    sendToUser(event.getPlayer1UUID(), new MatchStartMessage(event.getMatchUUID(), Piece.PLAYER_1));
    sendToUser(event.getPlayer2UUID(), new MatchStartMessage(event.getMatchUUID(), Piece.PLAYER_2));
  }

  @EventListener
  @Transactional
  public void onMatchEnd(MatchEndEvent event) {
    logger.info("MatchEndEvent {}", event.getMatchUUID());

    sendToUser(event.getPlayer1UUID(), new MatchEndMessage(event.getMatchUUID(), Piece.PLAYER_1));
    sendToUser(event.getPlayer2UUID(), new MatchEndMessage(event.getMatchUUID(), Piece.PLAYER_2));
  }

  @EventListener
  @Transactional
  public void onPlayerMove(MatchMoveEvent event) {
    MatchMove move = event.getMove();
    logger.info("On match {}, player {} moves {}", move.getMatchUUID(), move.getPlayerPiece(), move.getMove());
    if (matchMovesQueues.containsKey(move.getMatchUUID())) {
      matchMovesQueues.get(move.getMatchUUID()).add(Pair.of(move.getPlayerPiece(), move.getMove()));
    } else {
      logger.warn("no match found with id={}", move.getMatchUUID());
    }
  }

  @EventListener
  @Transactional
  public void onMatchStatusChanges(MatchStatusEvent event) {
    logger.info("onMatchStatusChanges {}", event.getMatchStatus());

    // update snapshot
    MatchStatus matchStatus = matchStatusRepository.findById(event.getMatchStatus().getId()).orElse(event.getMatchStatus());
    matchStatus.setSnapshot(event.getMatchStatus().getSnapshot());
    matchStatus = matchStatusRepository.save(matchStatus);

    GameSnapshot snapshot = event.getMatchStatus().getSnapshot();

    //UUID currentPlayer = event.getMatchStatus().getSnapshot().getActivePiece()
    sendToUser(event.getPlayer1().getUserId(), matchStatus.getSnapshot());
    sendToUser(event.getPlayer2().getUserId(), matchStatus.getSnapshot());
  }

  private void sendToUser(UUID userUUID, Object message) {
    Map<String, Object> headers = new HashMap<>();
    headers.put(HEADER_TYPE, message.getClass().getSimpleName());
    String userTopic = TOPIC_PREFIX + "/user/" + userUUID;
    logger.info("Send message to {}", userTopic);
    messagingTemplate.convertAndSend(userTopic, message, headers);
  }

  @Override
  public Iterable<MatchStatus> findAllUMatchStatus() {
    return matchStatusRepository.findAll();
  }

  @Override
  public User saveUser(UserRegistration userRegistration) {
    User user = new User();
    user.setName(userRegistration.getName());
    user.setStatus(UserStatus.NOT_READY_TO_PLAY);
    return userRepository.save(user);
  }

  @Override
  public Iterable<User> findAllUsers() {
    return userRepository.findAll();
  }

  private User updateUserStatus(UUID userUUID, UserStatus updatedStatus) {
    User user = userRepository.findById(userUUID).orElse(null);
    if (user != null) {
      user.setStatus(updatedStatus);
      userRepository.save(user);
    }
    return user;
  }

  @Override
  public MatchStatus findMatchByUserId(UUID userId) {
    User user = userRepository.findById(userId).orElse(null);

    return user != null ? user.getMatchStatus() : null;
  }

  @Override
  @Transactional
  public User readyToPlay(UUID userUUID) {
    User user = userRepository.findById(userUUID).orElse(null);
    User otherUser = userRepository.findByStatus(UserStatus.AWAITNG_TO_START).stream()
            .filter(other -> !other.getId().equals(userUUID))
            .findFirst().orElse(null);

    if (user != null && user.getStatus() == UserStatus.NOT_READY_TO_PLAY) {
      updateUserStatus(userUUID, UserStatus.AWAITNG_TO_START);

      if (otherUser != null) {
        // already set
        //updateUserStatus(otherUser.getId(), UserStatus.AWAITNG_TO_START);

        playMatch(new NetworkPlayer1(user.getId()),
                new NetworkPlayer2(otherUser.getId()));
      }
    }

    return user;
  }

  @Override
  public User stopPlaying(UUID userUUID) {
    return updateUserStatus(userUUID, UserStatus.NOT_READY_TO_PLAY);
  }


}
