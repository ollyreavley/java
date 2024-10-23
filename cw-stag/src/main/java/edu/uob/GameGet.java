package edu.uob;

import java.util.ArrayList;
import java.util.List;

/**
 * class for the get built in action
 * performed by a player
 */

public class GameGet extends GameBuiltInActions{
    /**
     * list of artifacts in a location
     */
    private List<String> artifacts;
    /**
     * ;ist of command words entered by a user
     */
    private List<String> words;
    /**
     * reference to the current location of the player
     */
    private GameLocation location;
    /**
     * reference to the current player
     */
    private GamePlayer player;

    /**
     * constructor for the class note only the name is relevant
     */
    public GameGet(final String name){
        super(name);
    }
    @Override
    public String actionOutput(final GameState state) throws GameExceptions {
        artifacts = new ArrayList<>();
        words = state.getCommands();
        player = state.getCurrentPlayer();
        location = player.getLocation();
        checkGetArtifacts();
        final int allowedGets = 1;
        //tests is the artifacts in the list are too many (more than one) or too few.
        if(artifacts.size() > allowedGets){
            throw new GameExceptions.InvalidBuiltInCommand("Silly " + player.getName() + ", you cannot get multiple artifacts!");
        } else if(artifacts.isEmpty()){
            throw new GameExceptions.InvalidBuiltInCommand("Get command did not have an artefact to pick up or artifact does not exist in this location.");
        }
        else{
            //removes the item from the location and adds to the player held items
            player.addToHeldItems((GameArtifact) location.removeEntity(artifacts.get(0)));
            return player.getName() + " picked up " + artifacts.get(0);
        }
    }

    private void checkGetArtifacts() throws GameExceptions {
        boolean commandFirst = false;
        for(final String word : words){
            if("get".equalsIgnoreCase(word)){
                commandFirst = true;
            }
            if(location.getEntities().get(word) instanceof GameFurniture){
                throw new GameExceptions.InvalidBuiltInCommand("Silly " + player.getName() + ", you cannot carry the " + word + "!");
            } else if(location.getEntities().get(word) instanceof GameCharacter){
                throw new GameExceptions.InvalidBuiltInCommand("Silly " + player.getName() + ", you cannot carry the " + word + "!");
            } else if(location.checkEntities(word) && commandFirst){
                artifacts.add(word);
            }
        }
    }
}
