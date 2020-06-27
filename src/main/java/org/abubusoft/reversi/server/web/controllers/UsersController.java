package org.abubusoft.reversi.server.web.controllers;

import org.abubusoft.reversi.messages.ConnectedUser;
import org.abubusoft.reversi.messages.UserRegistration;
import org.abubusoft.reversi.server.model.MatchStatus;
import org.abubusoft.reversi.server.model.User;
import org.abubusoft.reversi.server.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping(value = WebPathConstants.API_ENTRYPOINT + WebPathConstants.USERS_URL_SEGMENT, produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class UsersController {
  private final GameService gameService;

  public UsersController(@Autowired GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping
  public ResponseEntity<ConnectedUser> connect(@RequestBody UserRegistration userRegistration) {
    User user = gameService.saveUser(userRegistration);
    ConnectedUser connectedUser = new ConnectedUser(user.getId(), user.getName(), user.getStatus(), user.getPiece());
    return ResponseEntity.ok(connectedUser);
  }

  @PatchMapping(WebPathConstants.USER_READY_URL_SEGMENT)
  public ResponseEntity<ConnectedUser> ready2Play(@PathVariable("uuid") UUID userUUID) {
    User user = gameService.readyToPlay(userUUID);
    ConnectedUser connectedUser = new ConnectedUser(user.getId(), user.getName(), user.getStatus(), user.getPiece());
    return ResponseEntity.ok(connectedUser);
  }

  @PatchMapping(WebPathConstants.USER_NOT_READY_URL_SEGMENT)
  public ResponseEntity<ConnectedUser> notReady2Play(@PathVariable("uuid") UUID userUUID) {
    User user = gameService.stopPlaying(userUUID);
    ConnectedUser connectedUser = new ConnectedUser(user.getId(), user.getName(), user.getStatus(), user.getPiece());
    return ResponseEntity.ok(connectedUser);
  }

  @GetMapping(WebPathConstants.USER_MATCH_URL_SEGMENT)
  public ResponseEntity<MatchStatus> play(@PathVariable("uuid") UUID userUUID) {
    return ResponseEntity.ok(gameService.findMatchByUserId(userUUID));
  }

  @GetMapping
  public ResponseEntity<List<ConnectedUser>> getUsers() {
    List<ConnectedUser> users = gameService.findAllUsers()
            .stream()
            .map(user -> new ConnectedUser(user.getId(),
                    user.getName(), user.getStatus(),
                    user.getPiece()))
            .collect(Collectors.toList());
    return ResponseEntity.ok(users);
  }
}