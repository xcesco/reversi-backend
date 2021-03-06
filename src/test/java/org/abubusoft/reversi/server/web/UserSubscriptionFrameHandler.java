package org.abubusoft.reversi.server.web;

import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Piece;
import org.abubusoft.reversi.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.UUID;

import static org.abubusoft.reversi.server.services.GameServiceImpl.HEADER_TYPE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserSubscriptionFrameHandler implements StompFrameHandler {
  private final UUID userId;
  private final String userTopicUrl;
  private final StompSender stompSender;
  private final Piece userPiece;
  private UUID matchId;
  private static final Logger logger = LoggerFactory.getLogger(UserSubscriptionFrameHandler.class);

  public GameStatus getResult() {
    return result;
  }

  private GameStatus result;

  public UserSubscriptionFrameHandler(UUID userId, Piece userPiece, StompSender stompSender) {
    this.userTopicUrl = StompSender.buildUserTopic(userId);
    this.userId = userId;
    this.userPiece=userPiece;
    this.stompSender=stompSender;
  }

  @Override
  public Type getPayloadType(StompHeaders stompHeaders) {
    MatchMessageType messageType = MatchMessageType.valueOf(stompHeaders.getFirst(HEADER_TYPE));
    return messageType.getType();
  }

  @Override
  public void handleFrame(StompHeaders stompHeaders, Object o) {
    String destination = stompHeaders.getDestination();

    MatchMessageType messageType = MatchMessageType.valueOf(stompHeaders.getFirst(HEADER_TYPE));

    switch (messageType) {
      case MATCH_START:
        MatchStartMessage matchStart = (MatchStartMessage) o;
        if (userTopicUrl.equals(destination)) {
          matchId = matchStart.getMatchId();
          assertNotNull(matchStart);
        }

      break;
      case MATCH_STATUS:
        GameSnapshot gameSnapshot = ((MatchStatusMessage) o).getGameSnapshot();
        if (userTopicUrl.equals(destination) && gameSnapshot.getActivePiece() == userPiece && gameSnapshot.getAvailableMoves().getMovesActivePlayer().size() > 0) {
          logger.debug("user {} can moves: {}", gameSnapshot.getActivePiece(), gameSnapshot.getAvailableMoves().getMovesActivePlayer());
          assertNotNull(gameSnapshot);
          Coordinates move = gameSnapshot.getAvailableMoves().getMovesActivePlayer().get(0);
          logger.debug("user {} decides to move on {}", gameSnapshot.getActivePiece(), move);
          stompSender.sendMatchMove(userId, userPiece, matchId, move);
        } else if (gameSnapshot.getStatus()!= GameStatus.RUNNING) {
          logger.debug("player {} detects match has status {}, {} - {}",
                  userPiece, gameSnapshot.getStatus(),
                  gameSnapshot.getScore().getPlayer1Score(),
                  gameSnapshot.getScore().getPlayer2Score()
          );
          result = gameSnapshot.getStatus();
        }
      break;
      case MATCH_END:
        MatchEndMessage matchEnd = (MatchEndMessage) o;
        assertNotNull(matchEnd);
        logger.debug("player {} receives match end", userPiece);
        matchId =null;
      break;
    }
  }
}