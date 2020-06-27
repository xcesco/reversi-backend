package org.abubusoft.reversi.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Player;
import org.abubusoft.reversi.server.model.NetworkPlayer1;
import org.abubusoft.reversi.server.model.NetworkPlayer2;
import org.abubusoft.reversi.server.services.MatchService;
import org.abubusoft.reversi.server.services.MatchServiceImpl;
import org.abubusoft.reversi.server.services.NetworkUserInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.BlockingQueue;

@EnableScheduling
@SpringBootApplication
public class ReversiServerApplication {
  final static Logger logger = LoggerFactory.getLogger(ReversiServerApplication.class);

  public static final String MATCH_EXECUTOR = "gameExecutor";
  private static final String COMPUTE_THREAD_PREFIX = "queue-";

  public static void main(String[] args) {
    SpringApplication.run(ReversiServerApplication.class, args);
  }

//  @Bean
//  CommandLineRunner init(GameRepository gameRepository) {
//    return args -> {
//      Stream.of("John", "Julie", "Jennifer", "Helen", "Rachel").forEach(name -> {
//        GameStatus user = GameStatus.of(name, name + "@domain.com");
//        gameRepository.save(user);
//      });
//      gameRepository.findAll().forEach(System.out::println);
//    };
//  }

  @Bean
  public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion) {
    return new OpenAPI().info(new Info().title("Foobar API")
            .version(appVersion)
            .description("This is a sample Foobar server created using springdocs - a library for OpenAPI 3 with spring boot.")
            .termsOfService("http://swagger.io/terms/")
            .license(new License().name("Apache 2.0")
                    .url("http://springdoc.org")));
  }

  @Bean
  public ObjectMapper objectMapper() {
//    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
//    builder.serializationInclusion(JsonInclude.Include.NON_NULL);
//    builder.serializationInclusion(JsonInclude);

    return JSONMapperFactory.createMapper();
  }

  @Bean(MATCH_EXECUTOR)
  public ThreadPoolTaskExecutor matchExecutor() {
    Runtime runtime = Runtime.getRuntime();
    int numberOfProcessors = runtime.availableProcessors();
    logger.debug("{} max size is {} (available processors to this JVM)", MATCH_EXECUTOR, numberOfProcessors);
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(numberOfProcessors);
    // tutti eseguiti
    executor.setQueueCapacity(0);
    //executor.setMaxPoolSize(numberOfProcessors);
    // unlimited of execution in queue
    executor.setThreadNamePrefix(COMPUTE_THREAD_PREFIX);
    executor.initialize();
    return executor;
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
  public MatchService gameInstance(NetworkPlayer1 player1, NetworkPlayer2 player2, BlockingQueue<Pair<Player, Coordinates>> movesQueue) {
    return new MatchServiceImpl(player1, player2, movesQueue);
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
  public UserInputReader userInputReader(BlockingQueue<Pair<Player, Coordinates>> movesQueue, int turnTimeout) {
    return new NetworkUserInputReader(movesQueue, turnTimeout);
  }
}
