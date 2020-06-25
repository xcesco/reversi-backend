package org.abubusoft.reversi.messages;


public interface MatchMessageVisitor {
  void visit(MatchStartMessage command);

  void visit(MatchStatusMessage command);

  void visit(MatchEndMessage command);
}