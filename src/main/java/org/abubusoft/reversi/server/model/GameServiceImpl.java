package org.abubusoft.reversi.server.model;

import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import org.abubusoft.reversi.server.ReversiServerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

@Component
public class GameServiceImpl implements GameService {
  private final Map<UUID, GameInstanceImpl> instances;

  private Executor gamesExecutor;

  public GameServiceImpl() {
    instances = new HashMap<>();
  }

  @Qualifier(ReversiServerApplication.GAME_EXECUTOR)
  @Autowired
  public void setGamesExecutor(Executor gamesExecutor) {
    this.gamesExecutor = gamesExecutor;
  }

  @Override
  public GameInstance createMatch(Player1 player1, Player2 player2) {
    GameInstanceImpl instance = new GameInstanceImpl(player1, player2);
    instances.put(instance.getId(), instance);

    gamesExecutor.execute(instance::play);

    return instance;
  }

  @Override
  public GameInstance findMatchById(UUID gameUUID) {
    return instances.get(gameUUID);
  }

  @Override
  public void registryUser(String uuid) {

  }
}
