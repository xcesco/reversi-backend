package org.abubusoft.reversi.server.model;

import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameLogicImpl;
import it.fmt.games.reversi.model.Player;

import java.util.List;

public class NetworkGameLogicImpl extends GameLogicImpl {
  public NetworkGameLogicImpl(Player player1, Player player2, UserInputReader userInputReader) {
    super(player1, player2, userInputReader);
  }

  @Override
  public Coordinates readActivePlayerMove(List<Coordinates> availableMoves) {

    return super.readActivePlayerMove(availableMoves);
  }
}
