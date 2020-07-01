package org.abubusoft.reversi.server.model;

import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.cpu.RandomDecisionHandler;

import java.util.List;

public class ServerRandomDecisionHandler extends RandomDecisionHandler {
  final int waitTime;

  public ServerRandomDecisionHandler(int waitTime) {
    this.waitTime = waitTime;
  }

  @Override
  public Coordinates compute(List<Coordinates> availableMoves) {
    try {
      Thread.sleep(waitTime * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return super.compute(availableMoves);
  }
}
