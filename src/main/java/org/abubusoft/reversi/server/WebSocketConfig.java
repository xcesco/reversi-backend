package org.abubusoft.reversi.server;

import org.abubusoft.reversi.server.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.time.Duration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<ExpiringSession> {

  public static final String WE_ENDPOINT = "/api/messages";
  public static final String APPLICATION_ENDPOINT = "/app";
  public static final String TOPIC_PREFIX = "/topic";

  private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
  private GameService gameService;

  @Autowired
  public void setGameService(GameService gameService) {
    this.gameService = gameService;
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker(TOPIC_PREFIX);
    config.setApplicationDestinationPrefixes(APPLICATION_ENDPOINT);
  }

//  @Override
//  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//    logger.info("Registering {} handler", WE_ENDPOINT);
//
//    registry.addHandler(gameWebSocketHandler(), WE_ENDPOINT)
//            .setAllowedOrigins("*") // Avoid 403
//            .withSockJS();
//  }

//  @Override
//  public void registerStompEndpoints(StompEndpointRegistry registry) {
//    logger.info("Registering {} handler", WE_ENDPOINT);
//
//    registry.addEndpoint(WE_ENDPOINT)
//            .setAllowedOrigins("*") // Avoid 403
//            .withSockJS();
//  }

  // https://docs.spring.io/spring-session/docs/current/reference/html5/guides/boot-websocket.html
  @Override
  protected void configureStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
    logger.info("Registering {} handler", WE_ENDPOINT);

    stompEndpointRegistry.addEndpoint(WE_ENDPOINT)
            .setAllowedOrigins("*") // Avoid 403
            .withSockJS();
  }

//  @Override
//  public void configureMessageBroker(MessageBrokerRegistry registry) {
//    registry.enableSimpleBroker("/queue/", "/topic/");
//    registry.setApplicationDestinationPrefixes("/app");
//  }

  //@Bean
//  public WebSocketHandler gameWebSocketHandler() {
//    return new GameWebSocketHandler();
//  }

  @Bean
  public SessionRepository<ExpiringSession> sessionRepository(SessionProperties properties) {
    MapSessionRepository repository = new MapSessionRepository();
    Duration timeout = properties.getTimeout();
    if (timeout != null) {
      repository.setDefaultMaxInactiveInterval((int) timeout.toSeconds());
    }
    return repository;
  }

}