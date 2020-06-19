package org.abubusoft.reversi.server.model;

import it.fmt.games.reversi.*;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Player;
import org.abubusoft.reversi.server.model.events.GameStatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameInstanceImpl extends AbstractBaseEntity implements GameInstance, GameRenderer {
  Logger logger = LoggerFactory.getLogger(GameInstanceImpl.class);
  Reversi reversi;

  BlockingQueue<Pair<Player, Coordinates>> movesQueue = new LinkedBlockingQueue<>();
  BlockingQueue<Pair<UUID, GameSnapshot>> gamesQueue = new LinkedBlockingQueue<>();

  @Value("${game.turn.timeout}")
  private int turnTimeout;

  @Autowired
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  private ApplicationEventPublisher applicationEventPublisher;

  private GameSnapshot gameSnapshot;

  public GameInstanceImpl(Player1 player1, Player2 player2) {
    super(null);
    NetworkRenderer renderer = new NetworkRenderer();
    UserInputReader inputReader = new NetworkUserInputReader(movesQueue, turnTimeout);
    NetworkGameLogicImpl gameLogic = new NetworkGameLogicImpl(player1, player2, inputReader);
    reversi = new Reversi(renderer, gameLogic);
  }

  public void setTurnTimeout(int turnTimeout) {
    this.turnTimeout = turnTimeout;
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
    reversi.play();
  }

  private boolean isValidMove(Player player, Coordinates coordinates) {
    return gameSnapshot.getActivePiece() == player.getPiece() &&
            gameSnapshot.getAvailableMoves().getMovesActivePlayer().contains(coordinates);
  }

  @Override
  public void render(GameSnapshot gameSnapshot) {
    this.gameSnapshot = gameSnapshot;
    //this.gamesQueue.add(Pair.of(getId(), gameSnapshot));
    applicationEventPublisher.publishEvent(GameStatusEvent.of(getId(), gameSnapshot));
  }
}
