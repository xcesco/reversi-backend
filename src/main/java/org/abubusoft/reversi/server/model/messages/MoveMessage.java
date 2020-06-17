package org.abubusoft.reversi.server.model.messages;

import it.fmt.games.reversi.model.Coordinates;

import java.util.UUID;

public class MoveMessage {
  Coordinates coordinates;
  UUID userUID;

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }
}
