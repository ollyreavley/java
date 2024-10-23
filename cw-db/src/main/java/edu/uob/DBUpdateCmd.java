package edu.uob;

import java.io.File;
import java.io.IOException;
import static edu.uob.TokenShortHand.*;
import java.util.*;

public class DBUpdateCmd extends DBCmd {
    public ArrayList<String> tableNames;
    public ArrayList<String> attributeNames;
    public ArrayList<String> rows;
    public String databaseName;
    public ArrayList<String> condition;
    public ArrayList<TokenShortHand> tokens;
    public ArrayList<String> values;
    public ArrayList<String> conditionValues;
    public ArrayList<String> conditionAttribs;

    private ArrayList<ArrayList<Integer>> boolResult;
    private ArrayList<ArrayList<Integer>> tempResult;

    private String bool;

    public DBUpdateCmd() {
        tokens = new ArrayList<TokenShortHand>();
        tableNames = new ArrayList<String>();
        attributeNames = new ArrayList<String>();
        condition = new ArrayList<String>();
        boolResult = new ArrayList<>();
        tempResult = new ArrayList<>();
        values = new ArrayList<>();
        conditionValues = new ArrayList<>();
        conditionAttribs = new ArrayList<>();
    }

    public String query(DBServer s) {
        try {
            s.openFile(tableNames.get(0));
            ArrayList<Integer> rowsToUpdate = conditionInterp(s);
            if (rowsToUpdate.size() > 0) {
                s.data.editTable(s.currentDirectory, tableNames.get(0), rowsToUpdate, values, attributeNames);
                return "[OK] row updated.";
            }
        } catch (IOException | InterpreterExceptions ex) {
            return "[OK] " + ex.getMessage();
        }
        return "[ERROR] unknown error has occurred";
    }

    public void addCondition(ArrayList<String> conditionsParse){
        condition.addAll(conditionsParse);
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

    public void addConditionAttributes(ArrayList<String> attributeNamesFromParse){
        conditionAttribs.addAll(attributeNamesFromParse);
    }

    public void addValues(ArrayList<String> valuesFromParse){
        values.addAll(valuesFromParse);
    }

    public void addConditionValues(ArrayList<String> valuesFromParse){
        conditionValues.addAll(valuesFromParse);
    }

    public ArrayList<Integer> conditionInterp(DBServer s) throws IOException, InterpreterExceptions {
        ArrayList<Integer> rows = findingTheRows(s);
        if(rows != null){
            return rows;
        }
        return combineConds(s);
    }

    private ArrayList<Integer> findingTheRows(DBServer s) throws IOException, InterpreterExceptions {
        int i = 0, n = 0;
        while (i < condition.size()) {
            String attribute = condition.get(i);
            String comparator = condition.get(i + 1);
            String value = condition.get(i + 2);
            i = i + 3;
            tempResult.add(s.data.removeRowTest(attribute, value, comparator));
            if (tempResult.get(0).size() != 0) {
                boolResult.add(tempResult.get(0));
            }
            if(i < condition.size()){
                if (condition.get(i).equalsIgnoreCase("AND") || condition.get(i).equalsIgnoreCase("OR")) {
                    bool = condition.get(i);
                    i++;
                }
            }
            tempResult.remove(0);
        }
        if (boolResult.size() == 1) {
            return boolResult.get(0);
        }
        return null;
    }

    private ArrayList<Integer> combineConds(DBServer s){
        int n = 0;
        ArrayList<Integer> newResult = new ArrayList<>();
        while(boolResult.size() > 1){
            Set<Integer> leftSide = new HashSet<>(boolResult.get(0));
            Set<Integer> rightSide = new HashSet<>(boolResult.get(1));
            if(bool.equalsIgnoreCase("AND") && (!leftSide.isEmpty() && !rightSide.isEmpty())){
                newResult = new ArrayList<>(andComp(leftSide, rightSide));
            } else if( bool.equalsIgnoreCase("OR")){
                newResult = new ArrayList<>(orComp(leftSide, rightSide));
            }
            boolResult.remove(0);
            boolResult.remove(0);
            boolResult.add(0, newResult);
        }
        return boolResult.get(0);
    }

    public Set<Integer> andComp(Set<Integer> leftSide, Set<Integer> rightSide){
        leftSide.retainAll(rightSide);
        if(leftSide.size() > 0){
            return leftSide;
        } else{
            return null;
        }
    }

    public Set<Integer> orComp(Set<Integer> leftSide, Set<Integer> rightSide){
        if(leftSide.size() > 0 && rightSide.size() > 0) {
            Set<Integer> merged = new HashSet<Integer>();
            merged.addAll(leftSide);
            merged.addAll(rightSide);
            return merged;
        } else if(leftSide.size() > 0){
            return leftSide;
        } else if(rightSide.size() > 0){
            return rightSide;
        } else{
            return null;
        }
    }

}