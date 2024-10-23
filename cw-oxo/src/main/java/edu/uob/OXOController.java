package edu.uob;

import static edu.uob.OXOMoveException.RowOrColumn.COLUMN;
import static edu.uob.OXOMoveException.RowOrColumn.ROW;
import static java.lang.Integer.MAX_VALUE;

public class OXOController {
    OXOModel gameModel;
    private static int isLowerCase = 97;
    private static int isUpperCase = 65;
    private static int isNumber = 49;
    private static int instructionCount = 0;
    private static int maxRowsColumns = 9;

    public OXOController(OXOModel model) {
        gameModel = model;
        instructionCount = 0;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException{
        checkCommand(command);
        char rowInput = command.charAt(0);
        int row = convertRowInput(rowInput);
        char colInput = command.charAt(1);
        int col = convertColInput(colInput);
        checkNotTaken(row, col);
        instructionCount++;
        if (gameModel.getWinner() == null){
            settingCell(row, col);
            incrementPlayer();
            winDetection();
        }
    }

    private void checkCommand(String command)throws OXOMoveException {
        commandLengthCheck(command);
        commandColumnCheck(command);
        commandRowCheck(command);
    }

    private void commandLengthCheck(String command) throws OXOMoveException{
        if (command.length() != 2) {
            throw new OXOMoveException.InvalidIdentifierLengthException(command.length());
        }
    }

    private void commandColumnCheck(String command) throws OXOMoveException{
        int n = 0;
        if (command.charAt(1) >= '0' && command.charAt(1) <= '9') {
            n = command.charAt(1) - isNumber;
        } else{
            throw new OXOMoveException.InvalidIdentifierCharacterException(COLUMN, command.charAt(1));
        }
        if (n >= gameModel.getNumberOfColumns() || n < 0) {
            throw new OXOMoveException.OutsideCellRangeException(COLUMN, n);
        }
    }

    private void commandRowCheck(String command) throws OXOMoveException{
        int n = 0;
        if (isUpperLatinLetter(command.charAt(0))) {
            n = command.charAt(0) - isUpperCase;
        } else if (isLowerLatinLetter(command.charAt(0))) {
            n = command.charAt(0) - isLowerCase;
        } else{
            throw new OXOMoveException.InvalidIdentifierCharacterException(ROW, command.charAt(0));
        }
        if (n >= gameModel.getNumberOfRows()) {
            throw new OXOMoveException.OutsideCellRangeException(ROW, n);
        }
    }

    private void checkNotTaken(int row, int col) throws OXOMoveException {
        if (gameModel.getCellOwner(row, col) != null) {
            throw new OXOMoveException.CellAlreadyTakenException((row + 1), (col + 1));
        }
    }

    private boolean isUpperLatinLetter(char letter) {
        if (letter >= 'A' && letter <= 'Z') {
            return true;
        }
        return false;
    }

    private boolean isLowerLatinLetter(char letter){
        if (letter >= 'a' && letter <= 'z') {
            return true;
        }
        return false;
    }


    private int convertRowInput(char rowInput){
        int row = 0;
        if(isUpperLatinLetter(rowInput)){
            row = rowInput - isUpperCase;
            return row;
        } else if(isLowerLatinLetter(rowInput)){
            row = rowInput - isLowerCase;
            return row;
        }
        return row;
    }

    private int convertColInput(char colInput){
        int col = 0;
        if(colInput >= '0' && colInput <= '9'){
            col = colInput - isNumber;
        }
        return col;
    }

    private void settingCell(int row, int col){
        int currentPlayer = gameModel.getCurrentPlayerNumber();
        OXOPlayer player = gameModel.getPlayerByNumber(currentPlayer);
        gameModel.setCellOwner(row, col, player);
    }

    private void incrementPlayer(){
        if(gameModel.getCurrentPlayerNumber() == gameModel.getNumberOfPlayers() - 1){
            gameModel.setCurrentPlayerNumber(0);
        } else{
            gameModel.setCurrentPlayerNumber(gameModel.getCurrentPlayerNumber() + 1);
        }
    }

    public void winDetection(){
        OXOPlayer winner = gameModel.rowColWinChecker();
        if(winner != null){
            gameModel.setWinner(winner);
            return;
        }
        winner = gameModel.diagWinChecker();
        if(winner != null){
            gameModel.setWinner(winner);
        } else if(gameModel.isGridFull()){
            gameModel.setGameDrawn();
        }
    }

    public void addRow() {
        if(gameModel.getNumberOfRows() < maxRowsColumns){
            gameModel.addRow();
            gameModel.resetDrawn();
        }
    }

    public void removeRow() {
        if(!gameModel.isItOkToRemoveRow()){
            return;
        }
        if(gameModel.getWinner() == null) {
            gameModel.removeRow();
        } else if(gameModel.getWinner() != null){
            if(!gameModel.isRowFilled(gameModel.getNumberOfRows() - 1)){
                gameModel.removeRow();
            }
        }
    }

    public void addColumn() {
        if(gameModel.getNumberOfColumns() < maxRowsColumns){
            gameModel.addColumn();
            gameModel.resetDrawn();
        }
    }

    public void removeColumn() {
        if (!gameModel.isItOkToRemoveColumn()){
            return;
        }
        if(gameModel.getWinner() == null) {
            gameModel.removeColumn();
        } else if(gameModel.getWinner() != null){
            if(!gameModel.isColumnFilled(gameModel.getNumberOfColumns() - 1)){
                gameModel.removeColumn();
            }
        }
    }

    public void increaseWinThreshold() {
        if(gameModel.getWinThreshold() + 1 < MAX_VALUE)
        gameModel.setWinThreshold(gameModel.getWinThreshold() + 1);
    }

    public void decreaseWinThreshold() {
        if(gameModel.getWinThreshold() > 3 && instructionCount < 1){
            gameModel.setWinThreshold(gameModel.getWinThreshold() - 1);
        } else if(gameModel.getWinThreshold() > 3 && gameModel.getWinner() != null){
            gameModel.setWinThreshold(gameModel.getWinThreshold() - 1);
        }
    }

    public void reset() {
        int row, col;
        for(row = 0; row < gameModel.getNumberOfRows(); row++){
            for(col = 0; col < gameModel.getNumberOfColumns(); col++){
                gameModel.setCellOwner(row, col, null);
            }
        }
        gameModel.setWinner(null);
        gameModel.resetDrawn();
        gameModel.setCurrentPlayerNumber(0);
        instructionCount = 0;
    }
}
