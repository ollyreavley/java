package edu.uob;

/**
 *     Parent class for the actions.  The output method is
 *     overriden by all child classes.
 *     Child classes are GameAdditActions and GameBuiltInActions
 */

public class GameAction
{
    /**
     * Name for each action, this is set by the action parser
     * and not change hence final
     */
    private final String name;

    /**
     * Constructor used by both specific game actions and
     * built in actions as this is one of the few points they have in common
     * @param newAction
     */
    public GameAction(final String newAction){
        name = newAction;
    }
    public String getName(){
        return name;
    }

    /**
     * Method to complete the action, overriden by all child classes
     */
    public String actionOutput(final GameState state) throws GameExceptions {
        return "";
    }
}
