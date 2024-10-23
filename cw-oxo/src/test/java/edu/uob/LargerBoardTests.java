package edu.uob;

import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.management.ConstructorParameters;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class LargerBoardTests {
    private OXOModel model;
    private OXOController controller;

    @BeforeEach
    void setup() {
        model = new OXOModel(3, 3, 3);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
        controller = new OXOController(model);
        controller.addRow();
        controller.addColumn();
    }

    void sendCommandToController(String command) {
        String timeoutComment = "Controller took too long to respond (probably stuck in an infinite loop)";
        assertTimeoutPreemptively(Duration.ofMillis(1000), () -> controller.handleIncomingCommand(command), timeoutComment);
    }

    @Test
    void testBasicWin(){
        //Testing a row win
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        sendCommandToController("d2"); // First player
        sendCommandToController("b1"); // Second player
        sendCommandToController("d3"); // First player
        sendCommandToController("b2"); // Second player
        sendCommandToController("d4"); // First player
        String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
        assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
    }

    @Test
    void testIncreaseThresholdWin(){
        controller.increaseWinThreshold();
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        sendCommandToController("D4"); //first player
        sendCommandToController("A2"); //second player
        sendCommandToController("D3"); //first player
        sendCommandToController("C1"); //second player
        sendCommandToController("D2"); //first player
        sendCommandToController("B1"); //second player
        sendCommandToController("D1"); //first player
        String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't.";
        assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
    }


    @Test
    void testIncreaseThresholdWin2(){
        controller.increaseWinThreshold();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        sendCommandToController("A1"); //first player
        sendCommandToController("A2"); //second player
        sendCommandToController("B1"); //first player
        sendCommandToController("B2"); //second player
        String failedTestComment = "Game was expected to draw.";
        assertNull(model.getWinner(), failedTestComment);
        assertTrue(model.isGameDrawn(), failedTestComment);
    }

/*    @ParameterizedTest
    @ValueSource(strings = {"a1", "A1", "a2", "A2", "a3", "A3", "a4", "A4", "b1", "B1",
    "b3", "B3", "b4", "B4", "c1", "C1", "c2", "C2", "c3", "C3", "c4", "C4", "d1", "D2",
    "d3", "D3", "d4", "D4"})
    void testAllPossibleCommands(String strings){
        sendCommandToController(strings);

    }*/

    @Test
    void testWinIsLocked(){
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        sendCommandToController("D4"); //first player
        sendCommandToController("A2"); //second player
        sendCommandToController("D3"); //first player
        sendCommandToController("C1"); //second player
        sendCommandToController("D2"); //first player
        String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't.";
        assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
        controller.addRow();
        String failedTestComment2 = "Row should have been added.";
        assertEquals(model.getNumberOfRows(), 5, failedTestComment2);
        controller.addColumn();
        String failedTestComment3 = "Column should have been added.";
        assertEquals(model.getNumberOfColumns(), 5, failedTestComment3);
        controller.increaseWinThreshold();
        String failedTestComment4 = "Win threshold should have increased.";
        assertEquals(model.getWinThreshold(), 4, failedTestComment4);
        controller.decreaseWinThreshold();
        String failedTestComment5 = "Win threshold should have decreased.";
        assertEquals(model.getWinThreshold(), 3, failedTestComment5);
        controller.removeRow();
        String failedTestComment8 = "Row should not have been removed.";
        assertEquals(model.getNumberOfRows(), 4, failedTestComment8);
        controller.removeColumn();
        String failedTestComment9 = "Column should not have been removed.";
        assertEquals(model.getNumberOfColumns(), 4, failedTestComment9);
        controller.reset();
        controller.increaseWinThreshold();
        String failedTestComment6 = "Win threshold should have increased.";
        assertEquals(model.getWinThreshold(), 4, failedTestComment6);
        controller.decreaseWinThreshold();
        String failedTestComment7 = "Win threshold should have decreased.";
        assertEquals(model.getWinThreshold(), 3, failedTestComment7);
        controller.removeRow();
        controller.removeRow();
        String failedTestComment12 = "Row should have been removed.";
        assertEquals(model.getNumberOfRows(), 2, failedTestComment12);
        controller.removeColumn();
        controller.removeColumn();
        String failedTestComment13 = "Column should have been removed.";
        assertEquals(model.getNumberOfColumns(), 2, failedTestComment13);
    }

    @Test
    void testMultiPlayers(){
        model.addNewPlayer('a');
        model.addNewPlayer('A');
        model.addNewPlayer('b');
        model.addNewPlayer('B');
        model.addNewPlayer('c');
        model.addNewPlayer('C');
        model.addNewPlayer('d');
        model.addNewPlayer('D');
        model.addNewPlayer('e');
        model.addNewPlayer('E');
        model.addNewPlayer('f');
        model.addNewPlayer('F');
        model.addNewPlayer('g');
        model.addNewPlayer('G');
        sendCommandToController("a1"); //X
        sendCommandToController("A2"); //O
        sendCommandToController("a3"); //a
        sendCommandToController("A4"); //A
        sendCommandToController("b1"); //b
        sendCommandToController("B2"); //B
        sendCommandToController("b3"); //c
        sendCommandToController("B4"); //C
        sendCommandToController("c1"); //d
        sendCommandToController("C2"); //D
        sendCommandToController("c3"); //e
        sendCommandToController("C4"); //E
        sendCommandToController("d1"); //f
        sendCommandToController("D2"); //F
        sendCommandToController("d3"); //g
        sendCommandToController("D4"); //G
        String failedMessage = "Grid should be full";
        assertTrue(model.isGridFull(), failedMessage);
        String failedMessage1 = "Game should be drawn";
        assertTrue(model.isGameDrawn(), failedMessage1);
    }

    @Test
    void testMaxBoard(){
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        String not9Rows = "There should be 9 rows.";
        assertEquals(model.getNumberOfRows(), 9, not9Rows);
        String not9Columns = "There should be 9 rows.";
        assertEquals(model.getNumberOfColumns(), 9, not9Columns);
        OXOPlayer firstMover = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        sendCommandToController("f4");
        sendCommandToController("f5");
        sendCommandToController("g4");
        sendCommandToController("e4");
        sendCommandToController("h4");
        String failedMessage = "First player should have won.";
        assertEquals(model.getWinner(), firstMover, failedMessage);
    }

    @Test
    void testMaxBoard2(){
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        String not9Rows = "There should be 9 rows.";
        assertEquals(model.getNumberOfRows(), 9, not9Rows);
        String not9Columns = "There should be 9 rows.";
        assertEquals(model.getNumberOfColumns(), 9, not9Columns);
        sendCommandToController("f4"); //1
        OXOPlayer secondMover = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        sendCommandToController("f5"); //2
        sendCommandToController("g4"); //1
        sendCommandToController("e4"); //2
        sendCommandToController("d7"); //1
        sendCommandToController("d6"); //2
        sendCommandToController("c6"); //1
        sendCommandToController("e8"); //2
        sendCommandToController("e6"); //1
        sendCommandToController("g6"); //2
        String failedMessage = "First player should have won.";
        assertEquals(model.getWinner(), secondMover, failedMessage);
    }
}
