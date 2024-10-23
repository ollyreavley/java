package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SpecificActionsTests {

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
    void testUnlock(){
        String response;
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
    }

    @Test
    void testUnlock1(){
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
    void testunlock3(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "It was expected that the response would not contain \"key\".");
    }

    @Test
    void testUnlock4(){
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
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("key"), "It was expected that the response would not contain \"key\".");
    }

    @Test
    void testPotion(){
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertFalse(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");

    }

    @Test
    void testPotion2(){
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: drink potion");
        response = sendCommandToServer("simon: inventory");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\".");
    }

    @Test
    void testAttack(){
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
    }

    @Test
    void testAttack2(){
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
    void testAttack3() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "It was expected that the response would not contain \"key\".");
    }

    @Test
    void testAttack4() {
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
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("key"), "It was expected that the response would not contain \"key\".");
    }

    @Test
    void testUnlock5(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor with key");
        sendCommandToServer("simon: goto cellar");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cellar"), "Expected to have cellar in the response");
        assertTrue(response.contains("cabin"), "It was expected that there would be a path to the cabin");
        assertTrue(response.contains("Elf"), "It was expected that there would be an Elf in the cellar.");

    }

    @Test
    void testUnlock6(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor with key");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: attack Elf");
        Map<String, GamePlayer> players = server.getPlayer();
        assertEquals(2, players.get("simon").getHealth(), "Expected that health would reduce");
    }

    @Test
    void testAttack6(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor with key");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: attack Elf");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
    }

    @Test
    void testAttack7(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor with key");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: attack Elf");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertFalse(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");

    }

    @Test
    void testAttack8(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor with key");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: attack Elf");
        Map<String, GamePlayer> players = server.getPlayer();
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: drink potion");
        response = sendCommandToServer("simon: inventory");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\".");
        assertEquals(3, players.get("simon").getHealth(), "Expected that health would increase");
    }

    @Test
    void testCommandNoSubjectsA(){
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
    }

    @Test
    void testCommandNoSubjectsA2() {
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
    void testCommandNoSubjectsA3() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "It was expected that the response would not contain \"key\".");
    }

    @Test
    void testCommandNoSubjectsA4() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("cellar"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("key"), "It was expected that the response would not contain \"key\".");
    }

    @Test
    void testCommandNoSubjectsB(){
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
    }

    @Test
    void testCommandNoSubjectsB2() {
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
    void testCommandNoSubjectsB3() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "It was expected that the response would not contain \"key\".");

    }

    @Test
    void testCommandNoSubjectsB4() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertFalse(response.contains("cellar"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
    }

    @Test
    void testCombineCommand(){
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
    }

    @Test
    void textCombineCommand2(){
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
    void testCombineCommand3(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "It was expected that the response would not contain \"key\".");
    }

    @Test
    void combineCommand4(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
    }

    @Test
    void testCombineCommand5(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: unlock trapdoor and drink potion");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertFalse(response.contains("cellar"), "It was expected that the response would contain \"cellar\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
    }

    @Test
    void testCombineCommand2() {
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
    }

    @Test
    void testCombineCommandB2(){
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
    void testCombineCommandB3() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "It was expected that the response would not contain \"key\".");
    }

    @Test
    void testCombineCommandB4(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: unlock trapdoor with key and open");
        assertTrue(response.contains("unlock"), "Expected to unlock trapdoor to cellar");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("cellar"), "It was expected that the response would contain \"cellar\".");
    }

    @Test
    void testDeath(){
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("key"), "It was expected that the response would contain \"key\".");
        assertTrue(response.contains("tree"), "It was expected that the response would contain \"tree\".");
    }

    @Test
    void testDeath1(){
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
    void testDeath2(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "It was expected that the response would not contain \"key\".");

    }

    @Test
    void testDeath3(){
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
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("key"), "It was expected that the response would not contain \"key\".");

    }

    @Test
    void testDeath4(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor with key");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto cellar");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that there would be a path to the cabin");
        assertTrue(response.contains("Elf"), "It was expected that there would be an Elf in the cellar.");
    }

    @Test
    void testDeath4WithOpen(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: open trapdoor with key");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto cellar");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that there would be a path to the cabin");
        assertTrue(response.contains("Elf"), "It was expected that there would be an Elf in the cellar.");
    }

    @Test
    void testDeath5(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor with key");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: attack Elf");
        Map<String, GamePlayer> players = server.getPlayer();
        assertEquals(2, players.get("simon").getHealth(), "Expected that health would reduce");
    }

    @Test
    void testDeath6(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor with key");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: attack Elf");
        sendCommandToServer("simon: attack Elf");
        Map<String, GamePlayer> players = server.getPlayer();
        assertEquals(1, players.get("simon").getHealth(), "Expected that health would reduce");
    }

    @Test
    void testDeath7(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: open trapdoor with key");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: fight Elf");
        sendCommandToServer("simon: hit Elf");
        sendCommandToServer("simon: attack Elf");
        response = sendCommandToServer("simon: look");
        Map<String, GamePlayer> players = server.getPlayer();
        assertEquals(3, players.get("simon").getHealth(), "Expected that health would reduce");
        assertTrue(response.contains("cabin"), "It was expected that the response would contain \"cabin\".");
        assertTrue(response.contains("trapdoor"), "It was expected that the response would contain \"trapdoor\".");
        assertTrue(response.contains("forest"), "It was expected that the response would contain \"forest\".");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\".");
        assertTrue(response.contains("cellar"), "It was expected that the response would contain \"trapdoor\".");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("potion"), "It was expected that the response would not contain \"potion\".");
    }

    @Test
    void testDeath8(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor with key");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: attack Elf");
        sendCommandToServer("simon: attack Elf");
        sendCommandToServer("simon: attack Elf");
        sendCommandToServer("simon: goto cellar");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "It was expected that there would be a path to the cabin");
        assertTrue(response.contains("Elf"), "It was expected that there would be an Elf in the cellar.");
        assertTrue(response.contains("potion"), "It was expected that the response would contain \"potion\".");
    }
}
