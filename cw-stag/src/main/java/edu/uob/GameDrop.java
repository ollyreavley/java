package edu.uob;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * class for the built in action of dropping an item
 * from a character inventory
 */

public class GameDrop extends GameBuiltInActions{
    /**
     * List of words from the user command
     */
    private List<String> words;
    /**
     * ref. to player performing the command
     */
    private GamePlayer player;
    /**
     * to hold the list of objects held by the player
     */
    private Map<String, GameArtifact> heldItems;
    /**
     * to hold the list of objects which the player wants to drop
     */
    private List<String> artifactsToDrop;
    /**
     * To hold the OBJECT which the player can drop
     */
    private GameArtifact dropped;
    /**
     * to test whether the object can be dropped
     */
    private boolean droppable;
    /**
     * to test if the command comes first
     */
    private boolean commandFirst;

    /**
     * constructor, as these do not have a description
     * associated note the only parameter is the name
     * @param name
     */
    public GameDrop(final String name){
        super(name);
    }
    @Override
    public String actionOutput(final GameState state) throws GameExceptions {
        words = state.getCommands();
        player = state.getCurrentPlayer();
        heldItems = player.getHeldItems();
        final GameLocation location = player.getLocation();
        artifactsToDrop = new ArrayList<>();
        checkHeldItems();
        if(droppable && artifactsToDrop.size() == 1){
            location.addDroppedItem(player.removeItem(dropped));
            droppable = false;
            return player.getName() + " dropped the " + dropped.getName();
        } else{
            throw new GameExceptions.InvalidBuiltInCommand("Silly " + player.getName() + " you tried to drop an item you do not hold or you tried to drop too many items!");
        }
    }

    private void checkHeldItems() throws GameExceptions.InvalidBuiltInCommand {
        for (final String word : words) {
            if ("drop".equalsIgnoreCase(word)) {
                commandFirst = true;
            }
            if (heldItems.containsKey(word) && commandFirst) {
                artifactsToDrop.add(word);
                droppable = true;
                dropped = heldItems.get(word);
            } else if (heldItems.containsKey(word)) {
                throw new GameExceptions.InvalidBuiltInCommand("Silly " + player.getName() + " \"drop\" must come before the artifact being dropped.");
            }
        }
    }
}
