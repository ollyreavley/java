package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ExtendedSpecificActionsTests {

    private GameServer server;
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }

    String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
                "Server took too long to respond (probably stuck in an infinite loop)");
    }

    @Test
    void testExtendedActions(){
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        assertTrue(response.contains("axe"), "It was expected that the response would contain \"axe\".");
        assertTrue(response.contains("coin"), "It was expected that the response would contain \"coin\".");
    }

    @Test
    void testExtendedActions1(){
        String response;
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
    }

    @Test
    void testExtendedActions2(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertFalse(response.contains("key"), "It was expected that the response would not contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");

    }

    @Test
    void testExtendedActions3(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        assertTrue(response.contains("axe"), "It was expected that the response would contain \"axe\".");
        assertTrue(response.contains("coin"), "It was expected that the response would contain \"coin\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "It was expected that the response would not contain \"key\".");
    }

    @Test
    void testExtendedActions4(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor with key");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("cellar"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        assertTrue(response.contains("axe"), "It was expected that the response would contain \"axe\".");
        assertTrue(response.contains("coin"), "It was expected that the response would contain \"coin\".");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("key"), "It was expected that the response would not contain \"key\".");
    }

    @Test
    void testCommandWrongSubject(){
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        assertTrue(response.contains("axe"), "It was expected that the response would contain \"axe\".");
        assertTrue(response.contains("coin"), "It was expected that the response would contain \"coin\".");
    }

    @Test
    void testCommandWrongSubject1(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertFalse(response.contains("key"), "It was expected that the response would not contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
    }

    @Test
    void testCommandWrongSubject2(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        assertTrue(response.contains("axe"), "It was expected that the response would contain \"axe\".");
        assertTrue(response.contains("coin"), "It was expected that the response would contain \"coin\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
    }

    @Test
    void testCommandWrongSubject3() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: get axe");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("axe"), "It was expected that the response would contain \"axe\".");
        sendCommandToServer("simon: unlock trapdoor with axe");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertFalse(response.contains("cellar"), "It was expected that the response would contain \"cellar\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("axe"), "It was expected that the response would contain \"axe\".");
    }

    @Test
    void testNewCommands(){
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        assertTrue(response.contains("axe"), "It was expected that the response would contain \"axe\".");
        assertTrue(response.contains("coin"), "It was expected that the response would contain \"coin\".");
        sendCommandToServer("simon: get axe");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("axe"), "It was expected that the response would contain \"axe\".");
    }

    @Test
    void testNewCommands2() {
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
        assertFalse(response.contains("log"), "It was expected that the response would not contain \"log\".");
        sendCommandToServer("simon: chop tree with axe");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertFalse(response.contains("tree"), "It was expected that the response would not contain \"tree\".");
        assertTrue(response.contains("log"), "It was expected that the response would contain \"log\".");
    }

    @Test
    void moreExtendedActions(){
        String response;
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("axe"), "Axe expected to be on the floor of the cabin.");
        assertFalse(response.contains("potion"), "Potion is not expected to be on the floor of the cabin.");
        assertTrue(response.contains("cabin"), "Simon should still be in the cabin");
        assertTrue(response.contains("trapdoor"), "Trapdoor should still be in the cabin");
        assertTrue(response.contains("coin"), "Coin has not been picked up yet");
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("potion"), "potion should be in inventory");
    }

    @Test
    void moreExtendedActions2(){
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("axe"), "Axe not expected to be on the floor of the cabin.");
        assertFalse(response.contains("potion"), "Potion is not expected to be on the floor of the cabin.");
        assertTrue(response.contains("cabin"), "Simon should still be in the cabin");
        assertTrue(response.contains("trapdoor"), "Trapdoor should still be in the cabin");
        assertTrue(response.contains("coin"), "Coin has not been picked up yet");
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("potion"), "potion should be in inventory");
        assertTrue(response.contains("axe"), "axe should be in inventory");
    }

    @Test
    void moreExtendedActions3() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("axe"), "Axe not expected to be on the floor of the cabin.");
        assertFalse(response.contains("potion"), "Potion is not expected to be on the floor of the cabin.");
        assertTrue(response.contains("cabin"), "Simon should still be in the cabin");
        assertTrue(response.contains("trapdoor"), "Trapdoor should still be in the cabin");
        assertFalse(response.contains("coin"), "Coin has been picked up yet");
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("potion"), "potion should be in inventory");
        assertTrue(response.contains("axe"), "axe should be in inventory");
        assertTrue(response.contains("coin"), "coin should be in inventory");
    }

    @Test
    void moreExtendedActions4() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "Forest is expected in the response");
        assertTrue(response.contains("tree"), "Tree is expected in the response");
        assertTrue(response.contains("key"), "Key is expected in the response");
        assertTrue(response.contains("cabin"), "Cabin is expected in the response");
        assertTrue(response.contains("riverbank"), "Riverbank is expected int the response");
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("potion"), "potion should be in inventory");
        assertTrue(response.contains("axe"), "axe should be in inventory");
        assertTrue(response.contains("coin"), "coin should be in inventory");
    }

    @Test
    void moreExtendedActions5() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("potion"), "potion should be in inventory");
        assertTrue(response.contains("axe"), "axe should be in inventory");
        assertTrue(response.contains("coin"), "coin should be in inventory");
        assertTrue(response.contains("key"), "key should be in inventory");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "Forest is expected in the response");
        assertTrue(response.contains("tree"), "Tree is expected in the response");
        assertFalse(response.contains("key"), "Key is not expected in the response");
        assertTrue(response.contains("cabin"), "Cabin is expected in the response");
        assertTrue(response.contains("riverbank"), "Riverbank is expected int the response");
    }

    @Test
    void moreExtendedActions6() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto riverbank");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "Forest is expected in the response");
        assertTrue(response.contains("river"), "River is expected in the response");
        assertTrue(response.contains("horn"), "Horn is expected in the response");
        assertTrue(response.contains("riverbank"), "Riverbank is expected int the response");
    }

    @Test
    void moreExtendedActions7() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "Forest is expected in the response");
        assertTrue(response.contains("river"), "River is expected in the response");
        assertFalse(response.contains("horn"), "Horn is not expected in the response");
        assertTrue(response.contains("riverbank"), "Riverbank is expected int the response");
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("potion"), "potion should be in inventory");
        assertTrue(response.contains("axe"), "axe should be in inventory");
        assertTrue(response.contains("coin"), "coin should be in inventory");
        assertTrue(response.contains("key"), "key should be in inventory");
        assertTrue(response.contains("horn"), "horn should be in inventory");
    }

    @Test
    void moreExtendedActions8() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        response = sendCommandToServer("simon: please blow the horn");
        assertTrue(response.contains("lumberjack"), "A lumberjack was expected to appear");
    }
    @Test
    void moreExtendedActions9() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        sendCommandToServer("simon: please blow the horn");
        response = sendCommandToServer("simon: goto cabin");
        assertTrue(response.contains("invalid"), "Should not be able to go to the cabin from here");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "Forest is expected to be in the response");
        assertTrue(response.contains("cabin"), "Cabin is expected to be in the response");
        assertTrue(response.contains("tree"), "Tree is expected to be in the response");
        assertFalse(response.contains("lumberjack"), "Lumberjack is not expected to be in the forest");
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("potion"), "potion should be in inventory");
        assertTrue(response.contains("axe"), "axe should be in inventory");
        assertTrue(response.contains("coin"), "coin should be in inventory");
        assertTrue(response.contains("key"), "key should be in inventory");
        assertTrue(response.contains("horn"), "horn should be in inventory");
        assertFalse(response.contains("lumberjack"), "lumberjack should not be in inventory");
    }

    @Test
    void moreExtendedActions10() {
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
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "Forest is expected to be in the response");
        assertTrue(response.contains("cabin"), "Cabin is expected to be in the response");
        assertFalse(response.contains("tree"), "Tree is expected to be in the response");
        assertTrue(response.contains("log"), "Log is expected in the response");
    }

    @Test
    void moreExtendedActions11() {
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
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("potion"), "potion should be in inventory");
        assertTrue(response.contains("axe"), "axe should be in inventory");
        assertTrue(response.contains("coin"), "coin should be in inventory");
        assertTrue(response.contains("key"), "key should be in inventory");
        assertTrue(response.contains("horn"), "horn should be in inventory");
        assertTrue(response.contains("log"), "log should be in the inventory");
    }

    @Test
    void moreExtendedActions12() {
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
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "cabin should be in location");
        assertTrue(response.contains("trapdoor"), "trapdoor should be in location");
        assertTrue(response.contains("forest"), "forest path should be in location");
        assertFalse(response.contains("key"), "key should be in inventory not in location");
        assertFalse(response.contains("horn"), "horn should be in inventory not in location");
    }

    @Test
    void moreExtendedActions13() {
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
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: use key to unlock trapdoor");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "cabin should be in location");
        assertTrue(response.contains("trapdoor"), "trapdoor should be in location");
        assertTrue(response.contains("forest"), "forest path should be in location");
        assertTrue(response.contains("cellar"), "cellar path should be in location");
    }

    @Test
    void moreExtendedActions14() {
        String response;;
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
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: use key to unlock trapdoor");
        assertTrue(response.contains("cellar"), "narration should include cellar");
        sendCommandToServer("simon: goto cellar");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "cabin path should be in location");
        assertTrue(response.contains("Elf"), "elf should be in location");
        assertTrue(response.contains("cellar"), "cellar should be in location");
    }

    @Test
    void moreExtendedActions15() {
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
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: use key to unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        response = sendCommandToServer("simon: hit elf");
        assertTrue(response.contains("health"), "response should contain health");
        assertTrue(response.contains("lose"), "response should contain lose");
        Map<String, GamePlayer> players = server.getPlayer();
        assertEquals(2, players.get("simon").getHealth(), "Expected that health would reduce");
    }

    @Test
    void moreExtendedActions16() {
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
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: use key to unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: hit elf");
        response = sendCommandToServer("simon: pay elf with coin");
        assertTrue(response.contains("shovel"), "Expected that a shovel would be produced");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cellar"), "Expected that message would contain cellar");
        assertTrue(response.contains("shovel"), "Expected that message would contain hovel");
        assertTrue(response.contains("Elf"), "Expected that message would contain elf");
    }

    @Test
    void moreExtendedActions17() {
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
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: use key to unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: pay elf with coin");
        sendCommandToServer("simon: get shovel");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cellar"), "Expected that message would contain cellar");
        assertFalse(response.contains("shovel"), "Expected that message would contain hovel");
        assertTrue(response.contains("Elf"), "Expected that message would contain elf");
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("shovel"), "Expected that message would contain hovel");
    }

    @Test
    void moreExtendedActions18() {
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
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: use key to unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: pay elf with coin");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cellar"), "Expected that message would contain cellar");
        assertTrue(response.contains("cabin"), "Expected that message would contain cabin");
        assertTrue(response.contains("forest"), "Expected that message would contain fores");
    }
    @Test
    void moreExtendedActions19() {
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
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: use key to unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: pay elf with coin");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "expected that response would contain forest");
        assertTrue(response.contains("cabin"), "expected that the response would contain cabin");
        assertTrue(response.contains("riverbank"), "expected that the response would contain riverbank");
        assertFalse(response.contains("tree"), "not expected to contain tree as this has been cut down");
    }

    @Test
    void moreExtendedActions20() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        sendCommandToServer("simon: please blow the horn");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "expected that response would contain forest");
        assertTrue(response.contains("wood cutter"), "expected that the response would contain lumberjack");
        assertTrue(response.contains("riverbank"), "expected that the response would contain riverbank");
        assertTrue(response.contains("river"), "expected that the response would contain river");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: chop tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: use key to unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: pay elf with coin");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "expected that response would contain forest");
        assertTrue(response.contains("wood cutter"), "expected that the response would contain lumberjack");
        assertTrue(response.contains("riverbank"), "expected that the response would contain riverbank");
        assertTrue(response.contains("river"), "expected that the response would contain river");
    }

    @Test
    void moreExtendedActions21() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        sendCommandToServer("simon: please blow the horn");;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: chop tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: use key to unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: pay elf with coin");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        response = sendCommandToServer("simon: bridge the river using the log");
        assertTrue(response.contains("other side"), "Expected that the response would include other side");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("clearing"), "Expected that the response would contains clearing");
    }

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
        sendCommandToServer("simon: please blow the horn");;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: chop tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: use key to unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: pay elf with coin");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: bridge the river using the log");
        sendCommandToServer("simon: goto clearing");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("soil"), "Expected that the response would contain ground");
        assertTrue(response.contains("riverbank"), "expected that the response would contain riverbank");
    }

    @Test
    void moreExtendedActions23() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        sendCommandToServer("simon: please blow the horn");;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: chop tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: use key to unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: pay elf with coin");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: bridge the river using the log");
        sendCommandToServer("simon: goto clearing");
        sendCommandToServer("simon: dig the ground with the shovel");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("hole"), "response should contain the hole");
        assertTrue(response.contains("gold"), "response should contain the gold produced");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("hole"), "response should not contain the hole");
        assertFalse(response.contains("gold"), "response should not contain the gold produced");
    }

    @Test
    void moreExtendedActions24() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        sendCommandToServer("simon: please blow the horn");;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: chop tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: use key to unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: pay elf with coin");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: bridge the river using the log");
        sendCommandToServer("simon: goto clearing");
        sendCommandToServer("simon: dig the ground with the shovel");
        sendCommandToServer("simon: get gold");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("hole"), "response should contain the hole");
        assertFalse(response.contains("gold"), "response should not contain the gold produced");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("hole"), "response should not contain the hole");
        assertTrue(response.contains("gold"), "response should contain the gold produced");
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
        sendCommandToServer("simon: please blow the horn");;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut tree");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("tree"), "Expected that tree would not be there");
        sendCommandToServer("simon: get log");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("log"), "response should contain log");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: use key to unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: attack elf");
        Map<String, GamePlayer> players = server.getPlayer();
        assertEquals(2, players.get("simon").getHealth(), "Expected that health would reduce");
        sendCommandToServer("simon: pay elf with coin");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: bridge the river using the log");
        sendCommandToServer("simon: goto clearing");
        sendCommandToServer("simon: dig the ground with the shovel");
        sendCommandToServer("simon: get gold");
        sendCommandToServer("simon: blow the horn");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("hole"), "response should contain the hole");
        assertFalse(response.contains("gold"), "response should not contain the gold produced");
        assertTrue(response.contains("wood cutter"), "response should contain wood cutter");
    }

    @Test
    void moreExtendedActions26() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        sendCommandToServer("simon: please blow the horn");;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut down tree");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("tree"), "Expected that tree would not be there");
        sendCommandToServer("simon: get log");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("log"), "response should contain log");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: use key to open trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: hit elf");
        Map<String, GamePlayer> players = server.getPlayer();
        assertEquals(2, players.get("simon").getHealth(), "Expected that health would reduce");
        sendCommandToServer("simon: attack elf");
        assertEquals(1, players.get("simon").getHealth(), "Expected that health would reduce");
        sendCommandToServer("simon: pay elf with coin");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        response = sendCommandToServer("simon: bridge the river using the log");
        assertTrue(response.contains("other side"), "Expected that the response would include other side");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("clearing"), "Expected that the response would contains clearing");
    }

    @Test
    void testCutDown() {
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: cut down tree with axe");
        assertTrue(response.contains("You cut down the tree with the axe"), "Expected that the response would be the narration");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("log"), "expected that the response would contain a log");
        assertFalse(response.contains("tree"), "expected that the response would not contain a tree as should be cut down");
    }

    @Test
    void testSimpleMultiplayer7() {
        String response;
        sendCommandToServer("simon: look");
        sendCommandToServer("sion: look");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: cut down tree with axe");
        assertTrue(response.contains("You cut down the tree with the axe"), "should have cut down tree");
        sendCommandToServer("simon: get log");
        response = sendCommandToServer("simon: inv on simon");
        assertTrue(response.contains("key"), "Response should contain key");
        assertTrue(response.contains("log"), "Response should contain log");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "Response should contain cabin");
    }
}
