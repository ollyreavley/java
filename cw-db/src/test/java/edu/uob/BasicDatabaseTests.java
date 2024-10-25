package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class BasicDatabaseTests {

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

    @Test public void testBasicCreate(){
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        String response = sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test public void testBasicCreate1(){
        String randomName = generateRandomName();
        String response = sendCommandToServer("CREATE DATABASE " + randomName + ";");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test public void testBasicCreate2(){
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        String response = sendCommandToServer("CREATE TABLE marks;");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test public void testBasicCreateUse() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        String response = sendCommandToServer("USE " + randomName + ";");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test public void testBasicUseError() {
        String randomName = generateRandomName();
        String response = sendCommandToServer("USE " + randomName + ";");
        assertFalse(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertTrue(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test
    public void testBasicInsert() {
        String randomName = generateRandomName();
        sendCommandToServer("Use databases;");
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        String response = sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        response = sendCommandToServer("INSERT INTO marks VALUES ('Steve', 55, TRUE);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        response = sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        response = sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test
    public void testBasicInsert1() {
        String randomName = generateRandomName();
        sendCommandToServer("Use databases;");
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        String response = sendCommandToServer("INSERT INTO marks VALUES ('Steve', TRUE);");
        assertFalse(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertTrue(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        response = sendCommandToServer("INSERT INTO marks VALUES ('Steve', 55, TRUE);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        response = sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        response = sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test
    public void testBasicDelete1() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        String response = sendCommandToServer("DELETE FROM marks WHERE name LIKE 'Steve';");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test
    public void testBasicDelete6() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        String response = sendCommandToServer("DELETE FROM marks WHERE (name LIKE 'Steve') AND mark == 20;");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test
    public void testBasicAlter() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        String response = sendCommandToServer("ALTER TABLE marks DROP name;");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test
    public void testIdColNotAllowed1() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        String response = sendCommandToServer("ALTER TABLE marks DROP grade;");
        assertFalse(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertTrue(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test
    public void testIdColNotAllowed2() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        String response = sendCommandToServer("ALTER TABLE marks DROP id;");
        assertFalse(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertTrue(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test public void testBasicCreateDrop() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        String response = sendCommandToServer("DROP DATABASE " + randomName + ";");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test public void testBasicCreateDrop1() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        String response = sendCommandToServer("DROP DATABASE " + randomName + ";");
        assertFalse(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertTrue(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    @Test public void testBasicCreateDrop2() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        String response = sendCommandToServer("DROP TABLE marks;");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        response = sendCommandToServer("DROP DATABASE " + randomName + ";");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }

    ////Up to this point////

    @Test
    public void testBasicUpdate() throws IOException {
        String response = sendCommandToServer("CREATE DATABASE markbook;");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("USE markbook;");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("CREATE table marks (name, mark, pass);");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, TRUE);");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("SELECT * FROM marks;");
        assertTrue(response.contains("[OK]"));
        assertTrue(response.contains("Steve"));
        response = sendCommandToServer("SELECT * FROM marks where name != 'Dave';");
        assertTrue(response.contains("[OK]"));
        assertTrue(response.contains("Steve"));
        assertTrue(response.contains("Bob"));
        assertTrue(response.contains("Clive"));
        assertFalse(response.contains("Dave"));
        response = sendCommandToServer("SELECT * FROM marks where pass LIKE TRUE;");
        assertTrue(response.contains("[OK]"));
        assertTrue(response.contains("Steve"));
        assertTrue(response.contains("Dave"));
    }

    // A basic test that creates a database, creates a table, inserts some test data, then queries it.
    // It then checks the response to see that a couple of the entries in the table are returned as expected
    @Test
    public void testBasicCreateAndQuery() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        String response = sendCommandToServer("SELECT * FROM marks;");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        assertTrue(response.contains("Steve"), "An attempt was made to add Steve to the table, but they were not returned by SELECT *");
        assertTrue(response.contains("Clive"), "An attempt was made to add Clive to the table, but they were not returned by SELECT *");
    }

    // A test to make sure that querying returns a valid ID (this test also implicitly checks the "==" condition)
    // (these IDs are used to create relations between tables, so it is essential that they work !)
    @Test
    public void testQueryID() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        String response = sendCommandToServer("SELECT id FROM marks WHERE name == 'Steve';");
        System.out.println(response);
        // Convert multi-lined responses into just a single line
        String singleLine = response.replace("\n"," ").trim();
        // Split the line on the space character
        String[] tokens = singleLine.split(" ");
        // Check that the very last token is a number (which should be the ID of the entry)
        String lastToken = tokens[tokens.length-1];
        try {
            Integer.parseInt(lastToken);
        } catch (NumberFormatException nfe) {
            fail("The last token returned by `SELECT id FROM marks WHERE name == 'Steve';` should have been an integer ID, but was " + lastToken);
        }
    }

    // A test to make sure that databases can be reopened after server restart
    @Test
    public void testTablePersistsAfterRestart() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        // Create a new server object
        server = new DBServer();
        sendCommandToServer("USE " + randomName + ";");
        String response = sendCommandToServer("SELECT * FROM marks;");
        assertTrue(response.contains("Steve"), "Steve was added to a table and the server restarted - but Steve was not returned by SELECT *");
    }

    // Test to make sure that the [ERROR] tag is returned in the case of an error (and NOT the [OK] tag)
    @Test
    public void testForErrorTag() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        String response = sendCommandToServer("SELECT * FROM libraryfines;");
        assertTrue(response.contains("[ERROR]"), "An attempt was made to access a non-existent table, however an [ERROR] tag was not returned");
        assertFalse(response.contains("[OK]"), "An attempt was made to access a non-existent table, however an [OK] tag was returned");
    }

    @Test
    public void testBasicUpdate1() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        sendCommandToServer("UPDATE marks SET name = 'John' WHERE name LIKE 'Steve';");
     /*   assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        assertTrue(response.contains("Steve"), "An attempt was made to add Steve to the table, but they were not returned by SELECT *");
        assertTrue(response.contains("Clive"), "An attempt was made to add Clive to the table, but they were not returned by SELECT *");
  */  }

    @Test
    public void testBasicJoin() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        String response = sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        response = sendCommandToServer("CREATE TABLE students (name, age, grade);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        response = sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        response = sendCommandToServer("INSERT INTO marks VALUES ('Steve', 55, TRUE);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        response = sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, TRUE);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        response = sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        response = sendCommandToServer("INSERT INTO students VALUES ('Steve', 30, 'A');");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        response = sendCommandToServer("INSERT INTO students VALUES ('Steve', 25, 'B');");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        response = sendCommandToServer("INSERT INTO students VALUES ('Bob', 35, 'C');");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        response = sendCommandToServer("INSERT INTO students VALUES ('Clive', 21, 'G');");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        response = sendCommandToServer("JOIN marks AND students ON name AND grade;");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        assertTrue(response.contains("Steve"), "An attempt was made to join the tables, but they were not returned by JOIN");
        assertTrue(response.contains("A"), "An attempt was made to join the tables, but they were not returned by JOIN");
        assertTrue(response.contains("marks"), "An attempt was made to join the tables, but the table name was not returned by the join");
    }

    @Test
    public void testBasicSelect() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        String response = sendCommandToServer("SELECT * FROM marks;");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        assertTrue(response.contains("Steve"), "An attempt was made to add Steve to the table, but they were not returned by SELECT *");
        assertTrue(response.contains("Clive"), "An attempt was made to add Clive to the table, but they were not returned by SELECT *");
    }

    @Test
    public void testBasicSelect1() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        String response = sendCommandToServer("SELECT * FROM marks WHERE name LIKE 'Steve';");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        assertTrue(response.contains("Steve"), "An attempt was made to add Steve to the table, but they were not returned by SELECT *");
        assertTrue(response.contains("name"), "An attempt was made to add Steve to the table, but they were not returned by SELECT *");
        assertTrue(response.contains("id"), "An attempt was made to add Steve to the table, but they were not returned by SELECT *");
        assertTrue(response.contains("1"), "An attempt was made to add Steve to the table, but they were not returned by SELECT *");
        assertTrue(response.contains("2"), "An attempt was made to add Steve to the table, but they were not returned by SELECT *");
    }

    @Test
    public void testTablePersistsAfterRestart2() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        // Create a new server object
        server = new DBServer();
        sendCommandToServer("USE " + randomName + ";");
        String response = sendCommandToServer("SELECT * FROM marks;");
        assertTrue(response.contains("Steve"), "Steve was added to a table and the server restarted - but Steve was not returned by SELECT *");
    }


    @Test
    public void testBasicSelect2() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        String response = sendCommandToServer("SELECT mark, name FROM marks WHERE name LIKE 'Steve';");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        assertTrue(response.contains("Steve"), "An attempt was made to add Steve to the table, but they were not returned by SELECT *");
    }

    @Test
    public void testBasicSelect3() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        String response = sendCommandToServer("SELECT mark, name FROM marks;");
        System.out.println(response);
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        assertTrue(response.contains("65"), "An attempt was made to add Steve to the table, but they were not returned by SELECT *");
        assertTrue(response.contains("20"), "An attempt was made to add Clive to the table, but they were not returned by SELECT *");
    }

    @Test
    public void testBasicDelete2() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        String response = sendCommandToServer("DELETE FROM marks WHERE (name LIKE 'Steve') AND mark LIKE 20;");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
    }
}
