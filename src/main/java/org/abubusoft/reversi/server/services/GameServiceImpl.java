package org.abubusoft.reversi.server.services;

import org.abubusoft.reversi.server.events.MatchStatusEvent;
import org.abubusoft.reversi.server.model.*;
import org.abubusoft.reversi.server.repositories.MatchStatusRepository;
import org.abubusoft.reversi.server.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;

import static org.abubusoft.reversi.server.ReversiServerApplication.GAME_EXECUTOR;
import static org.abubusoft.reversi.server.WebSocketConfig.TOPIC_PREFIX;

@Component
public class GameServiceImpl implements GameService {
  private final static Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

  private final UserRepository userRepository;
  private final MatchStatusRepository matchStatusRepository;
  private final SimpMessageSendingOperations messagingTemplate;
  private final ObjectProvider<GameInstance> gameInstanceProvider;


  public GameServiceImpl(UserRepository userRepository,
                         MatchStatusRepository matchStatusRepository,
                         @Qualifier(GAME_EXECUTOR) Executor gamesExecutor,
                         ObjectProvider<GameInstance> gameInstanceProvider,
                         SimpMessageSendingOperations messagingTemplate) {
    this.userRepository = userRepository;
    this.matchStatusRepository = matchStatusRepository;
    this.gamesExecutor = gamesExecutor;
    this.messagingTemplate = messagingTemplate;
    this.gameInstanceProvider = gameInstanceProvider;
  }

  private final Executor gamesExecutor;

  public GameInstance playMatch(NetworkPlayer1 player1, NetworkPlayer2 player2) {
    GameInstance instance = gameInstanceProvider.getObject(player1, player2);
    gamesExecutor.execute(instance::play);

    return instance;
  }

  @EventListener
  @Transactional
  public void onGameStatusChanges(MatchStatusEvent event) {
    logger.info("onGameStatusChanges {}", event.getMatchStatus());

    MatchStatus matchStatus=matchStatusRepository.save(event.getMatchStatus());
    UUID matchId = matchStatus.getId();

    User user1 = userRepository.findById(event.getPlayer1().getUserId()).orElse(null);
    User user2 = userRepository.findById(event.getPlayer2().getUserId()).orElse(null);

    if (user1 != null) {
      user1.setStatus(UserStatus.IN_GAME);
      user1.setMatchStatus(matchStatus);
      userRepository.save(user1);
    }

    if (user2 != null) {
      user2.setStatus(UserStatus.IN_GAME);
      user2.setMatchStatus(matchStatus);
      userRepository.save(user2);
    }

    String topic = TOPIC_PREFIX + "/match/" + matchId;
    logger.info("Send message to {}", topic);
    messagingTemplate.convertAndSend(topic, matchStatus.getSnapshot());
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
