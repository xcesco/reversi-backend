package org.abubusoft.reversi.server;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.abubusoft.reversi.server.model.GameStatus;
import org.abubusoft.reversi.server.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.stream.Stream;

@SpringBootApplication
public class ReversiServerApplication {
  final static Logger logger= LoggerFactory.getLogger(ReversiServerApplication.class);

  public static final String GAME_EXECUTOR = "gameExecutor";
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

  @Bean(GAME_EXECUTOR)
  public Executor gameExecutor() {
    Runtime runtime = Runtime.getRuntime();
    int numberOfProcessors = runtime.availableProcessors();
    logger.info("{} max size is {} (available processors to this JVM)", GAME_EXECUTOR, numberOfProcessors);
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
}
