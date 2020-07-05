package org.abubusoft.reversi.server.services;

import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Player;
import org.abubusoft.reversi.server.exceptions.AppPlayerTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class NetworkUserInputReader implements UserInputReader {
  Logger logger = LoggerFactory.getLogger(NetworkUserInputReader.class);

  private final BlockingQueue<Pair<Player, Coordinates>> movesQueue;

  private final int turnTimeout;

  public NetworkUserInputReader(BlockingQueue<Pair<Player, Coordinates>> movesQueue, int turnTimeout) {
    this.movesQueue = movesQueue;
    this.turnTimeout = turnTimeout;
  }

  @Override
  public Coordinates readInputFor(Player player, List<Coordinates> list) {
    Pair<Player, Coordinates> move;

    try {
      logger.info("Waiting for {} input", player.getPiece());
      move = movesQueue.poll(turnTimeout * 2, TimeUnit.SECONDS);
      logger.info("Received {} input", player.getPiece());
    } catch (InterruptedException e) {
      logger.warn("Timeout during {} input", player.getPiece());
      e.printStackTrace();
      throw new AppPlayerTimeoutException(player, "timeout");
    }
    if (move == null) {
      logger.warn("No move for {} input", player.getPiece());
      throw new AppPlayerTimeoutException(player, "timeout");
    }
    return move.getSecond();
  }
}
