package edu.uob;

/**
 *  Child class of GameAction, has further child classes for each different action
 *  (inv and inventory are one class)
 */
abstract public class GameBuiltInActions extends GameAction{
    /**
     * constructor for the class
     * as this is built in (each with their own class)
     * only the name attribute relevant here, see child classes
     * @param name
     */
    public GameBuiltInActions(final String name){
        super(name);
    }
    @Override
    public abstract String actionOutput(GameState state) throws GameExceptions;
}
