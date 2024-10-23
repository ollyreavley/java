package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

import static edu.uob.TokenShortHand.*;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTests {

    private DBServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    public void setup() {
        server = new DBServer();
    }

    // Random name generator - useful for testing "bare earth" queries (i.e. where tables don't previously exist)
    private String generateRandomName()
    {
        String randomName = "";
        for(int i=0; i<10; i++) randomName += (char)( 97 + (Math.random() * 25.0));
        return randomName;
    }

    private String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
        "Server took too long to respond (probably stuck in an infinite loop)");
    }

    @Test
    public void basicUseDBParse() throws ParserExceptions, IOException {
        String instructions = "USE CENSUS;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBUseCmd use = (DBUseCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"USE CENSUS;\" is acceptable.";
        assertEquals(use.tokens.get(0), USE, failMessage);
        assertEquals(use.tokens.get(1), DATABASE, failMessage);
    }

    @Test
    public void basicReservedWordTest() throws ParserExceptions {
        String instructions = "CREATE DATABASE table;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        assertThrows(ParserExceptions.InvalidDatabaseOrTableName.class, () -> parsedInstructions.commandType(rawTokens));
    }

    @Test
    public void basicReservedWordTest1() throws ParserExceptions {
        String instructions = "CREATE table census ( join.words );";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        assertThrows(ParserExceptions.InvalidDatabaseOrTableName.class, () -> parsedInstructions.commandType(rawTokens));
    }

    @Test
    public void basicReservedWordTest2() throws ParserExceptions {
        String instructions = "CREATE table census ( id );";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        assertThrows(ParserExceptions.InvalidDatabaseOrTableName.class, () -> parsedInstructions.commandType(rawTokens));
    }

    @Test
    public void basicUseDBParse1() throws ParserExceptions, IOException {
        String instructions = "USE CENSUS1;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBUseCmd use = (DBUseCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"USE CENSUS;\" is acceptable.";
        assertEquals(use.tokens.get(0), USE, failMessage);
        assertEquals(use.tokens.get(1), DATABASE, failMessage);
    }

    @Test
    public void basicUseDBParse2() throws ParserExceptions, IOException {
        String instructions = "USE CEN1SUS;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBUseCmd use = (DBUseCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"USE CEN1SUS;\" is acceptable.";
        assertEquals(use.tokens.get(0), USE, failMessage);
        assertEquals(use.tokens.get(1), DATABASE, failMessage);
    }

    @Test
    public void basicCreateDBParse() throws ParserExceptions, IOException {
        String instructions = "CREATE TABLE votes;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBCreateCmd create = (DBCreateCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"CREATE TABLE votes;\" is acceptable.";
        assertEquals(create.tokens.get(0), CREATE, failMessage);
        assertEquals(create.tokens.get(1), TABLE, failMessage);
    }

    @Test
    public void basicCreateDBParse2() throws ParserExceptions, IOException {
        String instructions = "create TABLE votes (name, age);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBCreateCmd create = (DBCreateCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"create TABLE votes (name, age);\" is acceptable.";
        assertEquals(create.tokens.get(0), CREATE, failMessage);
        assertEquals(create.tokens.get(1), TABLE, failMessage);
        assertEquals(create.tokens.get(2), ATTRIBUTE, failMessage);
        assertEquals(create.tokens.get(3), ATTRIBUTE, failMessage);
    }

    @Test
    public void basicCreateDBParse3() throws ParserExceptions, IOException {
        String instructions = "create TABLE votes (votes.name, votes.age);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBCreateCmd create = (DBCreateCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"create TABLE votes (votes.name, votes.age);\" is acceptable.";
        assertEquals(create.tokens.get(0), CREATE, failMessage);
        assertEquals(create.tokens.get(1), TABLE, failMessage);
        assertEquals(create.tokens.get(2), ATTRIBUTE, failMessage);
        assertEquals(create.tokens.get(3), ATTRIBUTE, failMessage);
    }

    @Test
    public void basicCreateDBParse4() throws ParserExceptions, IOException {
        String instructions = "CREATE DATABASE votes;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBCreateCmd create = (DBCreateCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"CREATE DATABASE votes;\" is acceptable.";
        assertEquals(create.tokens.get(0), CREATE, failMessage);
        assertEquals(create.tokens.get(1), DATABASE, failMessage);
    }

    @Test
    public void basicDropDBParse() throws ParserExceptions, IOException {
        String instructions = "DROP TABLE votes;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBDropCmd drop = (DBDropCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"DROP TABLE votes;\" is acceptable.";
        assertEquals(drop.tokens.get(0), DROP, failMessage);
        assertEquals(drop.tokens.get(1), TABLE, failMessage);
    }

    @Test
    public void basicDropDBParse1() throws ParserExceptions, IOException {
        String instructions = "DROP TABLE votes;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBDropCmd drop = (DBDropCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"DROP TABLE votes;\" is acceptable.";
        assertEquals(drop.tokens.get(0), DROP, failMessage);
        assertEquals(drop.tokens.get(1), TABLE, failMessage);
    }

    @Test
    public void basicDropDBParse2() throws ParserExceptions, IOException {
        String instructions = "DROP DATABASE census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBDropCmd drop = (DBDropCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"DROP DATABASE votes;\" is acceptable.";
        assertEquals(drop.tokens.get(0), DROP, failMessage);
        assertEquals(drop.tokens.get(1), DATABASE, failMessage);
    }

    @Test
    public void basicSelectDBParse() throws ParserExceptions, IOException {
        String instructions = "select people FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBSelectCmd select = (DBSelectCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"select people FROM census;\" is acceptable.";
        assertEquals(SELECT, select.tokens.get(0), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(1), failMessage);
    }

    @Test
    public void basicErrorTest() throws ParserExceptions {
        String instructions = "BLAH people FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), "Was expecting this to fail as \"BLAH\" is not an accepted command.");
    }

    @Test
    public void basicSelectDBParse1() throws ParserExceptions, IOException {
        String instructions = "select * FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBSelectCmd select = (DBSelectCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"select * FROM census;\" is acceptable.";
        assertEquals(SELECT, select.tokens.get(0), failMessage);
        assertEquals(ALL, select.tokens.get(1), failMessage);
    }

    @Test
    public void basicSelectDBParse2() throws ParserExceptions, IOException {
        String instructions = "select people, votes FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBSelectCmd select = (DBSelectCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"select people, votes FROM census;\" is acceptable.";
        assertEquals(SELECT, select.tokens.get(0), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(1), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(2), failMessage);
    }

    @Test
    public void basicSelectDBParse3() throws ParserExceptions, IOException {
        String instructions = "select votes.people, test.votes FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBSelectCmd select = (DBSelectCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"select votes.people, test.votes FROM census;\" is acceptable.";
        assertEquals(SELECT, select.tokens.get(0), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(1), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(2), failMessage);
    }

    @Test
    public void basicAlterDPTest() throws ParserExceptions, IOException {
        String instructions = "ALTER TABLE votes ADD ward;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBAlterCmd alter = (DBAlterCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"ALTER TABLE votes ADD ward;\" is acceptable.";
        assertEquals(alter.tokens.get(0), ALTER, failMessage);
        assertEquals(alter.tokens.get(1), TABLE, failMessage);
        assertEquals(alter.tokens.get(2), ADD, failMessage);
        assertEquals(alter.tokens.get(3), ATTRIBUTE, failMessage);
    }

    @Test
    public void basicAlterDPTest1() throws ParserExceptions, IOException {
        String instructions = "ALTER TABLE votes ADD votes.ward;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBAlterCmd alter = (DBAlterCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"ALTER TABLE votes ADD votes.ward;\" is acceptable.";
        assertEquals(alter.tokens.get(0), ALTER, failMessage);
        assertEquals(alter.tokens.get(1), TABLE, failMessage);
        assertEquals(alter.tokens.get(2), ADD, failMessage);
        assertEquals(alter.tokens.get(3), ATTRIBUTE, failMessage);
    }

    @Test
    public void basicAlterDPTest2() throws ParserExceptions, IOException {
        String instructions = "ALTER TABLE votes DROP votes.ward;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBAlterCmd alter = (DBAlterCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"ALTER TABLE votes DROP votes.ward;\" is acceptable.";
        assertEquals(alter.tokens.get(0), ALTER, failMessage);
        assertEquals(alter.tokens.get(1), TABLE, failMessage);
        assertEquals(alter.tokens.get(2), DROP, failMessage);
        assertEquals(alter.tokens.get(3), ATTRIBUTE, failMessage);
    }

    @Test
    public void basicInsertIntoDPTest1() throws ParserExceptions, IOException {
        String instructions = "INSERT INTO votes VALUES ( 'hello', 'trick', '');";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBInsertCmd alter = (DBInsertCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"INSERT INTO votes VALUES ( 'hello', 'trick', '');\" is acceptable.";
        assertEquals(alter.tokens.get(0), INSERT, failMessage);
        assertEquals(alter.tokens.get(1), STRING, failMessage);
        assertEquals(alter.values.get(0), "hello");
        assertEquals(alter.tokens.get(2), STRING, failMessage);
        assertEquals(alter.tokens.get(3), STRING, failMessage);
    }

    @Test
    public void basicInsertIntoDPTest2() throws ParserExceptions, IOException {
        String instructions = "INSERT INTO votes VALUES ( TRUE, FALSE, '556788', '$');";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBInsertCmd alter = (DBInsertCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"INSERT INTO votes VALUES ( TRUE, FALSE, '556788', '$');\" is acceptable.";
        assertEquals(alter.tokens.get(0), INSERT, failMessage);
        assertEquals(alter.tokens.get(1), BOOLEAN, failMessage);
        assertEquals(alter.tokens.get(2), BOOLEAN, failMessage);
        assertEquals(alter.tokens.get(3), STRING, failMessage);
        assertEquals(alter.tokens.get(4), STRING, failMessage);
    }

    @Test
    public void basicInsertIntoDPTest3() throws ParserExceptions, IOException {
        String instructions = "INSERT INTO votes VALUES ( TRUE, FALSE, '556788', '$', NULL);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBInsertCmd alter = (DBInsertCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"INSERT INTO votes VALUES ( TRUE, FALSE, '556788', '$', NULL);\" is acceptable.";
        assertEquals(alter.tokens.get(0), INSERT, failMessage);
        assertEquals(alter.tokens.get(1), BOOLEAN, failMessage);
        assertEquals(alter.tokens.get(2), BOOLEAN, failMessage);
        assertEquals(alter.tokens.get(3), STRING, failMessage);
        assertEquals(alter.tokens.get(4), STRING, failMessage);
        assertEquals(alter.tokens.get(5), NULL, failMessage);
    }


    @Test
    public void basicInsertIntoDPTest4() throws ParserExceptions, IOException {
        String instructions = "INSERT INTO votes VALUES ( 156, 1, -259, +5963, 59871259);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBInsertCmd alter = (DBInsertCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"INSERT INTO votes VALUES ( 156, 1, -259, +5963, 59871259);\" is acceptable.";
        assertEquals(alter.tokens.get(0), INSERT, failMessage);
        assertEquals(alter.tokens.get(1), INTEGER, failMessage);
        assertEquals(alter.tokens.get(2), INTEGER, failMessage);
        assertEquals(alter.tokens.get(3), INTEGER, failMessage);
        assertEquals(alter.tokens.get(4), INTEGER, failMessage);
        assertEquals(alter.tokens.get(5), INTEGER, failMessage);
    }

    @Test
    public void basicInsertIntoDPTest5() throws ParserExceptions, IOException {
        String instructions = "INSERT INTO votes VALUES ( 156.59, 1.56934, -259.5748963, +5963.215469, 59871259.5);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBInsertCmd alter = (DBInsertCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"INSERT INTO votes VALUES ( 156.59, 1.56934, -259.5748963, +5963.215469, 59871259.5);\" is acceptable.";
        assertEquals(alter.tokens.get(0), INSERT, failMessage);
        assertEquals(alter.tokens.get(1), FLOAT, failMessage);
        assertEquals(alter.tokens.get(2), FLOAT, failMessage);
        assertEquals(alter.tokens.get(3), FLOAT, failMessage);
        assertEquals(alter.tokens.get(4), FLOAT, failMessage);
        assertEquals(alter.tokens.get(5), FLOAT, failMessage);
    }

    @Test
    public void basicInsertIntoDPTest6() throws ParserExceptions, IOException {
        String instructions = "INSERT INTO votes VALUES ( 5, 'John Smith', 54, 189.56, 85.6, 'johnsmith@hotmail.com');";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBInsertCmd alter = (DBInsertCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"INSERT INTO votes VALUES ( 5, 'John Smith', 54, 189.56, 85.6, 'johnsmith@hotmail.com');\" is acceptable.";
        assertEquals(alter.tokens.get(0), INSERT, failMessage);
        assertEquals(alter.tokens.get(1), INTEGER, failMessage);
        assertEquals(alter.tokens.get(2), STRING, failMessage);
        assertEquals(alter.tokens.get(3), INTEGER, failMessage);
        assertEquals(alter.tokens.get(4), FLOAT, failMessage);
        assertEquals(alter.tokens.get(5), FLOAT, failMessage);
        assertEquals(alter.tokens.get(6), STRING, failMessage);
    }

    @Test
    public void basicJoinTest() throws ParserExceptions, IOException {
        String instructions = "JOIN census AND votes ON population AND cast;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBJoinCmd join = (DBJoinCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"JOIN census AND votes ON population AND cast;\" is acceptable.";
        assertEquals(join.tokens.get(0), JOIN, failMessage);
        assertEquals(join.tokens.get(1), TABLE, failMessage);
        assertEquals(join.tokens.get(2), TABLE, failMessage);
        assertEquals(join.tokens.get(3), ATTRIBUTE, failMessage);
        assertEquals(join.tokens.get(4), ATTRIBUTE, failMessage);
    }

    @Test
    public void basicJoinTest2() throws ParserExceptions, IOException {
        String instructions = "JOIN census AND votes ON census.population AND votes.cast;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBJoinCmd join = (DBJoinCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"JOIN census AND votes ON census.population AND votes.cast;\" is acceptable.";
        assertEquals(join.tokens.get(0), JOIN, failMessage);
        assertEquals(join.tokens.get(1), TABLE, failMessage);
        assertEquals(join.tokens.get(2), TABLE, failMessage);
        assertEquals(join.tokens.get(3), ATTRIBUTE, failMessage);
        assertEquals(join.tokens.get(4), ATTRIBUTE, failMessage);
    }

    @Test
    public void checkMoreThanOneStopBreaks() throws ParserExceptions {
        String instructions = "JOIN census AND votes ON census.votes.population AND votes.cast;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failedMessage = "Was expecting this to fail as there cannot be more than one . in an attribute name.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failedMessage);
    }

    @Test
    public void checkMoreThanOneStopBreaks2() throws ParserExceptions {
        String instructions = "INSERT INTO votes VALUES ( 5, 'John Smith', 54, 18.9.56, 85.6, 'johnsmith@hotmail.com');";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failedMessage = "Was expecting this to fail as there cannot be more than one . in a digit sequence name.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failedMessage);
    }

    @Test
    public void checkMoreThanOnePlusBreaks() throws ParserExceptions {
        String instructions = "INSERT INTO votes VALUES ( 5, 'John Smith', 54, +1+9.56, 85.6, 'johnsmith@hotmail.com');";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failedMessage = "Was expecting this to fail as there cannot be a + part way through a digit sequence name.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failedMessage);
    }

    @Test
    public void checkMoreThanOneMinusBreaks() throws ParserExceptions {
        String instructions = "INSERT INTO votes VALUES ( 5, 'John Smith', 54, -1-9.56, 85.6, 'johnsmith@hotmail.com');";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failedMessage = "Was expecting this to fail as there cannot be a - part way through a digit sequence name.";
        //assertFalse(parsedInstructions.isCommand(rawTokens), failedMessage);
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failedMessage);
    }

    @Test
    public void checkMoreThanOnePlusBreaks1() throws ParserExceptions {
        String instructions = "INSERT INTO votes VALUES ( 5, 'John Smith', 54, 1+9.56, 85.6, 'johnsmith@hotmail.com');";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failedMessage = "Was expecting this to fail as there cannot be a + part way through a digit sequence name.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failedMessage);
    }

    @Test
    public void checkMoreThanOneMinusBreaks1() throws ParserExceptions {
        String instructions = "INSERT INTO votes VALUES ( 5, 'John Smith', 54, 1-9.56, 85.6, 'johnsmith@hotmail.com');";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failedMessage = "Was expecting this to fail as there cannot be a - part way through a digit sequence name.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failedMessage);
    }

    @Test
    public void basicSelectConditionDBParse() throws ParserExceptions, IOException {
        String instructions = "select people FROM census WHERE population LIKE '1000';";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBSelectCmd select = (DBSelectCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"select people FROM census WHERE population LIKE '1000';\" is acceptable.";
        assertEquals(SELECT, select.tokens.get(0), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(1), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(2), failMessage);
        assertEquals("population", select.condition.get(0), failMessage);
        assertEquals("LIKE", select.condition.get(1), failMessage);
        assertEquals("1000", select.condition.get(2), failMessage);
    }

    @Test
    public void basicSelectConditionDBParse1() throws ParserExceptions, IOException {
        String instructions = "select people FROM census WHERE (population LIKE '1000');";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBSelectCmd select = (DBSelectCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"select people FROM census WHERE (population LIKE '1000');\" is acceptable.";
        assertEquals(SELECT, select.tokens.get(0), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(1), failMessage);
        assertEquals("(", select.condition.get(0), failMessage);
        assertEquals("population", select.condition.get(1), failMessage);
        assertEquals("LIKE", select.condition.get(2), failMessage);
        assertEquals("1000", select.condition.get(3), failMessage);
        assertEquals(")", select.condition.get(4), failMessage);
    }

    @Test
    public void basicSelectConditionDBParse2() throws ParserExceptions, IOException {
        String instructions = "select people FROM census WHERE (population LIKE '1000' AND votes > 100);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBSelectCmd select = (DBSelectCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"select people FROM census WHERE (population LIKE '1000' AND votes > 100);\" is acceptable.";
        assertEquals(SELECT, select.tokens.get(0), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(1), failMessage);
        assertEquals("(", select.condition.get(0), failMessage);
        assertEquals("population", select.condition.get(1), failMessage);
        assertEquals("LIKE", select.condition.get(2), failMessage);
        assertEquals("1000", select.condition.get(3), failMessage);
        assertEquals("AND", select.condition.get(4), failMessage);
        assertEquals("votes", select.condition.get(5), failMessage);
        assertEquals(">", select.condition.get(6), failMessage);
        assertEquals("100", select.condition.get(7), failMessage);
        assertEquals(")", select.condition.get(8), failMessage);
    }

    @Test
    public void basicSelectConditionDBParse3() throws ParserExceptions, IOException {
        String instructions = "select people FROM census WHERE ((population LIKE '1000') AND (votes > 100));";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBSelectCmd select = (DBSelectCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"select people FROM census WHERE ((population LIKE '1000') AND (votes > 100));\" is acceptable.";
        assertEquals(SELECT, select.tokens.get(0), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(1), failMessage);
        assertEquals("(", select.condition.get(0), failMessage);
        assertEquals("(", select.condition.get(1), failMessage);
        assertEquals("population", select.condition.get(2), failMessage);
        assertEquals("LIKE", select.condition.get(3), failMessage);
        assertEquals("1000", select.condition.get(4), failMessage);
        assertEquals(")", select.condition.get(5), failMessage);
        assertEquals("AND", select.condition.get(6), failMessage);
        assertEquals("(", select.condition.get(7), failMessage);
        assertEquals("votes", select.condition.get(8), failMessage);
        assertEquals(">", select.condition.get(9), failMessage);
        assertEquals("100", select.condition.get(10), failMessage);
        assertEquals(")", select.condition.get(11), failMessage);
        assertEquals(")", select.condition.get(12), failMessage);
    }

    @Test
    public void basicSelectConditionDBParse4() throws ParserExceptions, IOException {
        String instructions = "select people FROM census WHERE population LIKE '1000' AND votes > 100;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBSelectCmd select = (DBSelectCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"select people FROM census WHERE population LIKE '1000' AND votes > 100;\" is acceptable.";
        assertEquals(SELECT, select.tokens.get(0), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(1), failMessage);
        assertEquals("population", select.condition.get(0), failMessage);
        assertEquals("LIKE", select.condition.get(1), failMessage);
        assertEquals("1000", select.condition.get(2), failMessage);
        assertEquals("AND", select.condition.get(3), failMessage);
        assertEquals("votes", select.condition.get(4), failMessage);
        assertEquals(">", select.condition.get(5), failMessage);
        assertEquals("100", select.condition.get(6), failMessage);
    }

    @Test
    public void basicSelectConditionDBParse5() throws ParserExceptions, IOException {
        String instructions = "select people FROM census WHERE (population LIKE '1000') AND (votes > 100);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBSelectCmd select = (DBSelectCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"select people FROM census WHERE (population LIKE '1000') AND (votes > 100);\" is acceptable.";
        assertEquals(SELECT, select.tokens.get(0), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(1), failMessage);
        assertEquals("(", select.condition.get(0), failMessage);
        assertEquals("population", select.condition.get(1), failMessage);
        assertEquals("LIKE", select.condition.get(2), failMessage);
        assertEquals("1000", select.condition.get(3), failMessage);
        assertEquals(")", select.condition.get(4), failMessage);
        assertEquals("AND", select.condition.get(5), failMessage);
        assertEquals("(", select.condition.get(6), failMessage);
        assertEquals("votes", select.condition.get(7), failMessage);
        assertEquals(">", select.condition.get(8), failMessage);
        assertEquals("100", select.condition.get(9), failMessage);
        assertEquals(")", select.condition.get(10), failMessage);
    }

    @Test
    public void basicSelectConditionDBParse6() throws ParserExceptions, IOException {
        String instructions = "select people FROM census WHERE population LIKE '1000' AND (votes > 100);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBSelectCmd select = (DBSelectCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"select people FROM census WHERE population LIKE '1000' AND (votes > 100);\" is acceptable.";
        assertEquals(SELECT, select.tokens.get(0), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(1), failMessage);
        assertEquals("population", select.condition.get(0), failMessage);
        assertEquals("LIKE", select.condition.get(1), failMessage);
        assertEquals("1000", select.condition.get(2), failMessage);
        assertEquals("AND", select.condition.get(3), failMessage);
        assertEquals("(", select.condition.get(4), failMessage);
        assertEquals("votes", select.condition.get(5), failMessage);
        assertEquals(">", select.condition.get(6), failMessage);
        assertEquals("100", select.condition.get(7), failMessage);
        assertEquals(")", select.condition.get(8), failMessage);
    }

    @Test
    public void basicSelectConditionDBParse7() throws ParserExceptions, IOException {
        String instructions = "select people FROM census WHERE population LIKE '1000' AND votes > 100 OR ward == 1000;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBSelectCmd select = (DBSelectCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"select people FROM census WHERE population LIKE '1000' AND votes > 100 OR ward == 1000;\" is acceptable.";
        assertEquals(SELECT, select.tokens.get(0), failMessage);
        assertEquals(ATTRIBUTE, select.tokens.get(1), failMessage);
        assertEquals("population", select.condition.get(0), failMessage);
        assertEquals("LIKE", select.condition.get(1), failMessage);
        assertEquals("1000", select.condition.get(2), failMessage);
        assertEquals("AND", select.condition.get(3), failMessage);
        assertEquals("votes", select.condition.get(4), failMessage);
        assertEquals(">", select.condition.get(5), failMessage);
        assertEquals("100", select.condition.get(6), failMessage);
        assertEquals("OR", select.condition.get(7), failMessage);
        assertEquals("ward", select.condition.get(8), failMessage);
        assertEquals("==", select.condition.get(9), failMessage);
        assertEquals("1000", select.condition.get(10), failMessage);
    }

    @Test
    public void basicSelectConditionDBParse8() throws ParserExceptions, IOException {
        String instructions = "select people FROM census WHERE population LIKE '1000' AND votes > 100 OR ward == 1000 OR population >= +100000.6;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBSelectCmd select = (DBSelectCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"select people FROM census WHERE population LIKE '1000' AND votes > 100 OR ward == 1000 OR population >= +100000.6;\" is acceptable.";
        assertEquals("population", select.condition.get(0), failMessage);
        assertEquals("LIKE", select.condition.get(1), failMessage);
        assertEquals("1000", select.condition.get(2), failMessage);
        assertEquals("AND", select.condition.get(3), failMessage);
        assertEquals("votes", select.condition.get(4), failMessage);
        assertEquals(">", select.condition.get(5), failMessage);
        assertEquals("100", select.condition.get(6), failMessage);
        assertEquals("OR", select.condition.get(7), failMessage);
        assertEquals("ward", select.condition.get(8), failMessage);
        assertEquals("==", select.condition.get(9), failMessage);
        assertEquals("1000", select.condition.get(10), failMessage);
        assertEquals("OR", select.condition.get(11), failMessage);
        assertEquals("population", select.condition.get(12), failMessage);
        assertEquals(">=", select.condition.get(13), failMessage);
        assertEquals("+100000.6", select.condition.get(14), failMessage);
    }

    @Test
    public void checkUnequalBracketsBreaks() throws ParserExceptions {
        String instructions = "select people FROM census WHERE population LIKE '1000' AND (votes > 100;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failedMessage = "Was expecting this to fail as there are unequal brackets.";
        //assertFalse(parsedInstructions.isCommand(rawTokens), failedMessage);
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failedMessage);
    }

    @Test
    public void checkUnequalBracketsBreaks1() throws ParserExceptions {
        String instructions = "select people FROM census WHERE population LIKE '1000' AND votes > 100);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failedMessage = "Was expecting this to fail as there are unequal brackets.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failedMessage);
    }

    @Test
    public void checkUnequalBracketsBreaks2() throws ParserExceptions {
        String instructions = "select people FROM census WHERE (population LIKE '1000') AND (votes > 100));";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failedMessage = "Was expecting this to fail as there are unequal brackets.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failedMessage);
    }

    @Test
    public void checkUnequalBracketsBreaks3() throws ParserExceptions {
        String instructions = "select people FROM census WHERE ((population LIKE '1000' AND (votes > 100);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failedMessage = "Was expecting this to fail as there are unequal brackets.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failedMessage);
    }

    @Test
    public void checkUnequalBracketsBreaks4() throws ParserExceptions {
        String instructions = "select people FROM census WHERE population LIKE '1000') AND (votes > 100;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failedMessage = "Was expecting this to fail as there are unequal brackets.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failedMessage);
    }

    @Test
    public void basicUpdateTest() throws ParserExceptions, IOException {
        String instructions = "UpDatE votes SET votescast = 1000 WHERE votescast LIKE 2000;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBUpdateCmd update = (DBUpdateCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"UpDatE votes SET votescast = 1000 WHERE votescast LIKE 2000;\" is acceptable.";
        assertEquals(UPDATE, update.tokens.get(0), failMessage);
        assertEquals("votes", update.tableNames.get(0), failMessage);
        assertEquals(ATTRIBUTE, update.tokens.get(1), failMessage);
        assertEquals(INTEGER, update.tokens.get(2), failMessage);
        assertEquals("votescast", update.condition.get(0), failMessage);
        assertEquals("LIKE", update.condition.get(1), failMessage);
        assertEquals("2000", update.condition.get(2), failMessage);
    }

    @Test
    public void basicUpdateTest1() throws ParserExceptions, IOException {
        String instructions = "UpDatE votes SET votescast = 1000, ward = 'Windmill Hill' WHERE votescast LIKE 2000;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBUpdateCmd update = (DBUpdateCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"UpDatE votes SET votescast = 1000, ward = 'Windmill Hill' WHERE votescast LIKE 2000;\" is acceptable.";
        assertEquals(UPDATE, update.tokens.get(0), failMessage);
        assertEquals("votes", update.tableNames.get(0), failMessage);
        assertEquals(ATTRIBUTE, update.tokens.get(1), failMessage);
        assertEquals(INTEGER, update.tokens.get(2), failMessage);
        assertEquals(ATTRIBUTE, update.tokens.get(3), failMessage);
        assertEquals(STRING, update.tokens.get(4), failMessage);
        assertEquals("votescast", update.condition.get(0), failMessage);
        assertEquals("LIKE", update.condition.get(1), failMessage);
        assertEquals("2000", update.condition.get(2), failMessage);
    }

    @Test
    public void basicUpdateTest2() throws ParserExceptions, IOException {
        String instructions = "UpDatE votes SET votescast = 1000, ward = 'Windmill Hill' WHERE votescast LIKE 2000 AND population > 1000;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBUpdateCmd update = (DBUpdateCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"UpDatE votes SET votescast = 1000 WHERE votescast LIKE 2000 AND population > 1000;\" is acceptable.";
        assertEquals(UPDATE, update.tokens.get(0), failMessage);
        assertEquals("votes", update.tableNames.get(0), failMessage);
        assertEquals(ATTRIBUTE, update.tokens.get(1), failMessage);
        assertEquals(INTEGER, update.tokens.get(2), failMessage);
        assertEquals(ATTRIBUTE, update.tokens.get(3), failMessage);
        assertEquals(STRING, update.tokens.get(4), failMessage);
        assertEquals("votescast", update.condition.get(0), failMessage);
        assertEquals("LIKE", update.condition.get(1), failMessage);
        assertEquals("2000", update.condition.get(2), failMessage);
        assertEquals("AND", update.condition.get(3), failMessage);
        assertEquals("population", update.condition.get(4), failMessage);
        assertEquals(">", update.condition.get(5), failMessage);
        assertEquals("1000", update.condition.get(6), failMessage);
    }

    @Test
    public void basicDeleteTest() throws ParserExceptions, IOException {
        String instructions = "DeleTE FROM votes WHERE (population >= 10000);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBDeleteCmd delete = (DBDeleteCmd) parsedInstructions.isCommand(rawTokens);
        String failMessage = "Was expecting this to pass as \"DeleTE FROM votes WHERE (population >= 10000);\" is acceptable.";
        assertEquals(delete.tokens.get(0), DELETE, failMessage);
        assertEquals(delete.tokens.get(1), ATTRIBUTE, failMessage);
        assertEquals(delete.tokens.get(2), COMPARATOR, failMessage);
        assertEquals(delete.tokens.get(3), INTEGER, failMessage);
    }

    @Test
    public void basicErrorTest1() throws ParserExceptions {
        String instructions = "USE;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as no database specified.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

  /*  @Test
    public void basicErrorTest2() throws ParserExceptions {
        String instructions = "USE census";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as no semi-colon specified.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }*/


    @Test
    public void basicErrorTest3() throws ParserExceptions {
        String instructions = "CREATE census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as it is not specified what type to create database or table.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest4() throws ParserExceptions {
        String instructions = "CREATE DATA census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as DATA is not a valid keyword.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest5() throws ParserExceptions {
        String instructions = "CREATE DATAbas census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as DATABAS is not a valid keyword.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest6() throws ParserExceptions {
        String instructions = "CREATE TaBl census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as TABL is not a valid keyword.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest7() throws ParserExceptions {
        String instructions = "CREATE TaBlE $%^&£;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as $%^&£ is not a valid tablename.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest8() throws ParserExceptions {
        String instructions = "CREATE TaBlE census£;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as census£ is not a valid tablename.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest9() throws ParserExceptions {
        String instructions = "CREATE DATABASE $%^&£;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as $%^&£ is not a valid database.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest10() throws ParserExceptions {
        String instructions = "CREATE databASE census£;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as census£ is not a valid database.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest11() throws ParserExceptions {
        String instructions = "CREATE TaBlE census ();";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as attribute name cannot be null.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest12() throws ParserExceptions {
        String instructions = "CREATE TaBlE census ();";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as attribute name cannot be null.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest13() throws ParserExceptions {
        String instructions = "CREATE TaBlE census (* , );";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as attribute list cannot contain a comma with no following attribute.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest14() throws ParserExceptions {
        String instructions = "CREATE TaBlE census (* , census.);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as attribute list cannot contain a tablename with no following attribute name.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest15() throws ParserExceptions {
        String instructions = "CREATE TaBlE census (* , census.);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as attribute list cannot contain a tablename with no following attribute name.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest16() throws ParserExceptions {
        String instructions = "CREATE TaBlE census (* , .votes);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as attribute list cannot contain a fullstop with no preceding tablename.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest17() throws ParserExceptions {
        String instructions = "CREATE TaBlE census (census.votes, census.population;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as if the attribute list starts with a bracket it must end with one.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest18() throws ParserExceptions {
        String instructions = "CREATE TaBlE census (census.votes, census.population;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as if the attribute list starts with a bracket it must end with one.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest19() throws ParserExceptions {
        String instructions = "CREATE TaBlE census (census.vo&es, census.popula*ion);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as the attribute names cannot contain symbols.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest20() throws ParserExceptions {
        String instructions = "DROP census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as drop must specify dropping table or database.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest21() throws ParserExceptions {
        String instructions = "TABLE census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as table is not valid without another keyword such as DROP or CREATE.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest22() throws ParserExceptions {
        String instructions = "ALTER census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as it is not clear what is being altered, TABLE keyword is missing.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest23() throws ParserExceptions {
        String instructions = "ALTER TABLE census votes;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as it is not clear how the table is being edited, ADD or DROP keyword is missing.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest24() throws ParserExceptions {
        String instructions = "ALTER TABLE census ADF votes;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as ADF is not an accepted word.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest25() throws ParserExceptions {
        String instructions = "ALTER TABLE census ADF votes;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as ADF is not an accepted word.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest26() throws ParserExceptions {
        String instructions = "ALTER TABLE (census ADF votes);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as brackets are not allowed in this command type.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest27() throws ParserExceptions {
        String instructions = "INTO census VALUES (1000, 2000);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as INSERT keyword is missing.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest28() throws ParserExceptions {
        String instructions = "INSERT census VALUES (1000, 2000);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as INTO keyword is missing.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest29() throws ParserExceptions {
        String instructions = "INSERT INTO census VALUES (1000, 2000;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as a bracket is missing.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest30() throws ParserExceptions {
        String instructions = "INSERT INTO census VALUES 1000, 2000);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as a bracket is missing.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest31() throws ParserExceptions {
        String instructions = "INSERT INTO census VALUES (1000 2000);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as a comma is missing.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest32() throws ParserExceptions {
        String instructions = "INSERT INTO census VALUE (1000, 2000);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as VALUE is incorrect.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest33() throws ParserExceptions {
        String instructions = "SELEC * FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as SELEC is incorrect.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest34() throws ParserExceptions {
        String instructions = "SELECT & FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as & is incorrect.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest35() throws ParserExceptions {
        String instructions = "SELECT census/table, votes FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as / is an incorrect filename divider.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest36() throws ParserExceptions {
        String instructions = "SELECT census/table, votes FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as / is an incorrect filename divider.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest37() throws ParserExceptions {
        String instructions = "SELECT census/table, votes FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as / is an incorrect filename divider.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }

    @Test
    public void basicErrorTest38() throws ParserExceptions {
        String instructions = "SELECT census/table, votes FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        DBParser parsedInstructions = new DBParser(rawTokens);
        String failMessage = "Was expecting this to fail as / is an incorrect filename divider.";
        assertThrows(ParserExceptions.InvalidCommand.class, () -> parsedInstructions.commandType(rawTokens), failMessage);
    }
}
