package edu.uob;
/**
 *Enum class for the built-in commands that are consistent across games
 */

public enum BuiltInCommands {
    INVENTORY("inventory"),
    INV("inv"),
    GET("get"),
    DROP("drop"),
    GOTO("goto"),
    LOOK("look");
    /**
     * Each enum has a string associated with it to allow for
     * the interpreter to test the string value to check if a command
     * is a built in or not
     */
    private final String action;
    BuiltInCommands(final String command) {
        this.action = command;
    }
    /**Returns the string value associated with the enum, used in
    *built in command testing in the interpreter
     */
    public String getAction(){
        return this.action;
    }
}
