package edu.uob;

import java.io.Serial;

public class ParserExceptions extends Exception{
    @Serial
    private static final long serialVersionUID = 1;

    public ParserExceptions(String message){
        super(message);
    }

    public static class InvalidCommand extends ParserExceptions{
        @Serial private static final long serialVersionUID = 1;
        public InvalidCommand(String errorPosition){
            super("The command " + errorPosition + " is syntactically incorrect.");
        }
    }

    public static class InvalidDatabaseOrTableName extends ParserExceptions{
        @Serial private static final long serialVersionUID = 1;
        public InvalidDatabaseOrTableName(String errorPosition){
            super("The name " + errorPosition + " is invalid.");
        }
    }

    public static class MissingSemiColon extends ParserExceptions{
        @Serial private static final long serialVersionUID = 1;
        public MissingSemiColon(int position){
            super("[ERROR] there is no ending semi-colon." + position);
        }
    }

    public static class UnevenQuotes extends ParserExceptions{
        @Serial private static final long serialVersionUID = 1;
        public UnevenQuotes(){
            super("There are an uneven number of single quotes around a string literal, please check and re-enter the query.");
        }
    }

    public static class LparFirst extends ParserExceptions{
        @Serial private static final long serialVersionUID = 1;
        public LparFirst(){
            super("Left parenthesis has been found before a right parenthesis, please re-enter the query.");
        }
    }
}