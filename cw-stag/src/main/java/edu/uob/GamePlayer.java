package edu.uob;

import java.util.*;

/**
 * special class for the players of the game,
 * extends game character
 */
public class GamePlayer extends GameCharacter{
    /**
     * map of all artifacts held by the player
     */
    private final Map<String, GameArtifact> artefactsHeld;
    /**
     * health of the player
     */
    private int health;
    /**
     * position of the player
     */
    private GameLocation position;
    /**
     * start position of the player
     */
    private GameLocation startPosition;
    /**
     * name of the player
     */
    private String name = "";
    /**
     * description of the player,
     * not necessairly used
     */
    private String description = "";

    /**
     * constructor, note
     * name and description are set later
     */
    public GamePlayer(){
        super("", "Another player");
        artefactsHeld = new HashMap<>();
        health = 3;
    }

    @Override
    public String getDescription(){
        return description;
    }

    public void setName(final String newName){
        name = newName;
        description = "another player called " + name;
    }

    @Override
    public String getName(){
        return name;
    }

    /**
     * to set the current locaron ofd the player,
     * note takes a reference to the class
     *  not just a name
     * @param place
     */
    public void setLocation(final GameLocation place){
        position = place;
    }

    /**
     * to get the current location of the playerm, note
     * returns a reference not just the name
     * @return
     */

    public GameLocation getLocation(){
        return position;
    }

    /**
     * for adding a singular item to the inventory
     * @param newItem
     */
    public void addToHeldItems(final GameArtifact newItem){
        artefactsHeld.put(newItem.getName(), newItem);
    }

    /**
     * for dropping a singluar item from the inventory
     * @param oldItem
     * @return
     */

    public GameArtifact removeItem(final GameArtifact oldItem){
        artefactsHeld.remove(oldItem.getName());
        return oldItem;
    }

    /**
     * to get a map of all held items for inventory class
     * @return
     */

    public Map<String, GameArtifact> getHeldItems(){
        return artefactsHeld;
    }

    /**Lose life method for the player, this includes
     * a drop items for each for if lives are 0
     */
    public void loseHealth(){
        health = health - 1;
        if(health == 0){
            health = 3;
            artefactsHeld.values().forEach((value) -> {
                position.addDroppedItem(value);
            });
            artefactsHeld.clear();
            position = startPosition;
            position.addCharacter(this);
        }
    }

    /**
     * to set the start position, this does not chanfge in the gAME
     * to keep a track of where the player should return to
     * @param start
     */
    public void setStart(final GameLocation start){
        startPosition = start;
    }

    /**
     * to add to the health of the player
     */
    public void addHealth(){
        health = health + 1;
    }

    public int getHealth(){
        return health;
    }
}
