package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TokeniserTests {

    private DBServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    public void setup() {
        server = new DBServer();
    }

    @Test
    public void tokeniserTest() throws ParserExceptions {
        String instructions = "USE CENSUS;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "USE");
        assertEquals(rawTokens.get(1), "CENSUS");
        assertEquals(rawTokens.get(2), ";");
    }

    @Test
    public void tokeniserTest1() throws ParserExceptions {
        String instructions = "USE CENSUS1;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "USE");
        assertEquals(rawTokens.get(1), "CENSUS1");
        assertEquals(rawTokens.get(2), ";");
    }

    @Test
    public void tokeniserTest2() throws ParserExceptions {
        String instructions = "USE CEN1SUS;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "USE");
        assertEquals(rawTokens.get(1), "CEN1SUS");
        assertEquals(rawTokens.get(2), ";");
    }

    @Test
    public void tokeniserTest3() throws ParserExceptions {
        String instructions = "CREATE TABLE votes;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "CREATE");
        assertEquals(rawTokens.get(1), "TABLE");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), ";");
    }

    @Test
    public void tokeniserTest4() throws ParserExceptions {
        String instructions = "create TABLE votes (id, name, age);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "create");
        assertEquals(rawTokens.get(1), "TABLE");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "(");
        assertEquals(rawTokens.get(4), "id");
        assertEquals(rawTokens.get(5), ",");
        assertEquals(rawTokens.get(6), "name");
        assertEquals(rawTokens.get(7), ",");
        assertEquals(rawTokens.get(8), "age");
        assertEquals(rawTokens.get(9), ")");
        assertEquals(rawTokens.get(10), ";");
    }

    @Test
    public void tokeniserTest5() throws ParserExceptions {
        String instructions = "create TABLE votes (votes.id, votes.name, votes.age);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "create");
        assertEquals(rawTokens.get(1), "TABLE");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "(");
        assertEquals(rawTokens.get(4), "votes.id");
        assertEquals(rawTokens.get(5), ",");
        assertEquals(rawTokens.get(6), "votes.name");
        assertEquals(rawTokens.get(7), ",");
        assertEquals(rawTokens.get(8), "votes.age");
        assertEquals(rawTokens.get(9), ")");
        assertEquals(rawTokens.get(10), ";");
    }

    @Test
    public void tokeniserTest6() throws ParserExceptions {
        String instructions = "CREATE DATABASE votes;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "CREATE");
        assertEquals(rawTokens.get(1), "DATABASE");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), ";");
    }

    @Test
    public void tokeniserTest7() throws ParserExceptions {
        String instructions = "DROP TABLE votes;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "DROP");
        assertEquals(rawTokens.get(1), "TABLE");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), ";");
    }

    @Test
    public void tokeniserTest8() throws ParserExceptions {
        String instructions = "DROP TABLE votes;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "DROP");
        assertEquals(rawTokens.get(1), "TABLE");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), ";");
    }

    @Test
    public void tokeniserTest9() throws ParserExceptions {
        String instructions = "DROP DATABASE census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "DROP");
        assertEquals(rawTokens.get(1), "DATABASE");
        assertEquals(rawTokens.get(2), "census");
        assertEquals(rawTokens.get(3), ";");
    }

    @Test
    public void tokeniserTest10() throws ParserExceptions {
        String instructions = "select people FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "select");
        assertEquals(rawTokens.get(1), "people");
        assertEquals(rawTokens.get(2), "FROM");
        assertEquals(rawTokens.get(3), "census");
        assertEquals(rawTokens.get(4), ";");
    }

    @Test
    public void tokeniserTest11() throws ParserExceptions {
        String instructions = "select * FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "select");
        assertEquals(rawTokens.get(1), "*");
        assertEquals(rawTokens.get(2), "FROM");
        assertEquals(rawTokens.get(3), "census");
        assertEquals(rawTokens.get(4), ";");
    }

    @Test
    public void tokeniserTest12() throws ParserExceptions {
        String instructions = "select people, votes FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "select");
        assertEquals(rawTokens.get(1), "people");
        assertEquals(rawTokens.get(2), ",");
        assertEquals(rawTokens.get(3), "votes");
        assertEquals(rawTokens.get(4), "FROM");
        assertEquals(rawTokens.get(5), "census");
        assertEquals(rawTokens.get(6), ";");
    }

    @Test
    public void tokeniserTest13() throws ParserExceptions {
        String instructions = "select votes.people, test.votes FROM census;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "select");
        assertEquals(rawTokens.get(1), "votes.people");
        assertEquals(rawTokens.get(2), ",");
        assertEquals(rawTokens.get(3), "test.votes");
        assertEquals(rawTokens.get(4), "FROM");
        assertEquals(rawTokens.get(5), "census");
        assertEquals(rawTokens.get(6), ";");
    }

    @Test
    public void tokeniserTest14() throws ParserExceptions {
        String instructions = "ALTER TABLE votes ADD ward;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "ALTER");
        assertEquals(rawTokens.get(1), "TABLE");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "ADD");
        assertEquals(rawTokens.get(4), "ward");
        assertEquals(rawTokens.get(5), ";");
    }

    @Test
    public void tokeniserTest15() throws ParserExceptions {
        String instructions = "ALTER TABLE votes ADD votes.ward;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "ALTER");
        assertEquals(rawTokens.get(1), "TABLE");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "ADD");
        assertEquals(rawTokens.get(4), "votes.ward");
        assertEquals(rawTokens.get(5), ";");
    }

    @Test
    public void tokeniserTest16() throws ParserExceptions {
        String instructions = "ALTER TABLE votes DROP votes.ward;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "ALTER");
        assertEquals(rawTokens.get(1), "TABLE");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "DROP");
        assertEquals(rawTokens.get(4), "votes.ward");
        assertEquals(rawTokens.get(5), ";");
    }

    @Test
    public void tokeniserTest17() throws ParserExceptions {
        String instructions = "INSERT INTO votes VALUES ( 'hello', 'trick', '');";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "INSERT");
        assertEquals(rawTokens.get(1), "INTO");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "VALUES");
        assertEquals(rawTokens.get(4), "(");
        assertEquals(rawTokens.get(5), "'hello'");
        assertEquals(rawTokens.get(6), ",");
        assertEquals(rawTokens.get(7), "'trick'");
        assertEquals(rawTokens.get(8), ",");
        assertEquals(rawTokens.get(9), "''");
        assertEquals(rawTokens.get(10), ")");
        assertEquals(rawTokens.get(11), ";");
    }

    @Test
    public void tokeniserTest18() throws ParserExceptions {
        String instructions = "INSERT INTO votes VALUES ( TRUE, FALSE, '556788', '$');";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "INSERT");
        assertEquals(rawTokens.get(1), "INTO");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "VALUES");
        assertEquals(rawTokens.get(4), "(");
        assertEquals(rawTokens.get(5), "TRUE");
        assertEquals(rawTokens.get(6), ",");
        assertEquals(rawTokens.get(7), "FALSE");
        assertEquals(rawTokens.get(8), ",");
        assertEquals(rawTokens.get(9), "'556788'");
        assertEquals(rawTokens.get(10), ",");
        assertEquals(rawTokens.get(11), "'$'");
        assertEquals(rawTokens.get(12), ")");
        assertEquals(rawTokens.get(13), ";");
    }

    @Test
    public void tokeniserTest19() throws ParserExceptions {
        String instructions = "INSERT INTO votes VALUES ( TRUE, FALSE, '556788', '$', NULL);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "INSERT");
        assertEquals(rawTokens.get(1), "INTO");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "VALUES");
        assertEquals(rawTokens.get(4), "(");
        assertEquals(rawTokens.get(5), "TRUE");
        assertEquals(rawTokens.get(6), ",");
        assertEquals(rawTokens.get(7), "FALSE");
        assertEquals(rawTokens.get(8), ",");
        assertEquals(rawTokens.get(9), "'556788'");
        assertEquals(rawTokens.get(10), ",");
        assertEquals(rawTokens.get(11), "'$'");
        assertEquals(rawTokens.get(12), ",");
        assertEquals(rawTokens.get(13), "NULL");
        assertEquals(rawTokens.get(14), ")");
        assertEquals(rawTokens.get(15), ";");
    }

    @Test
    public void tokeniserTest20() throws ParserExceptions {
        String instructions = "INSERT INTO votes VALUES ( 156, 1, -259, +5963, 59871259);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "INSERT");
        assertEquals(rawTokens.get(1), "INTO");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "VALUES");
        assertEquals(rawTokens.get(4), "(");
        assertEquals(rawTokens.get(5), "156");
        assertEquals(rawTokens.get(6), ",");
        assertEquals(rawTokens.get(7), "1");
        assertEquals(rawTokens.get(8), ",");
        assertEquals(rawTokens.get(9), "-259");
        assertEquals(rawTokens.get(10), ",");
        assertEquals(rawTokens.get(11), "+5963");
        assertEquals(rawTokens.get(12), ",");
        assertEquals(rawTokens.get(13), "59871259");
        assertEquals(rawTokens.get(14), ")");
        assertEquals(rawTokens.get(15), ";");
    }

    @Test
    public void tokeniserTest21() throws ParserExceptions {
        String instructions = "INSERT INTO votes VALUES ( 156.59, 1.56934, -259.5748963, +5963.215469, 59871259.5);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "INSERT");
        assertEquals(rawTokens.get(1), "INTO");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "VALUES");
        assertEquals(rawTokens.get(4), "(");
        assertEquals(rawTokens.get(5), "156.59");
        assertEquals(rawTokens.get(6), ",");
        assertEquals(rawTokens.get(7), "1.56934");
        assertEquals(rawTokens.get(8), ",");
        assertEquals(rawTokens.get(9), "-259.5748963");
        assertEquals(rawTokens.get(10), ",");
        assertEquals(rawTokens.get(11), "+5963.215469");
        assertEquals(rawTokens.get(12), ",");
        assertEquals(rawTokens.get(13), "59871259.5");
        assertEquals(rawTokens.get(14), ")");
        assertEquals(rawTokens.get(15), ";");
    }

    @Test
    public void tokeniserTest22() throws ParserExceptions {
        String instructions = "INSERT INTO votes VALUES ( 5, 'John Smith', 54, 189.56, 85.6, 'johnsmith@hotmail.com');";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "INSERT");
        assertEquals(rawTokens.get(1), "INTO");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "VALUES");
        assertEquals(rawTokens.get(4), "(");
        assertEquals(rawTokens.get(5), "5");
        assertEquals(rawTokens.get(6), ",");
        assertEquals(rawTokens.get(7), "'John Smith'");
        assertEquals(rawTokens.get(8), ",");
        assertEquals(rawTokens.get(9), "54");
        assertEquals(rawTokens.get(10), ",");
        assertEquals(rawTokens.get(11), "189.56");
        assertEquals(rawTokens.get(12), ",");
        assertEquals(rawTokens.get(13), "85.6");
        assertEquals(rawTokens.get(14), ",");
        assertEquals(rawTokens.get(15), "'johnsmith@hotmail.com'");
        assertEquals(rawTokens.get(16), ")");
        assertEquals(rawTokens.get(17), ";");
    }

    @Test
    public void tokeniserTest23() throws ParserExceptions {
        String instructions = "JOIN census AND votes ON population AND cast;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "JOIN");
        assertEquals(rawTokens.get(1), "census");
        assertEquals(rawTokens.get(2), "AND");
        assertEquals(rawTokens.get(3), "votes");
        assertEquals(rawTokens.get(4), "ON");
        assertEquals(rawTokens.get(5), "population");
        assertEquals(rawTokens.get(6), "AND");
        assertEquals(rawTokens.get(7), "cast");
        assertEquals(rawTokens.get(8), ";");
    }

    @Test
    public void tokeniserTest24() throws ParserExceptions {
        String instructions = "JOIN census AND votes ON census.population AND votes.cast;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "JOIN");
        assertEquals(rawTokens.get(1), "census");
        assertEquals(rawTokens.get(2), "AND");
        assertEquals(rawTokens.get(3), "votes");
        assertEquals(rawTokens.get(4), "ON");
        assertEquals(rawTokens.get(5), "census.population");
        assertEquals(rawTokens.get(6), "AND");
        assertEquals(rawTokens.get(7), "votes.cast");
        assertEquals(rawTokens.get(8), ";");
    }

    @Test
    public void tokeniserTest25() throws ParserExceptions {
        String instructions = "select people FROM census WHERE population LIKE '1000';";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "select");
        assertEquals(rawTokens.get(1), "people");
        assertEquals(rawTokens.get(2), "FROM");
        assertEquals(rawTokens.get(3), "census");
        assertEquals(rawTokens.get(4), "WHERE");
        assertEquals(rawTokens.get(5), "population");
        assertEquals(rawTokens.get(6), "LIKE");
        assertEquals(rawTokens.get(7), "'1000'");
        assertEquals(rawTokens.get(8), ";");
    }

    @Test
    public void tokeniserTest26() throws ParserExceptions {
        String instructions = "select people FROM census WHERE (population LIKE '1000');";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "select");
        assertEquals(rawTokens.get(1), "people");
        assertEquals(rawTokens.get(2), "FROM");
        assertEquals(rawTokens.get(3), "census");
        assertEquals(rawTokens.get(4), "WHERE");
        assertEquals(rawTokens.get(5), "(");
        assertEquals(rawTokens.get(6), "population");
        assertEquals(rawTokens.get(7), "LIKE");
        assertEquals(rawTokens.get(8), "'1000'");
        assertEquals(rawTokens.get(9), ")");
        assertEquals(rawTokens.get(10), ";");
    }

    @Test
    public void tokeniserTest27() throws ParserExceptions {
        String instructions = "select people FROM census WHERE (population LIKE '1000' AND votes > 100);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "select");
        assertEquals(rawTokens.get(1), "people");
        assertEquals(rawTokens.get(2), "FROM");
        assertEquals(rawTokens.get(3), "census");
        assertEquals(rawTokens.get(4), "WHERE");
        assertEquals(rawTokens.get(5), "(");
        assertEquals(rawTokens.get(6), "population");
        assertEquals(rawTokens.get(7), "LIKE");
        assertEquals(rawTokens.get(8), "'1000'");
        assertEquals(rawTokens.get(9), "AND");
        assertEquals(rawTokens.get(10), "votes");
        assertEquals(rawTokens.get(11), ">");
        assertEquals(rawTokens.get(12), "100");
        assertEquals(rawTokens.get(13), ")");
        assertEquals(rawTokens.get(14), ";");
    }

    @Test
    public void tokeniserTest28() throws ParserExceptions {
        String instructions = "select people FROM census WHERE ((population LIKE '1000') AND (votes > 100));";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "select");
        assertEquals(rawTokens.get(1), "people");
        assertEquals(rawTokens.get(2), "FROM");
        assertEquals(rawTokens.get(3), "census");
        assertEquals(rawTokens.get(4), "WHERE");
        assertEquals(rawTokens.get(5), "(");
        assertEquals(rawTokens.get(6), "(");
        assertEquals(rawTokens.get(7), "population");
        assertEquals(rawTokens.get(8), "LIKE");
        assertEquals(rawTokens.get(9), "'1000'");
        assertEquals(rawTokens.get(10), ")");
        assertEquals(rawTokens.get(11), "AND");
        assertEquals(rawTokens.get(12), "(");
        assertEquals(rawTokens.get(13), "votes");
        assertEquals(rawTokens.get(14), ">");
        assertEquals(rawTokens.get(15), "100");
        assertEquals(rawTokens.get(16), ")");
        assertEquals(rawTokens.get(17), ")");
        assertEquals(rawTokens.get(18), ";");
    }

    @Test
    public void tokeniserTest29() throws ParserExceptions {
        String instructions = "select people FROM census WHERE population LIKE '1000' AND votes > 100;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "select");
        assertEquals(rawTokens.get(1), "people");
        assertEquals(rawTokens.get(2), "FROM");
        assertEquals(rawTokens.get(3), "census");
        assertEquals(rawTokens.get(4), "WHERE");
        assertEquals(rawTokens.get(5), "population");
        assertEquals(rawTokens.get(6), "LIKE");
        assertEquals(rawTokens.get(7), "'1000'");
        assertEquals(rawTokens.get(8), "AND");
        assertEquals(rawTokens.get(9), "votes");
        assertEquals(rawTokens.get(10), ">");
        assertEquals(rawTokens.get(11), "100");
        assertEquals(rawTokens.get(12), ";");
    }

    @Test
    public void tokeniserTest30() throws ParserExceptions {
        String instructions = "select people FROM census WHERE (population LIKE '1000') AND (votes > 100);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "select");
        assertEquals(rawTokens.get(1), "people");
        assertEquals(rawTokens.get(2), "FROM");
        assertEquals(rawTokens.get(3), "census");
        assertEquals(rawTokens.get(4), "WHERE");
        assertEquals(rawTokens.get(5), "(");
        assertEquals(rawTokens.get(6), "population");
        assertEquals(rawTokens.get(7), "LIKE");
        assertEquals(rawTokens.get(8), "'1000'");
        assertEquals(rawTokens.get(9), ")");
        assertEquals(rawTokens.get(10), "AND");
        assertEquals(rawTokens.get(11), "(");
        assertEquals(rawTokens.get(12), "votes");
        assertEquals(rawTokens.get(13), ">");
        assertEquals(rawTokens.get(14), "100");
        assertEquals(rawTokens.get(15), ")");
        assertEquals(rawTokens.get(16), ";");
    }

    @Test
    public void tokeniserTest31() throws ParserExceptions {
        String instructions = "select people FROM census WHERE population LIKE '1000' AND (votes > 100);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "select");
        assertEquals(rawTokens.get(1), "people");
        assertEquals(rawTokens.get(2), "FROM");
        assertEquals(rawTokens.get(3), "census");
        assertEquals(rawTokens.get(4), "WHERE");
        assertEquals(rawTokens.get(5), "population");
        assertEquals(rawTokens.get(6), "LIKE");
        assertEquals(rawTokens.get(7), "'1000'");
        assertEquals(rawTokens.get(8), "AND");
        assertEquals(rawTokens.get(9), "(");
        assertEquals(rawTokens.get(10), "votes");
        assertEquals(rawTokens.get(11), ">");
        assertEquals(rawTokens.get(12), "100");
        assertEquals(rawTokens.get(13), ")");
        assertEquals(rawTokens.get(14), ";");
    }

    @Test
    public void tokeniserTest32() throws ParserExceptions {
        String instructions = "select people FROM census WHERE population LIKE '1000' AND votes > 100 OR ward == 1000;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "select");
        assertEquals(rawTokens.get(1), "people");
        assertEquals(rawTokens.get(2), "FROM");
        assertEquals(rawTokens.get(3), "census");
        assertEquals(rawTokens.get(4), "WHERE");
        assertEquals(rawTokens.get(5), "population");
        assertEquals(rawTokens.get(6), "LIKE");
        assertEquals(rawTokens.get(7), "'1000'");
        assertEquals(rawTokens.get(8), "AND");
        assertEquals(rawTokens.get(9), "votes");
        assertEquals(rawTokens.get(10), ">");
        assertEquals(rawTokens.get(11), "100");
        assertEquals(rawTokens.get(12), "OR");
        assertEquals(rawTokens.get(13), "ward");
        assertEquals(rawTokens.get(14), "==");
        assertEquals(rawTokens.get(15), "1000");
        assertEquals(rawTokens.get(16), ";");
    }

    @Test
    public void tokeniserTest33() throws ParserExceptions {
        String instructions = "select people FROM census WHERE population LIKE '1000' AND votes > 100 OR ward == 1000 OR population >= +100000.6;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "select");
        assertEquals(rawTokens.get(1), "people");
        assertEquals(rawTokens.get(2), "FROM");
        assertEquals(rawTokens.get(3), "census");
        assertEquals(rawTokens.get(4), "WHERE");
        assertEquals(rawTokens.get(5), "population");
        assertEquals(rawTokens.get(6), "LIKE");
        assertEquals(rawTokens.get(7), "'1000'");
        assertEquals(rawTokens.get(8), "AND");
        assertEquals(rawTokens.get(9), "votes");
        assertEquals(rawTokens.get(10), ">");
        assertEquals(rawTokens.get(11), "100");
        assertEquals(rawTokens.get(12), "OR");
        assertEquals(rawTokens.get(13), "ward");
        assertEquals(rawTokens.get(14), "==");
        assertEquals(rawTokens.get(15), "1000");
        assertEquals(rawTokens.get(16), "OR");
        assertEquals(rawTokens.get(17), "population");
        assertEquals(rawTokens.get(18), ">=");
        assertEquals(rawTokens.get(19), "+100000.6");
        assertEquals(rawTokens.get(20), ";");
    }

    @Test
    public void tokeniserTest34() throws ParserExceptions {
        String instructions = "UpDatE votes SET votescast = 1000 WHERE votescast LIKE 2000;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "UpDatE");
        assertEquals(rawTokens.get(1), "votes");
        assertEquals(rawTokens.get(2), "SET");
        assertEquals(rawTokens.get(3), "votescast");
        assertEquals(rawTokens.get(4), "=");
        assertEquals(rawTokens.get(5), "1000");
        assertEquals(rawTokens.get(6), "WHERE");
        assertEquals(rawTokens.get(7), "votescast");
        assertEquals(rawTokens.get(8), "LIKE");
        assertEquals(rawTokens.get(9), "2000");
        assertEquals(rawTokens.get(10), ";");
    }

    @Test
    public void tokeniserTest35() throws ParserExceptions {
        String instructions = "UpDatE votes SET votescast = 1000, ward = 'Windmill Hill' WHERE votescast LIKE 2000;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "UpDatE");
        assertEquals(rawTokens.get(1), "votes");
        assertEquals(rawTokens.get(2), "SET");
        assertEquals(rawTokens.get(3), "votescast");
        assertEquals(rawTokens.get(4), "=");
        assertEquals(rawTokens.get(5), "1000");
        assertEquals(rawTokens.get(6), ",");
        assertEquals(rawTokens.get(7), "ward");
        assertEquals(rawTokens.get(8), "=");
        assertEquals(rawTokens.get(9), "'Windmill Hill'");
        assertEquals(rawTokens.get(10), "WHERE");
        assertEquals(rawTokens.get(11), "votescast");
        assertEquals(rawTokens.get(12), "LIKE");
        assertEquals(rawTokens.get(13), "2000");
        assertEquals(rawTokens.get(14), ";");
    }

    @Test
    public void tokeniserTest36() throws ParserExceptions {
        String instructions = "UpDatE votes SET votescast = 1000, ward = 'Windmill Hill' WHERE votescast LIKE 2000 AND population > 1000;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "UpDatE");
        assertEquals(rawTokens.get(1), "votes");
        assertEquals(rawTokens.get(2), "SET");
        assertEquals(rawTokens.get(3), "votescast");
        assertEquals(rawTokens.get(4), "=");
        assertEquals(rawTokens.get(5), "1000");
        assertEquals(rawTokens.get(6), ",");
        assertEquals(rawTokens.get(7), "ward");
        assertEquals(rawTokens.get(8), "=");
        assertEquals(rawTokens.get(9), "'Windmill Hill'");
        assertEquals(rawTokens.get(10), "WHERE");
        assertEquals(rawTokens.get(11), "votescast");
        assertEquals(rawTokens.get(12), "LIKE");
        assertEquals(rawTokens.get(13), "2000");
        assertEquals(rawTokens.get(14), "AND");
        assertEquals(rawTokens.get(15), "population");
        assertEquals(rawTokens.get(16), ">");
        assertEquals(rawTokens.get(17), "1000");
        assertEquals(rawTokens.get(18), ";");
    }

    @Test
    public void tokeniserTest37() throws ParserExceptions {
        String instructions = "DeleTE FROM votes WHERE (population >= 10000);";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "DeleTE");
        assertEquals(rawTokens.get(1), "FROM");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "WHERE");
        assertEquals(rawTokens.get(4), "(");
        assertEquals(rawTokens.get(5), "population");
        assertEquals(rawTokens.get(6), ">=");
        assertEquals(rawTokens.get(7), "10000");
        assertEquals(rawTokens.get(8), ")");
        assertEquals(rawTokens.get(9), ";");
    }

    @Test
    public void tokeniserTest38() throws ParserExceptions {
        String instructions = "DeleTE    FROM    votes    WHERE    (   population    >=    10000   )   ;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "DeleTE");
        assertEquals(rawTokens.get(1), "FROM");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "WHERE");
        assertEquals(rawTokens.get(4), "(");
        assertEquals(rawTokens.get(5), "population");
        assertEquals(rawTokens.get(6), ">=");
        assertEquals(rawTokens.get(7), "10000");
        assertEquals(rawTokens.get(8), ")");
        assertEquals(rawTokens.get(9), ";");
    }

    @Test
    public void tokeniserTest39() throws ParserExceptions {
        String instructions = "DeleTE    FROM    votes    WHERE    (   population>=10000   )   ;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "DeleTE");
        assertEquals(rawTokens.get(1), "FROM");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "WHERE");
        assertEquals(rawTokens.get(4), "(");
        assertEquals(rawTokens.get(5), "population");
        assertEquals(rawTokens.get(6), ">=");
        assertEquals(rawTokens.get(7), "10000");
        assertEquals(rawTokens.get(8), ")");
        assertEquals(rawTokens.get(9), ";");
    }

    @Test
    public void tokeniserTest40() throws ParserExceptions {
        String instructions = "DeleTE    FROM    votes    WHERE    (   population >= 'sdf df'   )   ;";
        DBTokeniser tokens = new DBTokeniser(instructions);
        ArrayList<String> rawTokens = tokens.removeWhiteSpace();
        assertEquals(rawTokens.get(0), "DeleTE");
        assertEquals(rawTokens.get(1), "FROM");
        assertEquals(rawTokens.get(2), "votes");
        assertEquals(rawTokens.get(3), "WHERE");
        assertEquals(rawTokens.get(4), "(");
        assertEquals(rawTokens.get(5), "population");
        assertEquals(rawTokens.get(6), ">=");
        assertEquals(rawTokens.get(7), "'sdf df'");
        assertEquals(rawTokens.get(8), ")");
        assertEquals(rawTokens.get(9), ";");
    }
}
