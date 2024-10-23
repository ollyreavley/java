package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Paths;
import java.io.File;
import java.time.Duration;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class BasicCommandsTests {

    private GameServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }

    String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
                "Server took too long to respond (probably stuck in an infinite loop)");
    }

    @Test
    void testLookCommand(){
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
    }

    @Test
    void testLookCommand2(){
        String response;
        response = sendCommandToServer("simon: look over there");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
    }

    @Test
    void testLookCommand3(){
        String response;
        response = sendCommandToServer("simon: look at the forest");
        assertFalse(response.contains("cabin"), "It was expected that the response would not contain \"cabin\".");
        assertFalse(response.contains("trapdoor"), "It was expected that the response would not contain \"trapdoor\".");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\".");
        assertFalse(response.contains("tree"), "It was expected that the response would not contain \"tree\".");
        assertFalse(response.contains("key"), "It was expected that the response would not contain \"key\".");
        assertFalse(response.contains("forest"), "It was expected that the response would not contain \"forest\".");
    }

    @Test
    void testLookCommand4(){
        String response;
        response = sendCommandToServer("simon: please look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
    }

    @Test
    void testInventoryCommand() {
        String response;
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\" as potion has not been picked up.");
        assertFalse(response.contains("trapdoor"), "It was expected that the response not would contain \"trapdoor\" as the trapdoor cannot be picked up.");
        assertFalse(response.contains("cabin"), "It was expected that the response not would contain \"cabin\" as the cabin cannot be picked up.");
        response = sendCommandToServer("simon: inventory");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\" as potion has not been picked up.");
        assertFalse(response.contains("trapdoor"), "It was expected that the response not would contain \"trapdoor\" as the trapdoor cannot be picked up.");
        assertFalse(response.contains("cabin"), "It was expected that the response not would contain \"cabin\" as the cabin cannot be picked up.");
    }

    @Test
    void testInventoryCommand3() {
        String response;
        response = sendCommandToServer("simon: please do an inv");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\" as potion has not been picked up.");
        assertFalse(response.contains("trapdoor"), "It was expected that the response not would contain \"trapdoor\" as the trapdoor cannot be picked up.");
        assertFalse(response.contains("cabin"), "It was expected that the response not would contain \"cabin\" as the cabin cannot be picked up.");
        response = sendCommandToServer("simon: please do an inventory");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\" as potion has not been picked up.");
        assertFalse(response.contains("trapdoor"), "It was expected that the response not would contain \"trapdoor\" as the trapdoor cannot be picked up.");
        assertFalse(response.contains("cabin"), "It was expected that the response not would contain \"cabin\" as the cabin cannot be picked up.");
    }

    @Test
    void testInventoryCommand2() {
        String response;
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\" as potion has not been picked up.");
        assertFalse(response.contains("trapdoor"), "It was expected that the response not would contain \"trapdoor\" as the trapdoor cannot be picked up.");
        assertFalse(response.contains("cabin"), "It was expected that the response not would contain \"cabin\" as the cabin cannot be picked up.");
        response = sendCommandToServer("simon: please do an inventory");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\" as potion has not been picked up.");
        assertFalse(response.contains("trapdoor"), "It was expected that the response not would contain \"trapdoor\" as the trapdoor cannot be picked up.");
        assertFalse(response.contains("cabin"), "It was expected that the response not would contain \"cabin\" as the cabin cannot be picked up.");
    }

    @Test
    void testGetCommand() {
        String response;
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\" as potion has been picked up.");
        assertFalse(response.contains("trapdoor"), "It was expected that the response not would contain \"trapdoor\" as the trapdoor cannot be picked up.");
        assertFalse(response.contains("cabin"), "It was expected that the response not would contain \"cabin\" as the cabin cannot be picked up.");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\" as this should have been picked up.");
    }

    @Test
    void testGetCommandNoSubject() {
        String response;
        response = sendCommandToServer("simon: get");
        assertTrue(response.contains("Get command did not have an artefact to pick up"), "Error message expected as command did not have an item to get.");
    }

    @Test
    void testGetCommand2() {
        String response;
        sendCommandToServer("simon: please get potion");
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\" as potion has been picked up.");
        assertFalse(response.contains("trapdoor"), "It was expected that the response not would contain \"trapdoor\" as the trapdoor cannot be picked up.");
        assertFalse(response.contains("cabin"), "It was expected that the response not would contain \"cabin\" as the trapdoor cannot be picked up.");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\" as this should have been picked up.");
    }

    @Test
    void testDropCommand() {
        String response;
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: drop potion");
        response = sendCommandToServer("simon: inventory");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\" as potion has been dropped.");
        assertFalse(response.contains("trapdoor"), "It was expected that the response not would contain \"trapdoor\" as the trapdoor cannot be picked up.");
        assertFalse(response.contains("cabin"), "It was expected that the response not would contain \"cabin\" as the cabin cannot be picked up.");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\" as this should have been dropped.");
    }

    @Test
    void testDropCommand2() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: please drop potion");
        response = sendCommandToServer("simon: inventory");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\" as potion has been dropped.");
        assertFalse(response.contains("trapdoor"), "It was expected that the response not would contain \"trapdoor\" as the trapdoor cannot be picked up.");
        assertFalse(response.contains("cabin"), "It was expected that the response not would contain \"cabin\" as the cabin cannot be picked up.");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\" as this should have been dropped.");
    }

    @Test
    void testDropCommand3() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: please drop the potion over there");
        response = sendCommandToServer("simon: inventory");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\" as potion has been dropped.");
        assertFalse(response.contains("trapdoor"), "It was expected that the response not would contain \"trapdoor\" as the trapdoor cannot be picked up.");
        assertFalse(response.contains("cabin"), "It was expected that the response not would contain \"cabin\" as the cabin cannot be picked up.");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\" as this should have been dropped.");
    }

    @Test
    void testGotoCommand() {
        String response;
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
    }

    @Test
    void testGotoCommand2() {
        String response;
        sendCommandToServer("simon: goto forest forest");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertFalse(response.contains("tree"), "It was expected that the response would contain \"tree\".");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\"");
    }

    @Test
    void testGotoCommand3() {
        String response;
        sendCommandToServer("simon: please goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
    }

    @Test
    void testGotoCommand4() {
        String response;
        sendCommandToServer("simon: please goto the forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
    }

    @Test
    void testGotoCommand5() {
        String response;
        sendCommandToServer("simon: please goto the forest over there");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
    }

    @Test
    void testGotoCommand6() {
        String response;
        sendCommandToServer("simon: please goto the forest over there");
        response = sendCommandToServer("simon: look at the forest");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
    }

    @Test
    void testCombinedBasicCommand() {
        String response;
        response = sendCommandToServer("simon: goto forest and get key");
        assertTrue(response.contains("Too many commands entered."), "It was expected that this would fail as two commands were entered");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
    }

    @Test
    void testCombinedBasicCommand2() {
        String response;
        response = sendCommandToServer("simon: look and get key");
        assertTrue(response.contains("Too many commands entered."), "It was expected that this would fail as two commands were entered");
    }

    @Test
    void testCombinedBasicCommand3() {
        String response;
        response = sendCommandToServer("simon: get potion and drop potion");
        assertTrue(response.contains("Too many commands entered."), "It was expected that this would fail as two commands were entered");
    }

    @Test
    void testCombinedBasicCommand4() {
        String response;
        response = sendCommandToServer("simon: get potion and goto forest");
        assertTrue(response.contains("Too many commands entered."), "It was expected that this would fail as two commands were entered");
    }

    @Test
    void testCombinedBasicCommand5() {
        String response;
        response = sendCommandToServer("simon: please do an inventory and goto the forest");
        assertTrue(response.contains("Too many commands entered."), "It was expected that this would fail as two commands were entered");
    }

    @Test
    void testGetInWrongLocation() {
        String response;
        response = sendCommandToServer("simon: get key");
        assertTrue(response.contains("artifact does not exist in this location."), "It was expected that this would fail as the key is not in the cabin.");
    }

    @Test
    void testCombinedGet() {
        String response;
        sendCommandToServer("simon: please goto the forest over there");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: drop key");
        response = sendCommandToServer("simon: get key and potion");
        assertTrue(response.contains("Silly"), "error message should be returned");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("do not hold anything"), "expected to hold nothing");
    }

    @Test
    void testNoColon() {
        String response;
        response = sendCommandToServer("simon please goto the forest over there");
        assertTrue(response.contains("It appears as through no name has been entered please enter in following format \"player name: command\""), "Response should be an error message.");
    }

    @Test
    void testNoColon2() {
        String response;
        response = sendCommandToServer("simon; please goto the forest over there");
        assertTrue(response.contains("It appears as through no name has been entered please enter in following format \"player name: command\""), "Response should be an error message.");
    }

    @Test
    void testNoColon3() {
        String response;
        response = sendCommandToServer(": please goto the forest over there");
        assertTrue(response.contains("It appears as through no name has been entered please enter in following format \"player name: command\""), "Response should be an error message.");
    }

    @Test
    void testNoColon4() {
        String response;
        response = sendCommandToServer("simon:");
        assertTrue(response.contains("It appears as through no name has been entered please enter in following format \"player name: command\""), "Response should be an error message.");
    }

    @Test
    void testInvalidDrop(){
        String response;
        response = sendCommandToServer("simon: drop potion");
        assertTrue(response.contains("you tried to drop an item you do not hold or you tried to drop too many items"), "Should be invalid as does not hold the potion");
    }

    @Test
    void testInvalidDrop2(){
        String response;
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: potion drop");
        assertTrue(response.contains("\"drop\" must come before the artifact being dropped."), "Should be invalid as potion came before drop");
    }

    @Test
    void testInvalidGet(){
        String response;
        response = sendCommandToServer("simon: get trapdoor");
        assertTrue(response.contains("you cannot carry the trapdoor"), "Should be invalid as trapdoor cannot be carried");
    }

    @Test
    void testInvalidGet2(){
        String response;
        response = sendCommandToServer("simon: get cabin");
        assertTrue(response.contains("Get command did not have an artefact to pick up"), "Should be invalid as cabin cannot be carried");
    }

    @Test
    void testInvalidGet3(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor with key");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto cellar");
        response = sendCommandToServer("simon: get Elf");
        assertTrue(response.contains("you cannot carry the elf"), "Should be invalid as the Elf cannot be carried");
    }

    @Test
    void testInvalidDrop3(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: drop potion and key");
        assertTrue(response.contains("ou tried to drop an item you do not hold or you tried to drop too many items"), "Should be invalid as the tried to drop two items");
    }

    @Test
    void testInvalidDrop4(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: key drop");
        assertTrue(response.contains("must come before the artifact being droppe"), "Should be invalid as the tried to drop two items");
    }

    @Test
    void testInvalidDrop5(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: key drop");
        //System.out.println(response);
        assertTrue(response.contains("must come before the artifact being droppe"), "Should be invalid as drop came after item being dropped");
    }

    @Test
    void testInvalidGet5(){
        String response;
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: key get");
        //System.out.println(response);
        assertTrue(response.contains("Get command did not have an artefact to pick up or artifact does not exist in this location"), "Should be invalid as get came after item being got");
    }

    @Test
    void invalidGoToCommand(){
        String response;
        response = sendCommandToServer("simon: forest goto");
        assertTrue(response.contains("you have entered an invalid command"), "Should not be valid as goto came second");
    }
}
