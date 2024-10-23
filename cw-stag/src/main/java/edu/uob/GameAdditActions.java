package edu.uob;

import java.util.*;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Specialised class for game specific actions
 * Eact keyword from the XML has at least one GameAdditAction
 * object associated with it
 */

public class GameAdditActions extends GameAction{
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
     * Eaxch action has a narration if successfully completed
     */
    private String narration;
    /**
     * Each action has a list of associated triggers (eg
     * unlock and open)
     */
    private List<String> associatedTriggers;
    /**
     * a reference to the player completing the action
     */
    private GamePlayer player;
    /**
     * reference to the current location the action is bein
     * completed in
     */
    private GameLocation location;
    /**
     * this is for extracting the subjects from the command
     * to compare to the required subject list
     */
    private final List<String> listedObjects;
    /**
     * reference to current state of the game allowing access to
     * other classes
     */
    private GameState currentState;

    /**
     * constructor for the class, note the element passed has
     * all of the relevant info extracted from the addItems method
     * @param name
     * @param action
     */

    public GameAdditActions(final String name, final Element action){
        super(name);
        subjects = new ArrayList<>();
        consumed = new ArrayList<>();
        produced = new ArrayList<>();
        associatedTriggers = new ArrayList<>();
        listedObjects = new ArrayList<>();
        addItems(action.getElementsByTagName("subjects"), subjects);
        addItems(action.getElementsByTagName("consumed"), consumed);
        addItems(action.getElementsByTagName("produced"), produced);
        addNarration(action.getElementsByTagName("narration"));
    }

    private void addItems(final NodeList listOfSubjects, final List<String> toAddTo){
        final Element subjectsTest = (Element)listOfSubjects.item(0);
        final NodeList entities = subjectsTest.getElementsByTagName("entity");
        for(int i = 0; i < entities.getLength(); i++){
            final String subjectEntity = entities.item(i).getTextContent().trim();
            toAddTo.add(subjectEntity);
        }
        Collections.sort(toAddTo);
    }

    private void addNarration(final NodeList listOfNarration){
        narration = listOfNarration.item(0).getTextContent();
    }

    /**
     * this is for adding the associated trigger words
     * @param associated
     */
    public void addAssociated(final List<String> associated){
        associatedTriggers = associated;
    }

    public List<String> getSubjects(){
        return subjects;
    }

    public List<String> getConsumed(){
        return consumed;
    }

    public List<String> getProduced(){
        return produced;
    }

    public String getNarration(){
        return narration;
    }

    public List<String> getListedObjects(){
        return listedObjects;
    }

    /**
     * returns the associated trigger words
     * to this action
     * @return
     */
    public List<String> getAssociated(){
        return associatedTriggers;
    }

    @Override
    public String actionOutput(final GameState state) throws GameExceptions {
        currentState = state;
        /**
         * the command words entered by the user and passed
         * to the object
         */
        List<String> words = state.getCommands();
        player = state.getCurrentPlayer();
        location = player.getLocation();
        int i = 0;
        for(final String word : words){
            if(subjects.contains(word)){
                i++;
                listedObjects.add(word);
            }
        }
        if(i == subjects.size() && checkIfObjectsPresent()){
            new GameAdditActionsComplete(currentState, this);
            return getNarration();
        } else if(i >= 1 && checkIfHeldOrPresent()){
            new GameAdditActionsComplete(currentState, this);
            return getNarration();
        } else if(i == 0 && subjectIsLocation()){
            new GameAdditActionsComplete(currentState, this);
            return getNarration();
        }
        return "Not all the subjects of the action are present, please go find them first.";
    }

    private boolean checkIfObjectsPresent(){
        final Map<String, GameArtifact> heldItems = player.getHeldItems();
        final Map<String, GameEntity> entitiesInPlace = location.getEntities();
        int i = 0;
        for(final String listedObject : listedObjects){
            if(entitiesInPlace.containsKey(listedObject)){
                i++;
            } else if(heldItems.containsKey(listedObject)){
                i++;
            }
        }
        return i == listedObjects.size();
    }



    private boolean checkIfHeldOrPresent(){
        int i = 0;
        for(final String subject : subjects){
            if(player.getHeldItems().containsKey(subject)){
                i++;
                listedObjects.add(subject);
            } else if(player.getLocation().getEntities().containsKey(subject)){
                i++;
                listedObjects.add(subject);
            }
        }
        return i == subjects.size();
    }

    /**
     * tests if the subject of an action is also its location
     * @return
     */
    public boolean subjectIsLocation(){
        final GameMap map = currentState.getMap();
        final Map<String, GameLocation> locations = map.getLocations();
        for(final String subject : subjects) {
            if (locations.containsKey(subject) && player.getLocation().getName().equalsIgnoreCase(subject)){
                return true;
            }
        }
        return false;
    }
}
