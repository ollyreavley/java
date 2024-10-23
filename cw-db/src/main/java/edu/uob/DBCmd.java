package edu.uob;

import java.io.IOException;
import static edu.uob.TokenShortHand.*;
import java.util.*;

abstract class DBCmd {
    public ArrayList<String> tableNames;
    public ArrayList<String> attributeNames;
    public ArrayList<String> rows;
    public String databaseName;
    public ArrayList<String> condition;
    public ArrayList<TokenShortHand> tokens;
    public ArrayList<String> values;

    public DBCmd(){
        tokens = new ArrayList<TokenShortHand>();
        tableNames = new ArrayList<String>();
        attributeNames = new ArrayList<String>();
    }

    public String query(DBServer s) {
        return null;
    }

    public void addTokens(ArrayList<TokenShortHand> tokenStream){
        tokens.addAll(tokenStream);
    }

    public static void setServer(DBServer server){}

    public void addTableName(ArrayList<String> tableNamesFromParse){
        tableNames.addAll(tableNamesFromParse);
    }

    public void addDatabaseName(String databaseNameFromParse){
        databaseName = databaseNameFromParse;
    }

    public void addAttributes(ArrayList<String> attributeNamesFromParse){
        attributeNames.addAll(attributeNamesFromParse);
    }

    public void addValues(ArrayList<String> valuesFromParse){
        values.addAll(valuesFromParse);
    }

}
