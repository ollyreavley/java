package edu.uob;

import java.io.IOException;
import static edu.uob.TokenShortHand.*;
import java.util.*;

public class DBJoinCmd extends DBCmd{
    public ArrayList<String> tableNames;
    public ArrayList<String> attributeNames;
    public String databaseName;
    public ArrayList<TokenShortHand> tokens;
    public ArrayList<String> values;
    private ArrayList<String> leftColumn;
    private ArrayList<String> rightColumn;


    public DBJoinCmd(){
        tokens = new ArrayList<TokenShortHand>();
        tableNames = new ArrayList<String>();
        attributeNames = new ArrayList<String>();
    }

    public String query(DBServer s) {
        try {
            s.openDatabase(s.currentDatabase);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return ex.getMessage();
        }
        try{
            leftColumn = new ArrayList<>(s.data.columnInfo(tableNames.get(0), attributeNames.get(0)));
            rightColumn = new ArrayList<>(s.data.columnInfo(tableNames.get(1), attributeNames.get(1)));
            //builds up column headers for printing
            String columnHeaders = tableNames.get(0) + "." + attributeNames.get(0) + "\t" + tableNames.get(1) + "." + attributeNames.get(1) + "\n";
            columnHeaders = stringBuilder(columnHeaders);
            System.out.println(columnHeaders);
            return "[OK] \n" + columnHeaders;
        } catch (InterpreterExceptions ex){
            return ex.getMessage();
        }

    }

    private String stringBuilder(String columnHeaders){
        //buuilds up the rows for printing
        String tempLeft, tempRight;
        int i = 0;
        while (i < leftColumn.size() || i < rightColumn.size()) {
            if (i >= leftColumn.size()) {
                tempLeft = null;
            } else {
                tempLeft = leftColumn.get(i);
            }
            if (i >= rightColumn.size()) {
                tempRight = null;
            } else {
                tempRight = rightColumn.get(i);
            }
            columnHeaders = columnHeaders.concat(tempLeft + "\t" + tempRight + "\n");
            i++;
        }
        System.out.println(columnHeaders);
        return columnHeaders;
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

    public void addValues(ArrayList<String> valuesFromParse){
        values.addAll(valuesFromParse);
    }
}
