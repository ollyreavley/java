package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class MultiPlayerTests {

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
    void testSimpleMultiplayer(){
        String response;
        response = sendCommandToServer("Simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("sion: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        assertTrue(response.contains("Simon"), "It was expected that the response would contain simon.");
    }

    @Test
    void testSimpleMultiplayer2(){
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("sion: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        assertTrue(response.contains("simon"), "It was expected that the response would contain simon.");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
        response = sendCommandToServer("sion: inv");
        assertTrue(response.contains("You do not hold anything"), "It was expected that the response would contain \"You do not hold anything\" inventory is empty.");
        response = sendCommandToServer("sion: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
    }

    @Test
    void testSimpleMultiplayer3() {
        String response;
        sendCommandToServer("simon: look" );
        sendCommandToServer("sion: look" );
        sendCommandToServer("simon: goto forest" );
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "Expected for response to contain key.");
        sendCommandToServer("sion: goto forest");
        sendCommandToServer("sion: get key");
        response = sendCommandToServer("sion: inv");
        assertTrue(response.contains("You do not hold anything"), "It was expected that the response would contain \"You do not hold anything\" inventory is empty.");
        sendCommandToServer("simon: goto     cabin");
        sendCommandToServer("simon: unlock     trapdoor");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        assertTrue(response.contains("cellar"), "It was expected that a path to the cellar would appear.");
        sendCommandToServer("sion: goto cabin");
        sendCommandToServer("sion: goto cellar");
        response = sendCommandToServer("sion: look");
        assertTrue(response.contains("cabin"), "It was expected that there would be a path to the cabin");
        assertTrue(response.contains("Elf"), "It was expected that there would be an Elf in the cellar.");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
    }

    @Test
    void testSimpleMultiplayer4(){
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("sion: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        assertTrue(response.contains("simon"), "It was expected that the response would contain simon.");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        assertTrue(response.contains("sion"), "It was expected that the response would contain sion.");
        sendCommandToServer("sion: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        assertFalse(response.contains("sion"), "It was expected that the response would contain sion.");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("sion: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("simon"), "It was expected that the response would contain simon.");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("sion"), "It was expected that the response would contain sion.");
    }

    @Test
    void testSimpleMultiplayer5() {
        String response;
        sendCommandToServer("simon: look");
        sendCommandToServer("sion: look");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: inv on sion");
        assertTrue(response.contains("Silly simon you can't do an inventory on a different player!"), "Expected error message re different player as inv should be a one word command");
    }

    @Test
    void testSimpleMultiplayer6() {
        String response;
        sendCommandToServer("simon: look");
        sendCommandToServer("sion: look");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: inv on simon");
        assertTrue(response.contains("key"), "Response should contain key");
    }

    @Test
    void testMultiplayer7(){
        String response;

    }
}
