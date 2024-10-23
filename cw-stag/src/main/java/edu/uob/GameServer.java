package edu.uob;

import com.alexmerz.graphviz.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.*;

/** This class implements the STAG server. */
public final class GameServer {
    /**
     * holds the reference to the .DOT file
     */
    private File entitiesFile;
    /**
     * holds the reference to the XML file
     */
    private File actionsFile;
    /**
     * holds the reference to the current state of the game to
     * keep the state persistent between commands entered
     */
    private final GameState state;
    private static final char END_OF_TRANSMISSION = 4;
    public static void main(String[] args) throws IOException {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        GameServer server = new GameServer(entitiesFile, actionsFile);
        server.blockingListenOn(8888);
    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.GameServer(File, File)}) otherwise we won't be able to mark
    * your submission correctly.
    *
    * <p>You MUST use the supplied {@code entitiesFile} and {@code actionsFile}
    *
    * @param entitiesFile The game configuration file containing all game entities to use in your game
    * @param actionsFile The game configuration file containing all game actions to use in your game
    *
    */
    public GameServer(File entitiesFile, File actionsFile) {
        setEntitiesFile(entitiesFile);
        setActionsFile(actionsFile);
        state = new GameState();
        this.parseActions();
        this.parseEntities();
    }

    /**
     * calls the EntitiesParser class to extract the locations and entities from
     * the .DOT file and contruct the locations and entities classes
     */
    public void parseEntities(){
        try {
            final GameEntitiesParser mapParser = new GameEntitiesParser(this);
            mapParser.setMapOfLocations();
            state.addMap(mapParser.getMapOfLocations());
        } catch (FileNotFoundException | ParseException fnfe) {
            System.out.println(fnfe.getMessage());
        }
    }

    /**
     * calls the ActionsParser class to extract the possible actions from the
     * XML file and contrust the moves classes
     * Built ins are constructed at this point as well
     */

    public void parseActions(){
        try{
            final GameActionParser actionsParser = new GameActionParser(this);
            actionsParser.builtInActions();
            actionsParser.makeListOfActions();
            state.addMoves(actionsParser.returnPossibleMoves());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println(e.getMessage());
        }
    }

    public GameState getState(){
        return state;
    }

    /**
     * returns a map of all current players of the game
     * @return
     */
    public Map<String, GamePlayer> getPlayer(){
        return state.getPlayers();
    }

    public void setEntitiesFile(final File newEntitiesFile){
        entitiesFile = newEntitiesFile;
    }

    public File getEntitiesFile(){
        return entitiesFile;
    }

    public void setActionsFile(final File newActionsFile){
        actionsFile = newActionsFile;
    }

    public File getActionsFile(){
        return actionsFile;
    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.GameServer.handleCommand(String)}) otherwise we won't be
    * able to mark your submission correctly.
    *
    * <p>This method handles all incoming game commands and carries out the corresponding actions.
    */
    public String handleCommand(String command) {
        final String[] splitCommand = command.split(":");
        try {
            commandChecks(splitCommand);
        } catch (GameExceptions ex){
            return ex.getMessage();
        }
        final String playerName = splitCommand[0];
        checkSetPlayerName(playerName);
        final String commands = splitCommand[1];
        state.setNewCommand(splitCommands(commands));
        final GameInterpret interpreter = new GameInterpret(state);
        String message;
        try{
            message = interpreter.interpretCommands();
        } catch(GameExceptions ex){
            message = ex.getMessage();
        }
        return message;
    }

    private void commandChecks(final String[] splitCommand) throws GameExceptions {
        if(splitCommand.length <= 1){
            throw new GameExceptions.InvalidCommandStructure("It appears as through no name has been entered please enter in following format \"player name: command\"");
        }
        if(splitCommand[0].length() == 0){
            throw new GameExceptions.InvalidCommandStructure("It appears as through no name has been entered please enter in following format \"player name: command\"");
        }
    }

    private List<String> splitCommands(final String commands){
        final String lowerCommands = commands.toLowerCase();
        String trimmedCommands = lowerCommands.trim();
        while (trimmedCommands.contains("  ")) {
            trimmedCommands = trimmedCommands.replaceAll("  ", " " );
        }
        final String[] commandArray = trimmedCommands.split(" ");
        final List<String> commandArrayAsList = new ArrayList<>(Arrays.asList(commandArray));
        return arrayOfCommands(commandArrayAsList);
    }

    private List<String> arrayOfCommands(List<String> trimmedCommands){
        final Set<String> keySet = state.getMoves().getTriggerWords().keySet();
        for(final String key : keySet){
            if(key.contains(" ")){
                final String start = key.split(" ")[0];
                final String end = key.split(" ")[1];
                if(trimmedCommands.indexOf(start) + 1 == trimmedCommands.indexOf(end)){
                    trimmedCommands.set(trimmedCommands.indexOf(start), trimmedCommands.get(trimmedCommands.indexOf(start)) + " " + trimmedCommands.remove(trimmedCommands.indexOf(end)));
                }
            }
        }
        return trimmedCommands;
    }

    private void checkSetPlayerName(final String playerName){
        final Map<String, GamePlayer> players = state.getPlayers();
        GamePlayer newPlayer;
        if(players.isEmpty()){
            newPlayer = makeNewPlayer(playerName);
            state.addPlayer(newPlayer);
            state.setCurrentPlayer(newPlayer);
        } else {
            checkPlayers(players, playerName);
        }
    }

    private void checkPlayers(final Map<String, GamePlayer> players, final String playerName){
        GamePlayer newPlayer;
        if(!players.containsKey(playerName)){
            newPlayer = makeNewPlayer(playerName);
            state.addPlayer(newPlayer);
            state.setCurrentPlayer(newPlayer);
        } else{
            state.setCurrentPlayer(players.get(playerName));
        }
    }

    private GamePlayer makeNewPlayer(final String name){
        final GamePlayer player = new GamePlayer();
        player.setName(name);
        player.setLocation(state.getMap().getStart());
        player.setStart(player.getLocation());
        state.getMap().getStart().addCharacterForStart(player);
        return player;
    }

    //  === Methods below are there to facilitate server related operations. ===

    /**
    * Starts a *blocking* socket server listening for new connections. This method blocks until the
    * current thread is interrupted.
    *
    * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
    * you want to.
    *
    * @param portNumber The port to listen on.
    * @throws IOException If any IO related operation fails.
    */
    public void blockingListenOn(int portNumber) throws IOException {
        try (ServerSocket s = new ServerSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);
            while (!Thread.interrupted()) {
                try {
                    blockingHandleConnection(s);
                } catch (IOException e) {
                    System.out.println("Connection closed");
                }
            }
        }
    }

    /**
    * Handles an incoming connection from the socket server.
    *
    * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
    * * you want to.
    *
    * @param serverSocket The client socket to read/write from.
    * @throws IOException If any IO related operation fails.
    */
    private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
        try (Socket s = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {
            System.out.println("Connection established");
            String incomingCommand = reader.readLine();
            if(incomingCommand != null) {
                System.out.println("Received message from " + incomingCommand);
                String result = handleCommand(incomingCommand);
                writer.write(result);
                writer.write("\n" + END_OF_TRANSMISSION + "\n");
                writer.flush();
            }
        }
    }
}
