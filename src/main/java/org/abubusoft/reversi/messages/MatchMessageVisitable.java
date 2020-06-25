package org.abubusoft.reversi.messages;

public interface MatchMessageVisitable {
  void accept(MatchMessageVisitor visitor);
}