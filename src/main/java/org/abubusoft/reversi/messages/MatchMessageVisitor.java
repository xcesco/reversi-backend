package org.abubusoft.reversi.messages;


public interface MatchMessageVisitor {
  void visit(MatchStartMessage message);

  void visit(MatchStatusMessage message);

  void visit(MatchEndMessage message);

  void visit(MatchMessage message);
}