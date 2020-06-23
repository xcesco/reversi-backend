package org.abubusoft.reversi.server.services;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Reversi;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.model.*;
import org.abubusoft.reversi.server.events.MatchEndEvent;
import org.abubusoft.reversi.server.events.MatchStartEvent;
import org.abubusoft.reversi.server.events.MatchStatusEvent;
import org.abubusoft.reversi.server.exceptions.AppPlayerTimeoutException;
import org.abubusoft.reversi.server.model.MatchStatus;
import org.abubusoft.reversi.server.model.NetworkPlayer1;
import org.abubusoft.reversi.server.model.NetworkPlayer2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class MatchServiceImpl implements MatchService, GameRenderer {
  private final UUID id = UUID.randomUUID();
  private final NetworkPlayer1 player1;
  private final NetworkPlayer2 player2;
  private static final Logger logger = LoggerFactory.getLogger(MatchServiceImpl.class);

  private final BlockingQueue<Pair<Player, Coordinates>> movesQueue;

  @Autowired
  public void setUserInputReaderProvider(ObjectProvider<UserInputReader> userInputReaderProvider) {
    this.userInputReaderProvider = userInputReaderProvider;
  }

  private ObjectProvider<UserInputReader> userInputReaderProvider;

  @Value("${game.turn.timeout}")
  private int turnTimeout;
  private Reversi reversi;

  @Autowired
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  private ApplicationEventPublisher applicationEventPublisher;

  private GameSnapshot gameSnapshot;

  public MatchServiceImpl(NetworkPlayer1 player1, NetworkPlayer2 player2, BlockingQueue<Pair<Player, Coordinates>> movesQueue) {
    this.player1 = player1;
    this.player2 = player2;
    this.movesQueue = movesQueue;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public void updateStatus(Player player, Coordinates coordinates) {
    if (isValidMove(player, coordinates)) {
      movesQueue.add(Pair.of(player, coordinates));
    } else {
      logger.warn("Game {} ignored {} move from player {}", this.getId(), coordinates, player.getPiece());
    }
  }

  @Override
  public void play() {
    UserInputReader inputReader = userInputReaderProvider.getObject(movesQueue, turnTimeout);
    GameLogic gameLogic = new GameLogicImpl(player1, player2, inputReader);
    reversi = new Reversi(this, gameLogic);

    applicationEventPublisher.publishEvent(new MatchStartEvent(getId(), player1.getUserId(), player2.getUserId()));
    try {
      reversi.play();
    } catch (AppPlayerTimeoutException e) {
      GameSnapshot timeoutGameSnapshot = new GameSnapshot(gameSnapshot.getScore(),
              gameSnapshot.getLastMove(),
              gameSnapshot.getActivePiece(),
              gameSnapshot.getAvailableMoves(),
              gameSnapshot.getBoard(),
              e.getPlayer().getPiece() == Piece.PLAYER_1 ? GameStatus.PLAYER2_WIN : GameStatus.PLAYER1_WIN);

      applicationEventPublisher.publishEvent(new MatchStatusEvent(player1, player2, MatchStatus.of(getId(), timeoutGameSnapshot)));
    } finally {
      applicationEventPublisher.publishEvent(new MatchEndEvent(getId(), player1.getUserId(), player2.getUserId()));
    }
  }

  private boolean isValidMove(Player player, Coordinates coordinates) {
    return gameSnapshot.getActivePiece() == player.getPiece() &&
            gameSnapshot.getAvailableMoves().getMovesActivePlayer().contains(coordinates);
  }

  @Override
  public void render(GameSnapshot gameSnapshot) {
    this.gameSnapshot = gameSnapshot;
    applicationEventPublisher.publishEvent(new MatchStatusEvent(player1, player2, MatchStatus.of(getId(), gameSnapshot)));
  }
}
