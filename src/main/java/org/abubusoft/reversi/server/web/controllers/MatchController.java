package org.abubusoft.reversi.server.web.controllers;

import org.abubusoft.reversi.server.model.MatchStatus;
import org.abubusoft.reversi.server.repositories.MatchStatusRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping(value=WebPathConstants.API_ENTRYPOINT + WebPathConstants.MATCH_URL_SEGMENT, produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class MatchController {

  private final MatchStatusRepository matchStatusRepository;

  public MatchController(MatchStatusRepository matchStatusRepository) {
    this.matchStatusRepository = matchStatusRepository;
  }

  @GetMapping
  public ResponseEntity<Iterable<MatchStatus>> findAll() {
    return ResponseEntity.ok(matchStatusRepository.findAll());
  }
}
