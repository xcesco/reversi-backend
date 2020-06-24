package org.abubusoft.reversi.server.web.controllers;

public class WebPathConstants {
  public static final String MATCH_URL_SEGMENT = "/matches";
  public static final String USERS_URL_SEGMENT = "/users";

  /**
   * web service api entry point
   */
  public static final String API_ENTRYPOINT = "/api/v1/public";

  /**
   * Need <code>topic</code> prefix
   */
  public static final String WS_USER_TOPIC = "/users/{uuid}/moves";

  public static final String USER_READY_URL_SEGMENT = "/{uuid}/ready";
  public static final String USER_NOT_READY_URL_SEGMENT = "/{uuid}/not-ready";
  public static final String USER_MATCH_URL_SEGMENT = "/{uuid}/match";
}
