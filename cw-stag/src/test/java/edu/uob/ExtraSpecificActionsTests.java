package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ExtraSpecificActionsTests {

    private GameServer server;
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities-extra.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "extended-actions-extra.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }

    String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
                "Server took too long to respond (probably stuck in an infinite loop)");
    }

 /*   @Test
    void testExtendedActions4(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: use trapdoor with key");
        //System.out.println(response);
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("cellar"), "It was expected that the response would contain \"cellar\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        assertTrue(response.contains("axe"), "It was expected that the response would contain \"axe\".");
        assertTrue(response.contains("coin"), "It was expected that the response would contain \"coin\".");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("key"), "It was expected that the response would not contain \"key\".");
    }*/

    @Test
    void moreExtendedActions22() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        sendCommandToServer("simon: please blow the horn");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: chop tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("simon: use key to unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: pay elf with coin");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: bridge the river");
        sendCommandToServer("simon: get fire");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("fire"), "Fire should be in the response");
        sendCommandToServer("simon: burn crossing with fire");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("clearing"), "Clearing should not be in the response");
    }

    @Test
    void moreExtendedActions23() {
        String response;
        response = sendCommandToServer("simon: sing");
        assertTrue(response.contains("sing"), "Response should contain sing");
    }

    @Test
    void moreExtendedActions24() {
        String response;
        response = sendCommandToServer("simon: laugh");
        //System.out.println(response);
        assertTrue(response.contains("laugh"), "Response should contain laugh");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: laugh");
        assertFalse(response.contains("laugh"), "Response should not contain laugh");
    }
    @Test
    void moreExtendedActions25() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        sendCommandToServer("simon: please blow the horn");
        sendCommandToServer("simon: goto forest");
        //response = sendCommandToServer("simon: cut do wn tree");
        //System.out.println(response);
    }

    @Test
    void moreExtendedActions26() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: use");
        assertTrue(response.contains("Multiple"), "Should not pass as multiple commands are possible in this location");
    }

    @Test
    void moreExtendedActions27() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: get chain");
        sendCommandToServer("simon: unlock trapdoor with key");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: lock door with chain");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("no paths"), "no paths will be possible from this location");
    }
}
