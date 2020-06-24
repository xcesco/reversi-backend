package org.abubusoft.reversi.server.web.controllers;

import org.abubusoft.reversi.server.model.MatchStatus;
import org.abubusoft.reversi.server.model.User;
import org.abubusoft.reversi.messages.ConnectedUser;
import org.abubusoft.reversi.server.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(value = WebPathConstants.API_ENTRYPOINT + WebPathConstants.USERS_URL_SEGMENT, produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class UsersController {
  private final GameService gameService;

  public UsersController(@Autowired GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping
  public ResponseEntity<User> add(@RequestBody ConnectedUser connectedUser) {
    return ResponseEntity.ok(gameService.saveUser(connectedUser));
  }

  @PatchMapping(WebPathConstants.USER_READY_URL_SEGMENT)
  public ResponseEntity<User> ready2Play(@PathVariable("uuid") UUID userUUID) {
    return ResponseEntity.ok(gameService.readyToPlay(userUUID));
  }

  @PatchMapping(WebPathConstants.USER_NOT_READY_URL_SEGMENT)
  public ResponseEntity<User> notReady2Play(@PathVariable("uuid") UUID userUUID) {
    return ResponseEntity.ok(gameService.stopPlaying(userUUID));
  }

  @GetMapping(WebPathConstants.USER_MATCH_URL_SEGMENT)
  public ResponseEntity<MatchStatus> play(@PathVariable("uuid") UUID userUUID) {
    return ResponseEntity.ok(gameService.findMatchByUserId(userUUID));
  }

  @GetMapping
  public ResponseEntity<Iterable<User>> getUsers() {
    return ResponseEntity.ok(gameService.findAllUsers());
  }
}