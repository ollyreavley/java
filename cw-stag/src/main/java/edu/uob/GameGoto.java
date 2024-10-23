package edu.uob;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * specialied class for moving the player to a new location
 */
public class GameGoto extends GameBuiltInActions{
    /**
     * holds list of commands entered by user
     */
    private List<String> destination;
    /**
     * holds ref to current player
     */
    private GamePlayer player;
    /**
     * holds list of possible destinations entered by user
     */
    private Map<String, GameLocation> possibleDestinations;
    /**
     * holds ref to destination to move user to
     */
    private GameLocation goTo;
    /**
     * holds list of all destinations in the game
     */
    private Map<String, GameLocation> locations;
    /**
     * used to check only one destination enterted
     */
    private List<String> toCheckUnique;
    /**
     * used to test if a condition has been succesfull
     */
    private boolean failed;

    /**
     * constructor, similar to other built ins only name
     * is needed
     */
    public GameGoto(final String name){
        super(name);
    }
    @Override
    public String actionOutput(final GameState state) throws GameExceptions {
        player = state.getCurrentPlayer();
        destination = state.getCommands();
        final GameLocation location = player.getLocation();
        toCheckUnique = new ArrayList<>();
        locations = state.getMap().getLocations();
        possibleDestinations = location.getPaths();
        checkDestinationAndOrder();
        final int allowedGotos = 1;
        if(toCheckUnique.size() == allowedGotos){
            goTo.addCharacter((GameCharacter) player.getLocation().removeEntity(player.getName()));
            player.setLocation(goTo);
        } else if(toCheckUnique.size() > allowedGotos){
            throw new GameExceptions.InvalidBuiltInCommand("Silly " + player.getName() + " you can only specify one location to go to!");
        } else {
            throw new GameExceptions.InvalidBuiltInCommand("Silly " + player.getName() + " you have entered an invalid destination!");
        }
        return player.getName() + " went to " + player.getLocation().getName();
    }

    private void checkDestinationAndOrder() throws GameExceptions {
        final AtomicBoolean commandFirst = new AtomicBoolean(false);
        possibleDestinations.forEach((key, value) -> {
            for(final String destinations : destination) {
                if ("goto".equalsIgnoreCase(destinations)){
                    commandFirst.set(true);
                }
                if(commandFirst.get() && key.equalsIgnoreCase(destinations)){
                    goTo = value;
                    toCheckUnique.add(destinations);
                    failed = false;
                } else if(!commandFirst.get() && locations.containsKey(destinations)){
                    failed = true;
                }
            }
        });
        if(failed){
            throw new GameExceptions.InvalidBuiltInCommand("Silly " + player.getName() + ", you have entered an invalid command!");
        }
    }
}
