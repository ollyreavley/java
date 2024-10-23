package edu.uob;

import java.io.IOException;
import java.util.*;

public class DBInsertCmd extends DBCmd{

    public DBInsertCmd(){
        tokens = new ArrayList<TokenShortHand>();
        tableNames = new ArrayList<String>();
        attributeNames = new ArrayList<String>();
        values = new ArrayList<String>();
    }

    public void addTokens(ArrayList<TokenShortHand> tokenStream){
        tokens.addAll(tokenStream);
    }

    public void addTableName(ArrayList<String> tableNamesFromParse){
        tableNames.addAll(tableNamesFromParse);
    }

    public void addValues(ArrayList<String> valuesFromParse){
        values.addAll(valuesFromParse);
    }

    public String query(DBServer s){
        try{
            s.openFile(tableNames.get(0));
            if(values.size() + 1 != s.data.getNumberOfAttributes(tableNames.get(0))){
                return "[ERROR] Number of values entered must match the number of attributes in the table, " + values.size() + " values entered.";
            }
            if(s.data.addRow(s.currentDirectory, values, s)){
                return "[OK]";
            } else{
                return "[ERROR] row not inserted.";
            }
        } catch(IOException | InterpreterExceptions ex){
            return ex.getMessage();
        }
    }
}
