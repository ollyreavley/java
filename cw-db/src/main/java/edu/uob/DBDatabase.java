package edu.uob;

import java.io.*;
import java.util.*;

public class DBDatabase {
    public ArrayList<DBTable> tables;

    public DBDatabase(String folderName, String[] fileNames) throws IOException{
        tables = new ArrayList<DBTable>();
        File fileToOpen = null;
        int i = 0;
        while(i < fileNames.length){
            if(fileNames[i].contains(".")){
                //to remove the .tab if included
                String[] broken = fileNames[i].split("\\.");
                fileNames[i] = broken[0];
            }
            if(!fileNames[i].equals("config")){
                fileToOpen = new File(folderName + File.separator + fileNames[i]);
                DBTable singleTable = new DBTable(fileToOpen);
                tables.add(singleTable);
            }
            i++;
        }
    }

    public void editTable(String folderPath, String tableName, ArrayList<Integer> rowsToUpdate, ArrayList<String> newInfo, ArrayList<String> colMatch) throws IOException, InterpreterExceptions {
        int i = 0;
        if(tables.size() == 0){
            throw new InterpreterExceptions.Error("Table does not exist.");
        }
        while(i < tables.size()){
            if(tables.get(i).getTableName().equals(tableName)){
                tables.get(i).editData(folderPath, rowsToUpdate, newInfo, colMatch);
            }
            i++;
        }
    }

    public void printTable(String tableName){
        int i = 0;
        while(i < tables.size()){
            if(tables.get(i).getTableName().equals(tableName)){
                tables.get(i).printRows();
                return;
            }
            i++;
        }
    }

    public void writeToFile(File fileName, String tableToSave) throws IOException, InterpreterExceptions {
        int i = 0;
        while(i <= tables.size()){
            if(tables.get(i).getTableName().equals(tableToSave)){
                tables.get(i).writeRowsToFile(fileName);
            }
            i++;
        }
    }

    public String getCellInfo(String tableName, String whereMatches, String colMatch){
        int i = 0;
        while(i < tables.size()) {
            if (tables.get(i).getTableName().equals(tableName)) {
                return tables.get(i).getCellInfo(whereMatches, colMatch);
            }
            i++;
        }
        return null;
    }

    public boolean addAttributes(String folderName, ArrayList<String> attributeNames) throws IOException, InterpreterExceptions {
        if(tables.size() == 0){
            throw new InterpreterExceptions.Error("Table does not exist.");
        }
        return tables.get(0).addAttributes(folderName, attributeNames);
    }

    public boolean addOneAttributes(String folderName, ArrayList<String> attributeNames) throws IOException, InterpreterExceptions {
        if(tables.size() == 0){
            throw new InterpreterExceptions.Error("Table does not exist.");
        }
        return tables.get(0).addOneAttributes(folderName, attributeNames);
    }

    public boolean addRow(String folderName, ArrayList<String> rowData, DBServer s) throws IOException, InterpreterExceptions {
        if(tables.size() == 0){
            throw new InterpreterExceptions.Error("Table does not exist.");
        }
        return tables.get(0).addRow(folderName, rowData, s);
    }

    public int getNumberOfAttributes(String tableName) throws InterpreterExceptions {
        if(tables.size() == 0){
            throw new InterpreterExceptions.Error("Table does not exist.");
        }
        int i = 0;
        while(i <= tables.size()){
            if(tables.get(i).getTableName().equals(tableName)){
                return tables.get(i).NumberOfAttributes();
            }
            i++;
        }
        return 0;
    }

    public String getAttributes(String tableName) throws InterpreterExceptions {
        if(tables.size() == 0){
            throw new InterpreterExceptions.Error("Table does not exist.");
        }
        int i = 0;
        while(i <= tables.size()){
            if(tables.get(i).getTableName().equals(tableName)){
                return tables.get(i).getAttributes();
            }
            i++;
        }
        throw new InterpreterExceptions.Error("Table does not exist.");
    }

    public boolean removeAttribute(String folderName, ArrayList<String> attribute) throws IOException, InterpreterExceptions {
        if(tables.size() == 0){
            throw new InterpreterExceptions.Error("Table does not exist.");
        }
        if(tables.get(0).removeAttribute(folderName, attribute)){
            return true;
        } else{
            return false;
        }
    }

    public ArrayList<Integer> removeRowTest(String colMatch, String rowMatch, String comparison) throws IOException, InterpreterExceptions {
        if(tables.size() == 0){
            throw new InterpreterExceptions.Error("Table does not exist.");
        }
        return tables.get(0).removeRowTest(colMatch, rowMatch, comparison);
    }

    public boolean removingRows(String folderPath, ArrayList<Integer> rowsToRemove) throws IOException, InterpreterExceptions {
        if(tables.size() == 0){
            throw new InterpreterExceptions.Error("Table does not exist.");
        }
        return tables.get(0).removingRows(folderPath, rowsToRemove);
    }

    //returns the column headers for a table
    public ArrayList<String> columnInfo(String tableName, String columnHeader) throws InterpreterExceptions {
        int i = 0;
        while(i < tables.size()) {
            if(tables.size() == 0){
                throw new InterpreterExceptions.Error("Table does not exist.");
            }
            if (tables.get(i).getTableName().equals(tableName)) {
                return tables.get(i).getColumnInfo(columnHeader);
            }
            i++;
        }
        return null;
    }

    public ArrayList<String> rowsToPrint(ArrayList<Integer> printingRows) throws InterpreterExceptions.Error {
        int i = 0;
        ArrayList<String> combinedRows = new ArrayList<>();
        while(i < printingRows.size()){
            if(tables.size() == 0){
                throw new InterpreterExceptions.Error("Table does not exist.");
            }
            combinedRows.add(tables.get(0).getRowInfo(printingRows.get(i)));
            i++;
        }
        return combinedRows;
    }

    public int getAttributeColumn(String colMatch) throws InterpreterExceptions.Error {
        if(tables.size() == 0){
            throw new InterpreterExceptions.Error("Table does not exist.");
        }
        return tables.get(0).getAttributeCol(colMatch);
    }

    //gets specific cell information and concatanates it to make it printable
    public ArrayList<String> getSpecificInfo(ArrayList<Integer> columns, ArrayList<Integer> rows) throws InterpreterExceptions.Error {
        ArrayList<String> rowsSelected = new ArrayList<String>();
        int i = 0, n = 0;
        if(tables.size() == 0){
            throw new InterpreterExceptions.Error("Table does not exist.");
        }
        while(n < rows.size()){
            String rowInfo = new String();
            i = 0;
            while(i < columns.size()){
                if(tables.get(0).getSpecificCells(rows.get(n), columns.get(i)) != null){
                    rowInfo = rowInfo.concat(tables.get(0).getSpecificCells(rows.get(n), columns.get(i)));
                }
                 rowInfo = rowInfo.concat("\t");
                i++;
            }
            rowsSelected.add(rowInfo);
            n++;
        }
        return rowsSelected;
    }

    public ArrayList<Integer> getNumberOfRows() throws InterpreterExceptions.Error {
        if(tables.size() == 0){
            throw new InterpreterExceptions.Error("Table does not exist.");
        }
        ArrayList<Integer> rowNumbers = new ArrayList<Integer>();
        int i = 0;
        while(i < tables.get(0).getNumberOfRows()){
            rowNumbers.add(i);
            i++;
        }
        return rowNumbers;
    }
}
