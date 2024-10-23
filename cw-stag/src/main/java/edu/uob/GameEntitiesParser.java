package edu.uob;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to extract all the information from the output of the .DOT parser
 */

public class GameEntitiesParser
{
    /**
     * Data structure for the output of the parser
     */
    private final List<Graph> sections;
    /**
     * GameMap is the class which holds all of the locations,
     * and everything in each location in the game.
     */
    private final GameMap mapOfLocations;

    /**
     * Constructor class to call the .DOT parser and extract the
     * output in the methods below
     * @param server
     * @throws FileNotFoundException
     * @throws ParseException
     */

    public GameEntitiesParser(final GameServer server) throws FileNotFoundException, ParseException {
        final Parser parser = new Parser();
        final FileReader reader = new FileReader(server.getEntitiesFile());
        parser.parse(reader);
        final Graph wholeDocument = parser.getGraphs().get(0);
        sections = wholeDocument.getSubgraphs();
        mapOfLocations = new GameMap();
    }

    /**
     * Selecting just the locations not the paths
     * Then tterating over the locations and adding to the map
     * sets the special start position case
     */

    public void setMapOfLocations(){
        int i = 0;
        final List<Graph> listOfLocations = sections.get(0).getSubgraphs();
        for(final Graph locations : listOfLocations){
            final String name = locations.getNodes(false).get(0).getId().getId();
            final String description = locations.getNodes(false).get(0).getAttribute("description");
            final List<Graph> locationObjects = locations.getSubgraphs();
            final GameLocation newLocation = new GameLocation(name, description, locationObjects);
            if(i == 0){
                setStartPosition(newLocation);
            }
            mapOfLocations.addLocation(newLocation);
            i++;
        }
        setPaths();
        mapOfLocations.setStart();
    }

    /**
     * for setting the special case of the start position of the game
     * @param start
     */
    public void setStartPosition(final GameLocation start){
        start.setStart();
    }

    /**
     * Cycles through each start position and adds a reference to the object
     * of the destination I.e. cabin contains a path with a reference to
     * the forest in it
     */
    private void setPaths(){
        final List<Edge> paths = sections.get(1).getEdges();
        for(final Edge path : paths){
            final String source = path.getSource().getNode().getId().getId();
            final GameLocation place = mapOfLocations.getLocation(source);
            final String destination = path.getTarget().getNode().getId().getId();
            final GameLocation dest = mapOfLocations.getLocation(destination);
            place.setPaths(dest);
        }
    }

    public GameMap getMapOfLocations(){
        return mapOfLocations;
    }
}
