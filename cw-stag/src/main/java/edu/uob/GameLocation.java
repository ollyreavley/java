package edu.uob;

import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for each location in the game, each location
 * is an object
 */

public class GameLocation extends GameEntity{
    /**
     * list of destinations from the location
     */
    private final Map<String, GameLocation> destination;
    /**
     * all items (including characters and players)
     * in a location are stored as GameEntity class
     * this allows simpler access to the items, with
     * testing for instance of completed instead
     */
    private final Map<String, GameEntity> locationEntities;
    /**
     * holds whether this is the start location or not
     */
    private boolean startLocation;

    /**
     * constructor for the object.  Note the entities in the
     * location are passed in and extracted from the parser output in this
     * class not in the entities parser
     */
    public GameLocation(final String name, final String description, final List<Graph> locationDetails) {
        super(name, description);
        locationEntities = new HashMap<>();
        destination = new HashMap<>();
        for(final Graph location : locationDetails){
            if("artefacts".equals(location.getId().getId())){
                addArtifacts(location.getNodes(false));
            } else if("furniture".equals(location.getId().getId())){
                addFurniture(location.getNodes(false));
            } else {
                addCharacter(location.getNodes(false));
            }
        }
    }

    private void addArtifacts(final List<Node> objects){
        for(final Node object : objects) {
            GameArtifact newObject;
            newObject = new GameArtifact(object.getId().getId(), object.getAttribute("description"));
            locationEntities.put(newObject.getName(), newObject);
        }
    }

    /**
     * for adding an item dropped by the user in the game or on death
     */
    public void addDroppedItem(final GameEntity droppedItem){
        locationEntities.put(droppedItem.getName(), droppedItem);
    }

    private void addFurniture(final List<Node> objects){
        for(final Node object : objects) {
            GameFurniture newObject;
            newObject = new GameFurniture(object.getId().getId(), object.getAttribute("description"));
            locationEntities.put(newObject.getName(), newObject);
        }
    }

    /**
     * for adding the characters (not players) with a set location at the start
     * hence the private acces, only used by contructor
     * @param objects
     */
    private void addCharacter(final List<Node> objects){
        for(final Node object : objects) {
            GameCharacter newObject;
            newObject = new GameCharacter(object.getId().getId(), object.getAttribute("description"));
            locationEntities.put(newObject.getName(), newObject);
        }
    }

    /**
     * to add a character to a location after a goto command
     */
    public void addCharacter(final GameCharacter removedItem){
        locationEntities.put(removedItem.getName(), removedItem);
    }

    /**
     * used to add a new player to the location
     */
    public void addCharacterForStart(final GamePlayer newPlayer){
        locationEntities.put(newPlayer.getName(), newPlayer);
    }

    /**
     * used to set the paths for the location, as these can be
     * created this is public and used by some action commands
     */
    public void setPaths(final GameLocation availableDestination){
        destination.put(availableDestination.getName(), availableDestination);
    }

    /**
     * returns a map of entities in the location (artifacts,
     * furniture etc.)
     */
    public Map<String, GameEntity> getEntities(){
        return locationEntities;
    }

    /**
     * returns a list of paths from the location
     */
    public Map<String, GameLocation> getPaths(){
        return destination;
    }

    /**
     * to remove a path from a location, this is a speculative entry
     * on the assumption that a path could be consumed
     */

    public void removePath(final GameLocation toRemove){
        destination.remove(toRemove.getName());
    }

    /**
     * used to set the start location of the game
     */
    public void setStart(){
        startLocation = true;
    }

    /**
     * used to test if a location is the start position
     * @return
     */
    public boolean isStart(){
        return startLocation;
    }

    /**
     * used to remove items from the location (characters or artifacts or
     * furniture etc.)
     */
    public GameEntity removeEntity(final String item){
        return locationEntities.remove(item);
    }

    /**
     * used to chekc if an entity is in the location without
     * returning  a list of them
     * @param item
     * @return
     */
    public boolean checkEntities(final String item){
        return locationEntities.containsKey(item);
    }

    /**
     * used to add a list of entities, mainly used after
     * death of a player
     * @param newEntities
     */
    public void addEntities(final Map<String, GameEntity> newEntities){
        locationEntities.putAll(newEntities);
    }

}
