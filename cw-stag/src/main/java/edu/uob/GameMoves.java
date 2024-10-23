package edu.uob;

import java.util.*;

/**
 * class to hold all possible moves for the game
 */
public class GameMoves {
    /**
     * holds a map of possible moves for the game
     */
    private final Map<String, Set<GameAction>> possibleMoves;
    /**
     * holds a map of all triggers words and their associated trigger words
     * i.e. unlock would hold open
     */
    private Map<String, List<String>> triggerWords;

    /**
     * constructor only creates a new hashmap
     */
    public GameMoves(){
        possibleMoves = new HashMap<>();
    }

    /**
     * used to add a new action, this is called for each trigger word in the
     * actions parser
     * @param newAction
     */
    public void addMove(final GameAction newAction){
        if(possibleMoves.get(newAction.getName()) == null) {
            final Set<GameAction> listOfActions = new HashSet<>();
            listOfActions.add(newAction);
            possibleMoves.put(newAction.getName(), listOfActions);
        } else{
            final Set<GameAction> listOfActions = possibleMoves.get(newAction.getName());
            listOfActions.add(newAction);
            possibleMoves.put(newAction.getName(), listOfActions);
        }
    }

    /**
     * returns a map of the possible moves in the game
     * @return
     */
    public Map<String, Set<GameAction>> returnPossibleMoves(){
        return possibleMoves;
    }

    /**
     * returns a specific move in the game
     * @param key
     * @return
     */

    public GameAction returnAction(final String key){
        final Set<GameAction> actions = possibleMoves.get(key);
        GameAdditActions toReturn = null;
        for (final GameAction action : actions) {
            toReturn = (GameAdditActions)action;
        }
        return toReturn;
    }

    /**
     * returns a specific basic move in the game
     * @param key
     * @param state
     * @return
     * @throws GameExceptions
     */

    public String returnBasicAction(final String key, final  GameState state) throws GameExceptions {
        final Set<GameAction> move = possibleMoves.get(key);
        final List<GameAction> moves = new ArrayList<>(move);
        final GameAction action = moves.get(0);
        return action.actionOutput(state);
    }

    /**
     * the trigger word map is assembled in the game actions parser
     * and then handed to the Gamemoves object
     * @param triggers
     */

    public void addTriggerWords(final Map<String, List<String>> triggers){
        triggerWords = triggers;
    }

    /**
     * returns a map of the trigger words
     * @return
     */

    public Map<String, List<String>> getTriggerWords(){
        return triggerWords;
    }
}
