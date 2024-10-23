package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This holds the current state of the game, it is a collection
 * of objects which represent all items and moves in the game
 */
public class GameState {
    /**
     * ref to the map of locations in the game
     */
    private GameMap map;
    /**
     * ref to the possible moves in the game
     */
    private GameMoves moves;
    /**
     * map of players in the game
     */
    private final Map<String, GamePlayer> players;
    /**
     * current player (the one who entered the command)
     */
    private GamePlayer currentPlayer;
    /**
     * list of commands entered by the user
     */
    private final List<String> commands;

    /**
     * constructor has little in it as it is made as soon as program starts
     * before any information has been entered or parsing complete
     */
    public GameState(){
        players = new HashMap<>();
        commands = new ArrayList<>();
    }

    /**
     * accepts the map of locations and objects from the server class
     * @param gameMap
     */
    public void addMap(final GameMap gameMap){
        map = gameMap;
    }

    /**
     * returns the map of locations
     * @return
     */
    public GameMap getMap(){
        return map;
    }

    /**
     * adds the list of moves from the server
     * @param gameMoves
     */
    public void addMoves(final GameMoves gameMoves){
        moves = gameMoves;
    }

    /**
     * returns the list of moves
     * @return
     */
    public GameMoves getMoves(){
        return moves;
    }

    /**
     * adds a player to the list of players in the game
     * called at start and when a new player joins
     * @param gamePlayer
     */
    public void addPlayer(final GamePlayer gamePlayer){
        players.put(gamePlayer.getName(), gamePlayer);
    }

    public Map<String, GamePlayer> getPlayers(){
        return players;
    }

    public void setCurrentPlayer(final GamePlayer current){
        currentPlayer = current;
    }

    public GamePlayer getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * clears the old command and adds the new one to the list
     * @param newCommands
     */
    public void setNewCommand(final List<String> newCommands){
        commands.clear();
        commands.addAll(newCommands);
    }
    public List<String> getCommands(){
        return commands;
    }
}
