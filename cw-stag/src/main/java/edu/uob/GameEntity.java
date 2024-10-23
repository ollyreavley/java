package edu.uob;

/**
 * Parent class for locations, artifacts, furniture, characters and players.
 */

public abstract class GameEntity
{
    /**
     * name of the entity
     */
    private final String name;
    /**
     * description associated with the entity
     */
    private final String description;

    /**
     * constructor for creating the class, only adds the name
     * and description
     * @param name
     * @param description
     */

    public GameEntity(final String name, final String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }
}
