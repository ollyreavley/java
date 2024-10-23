package edu.uob;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * specialied class for completing the inv or
 * inventory command by a user
 */

public class GameInv extends GameBuiltInActions{

    /**
     * as with all built ins only the name is relevant
     */
    public GameInv(final String name){
        super(name);
    }
    @Override
    public String actionOutput(final GameState state) throws GameExceptions {
        final GamePlayer player = state.getCurrentPlayer();
        if(!checkIfDiffCharacter(state)){
            throw new GameExceptions.InvalidBuiltInCommand("Silly " + player.getName() + " you can't do an inventory on a different player!");
        }
        final StringBuilder itemsHeld = new StringBuilder();
        final Map<String, GameArtifact> items = player.getHeldItems();
        if(!items.isEmpty()){
            items.keySet().forEach(itemsHeld::append);
        } else{
            throw new GameExceptions.InvalidBuiltInCommand("You do not hold anything in your inventory.");
        }
        return itemsHeld.toString();
    }

    private boolean checkIfDiffCharacter(final GameState state){
        final List<String> words = state.getCommands();
        final AtomicBoolean diffCharacter = new AtomicBoolean(true);
        final Map<String, GamePlayer> players = state.getPlayers();
        for(final String word : words){
            players.keySet().forEach((key) -> {
                if(word.equalsIgnoreCase(key) && !word.equalsIgnoreCase(state.getCurrentPlayer().getName())){
                    diffCharacter.set(false);
                }
            });
        }
        return diffCharacter.get();
    }

}
