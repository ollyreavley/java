package edu.uob;

import java.io.IOException;
import java.util.*;
import static edu.uob.TokenShortHand.*;

public class DBParser {

    public ArrayList<String> rawTokens;
    public int position;
    public int conditionStart;
    public int conditionCount;
    public int conditionEnd;
    public ArrayList<TokenShortHand> tokenStream;
    public ArrayList<String> conditions;
    public String databaseName;
    public ArrayList<String> tableNames;

    public ArrayList<String> attributeNames;
    public ArrayList<String> conditionAttributes;

    public ArrayList<String> values;
    public ArrayList<String> conditionValues;

    public DBParser(ArrayList<String> fromTokeniser) {
        if(fromTokeniser != null){
            rawTokens = new ArrayList<String>();
            rawTokens.addAll(fromTokeniser);
            tableNames = new ArrayList<String>();
            tokenStream = new ArrayList<TokenShortHand>();
            attributeNames = new ArrayList<String>();
            values = new ArrayList<String>();
            conditions = new ArrayList<String>();
            conditionValues = new ArrayList<String>();
            conditionAttributes = new ArrayList<String>();
        }
    }

    public DBCmd isCommand(ArrayList<String> rawTokens) throws ParserExceptions, IOException {
        DBCmd cmd = null;
        cmd = commandType(rawTokens);
        if (position >= rawTokens.size()){
            missingColon();
        } else {
            tokenStream.add(END);
            return cmd;
        }
        return null;
    }

    public void missingColon() throws ParserExceptions{
        throw new ParserExceptions.MissingSemiColon(position);
    }

    public DBCmd commandType(ArrayList<String> rawTokens) throws ParserExceptions, IOException {
        if (rawTokens.get(position).equalsIgnoreCase("USE")) {
            return useCheck();
        } else if (rawTokens.get(position).equalsIgnoreCase("CREATE")) {
            return createCheck();
        } else if (rawTokens.get(position).equalsIgnoreCase("DROP")) {
            return dropCheck();
        } else if (rawTokens.get(position).equalsIgnoreCase("SELECT")) {
            return selectTableCheck();
        } else if (rawTokens.get(position).equalsIgnoreCase("ALTER")) {
            return alterTableCheck();
        } else if (rawTokens.get(position).equalsIgnoreCase("INSERT")) {
            return insertTableCheck();
        } else if(rawTokens.get(position).equalsIgnoreCase("JOIN")) {
            return JoinTableCheck();
        } else if(rawTokens.get(position).equalsIgnoreCase("UPDATE")) {
            return updateTableCheck();
        }  if(rawTokens.get(position).equalsIgnoreCase("DELETE")){
            return deleteTableCheck();
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public DBUseCmd useCheck() throws ParserExceptions{
        DBUseCmd use = new DBUseCmd();
        tokenStream.add(USE);
        incrementPosition();
        if (databaseOrTableName(rawTokens)) {
            tokenStream.add(DATABASE);
            use.addTokens(tokenStream);
            use.databaseName = rawTokens.get(position);
            incrementPosition();
            return use;
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public DBCreateCmd createCheck() throws ParserExceptions{
        tokenStream.add(CREATE);
        DBCreateCmd create = new DBCreateCmd();
        incrementPosition();
        if (tableDatabaseCheck(rawTokens)) {
            //To check a table name has been entered.
            if(!tableNames.isEmpty()){
                create.addTableName(tableNames);
                //to check a database name has been entered
            } else if(!databaseName.isEmpty()){
                create.addDatabaseName(databaseName);
            }
            positionCheck();
            if (rawTokens.get(position).equals("(")) {
                if(bracketAttributeCheck(attributeNames)){
                    create.addAttributes(attributeNames);
                    create.addTokens(tokenStream);
                    return create;
                } else{
                    return null;
                }
            }
            create.addTokens(tokenStream);
            return create;
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public void positionCheck() throws ParserExceptions{
        if(position >= rawTokens.size()){
            throw new ParserExceptions.MissingSemiColon(position);
        }
    }

    public DBDropCmd dropCheck() throws ParserExceptions{
        tokenStream.add(DROP);
        DBDropCmd drop = new DBDropCmd();
        incrementPosition();
        if(tableDatabaseCheck(rawTokens)) {
            if(!tableNames.isEmpty()){
                drop.addTableName(tableNames);
            } else if(!databaseName.isEmpty()){
                drop.addDatabaseName(databaseName);
            }
            drop.addTokens(tokenStream);
            return drop;
        } else{
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public DBAlterCmd alterTableCheck() throws ParserExceptions{
        tokenStream.add(ALTER);
        DBAlterCmd alter = new DBAlterCmd();
        incrementPosition();
        tableDatabaseCheck(rawTokens);
        if (addOrDrop()) {
            incrementPosition();
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
        if (attributeCheck(attributeNames)) {
            alter.addTokens(tokenStream);
            alter.addTableName(tableNames);
            alter.addAttributes(attributeNames);
            incrementPosition();
            return alter;
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public boolean addOrDrop() {
        if (rawTokens.get(position).equalsIgnoreCase("ADD")) {
            tokenStream.add(ADD);
            return true;
        } else if (rawTokens.get(position).equalsIgnoreCase("DROP")) {
            tokenStream.add(DROP);
            return true;
        } else{
            return false;
        }
    }

    public DBInsertCmd insertTableCheck() throws ParserExceptions{
        tokenStream.add(INSERT);
        DBInsertCmd insert = new DBInsertCmd();
        incrementPosition();
        compareKeyWordsIncrmt("INTO");
        //Function call to incremement counter, check result of
        //nested functioncall is correct and if not throw an exception
        incrmtOrThrowExcep(databaseOrTableName(rawTokens));
        tableNames.add(rawTokens.get(position - 1));
        //function call to incremeemtn and compare to stated word
        compareKeyWordsIncrmt("VALUES");
        if (bracketValueCheck(values)) {
            insert.addTokens(tokenStream);
            insert.addTableName(tableNames);
            insert.addValues(values);
            return insert;
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public boolean tableDatabaseCheck(ArrayList<String> rawTokens) throws ParserExceptions{
        if (rawTokens.get(position).equalsIgnoreCase("table")) {
            tokenStream.add(TABLE);
            incrementPosition();
            //check that database/table name is not a reserved word
            reservedWordCheck(rawTokens.get(position));
            tableNames.add(rawTokens.get(position));
            incrmtOrThrowExcep(databaseOrTableName(rawTokens));
            return true;
        } else if (rawTokens.get(position).equalsIgnoreCase("database")) {
            tokenStream.add(DATABASE);
            incrementPosition();
            reservedWordCheck(rawTokens.get(position));
            databaseName = rawTokens.get(position);
            incrmtOrThrowExcep(databaseOrTableName(rawTokens));
            return true;
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public void reservedWordCheck(String word) throws ParserExceptions{
        if(rawTokens.get(position - 1).equalsIgnoreCase("SELECT")){
            return;
        }
        String[] reserved = new String[] {"use", "create", "database", "table", "drop", "alter", "insert", "into", "values", "select", "from", "where", "update", "set", "delete", "join", "and", "on", "add", "drop", "true", "false", "or", "like", "id"};
        int i = 0;
        while(i < reserved.length){
            if(reserved[i].equalsIgnoreCase(word)){
                throw new ParserExceptions.InvalidDatabaseOrTableName(rawTokens.get(position));
            }
            i++;
        }
    }

    public DBSelectCmd selectTableCheck() throws ParserExceptions{
        tokenStream.add(SELECT);
        DBSelectCmd select = new DBSelectCmd();
        incrementPosition();
        //check attributes are words or *
        if (!attributeListCheck(attributeNames) && !wildAttributeCheck(attributeNames)) {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
        select.addAttributes(attributeNames);
        compareKeyWordsIncrmt("FROM");
        if(databaseOrTableName(rawTokens)) {
            tableNames.add(rawTokens.get(position));
            select.addTableName(tableNames);
            incrementPosition();
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
        if(rawTokens.get(position).equalsIgnoreCase("WHERE")) {
            incrementPosition();
            conditionStart = position;
            if(conditionTest()){
                conditionEnd = position;
                bracketCount();
                //adding tokens and other data to structures in interpreter class
                select.addCondition(conditions);
                select.addConditionAttributes(conditionAttributes);
                select.addConditionValues(conditionValues);
                select.addTokens(tokenStream);
                if(!rawTokens.get(position).equals(";")){
                    throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
                }
                return select;
            } else{
                throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
            }
        }
        select.addTokens(tokenStream);
        if(!rawTokens.get(position).equals(";")){
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
        return select;
    }

    public DBJoinCmd JoinTableCheck() throws ParserExceptions {
        DBJoinCmd join = new DBJoinCmd();
        tokenStream.add(JOIN);
        incrementPosition();
        incrmtOrThrowExcep(databaseOrTableName(rawTokens));
        tokenStream.add(TABLE);
        tableNames.add(rawTokens.get(position - 1));
        compareKeyWordsIncrmt("AND");
        incrmtOrThrowExcep(databaseOrTableName(rawTokens));
        tokenStream.add(TABLE);
        tableNames.add(rawTokens.get(position - 1));
        compareKeyWordsIncrmt("ON");
        incrmtOrThrowExcep(attributeCheck(attributeNames));
        compareKeyWordsIncrmt("AND");
        if(attributeCheck(attributeNames)){
            incrementPosition();
            join.addTableName(tableNames);
            join.addAttributes(attributeNames);
            join.addTokens(tokenStream);
            return join;
        } else{
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public boolean bracketAttributeCheck(ArrayList<String> attributes) throws ParserExceptions{
        if (rawTokens.get(position).equals("(")) {
            incrementPosition();
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
        if (attributeListCheck(attributes) && rawTokens.get(position).equals(")")) {
            incrementPosition();
            return true;
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public boolean bracketValueCheck(ArrayList<String> values) throws ParserExceptions{
        if (rawTokens.get(position).equals("(")) {
            incrementPosition();
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
        if (valueListCheck(values) && rawTokens.get(position).equals(")")) {
            incrementPosition();
            return true;
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public boolean valueListCheck(ArrayList<String> values) throws ParserExceptions{
        if (valueCheck(values)) {
            if (rawTokens.get(position).equals(",")) {
                incrementPosition();
                //values can be chained together hence recursive call
                if (valueListCheck(values)) {
                    return true;
                } else {
                    throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
                }
            } else if(rawTokens.get(position).equals(")")){
                return true;
            } else {
                throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
            }
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public boolean valueCheck(ArrayList<String> values) throws ParserExceptions{
        if(stringLiteralCheck(rawTokens)){
            tokenStream.add(STRING);
            //stripping off the ' marks
            values.add(rawTokens.get(position).replace("'",""));
            incrementPosition();
            return true;
        } else if(booleanLiteralCheck(rawTokens)){
            tokenStream.add(BOOLEAN);
            values.add(rawTokens.get(position));
            incrementPosition();
            return true;
        } else if(isDigitPlusNeg(rawTokens.get(position))){
            if(floatLiteralCheck(rawTokens)){
                tokenStream.add(FLOAT);
                values.add(rawTokens.get(position));
                incrementPosition();
                return true;
            } else if(integerLiteralCheck(rawTokens)){
                tokenStream.add(INTEGER);
                values.add(rawTokens.get(position));
                incrementPosition();
                return true;
            }
        } else if(rawTokens.get(position).equalsIgnoreCase("NULL")){
            tokenStream.add(NULL);
            values.add(rawTokens.get(position));
            incrementPosition();
            return true;
        } else{
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
        throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
    }

    public boolean attributeListCheck(ArrayList<String> attributes) throws ParserExceptions {
        if (attributeCheck(attributes)) {
            incrementPosition();
            if (rawTokens.get(position).equals(",")) {
                incrementPosition();
                return attributeListCheck(attributes);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean attributeCheck(ArrayList<String> attributes) throws ParserExceptions {
        //attribute names can be table.attribute, checks both halves are correct.
        if (rawTokens.get(position).contains(".")) {
            String[] splitName = rawTokens.get(position).split("\\.");
            reservedWordCheck(splitName[0]);
            reservedWordCheck(splitName[1]);
            //checks only one full stop
            if(splitName.length > 2){
                return false;
            }
            if(isAlphabeticOrNumeric(splitName[0]) && isAlphabeticOrNumeric(splitName[1])){
                tokenStream.add(ATTRIBUTE);
                attributes.add(rawTokens.get(position));
                return true;
            } else{
                return false;
            }
        } else {
            if(isAlphabeticOrNumeric(rawTokens.get(position))){
                reservedWordCheck(rawTokens.get(position));
                tokenStream.add(ATTRIBUTE);
                attributes.add(rawTokens.get(position));
                return true;
            } else{
                return false;
            }
        }
    }

    public boolean wildAttributeCheck(ArrayList<String> attributes) {
        if (rawTokens.get(position).equals("*")) {
            tokenStream.add(ALL);
            attributes.add(rawTokens.get(position));
            incrementPosition();
            return true;
        } else {
            return false;
        }
    }

    public boolean databaseOrTableName(ArrayList<String> rawTokens) throws ParserExceptions{
        if (isAlphabeticOrNumeric(rawTokens.get(position))) {
            return true;
        }
        throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
    }

    public boolean stringLiteralCheck(ArrayList<String> rawTokens) {
        if(!rawTokens.get(position).startsWith("'") && !rawTokens.get(position).endsWith("'")){
            return false;
        }
        int i = 1;
        String testChar = String.valueOf(rawTokens.get(position).charAt(i));
        while(!Objects.equals(testChar, "'")){
            //can't throw an error here are string literal check
            //is followed by float check, hence empty bodies
            if(charLiteralCheck(testChar)){
            } else if(stringLiteralCheck(rawTokens)){
                if(charLiteralCheck(testChar)){
                } else{
                    return false;
                }
            } else{
                return false;
            }
            i++;
            testChar = String.valueOf(rawTokens.get(position).charAt(i));
        }
        return true;
    }

    public boolean charLiteralCheck(String testChar) {
        if(isAlphabeticOrNumeric(testChar)){
            return true;
        } else if(symbolCheck(testChar)){
            return true;
        } else if (testChar.equals(" ")){
            return true;
        } else return testChar.isEmpty();
    }

    public boolean booleanLiteralCheck(ArrayList<String> rawTokens) {
        if(rawTokens.get(position).equalsIgnoreCase("TRUE")){
            return true;
        } else return rawTokens.get(position).equalsIgnoreCase("FALSE");
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

    public boolean symbolCheck(String word) {
        String[] symbols = {"!", "#", "$", "%", "&", "(", ")", "*", ",", "-", ".", "/", ":", ";", ">", "=", "<", "?", "@", "[", "\\", "]", "^", "_", "`", "{", "}", "~"};
        ArrayList<String> newSymbols = new ArrayList<String> (Arrays.asList(symbols));
        return newSymbols.contains(word);
    }

    public boolean isDigitPlusNeg(String testChar) {
        if(testChar.charAt(0) == '-'){
            return true;
        } else if(testChar.charAt(0) == '+'){
            return true;
        } else return testChar.charAt(0) >= '0' && testChar.charAt(0) <= '9';
    }

    public boolean floatLiteralCheck(ArrayList<String> rawTokens) {
        if(rawTokens.get(position).contains(".")){
            String[] testStrings = rawTokens.get(position).split("\\.");
            if(testStrings.length > 2){
                return false;
            }
            //if numbers have + or - then make a copy and replace to ensure rest of
            //number is just digits
            if(testStrings[0].charAt(0) == '-' || testStrings[0].charAt(0) == '+'){
                testStrings[0] = testStrings[0].replaceFirst("-", "1");
                testStrings[0] = testStrings[0].replaceFirst("\\+", "1");
            }
            if(isNumeric(testStrings[0]) && isNumeric(testStrings[1])){
                return true;
            } else{
                return false;
            }
        }
        return false;
    }

    public boolean integerLiteralCheck(ArrayList<String> rawTokens) {
        String testString = rawTokens.get(position);
        //as above for floats
        if(testString.charAt(0) == '-' || testString.charAt(0) == '+'){
            testString = testString.replaceFirst("-", "1");
            testString = testString.replaceFirst("\\+", "1");
        }
        if(isNumeric(testString)){
            return true;
        } else{
            return false;
        }
    }

    public boolean isNumeric(String word) {
        char[] letters = word.toCharArray();
        int i = 0, count = 0;
        for (i = 0; i < letters.length; i++) {
            if (letters[i] >= '0' && letters[i] <= '9') {
                count++;
            }
        }
        return count == word.length();
    }

    public boolean conditionTest() throws ParserExceptions{
        if (rawTokens.get(position).equals("(")) {
            conditions.add(rawTokens.get(position));
            position++;
        }
        if (rawTokens.get(position).equals("(")) {
            conditions.add(rawTokens.get(position));
            position++;
        }
        if (attributeCheck(conditionAttributes)) {
            conditions.add(rawTokens.get(position));
            conditionCount++;
            position++;
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
        if (comparatorTest(rawTokens.get(position))) {
            conditions.add(rawTokens.get(position));
            conditionCount++;
            position++;
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
        if (valueCheck(conditionValues)) {
            conditions.add(rawTokens.get(position - 1).replace("'",""));
            conditionCount++;
        } else{
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
        if (rawTokens.get(position).equals(")")) {
            conditions.add(rawTokens.get(position));
            position++;
        }
        if (isAndOrOr()) {
            position++;
            if (conditionTest()) {
                if (rawTokens.get(position).equals(")")) {
                    conditions.add(rawTokens.get(position));
                    position++;
                }
                return true;
            } else {
                throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
            }
        } else{
            return true;
        }
    }
//bracket count checks the number of brackets, condition test above
    //only tests the position of brckets
    //also ensures no right bracket entered before  aleft one
    public void bracketCount() throws ParserExceptions{
        int count = conditionEnd, bracketCount = 0;
        while(count >= conditionStart){
            if(rawTokens.get(count).equals(")")){
                bracketCount++;
            } else if(rawTokens.get(count).equals("(")){
                bracketCount--;
            }
            if(bracketCount < 0){
                throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
            }
            count--;
        }
        if(bracketCount != 0){
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public boolean isAndOrOr(){
        if (rawTokens.get(position).equalsIgnoreCase("AND")){
            conditions.add("AND");
            return true;
        } else if(rawTokens.get(position).equalsIgnoreCase("OR")) {
            conditions.add("OR");
            return true;
        } else{
            return false;
        }
    }

    public DBUpdateCmd updateTableCheck() throws ParserExceptions{
        tokenStream.add(UPDATE);
        DBUpdateCmd update = new DBUpdateCmd();
        incrementPosition();
        incrmtOrThrowExcep(databaseOrTableName(rawTokens));
        tableNames.add(rawTokens.get(position - 1));
        compareKeyWordsIncrmt("SET");
        if(!nameValuePair()){
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
        compareKeyWordsIncrmt("WHERE");
        if(conditionTest()){
            copyToUpdate(update);
            return update;
        } else{
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    private void copyToUpdate(DBUpdateCmd update){
        update.addCondition(conditions);
        update.addTableName(tableNames);
        update.addTokens(tokenStream);
        update.addAttributes(attributeNames);
        update.addConditionAttributes(conditionAttributes);
        update.addValues(values);
        update.addConditionValues(conditionValues);
    }

    public DBDeleteCmd deleteTableCheck() throws ParserExceptions{
        tokenStream.add(DELETE);
        DBDeleteCmd delete = new DBDeleteCmd();
        incrementPosition();
        compareKeyWordsIncrmt("FROM");
        incrmtOrThrowExcep(databaseOrTableName(rawTokens));
        tableNames.add(rawTokens.get(position - 1));
        compareKeyWordsIncrmt("WHERE");
        if(conditionTest()){
            delete.addCondition(conditions);
            delete.addTableName(tableNames);
            delete.addTokens(tokenStream);
            return delete;
        } else{
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public boolean nameValuePair() throws ParserExceptions{
        incrmtOrThrowExcep(attributeCheck(attributeNames));
        if(rawTokens.get(position).equals("=")){
            incrementPosition();
        } else{
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
        if(!valueCheck(values)){
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
        if(rawTokens.get(position).equals(",")){
            incrementPosition();
            if(nameValuePair()){
                return true;
            } else{
                throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
            }
        } else{
            return true;
        }
    }

    public boolean comparatorTest(String testString) throws ParserExceptions{
        String[] symbols = {"==", ">", "<", ">=", "<=", "!="};
        String like = "LIKE";
        ArrayList<String> newSymbols = new ArrayList<String> (Arrays.asList(symbols));
        if(newSymbols.contains(testString) || testString.equalsIgnoreCase(like)){
            tokenStream.add(COMPARATOR);
            return true;
        } else{
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public void incrementPosition(){
        position++;
    }

    public void compareKeyWordsIncrmt(String wordToTest) throws ParserExceptions{
        if (rawTokens.get(position).equalsIgnoreCase(wordToTest)) {
            incrementPosition();
        } else {
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }

    public void incrmtOrThrowExcep(boolean testResult) throws ParserExceptions{
        if(testResult){
            incrementPosition();
        } else{
            throw new ParserExceptions.InvalidCommand(rawTokens.get(position));
        }
    }
}
