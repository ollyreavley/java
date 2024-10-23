package edu.uob;

/**
 *  Artifact class for items such as key etc.
 */

public class GameArtifact extends GameLocationObject{

    /**
     * constructor for the Artifact class, only the name a and description are
     * necessary
     * @param name
     * @param description
     */
    public GameArtifact(final String name, final String description){
        super(name, description);
    }
}
