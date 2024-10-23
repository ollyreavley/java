package edu.uob;

import java.io.IOException;
import java.util.*;

import static edu.uob.TokenShortHand.COMPARATOR;

public class DBDeleteCmd extends DBCmd{

    public ArrayList<String> tableNames;
    public ArrayList<String> attributeNames;
    public ArrayList<String> rows;
    public String databaseName;
    public ArrayList<String> condition;
    public ArrayList<String> subCondition;
    public ArrayList<String> convertedCondition;
    public ArrayList<TokenShortHand> tokens;
    public ArrayList<String> values;

    private ArrayList<ArrayList<Integer>> boolResult;
    private ArrayList<ArrayList<Integer>> tempResult;


    private String bool;

    public DBDeleteCmd(){
        tokens = new ArrayList<TokenShortHand>();
        tableNames = new ArrayList<String>();
        attributeNames = new ArrayList<String>();
        condition = new ArrayList<String>();
        subCondition = new ArrayList<String>();
        convertedCondition = new ArrayList<String>();
        boolResult = new ArrayList<>();
        tempResult = new ArrayList<>();
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

    public void addValues(ArrayList<String> valuesFromParse){
        values.addAll(valuesFromParse);
    }

    public String query(DBServer s) {
        try{
            s.openFile(tableNames.get(0));
            bracketFinder(0, s);
            ArrayList<Integer> rowsToRemove = cycleThroughArray(s);
            if(rowsToRemove.size() > 0){
                s.data.removingRows(s.currentDirectory, rowsToRemove);
                return "[OK]";
            } else{
                return "[ERROR] Row has not been removed.";
            }
        } catch (IOException | InterpreterExceptions ex) {
            return ex.getMessage();
        }
    }

    public void bracketFinder(int position, DBServer s) throws IOException {
        String combined = new String();
        while(position < condition.size()){
            if(condition.get(position).equals(")")){
                position++;
                combined = combined.trim();
                convertedCondition.add(combined);
                combined = "";
            }
            else if(condition.get(position).equals("(")){
                position++;
                combined = "";
            }
            else if(condition.get(position).equalsIgnoreCase("AND") || condition.get(position).equalsIgnoreCase("OR")){
                while(condition.get(position).equals(" ") || isAlphabeticOrNumeric(condition.get(position)) || comparatorTest(condition.get(position)) ){
                    combined = combined.concat( condition.get(position) + " ");
                    position++;
                    if(position >= condition.size()){
                        combined = combined.trim();
                        convertedCondition.add(combined);
                        return;
                    }
                }
                combined = combined.trim();
                convertedCondition.add(combined);
                combined = "";
            }
            else{
                combined = combined.concat( condition.get(position) + " ");
                position++;
            }
        }
        combined = combined.trim();
        convertedCondition.add(combined);
    }

    public boolean isAlphabeticOrNumeric(String word) {
        char[] letters = word.toCharArray();
        int i = 0, count = 0;
        for (i = 0; i < letters.length; i++) {
            if (letters[i] >= 'a' && letters[i] <= 'z') {
                count++;
            } else if (letters[i] >= 'A' && letters[i] <= 'Z') {
                count++;
            } else if (letters[i] >= '0' && letters[i] <= '9') {
                count++;
            }
        }
        return count == word.length();
    }

    public boolean comparatorTest(String testString){
        String[] symbols = {"==", ">", "<", ">=", "<=", "!="};
        String like = "LIKE";
        ArrayList<String> newSymbols = new ArrayList<String> (Arrays.asList(symbols));
        if(newSymbols.contains(testString) || testString.equalsIgnoreCase(like)){
            return true;
        } else{
            return false;
        }
    }

    private ArrayList<Integer> cycleThroughArray(DBServer s) throws IOException, InterpreterExceptions {
        changeCondition(s);
        endingAndOr(s);
        startingAndOr(s);
        if(boolResult.size() == 1){
            return boolResult.get(0);
        }
        return combineResults();
    }

    private ArrayList<Integer> combineResults(){
        int n = 0;
        ArrayList<Integer> newResult = new ArrayList<>();
        while(n < boolResult.size()){
            if(boolResult.get(n).contains( - 1)){
                if(n <= (boolResult.size() - 1) && n >= 1){
                    Set<Integer> leftSide = new HashSet<>(boolResult.get(n - 1));
                    Set<Integer> rightSide = new HashSet<>(boolResult.get(n + 1));
                    newResult = new ArrayList<>(andComp(leftSide, rightSide));
                }
            }
            if(boolResult.get(n).contains(-2)){
                if(n <= (boolResult.size() - 1) && n >= 1){
                    Set<Integer> leftSide = new HashSet<>(boolResult.get(n - 1));
                    Set<Integer> rightSide = new HashSet<>(boolResult.get(n + 1));
                    newResult = new ArrayList<>(orComp(leftSide, rightSide));
                }

            }
            n++;
        }
        return newResult;
    }


    private void changeCondition(DBServer s) throws IOException, InterpreterExceptions {
        int i = 0;
        while(i < convertedCondition.size()){
            if(!convertedCondition.get(i).contains("AND") && !convertedCondition.get(i).contains("OR") && convertedCondition.get(i).length() > 1){
                boolResult.add(splitArrayRow(s, convertedCondition.get(i)));
                convertedCondition.remove(i);
                convertedCondition.add(i, String.valueOf(boolResult.get(0)));
            }
            i++;
        }
    }

    private void endingAndOr(DBServer s) throws IOException, InterpreterExceptions {
        int i = 0;
        while(i < convertedCondition.size()){
            if(convertedCondition.get(i).endsWith("AND")){
                convertedCondition.add(i + 1, convertedCondition.get(i).substring(convertedCondition.get(i).length() - 3, convertedCondition.get(i).length() - 1));
                convertedCondition.set(i, convertedCondition.get(i).replace("AND", ""));
                boolResult.add(splitArrayRow(s, convertedCondition.get(i)));
                convertedCondition.remove(i);
                convertedCondition.add(i, String.valueOf(boolResult.get(0)));
            }
            if(convertedCondition.get(i).endsWith("OR")){
                convertedCondition.add(i + 1, convertedCondition.get(i).substring(convertedCondition.get(i).length() - 2, convertedCondition.get(i).length() - 1));
                convertedCondition.set(i, convertedCondition.get(i).replace("OR", ""));
                boolResult.add(splitArrayRow(s, convertedCondition.get(i)));
                convertedCondition.remove(i);
                convertedCondition.add(i, String.valueOf(boolResult.get(0)));
            }
            if(convertedCondition.get(i).contains("AND")){
                convertedCondition.set(i, convertedCondition.get(i).replace("AND", "-1"));
            }
            if(convertedCondition.get(i).contains("OR")){
                convertedCondition.set(i, convertedCondition.get(i).replace("OR", "-2"));
            }
            i++;
        }
    }

    private void startingAndOr(DBServer s) throws IOException{
        int i = 0;
        while(i < convertedCondition.size()){
            if(convertedCondition.get(i).startsWith("AND")){
                convertedCondition.add(i + 1, convertedCondition.get(i).substring(0, 3));
                convertedCondition.set(i, convertedCondition.get(i).replace("AND", ""));
            }
            if(convertedCondition.get(i).startsWith("OR")){
                convertedCondition.add(i + 1, convertedCondition.get(i).substring(0, 2));
                convertedCondition.set(i, convertedCondition.get(i).replace("OR", ""));
            }
            i++;
        }
    }

    private ArrayList<Integer> splitArrayRow(DBServer s, String conds) throws IOException, InterpreterExceptions {
        conds = conds.trim();
        String[] conditSplit = conds.split(" ");
        if(conditSplit.length != 3){
            return null;
        }
        return findingTheRows(s, conditSplit);
    }

    private ArrayList<Integer> findingTheRows(DBServer s, String[] conditSplit) throws IOException, InterpreterExceptions {
        String attribute = conditSplit[0];
        String comparator = conditSplit[1];
        String value = conditSplit[2];
        return s.data.removeRowTest(attribute, value, comparator);
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
