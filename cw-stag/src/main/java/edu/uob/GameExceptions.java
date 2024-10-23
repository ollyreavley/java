package edu.uob;

import java.io.Serial;

/**
 * For throwing specialised error messages if the command is incorrect
 */

public class GameExceptions extends Exception {
    @Serial private static final long serialVersionUID = 1;

    /**
     * constructor for the super class
     */
    public GameExceptions(final String message){
        super(message);
    }

    /**
     * if the built in command is invalid this is called
     */
    public static class InvalidBuiltInCommand extends GameExceptions{
        @Serial private static final long serialVersionUID = 1;

        /**
         * constructor for the class
         */
        public InvalidBuiltInCommand(final String message){
            super(message);
        }
    }

    /**
     * class for if too many unrelated commands are entered
     */
    public static class TooManyCommands extends GameExceptions{
        @Serial private static final long serialVersionUID = 1;

        /**
         * constructor for the class
         */
        public TooManyCommands(final String message){
            super(message);
        }
    }

    /**
     * if the command is incorrectly structured
     */
    public static class InvalidCommandStructure extends GameExceptions{
        @Serial private static final long serialVersionUID = 1;

        /**
         * constructor for the class
         */
        public InvalidCommandStructure(final String message){
            super(message);
        }
    }

    /**
     * for if the subject is not valid to the command
     */
    public static class SubjectFromDiffCommand extends GameExceptions{
        @Serial private static final long serialVersionUID = 1;

        /**
         * constructor for the class
         */
        public SubjectFromDiffCommand(final String message){super(message);}
    }
}
