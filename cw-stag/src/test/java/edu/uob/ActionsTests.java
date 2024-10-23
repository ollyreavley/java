package edu.uob;

import org.junit.jupiter.api.Test;
import java.nio.file.Paths;
import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ActionsTests {

    @Test
    void testInBasicCommandsEntered() {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        GameServer server = new GameServer(entitiesFile, actionsFile);
        GameState state = server.getState();
        GameMoves possibleMoves = state.getMoves();
        Map<String, Set<GameAction>> moves = possibleMoves.returnPossibleMoves();

        //built in moves
        assertTrue(moves.containsKey("inv"), "inv should be a possible move");
        assertTrue(moves.containsKey("look"), "look should be a possible move");
        assertTrue(moves.containsKey("goto"), "goto should be a possible move");
        assertTrue(moves.containsKey("get"), "get should be a possible move");
        assertTrue(moves.containsKey("drop"), "drop should be a possible move");
        assertTrue(moves.containsKey("inventory"), "inventory should be a possible move");

        //game specific moves
        assertTrue(moves.containsKey("open"), "drop should be a possible move");
        assertTrue(moves.containsKey("unlock"), "unlock should be a possible move");
        assertTrue(moves.containsKey("chop"), "chop should be a possible move");
        assertTrue(moves.containsKey("cut"), "cut should be a possible move");
        assertTrue(moves.containsKey("cutdown"), "cutdown should be a possible move");
        assertTrue(moves.containsKey("drink"), "drink should be a possible move");
        assertTrue(moves.containsKey("fight"), "fight should be a possible move");
        assertTrue(moves.containsKey("hit"), "hit should be a possible move");
        assertTrue(moves.containsKey("attack"), "attack should be a possible move");

        //testing that number of moves is correct
        assertEquals(15, moves.size(), "Number of moves should be 15");

        //testing that subjects for game moves are correct
        ArrayList<String> openSubjects = new ArrayList<String>(List.of(new String[]{"key", "trapdoor"}));
        assertNotNull(possibleMoves.returnAction("open"), "Expected both key and trapdoor to be present");
        ArrayList<String> unlockSubjects = new ArrayList<String>(List.of(new String[]{"key", "trapdoor"}));
        assertNotNull(possibleMoves.returnAction("unlock"), "Expected both key and trapdoor to be present");
        ArrayList<String> chopSubjects = new ArrayList<String>(List.of(new String[]{"axe", "tree"}));
        assertNotNull(possibleMoves.returnAction("chop"), "Expected both tree and axe to be present");
        ArrayList<String> cutSubjects = new ArrayList<String>(List.of(new String[]{"axe", "tree"}));
        assertNotNull(possibleMoves.returnAction("cut"), "Expected both tree and axe to be present");
        ArrayList<String> cutdownSubjects = new ArrayList<String>(List.of(new String[]{"axe", "tree"}));
        assertNotNull(possibleMoves.returnAction("cutdown"), "Expected both tree and axe to be present");
        ArrayList<String> drinkSubjects = new ArrayList<String>(List.of(new String[]{"potion"}));
        assertNotNull(possibleMoves.returnAction("drink"), "Expected potion to be present");
        ArrayList<String> fightSubjects = new ArrayList<String>(List.of(new String[]{"elf"}));
        assertNotNull(possibleMoves.returnAction("fight"), "Expected elf to be present");
        ArrayList<String> hitSubjects = new ArrayList<String>(List.of(new String[]{"elf"}));
        assertNotNull(possibleMoves.returnAction("hit"), "Expected elf to be present");
        ArrayList<String> attackSubjects = new ArrayList<String>(List.of(new String[]{"elf"}));
        assertNotNull(possibleMoves.returnAction("attack"), "Expected elf to be present");

        //testing that consumed entities are correct for game specific moves
        GameAdditActions test = (GameAdditActions)possibleMoves.returnAction("open");
        ArrayList<String> openConsumed = new ArrayList<String>(List.of(new String[]{"key"}));
        assertEquals(openConsumed, test.getConsumed(), "Expected key to be in consumed list");
        test = (GameAdditActions)possibleMoves.returnAction("unlock");
        ArrayList<String> unlockConsumed = new ArrayList<String>(List.of(new String[]{"key"}));
        assertEquals(unlockConsumed, test.getConsumed(), "Expected key to be in consumed list");
        test = (GameAdditActions)possibleMoves.returnAction("chop");
        ArrayList<String> chopConsumed = new ArrayList<String>(List.of(new String[]{"tree"}));
        assertEquals(chopConsumed, test.getConsumed(), "Expected tree to be in consumed list");
        test = (GameAdditActions)possibleMoves.returnAction("cut");
        ArrayList<String> cutConsumed = new ArrayList<String>(List.of(new String[]{"tree"}));
        assertEquals(cutConsumed, test.getConsumed(), "Expected tree to be in consumed list");
        test = (GameAdditActions)possibleMoves.returnAction("cutdown");
        ArrayList<String> cutdownConsumed = new ArrayList<String>(List.of(new String[]{"tree"}));
        assertEquals(cutdownConsumed, test.getConsumed(), "Expected tree to be in consumed list");
        test = (GameAdditActions)possibleMoves.returnAction("drink");
        ArrayList<String> drinkConsumed = new ArrayList<String>(List.of(new String[]{"potion"}));
        assertEquals(drinkConsumed, test.getConsumed(), "Expected potion to be in consumed list");
        test = (GameAdditActions)possibleMoves.returnAction("fight");
        ArrayList<String> fightConsumed = new ArrayList<String>(List.of(new String[]{"health"}));
        assertEquals(fightConsumed, test.getConsumed(), "Expected health to be in consumed list");
        test = (GameAdditActions)possibleMoves.returnAction("hit");
        ArrayList<String> hitConsumed = new ArrayList<String>(List.of(new String[]{"health"}));
        assertEquals(hitConsumed, test.getConsumed(), "Expected health to be in consumed list");
        test = (GameAdditActions)possibleMoves.returnAction("attack");
        ArrayList<String> attackConsumed = new ArrayList<String>(List.of(new String[]{"health"}));
        assertEquals(attackConsumed, test.getConsumed(), "Expected health to be in consumed list");

        //Testing that the produced items are correct
        GameAdditActions testProduced = (GameAdditActions)possibleMoves.returnAction("open");
        ArrayList<String> openProduced = new ArrayList<String>(List.of(new String[]{"cellar"}));
        assertEquals(openProduced, testProduced.getProduced(), "Expected cellar to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("unlock");
        ArrayList<String> unlockProduced = new ArrayList<String>(List.of(new String[]{"cellar"}));
        assertEquals(unlockProduced, testProduced.getProduced(), "Expected cellar to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("chop");
        ArrayList<String> chopProduced = new ArrayList<String>(List.of(new String[]{"log"}));
        assertEquals(chopProduced, testProduced.getProduced(), "Expected log to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("cut");
        ArrayList<String> cutProduced = new ArrayList<String>(List.of(new String[]{"log"}));
        assertEquals(cutProduced, testProduced.getProduced(), "Expected log to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("cutdown");
        ArrayList<String> cutdownProduced = new ArrayList<String>(List.of(new String[]{"log"}));
        assertEquals(cutdownProduced, testProduced.getProduced(), "Expected log to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("drink");
        ArrayList<String> drinkProduced = new ArrayList<String>(List.of(new String[]{"health"}));
        assertEquals(drinkProduced, testProduced.getProduced(), "Expected health to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("fight");
        ArrayList<String> fightProduced = new ArrayList<String>(List.of(new String[]{}));
        assertEquals(fightProduced, testProduced.getProduced(), "Expected nothing to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("hit");
        ArrayList<String> hitProduced = new ArrayList<String>(List.of(new String[]{}));
        assertEquals(hitProduced, testProduced.getProduced(), "Expected nothing to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("attack");
        ArrayList<String> attackProduced = new ArrayList<String>(List.of(new String[]{}));
        assertEquals(attackProduced, testProduced.getProduced(), "Expected nothing to be in produced list");
    }

    @Test
    void testInExtendedCommandsEntered() {
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
        GameServer server = new GameServer(entitiesFile, actionsFile);
        GameState state = server.getState();
        GameMoves possibleMoves = state.getMoves();
        Map<String, Set<GameAction>> moves = possibleMoves.returnPossibleMoves();

        //built in moves
        assertTrue(moves.containsKey("inv"), "inv should be a possible move");
        assertTrue(moves.containsKey("look"), "look should be a possible move");
        assertTrue(moves.containsKey("goto"), "goto should be a possible move");
        assertTrue(moves.containsKey("get"), "get should be a possible move");
        assertTrue(moves.containsKey("drop"), "drop should be a possible move");

        //game specific moves
        assertTrue(moves.containsKey("open"), "drop should be a possible move");
        assertTrue(moves.containsKey("unlock"), "unlock should be a possible move");
        assertTrue(moves.containsKey("chop"), "chop should be a possible move");
        assertTrue(moves.containsKey("cut"), "cut should be a possible move");
        assertTrue(moves.containsKey("cut down"), "cut down should be a possible move");
        assertTrue(moves.containsKey("drink"), "drink should be a possible move");
        assertTrue(moves.containsKey("fight"), "fight should be a possible move");
        assertTrue(moves.containsKey("hit"), "hit should be a possible move");
        assertTrue(moves.containsKey("attack"), "attack should be a possible move");
        assertTrue(moves.containsKey("pay"), "pay should be a possible move");
        assertTrue(moves.containsKey("bridge"), "bridge should be a possible move");
        assertTrue(moves.containsKey("dig"), "dig should be a possible move");
        assertTrue(moves.containsKey("blow"), "blow should be a possible move");

        assertEquals(19, moves.size(), "Number of moves should be 19");

        //testing that subjects for game moves are correct
        assertNotNull(possibleMoves.returnAction("open"), "Expected both key and trapdoor to be present");
        assertNotNull(possibleMoves.returnAction("unlock"), "Expected both key and trapdoor to be present");
        assertNotNull(possibleMoves.returnAction("chop"), "Expected both tree and axe to be present");
        assertNotNull(possibleMoves.returnAction("cut"), "Expected both tree and axe to be present");
        assertNotNull(possibleMoves.returnAction("cut down"), "Expected both tree and axe to be present");
        assertNotNull(possibleMoves.returnAction("drink"), "Expected potion to be present");
        assertNotNull(possibleMoves.returnAction("fight"), "Expected elf to be present");
        assertNotNull(possibleMoves.returnAction("hit"), "Expected elf to be present");
        assertNotNull(possibleMoves.returnAction("attack"), "Expected elf to be present");
        assertNotNull(possibleMoves.returnAction("pay"), "Expected elf and coin to be present");
        assertNotNull(possibleMoves.returnAction("bridge"), "Expected log and river to be present");
        assertNotNull(possibleMoves.returnAction("dig"), "Expected ground and shovel to be present");
        assertNotNull(possibleMoves.returnAction("blow"), "Expected horn to be present");

        GameAdditActions testProduced = (GameAdditActions)possibleMoves.returnAction("open");
        ArrayList<String> openProduced = new ArrayList<String>(List.of(new String[]{"cellar"}));
        assertEquals(openProduced, testProduced.getProduced(), "Expected cellar to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("unlock");
        ArrayList<String> unlockProduced = new ArrayList<String>(List.of(new String[]{"cellar"}));
        assertEquals(unlockProduced, testProduced.getProduced(), "Expected cellar to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("chop");
        ArrayList<String> chopProduced = new ArrayList<String>(List.of(new String[]{"log"}));
        assertEquals(chopProduced, testProduced.getProduced(), "Expected log to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("cut");
        ArrayList<String> cutProduced = new ArrayList<String>(List.of(new String[]{"log"}));
        assertEquals(cutProduced, testProduced.getProduced(), "Expected log to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("cut down");
        ArrayList<String> cutdownProduced = new ArrayList<String>(List.of(new String[]{"log"}));
        assertEquals(cutdownProduced, testProduced.getProduced(), "Expected log to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("drink");
        ArrayList<String> drinkProduced = new ArrayList<String>(List.of(new String[]{"health"}));
        assertEquals(drinkProduced, testProduced.getProduced(), "Expected health to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("fight");
        ArrayList<String> fightProduced = new ArrayList<String>(List.of(new String[]{}));
        assertEquals(fightProduced, testProduced.getProduced(), "Expected nothing to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("hit");
        ArrayList<String> hitProduced = new ArrayList<String>(List.of(new String[]{}));
        assertEquals(hitProduced, testProduced.getProduced(), "Expected nothing to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("attack");
        ArrayList<String> attackProduced = new ArrayList<String>(List.of(new String[]{}));
        assertEquals(attackProduced, testProduced.getProduced(), "Expected nothing to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("pay");
        ArrayList<String> payProduced = new ArrayList<String>(List.of(new String[]{"shovel"}));
        assertEquals(payProduced, testProduced.getProduced(), "Expected a shovel to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("bridge");
        ArrayList<String> bridgeProduced = new ArrayList<String>(List.of(new String[]{"clearing"}));
        assertEquals(bridgeProduced, testProduced.getProduced(), "Expected a clearing to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("dig");
        ArrayList<String> digProduced = new ArrayList<String>(List.of(new String[]{"gold", "hole"}));
        assertEquals(digProduced, testProduced.getProduced(), "Expected a hole and some gold to be in produced list");
        testProduced = (GameAdditActions)possibleMoves.returnAction("blow");
        ArrayList<String> blowProduced = new ArrayList<String>(List.of(new String[]{"lumberjack"}));
        assertEquals(blowProduced, testProduced.getProduced(), "Expected a lumberjack to be in produced list");
    }
}
