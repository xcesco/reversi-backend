package org.abubusoft.reversi.server.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-server.properties")
public class MatchSimulationServerSideTest extends MatchSimulationClientSideTest {

  @Test
  public void testMatch() throws Exception {
    super.testMatch();
  }
}
