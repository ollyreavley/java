package edu.uob;

import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

import static java.lang.Integer.MAX_VALUE;
import static org.junit.jupiter.api.Assertions.*;

class ExampleControllerTests {
  private OXOModel model;
  private OXOController controller;

  // Make a new "standard" (3x3) board before running each test case (i.e. this method runs before every `@Test` method)
  // In order to test boards of different sizes, winning thresholds or number of players, create a separate test file (without this method in it !)
  @BeforeEach
  void setup() {
    model = new OXOModel(3, 3, 3);
    model.addPlayer(new OXOPlayer('X'));
    model.addPlayer(new OXOPlayer('O'));
    controller = new OXOController(model);
  }

  void sendCommandToController(String command) {
    String timeoutComment = "Controller took too long to respond (probably stuck in an infinite loop)";
    assertTimeoutPreemptively(Duration.ofMillis(1000), () -> controller.handleIncomingCommand(command), timeoutComment);
  }

  // Test simple move taking and cell claiming functionality
  @Test
  void testBasicMoveTaking() {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("a1");
    String failedTestComment = "Cell a1 wasn't claimed by the first player";
    assertEquals(firstMovingPlayer, controller.gameModel.getCellOwner(0, 0), failedTestComment);
  }

  // Test out basic win detection
  @Test
  void testBasicWin(){
    //Testing a row win
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a3"); // First player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testBasicWin2(){
    //Testing a column win
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("a1"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("b1"); // First player
    sendCommandToController("a3"); // Second player
    sendCommandToController("c1"); // First player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testBasicWin3(){
    //Testing a diagonal win
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("a1"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("b2"); // First player
    sendCommandToController("a3"); // Second player
    sendCommandToController("c3"); // First player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testBasicWin4() {
    //Testing a win where the player starts on the right
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("a3"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a1"); // First player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testBasicWin5() {
    //Testing a win where the player starts on the bottom
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("c1"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("c2"); // First player
    sendCommandToController("a3"); // Second player
    sendCommandToController("c3"); // First player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testBasicWin6() {
    //Testing a win where the players starts bottom right
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("c3"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("c2"); // First player
    sendCommandToController("a3"); // Second player
    sendCommandToController("c1"); // First player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testBasicWin7(){
    //Testing a win where the players starts bottom right
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("c3"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("b3"); // First player
    sendCommandToController("a1"); // Second player
    sendCommandToController("a3"); // First player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testBasicWin8() {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("b3"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("b1"); // First player
    sendCommandToController("a3"); // Second player
    sendCommandToController("b2"); // First player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testBasicWin10() {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("a1"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("b2"); // First player
    sendCommandToController("a3"); // Second player
    sendCommandToController("c3"); // First player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testBasicWin11() {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("a3"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("b2"); // First player
    sendCommandToController("c2"); // Second player
    sendCommandToController("c1"); // First player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testBasicWin12() {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.addRow();
    sendCommandToController("b1"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("c1"); // First player
    sendCommandToController("a3"); // Second player
    sendCommandToController("d1"); // First player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testBasicWin13() {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.addColumn();
    sendCommandToController("b4"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("c4"); // First player
    sendCommandToController("a3"); // Second player
    sendCommandToController("a4"); // First player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testInvalidLengthException() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an InvalidIdentifierLengthException for command `abc123`";
    assertThrows(InvalidIdentifierLengthException.class, () -> sendCommandToController("abc123"), failedTestComment);
  }

  @Test
  void testCellTaken() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw a CellAlreadyTakenException for command b1 when entered by both players.";
    sendCommandToController("b1"); // First player
    assertThrows(CellAlreadyTakenException.class, () -> sendCommandToController("b1"), failedTestComment);
  }

  @Test
  void testInvalidIdentifierException() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an InvalidIdentifierCharacterException for command £1.";
    assertThrows(InvalidIdentifierCharacterException.class, () -> sendCommandToController("£1"), failedTestComment);
  }

  @Test
  void testOutsideCellRangeException() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an OutsideCellRangeException for command a4 on a 3x3 board.";
    assertThrows(OutsideCellRangeException.class, () -> sendCommandToController("a4"), failedTestComment);
  }

  @Test
  void testOutsideCellRangeException1() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an OutsideCellRangeException for command a4 on a 3x3 board.";
    assertThrows(OutsideCellRangeException.class, () -> sendCommandToController("a0"), failedTestComment);
  }
  @Test
  void testOutsideCellRangeException2() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an OutsideCellRangeException for command g1 on a 3x3 board.";
    assertThrows(OutsideCellRangeException.class, () -> sendCommandToController("g1"), failedTestComment);
  }
  @Test
  void testAddPlayer() {
    model.addNewPlayer('P');
    String failedTestComment = "Number of players expected to be 3 but was " + model.getNumberOfPlayers();
    assertEquals(3, model.getNumberOfPlayers(), failedTestComment);
  }
  @Test
  void testDrawn(){
    sendCommandToController("a1"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("a3"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("b2"); // First player
    sendCommandToController("c1"); // Second player
    sendCommandToController("b3"); // First player
    sendCommandToController("c3"); // Second player
    sendCommandToController("c2"); // First player
    String failedTestComment = "Game was expected to draw.";
    assertTrue(model.isGameDrawn(), failedTestComment);
  }

  @Test
  void checkRemoveColumnFails(){
    sendCommandToController("a3"); // First player
    controller.removeColumn();
    String failedTestComment = "Column should not have been removed.";
    assertEquals(model.getNumberOfColumns(), 3, failedTestComment);
  }

  @Test
  void checkRemoveColumnPasses(){
    sendCommandToController("a1"); // First player
    controller.removeColumn();
    String failedTestComment = "Column should have been removed.";
    assertEquals(model.getNumberOfColumns(), 2, failedTestComment);
  }

  @Test
  void checkRemoveRowFails(){
    sendCommandToController("c2"); // First player
    controller.removeRow();
    String failedTestComment = "Row should not have been removed.";
    assertEquals(model.getNumberOfRows(), 3, failedTestComment);
  }

  @Test
  void checkRemoveRowPasses(){
    sendCommandToController("a3"); // First player
    controller.removeRow();
    String failedTestComment = "Row should have been removed.";
    assertEquals(model.getNumberOfRows(), 2, failedTestComment);
  }

  @Test
  void testDrawnThenIncreasePasses(){
    sendCommandToController("a1"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("a3"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("b2"); // First player
    sendCommandToController("c1"); // Second player
    sendCommandToController("b3"); // First player
    sendCommandToController("c3"); // Second player
    sendCommandToController("c2"); // First player
    String failedTestComment = "Game was expected to draw.";
    assertTrue(model.isGameDrawn(), failedTestComment);
    controller.addRow();
    String failedTestComment2 = "Game drawn status should be reset.";
    assertFalse(model.isGameDrawn(), failedTestComment2);
    OXOPlayer secondMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("d1"); // Second player
    String failedTestComment3 = "Second player was expected to win.";
    assertEquals(secondMovingPlayer, model.getWinner(), failedTestComment3);
    String failedTestComment4 = "Drawn status should not be set.";
    assertFalse(model.isGameDrawn(), failedTestComment4);
  }

  @Test
  void testWonThenIncreaseFails(){
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("a1"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("b1"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("c1"); // First player
    String failedTestComment = "Game was expected to be won by " + firstMovingPlayer;
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
    controller.addRow();
    controller.increaseWinThreshold();
    String failedTestComment2 = "Number of rows should have increased.";
    assertEquals(model.getNumberOfRows(), 4, failedTestComment2);
    String failedTestComment3 = "Win threshold should not have increased.";
    assertEquals(model.getWinThreshold(), 4, failedTestComment3);
  }

  @Test
  void testDrawnThenIncreasePasses2() {
    sendCommandToController("a1"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("a3"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("b2"); // First player
    sendCommandToController("c1"); // Second player
    sendCommandToController("b3"); // First player
    sendCommandToController("c3"); // Second player
    sendCommandToController("c2"); // First player
    String failedTestComment = "Game was expected to draw.";
    assertTrue(model.isGameDrawn(), failedTestComment);
    controller.addColumn();
    String failedTestComment2 = "Game drawn status should be reset.";
    assertFalse(model.isGameDrawn(), failedTestComment2);
    sendCommandToController("a4"); // Second player
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("b4"); // First player
    String failedTestComment3 = "First player was expected to win.";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment3);
    String failedTestComment4 = "Drawn status should not be set.";
    assertFalse(model.isGameDrawn(), failedTestComment4);
  }
tThrows(Nu
  @Test
  void testThresholdChanges(){
    sendCommandToController("a1"); // First player
    sendCommandToController("a2"); // Second player
    controller.increaseWinThreshold();
    String failedTestComment3 = "Win threshold should have increased.";
    assertEquals(model.getWinThreshold(), 4, failedTestComment3);
    sendCommandToController("a3"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("b2"); // First player
    sendCommandToController("c1"); // Second player
    sendCommandToController("b3"); // First player
    sendCommandToController("c3"); // Second player
    sendCommandToController("c2"); // First player
    String failedTestComment = "Game was expected to draw.";
    assertTrue(model.isGameDrawn(), failedTestComment);
  }

  @Test
  void testThresholdChanges2(){
    controller.increaseWinThreshold();
    String failedTestComment = "Win threshold should have increased.";
    assertEquals(model.getWinThreshold(), 4, failedTestComment);
    sendCommandToController("a1"); // First player
    controller.decreaseWinThreshold();
    String failedTestComment2 = "Win threshold should not have decreased.";
    assertEquals(model.getWinThreshold(), 4, failedTestComment2);
    sendCommandToController("a2"); // Second player
    controller.reset();
    controller.decreaseWinThreshold();
    String failedTestComment3 = "Win threshold should have decreased.";
    assertEquals(model.getWinThreshold(), 3, failedTestComment3);
    sendCommandToController("a1"); // First player
    controller.decreaseWinThreshold();
    String failedTestComment4 = "Win threshold should not have decreased.";
    assertEquals(model.getWinThreshold(), 3, failedTestComment4);
    sendCommandToController("a2"); // Second player
    sendCommandToController("a3"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("b2"); // First player
    sendCommandToController("c1"); // Second player
    sendCommandToController("b3"); // First player
    sendCommandToController("c3"); // Second player
    sendCommandToController("c2"); // First player
    String failedTestComment5 = "Game was expected to draw.";
    assertTrue(model.isGameDrawn(), failedTestComment5);
  }

  @Test
  void testThreeMustBeInARow(){
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.addColumn();
    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a4"); // First player
    String failedTestComment = "Player one should not win";
    assertNull(model.getWinner(), failedTestComment);
  }

  @Test
  void testColumnDrawRemoval(){
    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("c1"); // First player
    sendCommandToController("c2"); // Second player
    controller.removeColumn();
    String failedTestComment = "Should not be able to remove a column.";
    assertEquals(model.getNumberOfColumns(), 3, failedTestComment);
  }

  @Test
  void testRowDrawRemoval(){
    sendCommandToController("a1"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("b1"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a3"); // First player
    sendCommandToController("b3"); // Second player
    controller.removeRow();
    String failedTestComment = "Should not be able to remove a column.";
    assertEquals(model.getNumberOfRows(), 3, failedTestComment);
  }

  @Test
  void testWinningFinalPlayResultsInWinNotDraw(){
    sendCommandToController("a1"); // First player
    OXOPlayer secondPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("b2"); // Second player
    sendCommandToController("b1"); // First player
    sendCommandToController("b3"); // Second player
    sendCommandToController("c2"); // First player
    sendCommandToController("c3"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("a3"); // Second player
    String failedTestComment = "Should be a win for second player.";
    assertEquals(model.getWinner(), secondPlayer, failedTestComment);
    String failedTestComment1 = "There should not be a draw.";
    assertFalse(model.isGameDrawn(), failedTestComment1);
  }

 /* @Test
=======
/*
  @Test
>>>>>>> 497e18cd1a007d03dcf2e84b4ae31f4d075e9277
  void testMaxWinThreshold(){
    long n = 0;
    for(n = 0; n < MAX_VALUE; n++){
      controller.increaseWinThreshold();
    }
    String failedComment = "Win threshold should be one less than max integer value.";
    assertEquals(model.getWinThreshold(), MAX_VALUE - 1, failedComment);
  }*/
}
