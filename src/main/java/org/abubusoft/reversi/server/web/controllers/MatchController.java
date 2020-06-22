package org.abubusoft.reversi.server.web.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController(UsersController.API_ENTRYPOINT + MatchController.MATCH)
public class MatchController {
  public static final String MATCH = "/matches";
}
