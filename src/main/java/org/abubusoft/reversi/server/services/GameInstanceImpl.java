package org.abubusoft.reversi.server.services;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Reversi;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.model.*;
import org.abubusoft.reversi.server.events.MatchStatusEvent;
import org.abubusoft.reversi.server.exceptions.AppPlayerTimeoutException;
import org.abubusoft.reversi.server.model.AbstractBaseEntity;
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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameInstanceImpl extends AbstractBaseEntity implements GameInstance, GameRenderer {
  private final NetworkPlayer1 player1;
  private final NetworkPlayer2 player2;
  private static final Logger logger = LoggerFactory.getLogger(GameInstanceImpl.class);

  BlockingQueue<Pair<Player, Coordinates>> movesQueue = new LinkedBlockingQueue<>();

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

  public GameInstanceImpl(NetworkPlayer1 player1, NetworkPlayer2 player2) {
    super(null);
    this.player1 = player1;
    this.player2 = player2;
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
    NetworkGameLogicImpl gameLogic = new NetworkGameLogicImpl(player1, player2, inputReader);
    reversi = new Reversi(this, gameLogic);

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
