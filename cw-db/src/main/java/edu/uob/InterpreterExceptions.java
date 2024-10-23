package edu.uob;

import java.io.Serial;

public class InterpreterExceptions extends Exception{
    @Serial
    private static final long serialVersionUID = 1;

    public InterpreterExceptions(String message){
        super(message);
    }

    public static class Error extends InterpreterExceptions{
        @Serial
        private static final long serialVersionUID = 1;
        public Error(String message){
            super(message);
        }
    }


}
