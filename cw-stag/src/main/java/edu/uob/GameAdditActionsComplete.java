package edu.uob;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * used by the GameAdditActions class to complete the action
 */
public class GameAdditActionsComplete {
    /**
     * subjects is thelist of objects required present
     * to complete the action
     */
    private final List<String> subjects;
    /**
     * consumed is the list of objects used up in the action
     */
    private final List<String> consumed;
    /**
     * produced is the list of objects which appear
     * following the action
     */
    private final List<String> produced;
    /**
     * the command words entered by the user and passed
     * to the object
     */
    private final List<String> words;
    /**
     * a reference to the player completing the action
     */
    private final GamePlayer player;
    /**
     * this is for extracting the subjects from the command
     * to compare to the required subject list
     */
    private final List<String> listedObjects;
    /**
     * reference to the current state of the game
     */
    private final GameState currentState;
    /**
     * this extracts the subjects if consumed to
     * remove from the locaton
     */
    private final List<String> toConsume;
    /**
     * Each action has a list of associated triggers (eg
     * unlock and open)
     */
    private final List<String> associatedTriggers;
    /**
     * reference to the action which called this
     * class object
     */
    private final GameAdditActions parentAction;

    /**
     * constructor for the complete action class.  Takes various
     * information from the GameAdditActions for use in this class
     * @param state
     * @param action
     * @throws GameExceptions
     */
    public GameAdditActionsComplete(final GameState state, final GameAdditActions action) throws GameExceptions {
        parentAction = action;
        subjects = action.getSubjects();
        consumed = action.getConsumed();
        produced = action.getProduced();
        currentState = state;
        words = state.getCommands();
        player = state.getCurrentPlayer();
        /**
         * reference to the current location the action is bein
         * completed in
         */
        GameLocation location = player.getLocation();
        listedObjects = action.getListedObjects();
        toConsume = new ArrayList<>();
        associatedTriggers = action.getAssociated();
        completeAction();
    }

    private void completeAction() throws GameExceptions {
        checkSubjectsOtherActions();
        for(final String listedObject : listedObjects){
            if(consumed.contains(listedObject)){
                toConsume.add(listedObject);
            }
        }
        produceItems();
        consumeItems();
    }

    private void produceItems(){
        final GameMap map = currentState.getMap();
        final Map<String, GameEntity> newlyProduced = new ConcurrentHashMap<>();
        final Map<String, GameLocation> locations = map.getLocations();
        for(final String producedThing : produced) {
            locations.forEach((key, value) -> {
                if (value.checkEntities(producedThing)) {
                    newlyProduced.put(producedThing, value.removeEntity(producedThing));
                } else if (map.getLocation(producedThing) != null && !player.getLocation().getPaths().containsKey(producedThing)) {
                    player.getLocation().setPaths(map.getLocation(producedThing));
                }
            });
            if("health".equalsIgnoreCase(producedThing)){
                currentState.getCurrentPlayer().addHealth();
            }
        }
        final GameLocation currentLocation = player.getLocation();
        currentLocation.addEntities(newlyProduced);
    }

    private void consumeItems(){
        final GameMap map = currentState.getMap();
        final Map<String, GameLocation> locations = map.getLocations();
        final Map<String, GameArtifact> playerItems = currentState.getCurrentPlayer().getHeldItems();
        for(final String consumedThing : consumed) {
            locations.forEach((key, value) -> {
                if(value.checkEntities(consumedThing)) {
                    map.getLocation("storeroom").addDroppedItem(value.removeEntity(consumedThing));
                } else if (map.getLocation(consumedThing) != null /*&& !player.getLocation().getPaths().containsKey(consumedThing)*/) {
                    player.getLocation().removePath(map.getLocation(consumedThing));
                }
            });
            if(playerItems.containsKey(consumedThing)){
                map.getLocation("storeroom").addDroppedItem(playerItems.remove(consumedThing));
            } else if("health".equalsIgnoreCase(consumedThing)){
                currentState.getCurrentPlayer().loseHealth();
            }
        }
    }

    private void checkSubjectsOtherActions() throws GameExceptions {
        final GameMoves moves = currentState.getMoves();
        final Map<String, Set<GameAction>> allMoves = moves.returnPossibleMoves();
        final AtomicBoolean differentSubject = new AtomicBoolean(false);
        allMoves.values().forEach((value) -> {
            value.forEach((item) -> {
                checkingType(item, differentSubject);
            });
        });
        if(differentSubject.get()){
            throw new GameExceptions.SubjectFromDiffCommand("Command appears to include a subject from a different action.");
        }
    }

    private void checkingType(final GameAction item, final AtomicBoolean differentSubject){
        if(item instanceof GameAdditActions && !item.equals(parentAction) && !associatedTriggers.contains(item.getName())){
            final List<String> otherSubjects = ((GameAdditActions) item).getSubjects();
            for(final String word : words){
                if(otherSubjects.contains(word) && !this.subjects.contains(word) && !parentAction.getName().equalsIgnoreCase(word)){
                    differentSubject.set(true);
                }
            }
        }
    }
}
