package org.abubusoft.reversi.server.web.controllers;

import org.abubusoft.reversi.server.model.MatchStatus;
import org.abubusoft.reversi.server.model.User;
import org.abubusoft.reversi.server.model.UserRegistration;
import org.abubusoft.reversi.server.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(value = UsersController.API_ENTRYPOINT + UsersController.USERS, produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class UsersController {
  public static final String API_ENTRYPOINT = "/api/v1/public";
  public static final String USERS = "/users";
  private final GameService gameService;

  public UsersController(@Autowired GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping()
  public ResponseEntity<User> add(@RequestBody UserRegistration userRegistration) {
    return ResponseEntity.ok(gameService.saveUser(userRegistration));
  }

  @PatchMapping("/{uuid}/ready")
  public User ready2Play(@PathVariable("uuid") UUID userUUID) {
    return gameService.readyToPlay(userUUID);
  }

  @PatchMapping("/{uuid}/not-ready")
  public User notReady2Play(@PathVariable("uuid") UUID userUUID) {
    return gameService.stopPlaying(userUUID);
  }

  @GetMapping("/{uuid}/match")
  public MatchStatus play(@PathVariable("uuid") UUID userUUID) {
    return gameService.findMatchByUserId(userUUID);
  }

  @GetMapping
  public ResponseEntity<Iterable<User>> getUsers() {
    return ResponseEntity.ok(gameService.findAllUsers());
  }
}