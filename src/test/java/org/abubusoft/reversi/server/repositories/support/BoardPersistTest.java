package org.abubusoft.reversi.server.repositories.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.fmt.games.reversi.model.Board;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;
import org.abubusoft.reversi.server.JSONMapperFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardPersistTest {
  Logger logger = LoggerFactory.getLogger(BoardPersistTest.class);


  @Test
  public void testPersist() throws JsonProcessingException {
    ObjectMapper objectMapper = JSONMapperFactory.createMapper();
    Board boardSource = new Board();
    boardSource.setCell(Coordinates.of("a1"), Piece.PLAYER_2);
    boardSource.setCell(Coordinates.of("a2"), Piece.PLAYER_1);
    boardSource.setCell(Coordinates.of("b4"), Piece.PLAYER_1);
    boardSource.setCell(Coordinates.of("h8"), Piece.PLAYER_2);
    String value = objectMapper.writeValueAsString(boardSource);

    Board boardDest = objectMapper.readValue(value, Board.class);

    logger.info(value);
    logger.info(objectMapper.writeValueAsString(boardDest));

    Assertions.assertEquals(boardSource, boardDest);


  }
}
