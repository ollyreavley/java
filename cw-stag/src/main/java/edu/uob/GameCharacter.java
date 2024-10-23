package edu.uob;

/**
 * Child of GameLocationObject, only used for characters
 * such as elf and lumberjack.
 * Further child class for players (sion and simon).
 */
public class GameCharacter extends GameLocationObject{

    /**
     * constructor for the Character class
     * only name and description are really necessary
     * @param name
     * @param description
     */
    public GameCharacter(final String name, final String description) {
        super(name, description);
    }
}
