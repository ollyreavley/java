package edu.uob;

/**
 * Class for trapdoor, tree etc.  Anything which cannot be picked up.
 * No child classes
 */
public class GameFurniture extends GameLocationObject{

    /**
     * constructor for the class
     * only name and description are needed in here
     */
    public GameFurniture(final String name, final String description) {
        super(name, description);
    }
}
