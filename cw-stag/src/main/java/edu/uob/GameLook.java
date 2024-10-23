package edu.uob;

import java.util.List;
import java.util.Map;

/**
 * Spcialised class for the built in look command
 */

public class GameLook extends GameBuiltInActions{
/**
 * reference to the current player object
 */
    private GamePlayer currentPlayer;

    /**
     * constructor, note only the name is necesary for this
     */
    public GameLook(final String name){
        super(name);
    }
    @Override
    public String actionOutput(final GameState state) throws GameExceptions {
        currentPlayer = state.getCurrentPlayer();
        if(!checkRestOfCommand(state)){
            throw new GameExceptions.InvalidBuiltInCommand("Silly " + " you can't \"look\" in a different location!");
        }
        final GameLocation location = currentPlayer.getLocation();
        return ("You are in a " + location.getDescription()) + ". " + entitiesOutput(location) +
                pathsOutput(location);
    }

    private boolean checkRestOfCommand(final GameState state){
        final List<String> words = state.getCommands();
        final GameMap map = state.getMap();
        final Map<String, GameLocation> locations = map.getLocations();
        for(final String word : words){
            if(locations.containsKey(word)){
                final String currentLocation = state.getCurrentPlayer().getLocation().getName();
                if(!word.equalsIgnoreCase(currentLocation)){
                    return false;
                }
            }
        }
        return true;
    }

    private StringBuilder entitiesOutput(final GameLocation location){
        final StringBuilder description = new StringBuilder();
        description.append(" There is a ");
        final Map<String, GameEntity> artefacts = location.getEntities();
        artefacts.forEach((key, value) -> {
            if(!key.equalsIgnoreCase(currentPlayer.getName())) {
                description.append(value.getDescription());
                description.append(" and a ");
            }
        });
        return description;
    }

    private StringBuilder pathsOutput(final GameLocation location){
        final StringBuilder description = new StringBuilder();
        final String and = " and ";
        if(!location.getPaths().isEmpty()){
            description.append(" There is a path to the ");
            final Map<String, GameLocation> paths = location.getPaths();
            paths.keySet().forEach((key) -> {
                description.append(key);
                description.append(and);
            });
            description.replace(description.lastIndexOf(and), description.lastIndexOf(and) + 5, "");
            description.append(". ");
        } else {
            return description.append("There are no paths from this location");
        }
        return description;
    }
}
