package edu.uob;

import java.util.*;

import static edu.uob.BuiltInCommands.*;

/**
 * Class for interpeting the commands entered by
 * the user
 */

public class GameInterpret {
    /**
     * list of commands from the user
     */
    private final List<String> command;
    /**
     * reference to moves possible in the game
     */
    private final GameMoves possibleMoves;
    /**
     * reference to current state of the game
     */
    private final GameState currentState;

    /**
     * constructor for the class, only need to pass
     * in the state as the state holds the list of commands
     * @param state
     */
    public GameInterpret(final GameState state){
        command = new ArrayList<>(state.getCommands());
        possibleMoves = state.getMoves();
        currentState = state;
    }

    /**
     * intially checks the command to the set of actions
     * if not null then proceeds to try and do the command
     */
    public String interpretCommands() throws GameExceptions {
        final Set<GameAction> basic = checkBuiltIns(command);
        return checkWhichBuiltIn(basic);
    }

    /**
     * special case for the built in commands,
     * cycles through the enum checking if the command
     * is equivalent to the string associated with
     * the enum and completing that command if so
     */

    private String checkWhichBuiltIn(final Set<GameAction> commands) throws GameExceptions {
        for(final GameAction command : commands) {
            if (LOOK.getAction().equalsIgnoreCase(command.getName())) {
                return possibleMoves.returnBasicAction("look", currentState);
            } else if (INV.getAction().equalsIgnoreCase(command.getName()) || INVENTORY.getAction().equalsIgnoreCase(command.getName())) {
                return possibleMoves.returnBasicAction("inv", currentState);
            } else if (GET.getAction().equalsIgnoreCase(command.getName())) {
                return possibleMoves.returnBasicAction("get", currentState);
            } else if (DROP.getAction().equalsIgnoreCase(command.getName())) {
                return possibleMoves.returnBasicAction("drop", currentState);
            } else if (GOTO.getAction().equalsIgnoreCase(command.getName())) {
                return possibleMoves.returnBasicAction("goto", currentState);
            } else{
                return specificCommandCalls(command, commands);
            }
        }
        return "";
    }

    private String specificCommandCalls(final GameAction command, final Set<GameAction> commands) throws GameExceptions {
        final int maxCommand = 1;
        if(commands.size() == maxCommand){
            final GameAdditActions specificCommand = (GameAdditActions)command;
            return specificCommand.actionOutput(currentState);
        } else {
            final Map<String, GameAction> specificCommand = findCorrectCommand(commands);
            final GameAdditActions match = (GameAdditActions) specificCommand.values().iterator().next();
            return match.actionOutput(currentState);
        }
    }

    private Map<String, GameAction> findCorrectCommand(final Set<GameAction> commands) throws GameExceptions {
        final Map<String, GameAction> match = new HashMap<>();
        for (final GameAction action : commands) {
            final GameAdditActions current = (GameAdditActions) action;
            for(final String word : command){
                if (current.getSubjects().contains(word)) {
                    match.putIfAbsent(current.getName(), current);
                }
            }
        }
        if(match.size() == 1){
            return match;
        } else{
            throw new GameExceptions.TooManyCommands("Multiple command possible in this location with this command");
        }
    }

    private Set<GameAction> checkBuiltIns(final List<String> commands) throws GameExceptions {
        int commandCount = 0;
        Set<GameAction> toDo = null;
        final List<String> possibleCommands = new ArrayList<>();
        final Map<String, Set<GameAction>> actions = possibleMoves.returnPossibleMoves();
        final Map<String, List<String>> commandWords = possibleMoves.getTriggerWords();
        final Set<String> keySet = actions.keySet();
        //compares the list of words entered in the command stream to the
        //trigger words stored in the program. Extracts the command words (if present)
        //and stores them for possible later testing if multiple command words found.
        for (final String s : commands) {
            for (final String key : keySet) {
                if (key.equalsIgnoreCase(s)) {
                    possibleCommands.add(s);
                    commandCount++;
                    toDo = actions.get(s);
                }
            }
        }
        int allowedCommands = 1;
        if(commandCount == allowedCommands){
            return toDo;
        } else if(commandCount > allowedCommands && multipleCommandCheck(possibleCommands, commandWords)){
            return toDo;
        }
        return new HashSet<>();
    }

    /**
     *checks to see if multiple commands have been entered
     * and if so if they are different commands
     * i.e. unlock and open are allowed in the same command.
     */
    private boolean multipleCommandCheck(final List<String> possibleCommands, final Map<String, List<String>> commandWords) throws GameExceptions{
        int i = 0;
        final List<String> testingCommands = commandWords.get(possibleCommands.get(0));
        for(final String possibleCommand : possibleCommands){
            if(testingCommands.contains(possibleCommand)){
                i++;
            }
        }
        if(i == possibleCommands.size()){
            return true;
        } else{
            throw new GameExceptions.TooManyCommands("Too many commands entered.");
        }
    }
}
