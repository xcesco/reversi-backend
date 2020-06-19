package org.abubusoft.reversi.server.model;

import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Player;
import org.abubusoft.reversi.server.exceptions.AppAssert;
import org.abubusoft.reversi.server.exceptions.TimeoutException;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class NetworkUserInputReader implements UserInputReader {

  private final BlockingQueue<Pair<Player, Coordinates>> movesQueue;

  private final int turnTimeout;

  public NetworkUserInputReader(BlockingQueue<Pair<Player, Coordinates>> movesQueue, int turnTimeout) {
    this.movesQueue = movesQueue;
    this.turnTimeout = turnTimeout;
  }

  @Override
  public Coordinates readInputFor(Player player, List<Coordinates> list) {
    Pair<Player, Coordinates> move = null;
    try {
      move = movesQueue.poll(turnTimeout * 2, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      AppAssert.fail("timeout");
    }

    AppAssert.assertTrue(move != null, TimeoutException.class, "timeout");

    return move.getSecond();
  }
}
