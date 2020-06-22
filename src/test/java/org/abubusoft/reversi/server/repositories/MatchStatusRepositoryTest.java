package org.abubusoft.reversi.server.repositories;

import it.fmt.games.reversi.PlayerFactory;
import it.fmt.games.reversi.Reversi;
import it.fmt.games.reversi.model.GameSnapshot;
import org.abubusoft.reversi.server.model.MatchStatus;
import org.abubusoft.reversi.server.model.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class MatchStatusRepositoryTest {
  private static Logger logger = LoggerFactory.getLogger(MatchStatusRepositoryTest.class);

  @Autowired
  public void setMatchStatusRepository(MatchStatusRepository matchStatusRepository) {
    this.matchStatusRepository = matchStatusRepository;
  }

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  MatchStatusRepository matchStatusRepository;
  UserRepository userRepository;

  @Test
  public void testBuild() {
    User user1 = new User();
    user1.setName("prova");

    userRepository.save(user1);

    MatchStatus matchStatus = new MatchStatus();

    Reversi reversi = new Reversi(gameSnapshot -> {

    }, (player, list) -> null, PlayerFactory.createCpuPlayer1(), PlayerFactory.createCpuPlayer2());
    GameSnapshot gameSnapshot = reversi.play();
    matchStatus.setSnapshot(gameSnapshot);

    matchStatusRepository.save(matchStatus);

    user1.setMatchStatus(matchStatus);
    userRepository.save(user1);
  }
}