package edu.uob;

/**
 * parent class for all ojbects which are held in a location
 * to differentiate from GameLocation which also inherits from
 * GameEntity
 */

public abstract class GameLocationObject extends GameEntity{

    /**
     * constructor for the class
     * note they ave name and description of item
     * @param name
     * @param description
     */
    public GameLocationObject(final String name, final String description) {
        super(name, description);
    }
}
