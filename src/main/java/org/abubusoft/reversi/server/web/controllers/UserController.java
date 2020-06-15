package org.abubusoft.reversi.server.web.controllers;

import org.abubusoft.reversi.server.model.User;
import org.abubusoft.reversi.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

  public UserController(@Autowired UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  private final UserRepository userRepository;

  @GetMapping("/users")
  public List<User> getUsers() {
    return (List<User>) userRepository.findAll();
  }

  @PostMapping("/users")
  void addUser(@RequestBody User user) {
    userRepository.save(user);
  }
}