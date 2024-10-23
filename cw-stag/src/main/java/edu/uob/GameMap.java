package edu.uob;

import java.util.*;

/**
 * class to hold all of the locations in the game
 * including storeroom which cannot be travelled to
 */
public class GameMap {
    /**
     * map of all locations
     */
    private final Map<String, GameLocation> locations;
    /**
     * start location reference
     */
    private GameLocation start;

    /**
     * constructor, as this is added to
     * peicemeal no parameters passed in
     */
    public GameMap(){
        locations = new HashMap<>();
    }

    /**
     * this is called by entities parser to add  a new location
     */
    public void addLocation(final GameLocation newLocation){
        locations.put(newLocation.getName(), newLocation);
    }

    /**
     * used to return the list of all locations
     */
    public Map<String, GameLocation> getLocations(){
        return locations;
    }

    /**
     * used to return a specific location
     * @param requiredPlace
     * @return
     */

    public GameLocation getLocation(final String requiredPlace){
        return locations.get(requiredPlace);
    }

    /**
     * sets the start location by cycling through each and checking
     * if the value is the start andthen
     * sets the start in the class
     */
    public void setStart(){
        locations.forEach((key, value) -> {
            if(value.isStart()){
                start = value;
            }
        });
    }

    /**
     * returns a ref to the start
     * @return
     */
    public GameLocation getStart(){
        return start;
    }
}
