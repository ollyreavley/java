package edu.uob;

import java.util.ArrayList;

import static edu.uob.OXOMoveException.RowOrColumn.COLUMN;
import static edu.uob.OXOMoveException.RowOrColumn.ROW;

public class OXOModel {

    private ArrayList<ArrayList<OXOPlayer>> cells;
    private ArrayList<OXOPlayer> players;
    private int currentPlayerNumber;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;
    private static int maxRowsColumns = 9;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
        if(winThresh < 3){
            winThresh = 3;
        }
        if(numberOfRows < 0){
            numberOfRows = 1;
        }
        if(numberOfRows > 9){
            numberOfRows = 9;
        }
        if(numberOfColumns < 0){
            numberOfColumns = 1;
        }
        if(numberOfColumns > 9){
            numberOfColumns = 9;
        }
        winThreshold = winThresh;
        cells = new ArrayList<>(numberOfRows);
        for(int n = 0; n < numberOfRows; n++){
            ArrayList<OXOPlayer> row = new ArrayList<>();
            for(int i = 0; i < numberOfColumns; i++){
                row.add(null);
            }
            cells.add(row);
        }
        players = new ArrayList<>();
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public void addPlayer(OXOPlayer player) {
        players.add(player);
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players.get(number);
    }

    public OXOPlayer getWinner() {
        return winner;
    }

    public void setWinner(OXOPlayer player) {
        winner = player;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public void setCurrentPlayerNumber(int playerNumber) {
        currentPlayerNumber = playerNumber;
    }

    public int getNumberOfRows() {
        return cells.size();
    }

    public int getNumberOfColumns() {
        return cells.get(0).size();
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        return cells.get(rowNumber).get(colNumber);
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        cells.get(rowNumber).set(colNumber, player);
    }

    public void setWinThreshold(int winThresh) {
        winThreshold = winThresh;
    }

    public int getWinThreshold() {
        return winThreshold;
    }

    public void setGameDrawn() {
        gameDrawn = true;
    }

    public boolean isGameDrawn() {
        return gameDrawn;
    }

    public void addRow(){
        if(getNumberOfRows() < maxRowsColumns) {
            ArrayList<OXOPlayer> row = new ArrayList<>();
            for (int n = 0; n < getNumberOfColumns(); n++) {
                row.add(null);
            }
            cells.add(row);
        }
    }

    public void addColumn(){
        if(getNumberOfColumns() < maxRowsColumns){
            for (int n = 0; n < getNumberOfRows(); n++) {
                cells.get(n).add(null);
            }
        }
    }

    public void removeRow(){
        if(!isItOkToRemoveRow()){
            return;
        }
        if(getWinner() == null) {
            cells.remove(getNumberOfRows() - 1);
        } else if(getWinner() != null){
            if(!isRowFilled(getNumberOfRows() - 1)){
                cells.remove(getNumberOfRows() - 1);
            }
        }
    }

    public boolean isItOkToRemoveRow(){
        if(getNumberOfRows() <= 1){
            return false;
        } else if(isRowFilled(getNumberOfRows() - 1)){
            return false;
        } else if(isGridFullRowDown()){
            return false;
        }
        return true;
    }

    public boolean isGridFullRowDown(){
        int rowCount, columnCount = 0, fullCount = 0;
        for(rowCount = 0; rowCount < (getNumberOfRows() - 1); rowCount++){
            for(columnCount = 0; columnCount < getNumberOfColumns(); columnCount++){
                if(getCellOwner(rowCount, columnCount) != null){
                    fullCount++;
                }
            }
        }
        if(fullCount == (rowCount * columnCount)){
            return true;
        }
        return false;
    }

    public void removeColumn() {
        if (!isItOkToRemoveColumn()){
            return;
        }
        if (getWinner() == null) {
            for (int n = 0; n < getNumberOfRows(); n++) {
                cells.get(n).remove(getNumberOfColumns() - 1);
            }
        } else if (getWinner() != null) {
            if (!isColumnFilled(getNumberOfColumns() - 1)) {
                for (int n = 0; n < getNumberOfRows(); n++) {
                    cells.get(n).remove(getNumberOfColumns() - 1);
                }
            }
        }
    }

    public boolean isItOkToRemoveColumn(){
        if (getNumberOfColumns() <= 1) {
            return false;
        } else if (isColumnFilled(getNumberOfColumns() - 1)) {
            return false;
        } else if (isGridFullColumnDown()) {
            return false;
        }
        return true;
    }

    public boolean isGridFullColumnDown(){
        int rowCount, columnCount = 0, fullCount = 0;
        for(rowCount = 0; rowCount < getNumberOfRows(); rowCount++){
            for(columnCount = 0; columnCount < (getNumberOfColumns() - 1); columnCount++){
                if(getCellOwner(rowCount, columnCount) != null){
                    fullCount++;
                }
            }
        }
        if(fullCount == (rowCount * columnCount)){
            return true;
        }
        return false;
    }

    public OXOPlayer rowColWinChecker(){
        for(int n = 0; n < getNumberOfColumns(); n++){
            for(int i = 0; i < getNumberOfRows(); i++){
                if (cells.get(i).get(n) != null) {
                    isRowColWon(i, n, ROW);
                    isRowColWon(i, n, COLUMN);
                }
            }
        }
        return null;
    }

    private void isRowColWon(int row, int col, OXOMoveException.RowOrColumn dimension){
        if(dimension == ROW){
            if((row + getWinThreshold()) > (getNumberOfRows())){
                return;
            } else {
                checkWinColumn(row, col);
                }
            }
        if(dimension == COLUMN){
            if((col + getWinThreshold()) > (getNumberOfColumns())){
                return;
            } else {
                checkWinRow(row, col);
            }
        }
    }

    private void checkWinColumn(int row, int col){
        int winCount = 0;
        for(int n = row; n < getNumberOfRows(); n++) {
            if (n + 1 <= (getNumberOfRows() - 1)) {
                if(cells.get(n).get(col) != null) {
                    if (cells.get(n).get(col) == cells.get(n + 1).get(col)) {
                        winCount++;
                        if(winCount == (getWinThreshold() - 1)){
                            setWinner(getCellOwner(n, col));
                        }
                    } else {
                        winCount = 0;
                    }
                }
            }
        }
    }

    private void checkWinRow(int row, int col){
        int winCount = 0;
        for(int n = col; n < getNumberOfColumns(); n++){
            if(n + 1 <= (getNumberOfColumns() - 1)) {
                if(cells.get(row).get(n) != null) {
                    if (cells.get(row).get(n) == cells.get(row).get(n + 1)) {
                        winCount++;
                        if(winCount == (getWinThreshold() - 1)){
                            setWinner(getCellOwner(row, n));
                            return;
                        }
                    } else {
                        winCount = 0;
                    }
                }
            }
        }
    }

    public OXOPlayer diagWinChecker(){
        for(int n = 0; n < getNumberOfColumns(); n++){
            for(int i = 0; i < getNumberOfRows(); i++){
                if(cells.get(i).get(n) != null) {
                    diagonalCheckPlus(i, n);
                    diagonalCheckMinus(i, n);
                }
            }
        }
        return null;
    }

    private void diagonalCheckPlus(int row, int col) {
        int winCount = 0;
        int n = col, i = row;
        while (n + 1 <= (getNumberOfColumns() - 1) && i + 1 <= (getNumberOfRows() - 1)) {
            if (cells.get(i).get(n) != null) {
                if (cells.get(i).get(n) == cells.get(i + 1).get(n + 1)) {
                    winCount++;
                    if (winCount == (getWinThreshold() - 1)) {
                        setWinner(getCellOwner(i, n));
                        return;
                    }
                } else {
                    winCount = 0;
                }
            }
            i++;
            n++;
        }
    }


    private void diagonalCheckMinus(int row, int col){
        int winCount = 0;
        int n = col, i = row;
        while (n - 1 >= 0 && i + 1 <= (getNumberOfRows() - 1)) {
            if (cells.get(i).get(n) != null) {
                if (cells.get(i).get(n) == cells.get(i + 1).get(n - 1)) {
                    winCount++;
                    if (winCount == (getWinThreshold() - 1)) {
                        setWinner(getCellOwner(i, n));
                        return;
                    }
                } else {
                    winCount = 0;
                }
            }
            i++;
            n--;
        }
    }

    public void addNewPlayer(char newPlayerLetter) {
        players.add(new OXOPlayer(newPlayerLetter));
    }

    public boolean isGridFull(){
        int rowCount, columnCount = 0, fullCnt = 0;
        for(rowCount = 0; rowCount < getNumberOfRows(); rowCount++){
            for(columnCount = 0; columnCount < getNumberOfColumns(); columnCount++){
                if(getCellOwner(rowCount, columnCount) != null){
                    fullCnt++;
                }
            }
        }
        if(fullCnt == (rowCount * columnCount)){
            return true;
        }
        return false;
    }

    public boolean isRowFilled(int row){
        for(int n = 0; n < getNumberOfColumns(); n++){
            if(getCellOwner(row, n) != null){
                return true;
            }
        }
        return false;
    }

    public boolean isColumnFilled(int column){
        for(int n = 0; n < getNumberOfRows(); n++){
            if(getCellOwner(n, column) != null){
                return true;
            }
        }
        return false;
    }

    public void resetDrawn(){
        if(gameDrawn){
            gameDrawn = false;
        }
    }
}