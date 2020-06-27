package org.abubusoft.reversi.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class UserRegistration implements Serializable {
  @JsonCreator
  public UserRegistration(@JsonProperty("name") String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  private final String name;
}
