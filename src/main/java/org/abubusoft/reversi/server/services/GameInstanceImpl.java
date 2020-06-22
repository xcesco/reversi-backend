package org.abubusoft.reversi.server.services;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Reversi;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Player;
import org.abubusoft.reversi.server.events.MatchStatusEvent;
import org.abubusoft.reversi.server.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  @Value("${game.turn.timeout}")
  private int turnTimeout;

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
    UserInputReader inputReader = new NetworkUserInputReader(movesQueue, turnTimeout);
    NetworkGameLogicImpl gameLogic = new NetworkGameLogicImpl(player1, player2, inputReader);
    Reversi reversi = new Reversi(this, gameLogic);
    reversi.play();
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
