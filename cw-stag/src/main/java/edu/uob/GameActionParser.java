package edu.uob;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

import static edu.uob.BuiltInCommands.*;

/**
 * This takes the output from the XML parser and
 * extracts the necesary information into the GameAdditAction objects
 * There is a different method of creating the built in actions below which
 * runs every time the pprogram is run regardless
 */

public class GameActionParser {
    /**
     * This is the output from the actions parser hence final as not changed
     */
    private final NodeList actions;
    /**
     * This is a list of trigger words and their associated trigger words
     * As each action can have multiple trigger words a list of the
     * associated triggers must be kept to allow for commands to be completed
     * where the second trigger word is for the same action as the first
     */
    private final Map<String, List<String>> triggerWords;
    /**
     * GameMpves is the class for holding all of the possible moves of the game
     */
    private final GameMoves possibleMoves;

    /**
     * As should be clear below, this takes a server reference as input
     * and extracts from the server the actions file, passes it to the parser which
     * retrurns the nodelist of items.
     */
    public GameActionParser(final GameServer server) throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final Document document = builder.parse(server.getActionsFile());
        final Element root = document.getDocumentElement();
        actions = root.getChildNodes();
        possibleMoves = new GameMoves();
        triggerWords = new HashMap<>();
    }

    /**
     * ensures only the odd entries are chosen from the parser output
     */
    public void makeListOfActions(){
        int i;
        for(i = 0; i < actions.getLength(); i++){
            if(i % 2 != 0){
                final Element newAction = (Element)actions.item(i);
                forTriggers(newAction);
            }
        }
        possibleMoves.addTriggerWords(triggerWords);
    }

    /**
     * extracts each keyword and creates a new key in the hashmap
     * trigger words is the list of all keywords which trigger an action,
     * in the hashmap below the word like "open" is the other
     * associated words such as "unlock"
     * the other associated words are also stored in the game action class itself
     */
    private void forTriggers(final Element newAction){
        GameAdditActions newMove;
        final Element triggers = (Element)newAction.getElementsByTagName("triggers").item(0);
        final NodeList keywords = triggers.getElementsByTagName("keyphrase");

         for(int i = 0; i < keywords.getLength(); i++){
            final List<String> associated = new ArrayList<>();
            for(int n = 0; n < keywords.getLength(); n++){
                associated.add(keywords.item(n).getTextContent().trim());
            }
            newMove = new GameAdditActions(keywords.item(i).getTextContent().trim(), newAction);
            possibleMoves.addMove(newMove);
            triggerWords.put(keywords.item(i).getTextContent(), associated);
            newMove.addAssociated(associated);
        }
    }

    /**
     * run everytime the program starts, this creates all of the object classes for
     *  the built in commands
     */
    public void builtInActions(){
        GameBuiltInActions newAction = new GameInv(INVENTORY.getAction());
        possibleMoves.addMove(newAction);
        addToTriggers(INVENTORY.getAction());
        newAction = new GameInv(INV.getAction());
        possibleMoves.addMove(newAction);
        addToTriggers(INV.getAction());
        newAction = new GameLook(LOOK.getAction());
        possibleMoves.addMove(newAction);
        addToTriggers(LOOK.getAction());
        newAction = new GameGet(GET.getAction());
        possibleMoves.addMove(newAction);
        addToTriggers(GET.getAction());
        newAction = new GameDrop(DROP.getAction());
        possibleMoves.addMove(newAction);
        addToTriggers(DROP.getAction());
        newAction = new GameGoto(GOTO.getAction());
        possibleMoves.addMove(newAction);
        addToTriggers(GOTO.getAction());

    }

    private void addToTriggers(final String builtInTrigger){
        final List<String> trigger = new ArrayList<>(Collections.singleton(builtInTrigger));
        //special case to add inv and inventory to each others list of trigger words.
        if("inv".equalsIgnoreCase(builtInTrigger)){
            trigger.add("inventory");
        }
        if("inventory".equalsIgnoreCase(builtInTrigger)){
            trigger.add("inv");
        }
        triggerWords.put(builtInTrigger, trigger);
    }

    /**
     * To extract the GameMoves object from the class
     */
    public GameMoves returnPossibleMoves(){
        return possibleMoves;
    }
}
