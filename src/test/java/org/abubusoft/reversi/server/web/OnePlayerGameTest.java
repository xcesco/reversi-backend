package org.abubusoft.reversi.server.web;

import org.abubusoft.reversi.server.model.User;
import org.abubusoft.reversi.server.model.UserRegistration;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OnePlayerGameTest extends AbstractWebTest {

  @Test
  public void playersAreReadyToPlay() throws Exception {
    UserRegistration userRegistration = new UserRegistration();
    userRegistration.setName("ciao");
    User user1 = restTemplate.postForEntity(baseUrl + "api/v1/public/users", userRegistration, User.class).getBody();
    User user2 = restTemplate.postForEntity(baseUrl + "api/v1/public/users", userRegistration, User.class).getBody();

    assert user1 != null;
    user1 = restTemplate.patchForObject(baseUrl + String.format("api/v1/public/users/%s/ready", user1.getId()), HttpMethod.PUT, User.class);
    assert user2 != null;
    user2 = restTemplate.patchForObject(baseUrl + String.format("api/v1/public/users/%s/ready", user2.getId()), HttpMethod.PUT, User.class);

    logger.info(objectMapper.writeValueAsString(user1));
    logger.info(objectMapper.writeValueAsString(user2));
    assertNotNull(user1.getId());
    assertNotNull(user2.getId());

    executor.getThreadPoolExecutor().awaitTermination(20, TimeUnit.SECONDS);
  }
}
