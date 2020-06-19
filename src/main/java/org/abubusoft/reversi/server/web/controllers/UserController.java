package org.abubusoft.reversi.server.web.controllers;

import org.abubusoft.reversi.server.model.GameUser;
import org.abubusoft.reversi.server.repositories.GameUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

  public UserController(@Autowired GameUserRepository gameUserRepository) {
    this.gameUserRepository = gameUserRepository;
  }

  private final GameUserRepository gameUserRepository;

  @GetMapping("/users")
  public List<GameUser> getUsers() {
    return (List<GameUser>) gameUserRepository.findAll();
  }

  @PostMapping("/users")
  void addUser(@RequestBody GameUser gameUser) {
    gameUserRepository.save(gameUser);
  }
}