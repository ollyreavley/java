package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;

public class DBAttributes {

    public ArrayList<String> attributes;

    private int removedCol;

    public static int tableSize;

    public int size;

    public DBAttributes(String attributeInfo){
        attributes = new ArrayList<String>();
        //include ID if not included
        if(!attributeInfo.contains("id")){
            StringBuffer addId = new StringBuffer();
            addId.append("id");
            addId.append("\t");
            addId.append(attributeInfo);
            attributeInfo = addId.toString();
        }
        //including the tab
        String[] cutData = attributeInfo.split("\\t");
        attributes.addAll(Arrays.asList(cutData));
        tableSize = attributes.size();
        setTableSize(attributes.size());
    }

    //static attribute included here to allow rows class to easily check the size of the table
    public static int getTableSize(){
        return tableSize;
    }

    public void setTableSize(int i){
        size = i;
    }

    public int getSize(){
        return size;
    }

    public String returnString() {
        int i = 0;
        String attributeString = new String();
        //returning the cells of the attributes in a string form for printing
        while(i < getSize()){
            attributeString = attributeString.concat(attributes.get(i));
            if(i < (getSize() - 1)){
                attributeString = attributeString.concat("\t");
            }
            i++;
        }
        return attributeString;
    }

    public boolean addAttribute(ArrayList<String> attributeNames, int oldColNumber){
        attributes.addAll(attributeNames);
        return attributes.size() > oldColNumber;
    }

    public String getAttribute(int column){
        return attributes.get(column);
    }

    public String addOneAttribute(ArrayList<String> attributeNames){
        int oldAttributeSize = attributes.size();
        attributes.addAll(attributeNames);
        tableSize = attributes.size();
        setTableSize(attributes.size());
        if(attributes.size() > oldAttributeSize){
            return "Attribute has been successfully added";
        } else{
            return "Attribute has not been added";
        }
    }

    public boolean removeAttribute(ArrayList<String> attributeToRemove){
        int i = 0, oldTableSize = getSize();
        while(i < getSize()){
            if(attributes.get(i).equals(attributeToRemove.get(0))){
                attributes.remove(i);
                removedCol(i);
                tableSize = attributes.size();
                setTableSize(attributes.size());
            }
            i++;
        }
        if(getSize() < oldTableSize){
            return true;
        } else{
            return false;
        }
    }

    public void removedCol(int i){
        removedCol = i;
    }

    public int removedColRet(){
        return removedCol;
    }
}
