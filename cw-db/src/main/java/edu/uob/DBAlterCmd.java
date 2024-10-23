package edu.uob;

import java.io.IOException;
import java.util.*;
import static edu.uob.TokenShortHand.*;


public class DBAlterCmd extends DBCmd{

    public ArrayList<String> tableNames;
    public ArrayList<String> attributeNames;
    public String databaseName;
    public ArrayList<TokenShortHand> tokens;
    public ArrayList<String> values;

    public DBAlterCmd(){
        tokens = new ArrayList<TokenShortHand>();
        tableNames = new ArrayList<String>();
        attributeNames = new ArrayList<String>();
    }

    public void addTokens(ArrayList<TokenShortHand> tokenStream){
        tokens.addAll(tokenStream);
    }

    public void addTableName(ArrayList<String> tableNamesFromParse){
        tableNames.addAll(tableNamesFromParse);
    }

    public void addDatabaseName(String databaseNameFromParse){
        databaseName = databaseNameFromParse;
    }

    public void addAttributes(ArrayList<String> attributeNamesFromParse){
        attributeNames.addAll(attributeNamesFromParse);
    }

    public String query(DBServer s) {
        try {
            s.openFile(tableNames.get(0));
            if (attributeNames.contains("id") && tokens.contains(DROP)) {
                return "[ERROR] Unable to remove the id column.";
            }
            else if (tokens.contains(ADD)) {
                return addColumn(s);
            } else if (tokens.contains(DROP)) {
                return removeColumn(s);
            } else{
                return "[ERROR] unknown error.";
            }
        } catch (IOException | InterpreterExceptions ex) {
            return "[ERROR] " + ex.getMessage();
        }
    }
    private String addColumn(DBServer s) throws IOException, InterpreterExceptions {

        if(s.data.getNumberOfAttributes(tableNames.get(0)) == 0){
            //for edge case where no attributes in the table before
            s.data.addOneAttributes(s.currentDirectory, attributeNames);
            return "[OK] attribute added";
        }
        else if (s.data.getAttributes(tableNames.get(0)).contains(attributeNames.get(0))) {
            return "[ERROR] This attribute is already in the table.";
        } else {
            if (s.data.addOneAttributes(s.currentDirectory, attributeNames)) {
                return "[OK] attribute added";
            } else {
                return "[ERROR] attribute has not been added";
            }
        }
    }

    private String removeColumn(DBServer s) throws IOException, InterpreterExceptions {
        if (!s.data.getAttributes(tableNames.get(0)).contains(attributeNames.get(0))) {
            return "[ERROR] This attribute is not in the table.";
        } else {
            if (s.data.removeAttribute(s.currentDirectory, attributeNames)) {
                return "[OK] Column " + attributeNames.get(0) + " has been removed.";
            } else {
                return "[ERROR] failed to remove attribute";
            }
        }
    }

}
