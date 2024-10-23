package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;

public class DBRow {

    private final ArrayList<String> rowInfo;

    public DBRow(String rowData){
        rowInfo = new ArrayList<String>();
        String[] cutRowData = rowData.split("\\t");
        rowInfo.addAll(Arrays.asList(cutRowData));
    }

    public void editInfo(String newInfo, int cellToEdit){
        rowInfo.set(cellToEdit, newInfo);
    }

    public void printOut(){
        System.out.println(returnString());
    }

    public String returnString(){
        int i = 0;
        //returns a string ready for printing
        String rowString = new String();
        while(i < DBAttributes.getTableSize()){
            if(i < rowInfo.size()){
                rowString = rowString.concat(rowInfo.get(i));
            } else{
                rowString = rowString.concat("");
            }
            if(i < (DBAttributes.getTableSize() - 1)){
                rowString = rowString.concat("\t");
            }
            i++;
        }
        return rowString;
    }

    public String getCell(int colMatch){
        return rowInfo.get(colMatch);
    }

    public void addOneCell(){
        //used where a new column is added
        String str = "NULL";
        rowInfo.add(str);
    }

    public void removeCols(int i){
        if(i < rowInfo.size()){
            rowInfo.remove(i);
        }
    }

    public int size(){
        return rowInfo.size();
    }

}
