package edu.uob;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DBTable {

    private String name;

    private ArrayList<DBRow> rows;

    private ArrayList<DBAttributes> attributes;

    private ArrayList<Integer> rowsToRemove;

    public DBTable(File fileToOpen) throws IOException {
        name = fileToOpen.getName();
        int i = 0;
        DBRow row;
        rows = new ArrayList<DBRow>();
        DBAttributes attribute;
        attributes = new ArrayList<DBAttributes>();
        FileReader reader = new FileReader(fileToOpen + ".tab");
        BufferedReader buffReader = new BufferedReader(reader);
        while(buffReader.ready()) {
            String line = buffReader.readLine();
                if (line.contains("id")) {
                    //finding the header line
                    attribute = new DBAttributes(line);
                    attributes.add(attribute);
                } else {
                    row = new DBRow(line);
                    rows.add(i, row);
                    i++;
                }
        }
        buffReader.close();
        reader.close();
    }

    public String getTableName(){
        return name;
    }

    public void editData(String folderPath, ArrayList<Integer> rowsToUpdate, ArrayList<String> newInfo, ArrayList<String> colMatch) throws IOException, InterpreterExceptions {
        int i = 0, n = 0;
        while(i < colMatch.size()){
            int column = getAttributeCol(colMatch.get(i));
            n = 0;
            while(n < rowsToUpdate.size()){
                rows.get(rowsToUpdate.get(n)).editInfo(newInfo.get(i), column);
                n++;
            }
            i++;
        }
        File fileToOpen = new File(folderPath + getTableName() + ".tab");
        writeRowsToFile(fileToOpen);
    }

    public void printRows(){
        int i = 0;
        while(i < rows.size()) {
            rows.get(i).printOut();
            i++;
        }
    }

    public void writeRowsToFile(File fileName) throws IOException, InterpreterExceptions {
        int i = 0;
        FileWriter newFile = new FileWriter(fileName);
        BufferedWriter newBuffFile = new BufferedWriter(newFile);
        if(!fileName.canWrite()){
            throw new InterpreterExceptions.Error("Cannot write to this file.");
        } else {
            newBuffFile.write(attributes.get(i).returnString());
            newBuffFile.newLine();
            while (i < rows.size()) {
                newBuffFile.write(rows.get(i).returnString());
                if(i < rows.size() - 1){
                    newBuffFile.newLine();
                }
                i++;
            }
        }
        newBuffFile.close();
        newFile.close();
    }

    public String getCellInfo(String rowMatch, String colMatch){
        int column = 0, row = 0, n = 0;
        column = getAttributeCol(colMatch);
        while(n < rows.size()){
            if(rows.get(n).getCell(column).equals(rowMatch)){
                row = n;
            }
            n++;
        }
        return rows.get(row).getCell(column);
    }

    public int getAttributeCol(String colMatch){
        int i = 0, column = 0;
        while(i < attributes.get(0).getSize()){
            if(attributes.get(0).getAttribute(i).equalsIgnoreCase(colMatch)){
                column = i;
            }
            i++;
        }
        return column;
    }

    public ArrayList<Integer> removeRowTest(String colMatch, String rowMatch, String comparison) {
        rowsToRemove = new ArrayList<Integer>();
        int column = 0, row = 0, n = 0, oldRowNums = rows.size();
        column = getAttributeCol(colMatch);
        while(n < rows.size()){
            if(conversion(comparison, rowMatch, column, n)){
                row = n;
                rowsToRemove.add(row);
            }
            n++;
        }
        return rowsToRemove;
    }

    public boolean removingRows(String folderName, ArrayList<Integer> rowsRemove) throws IOException, InterpreterExceptions {
        int  oldRowNums = rows.size(), i = 0;
        while(i < rowsRemove.size()){
            int row = rowsRemove.get(i);
            row = row - i;
            rows.remove(row);
            i++;
        }
        if(rows.size() < oldRowNums){
            File fileToWrite = new File(folderName + File.separator + name + ".tab");
            writeRowsToFile(fileToWrite);
            return true;
        } else{
            return false;
        }
    }

    public boolean conversion(String comparison, String rowMatch, int column, int row){
        float convertedFloat = 0, convertedTestRowF = 0;
        //null and booleans are the main restrictions on comparisons so done first
        if(rowMatch.equalsIgnoreCase("NULL") || rows.get(row).getCell(column).equalsIgnoreCase("NULL")){
            return false;
        }
        if(boolMatch(rowMatch) && boolMatch(rows.get(row).getCell(column))){
            if(rowMatch.equalsIgnoreCase(rows.get(row).getCell(column))){
                return true;
            }
        }
        try{
            //integers can be treated as floats so done so here
            convertedFloat = Float.parseFloat(rowMatch);
            convertedTestRowF = Float.parseFloat(rows.get(row).getCell(column));
            return comparisonFloat(comparison, convertedFloat, convertedTestRowF);
            //ignore an exception in case the converison is invalid
        } catch (NumberFormatException ignored){}
        int diff = rowMatch.compareTo(rows.get(row).getCell(column));
        return stringComparison(comparison, diff);
    }

    private boolean boolMatch(String cell){
        if(cell.equalsIgnoreCase("true")){
            return true;
        } else return cell.equalsIgnoreCase("false");
    }

    public boolean comparisonFloat(String comparison, float notFromTable, float fromTable) {
        if (comparison.equals("==") || comparison.equalsIgnoreCase("LIKE")) {
            return fromTable == notFromTable;
        } else if (comparison.equals(">")) {
            return fromTable > notFromTable;
        } else if (comparison.equals("<")) {
            return fromTable < notFromTable;
        } else if (comparison.equals(">=")) {
            return fromTable >= notFromTable;
        } else if (comparison.equals("<=")) {
            return fromTable <= notFromTable;
        } else if (comparison.equals("!=")) {
            return fromTable != notFromTable;
        }
        return false;
    }

    public boolean stringComparison(String Comparison, int difference){
        if(Comparison.equals("==") || Comparison.equalsIgnoreCase("LIKE")) {
            return difference == 0;
        } else if(Comparison.equals(">")) {
            return difference > 0;
        }else if(Comparison.equals(">=")) {
            return difference >= 0;
        }else if(Comparison.equals("<")) {
            return difference > 0;
        }else if(Comparison.equals("<=")) {
            return difference >= 0;
        }else if(Comparison.equals("!=")) {
            return difference != 0;
        }
        return false;
    }

    public boolean addAttributes(String storageFolder, ArrayList<String> attributeNames) throws IOException, InterpreterExceptions {
        int oldColNumber = 0;
        if(attributes.size() == 0){
            oldColNumber = 0;
        } else{
            oldColNumber = attributes.get(0).getSize();
        }
        StringJoiner names = new StringJoiner("\t");
        for (String attributeName : attributeNames) {
            names.add(attributeName);
        }
        String tabbedNames = names.toString();
        DBAttributes attribute = new DBAttributes(tabbedNames);
        attributes.add(attribute);
        File fileToWrite = new File(storageFolder + File.separator + name + ".tab");
        writeRowsToFile(fileToWrite);
        return attributes.get(0).addAttribute(attributeNames, oldColNumber);
    }

    public boolean addOneAttributes(String storageFolder, ArrayList<String> attributeNames) throws IOException, InterpreterExceptions {
        int i = 0, oldColNumber = 0;
        if(attributes.size() == 0){
            oldColNumber = 0;
        } else{
            oldColNumber = attributes.get(0).getSize();
        }

        StringJoiner names = new StringJoiner("\t");
        for (String attributeName : attributeNames) {
            names.add(attributeName);
        }
        String tabbedNames = names.toString();
        ArrayList<String> listified = new ArrayList<String> (Arrays.asList(tabbedNames));
        if(attributes.size() == 0){
            DBAttributes newAttributes = new DBAttributes(listified.get(0));
            attributes.add(newAttributes);
        } else{
            attributes.get(0).addOneAttribute(listified);
        }
        while(i < rows.size()){
            rows.get(i).addOneCell();
            i++;
        }
        File fileToWrite = new File(storageFolder + File.separator + name + ".tab");
        writeRowsToFile(fileToWrite);
        return attributes.get(0).addAttribute(attributeNames, oldColNumber);
    }

    public boolean addRow(String storageFolder, ArrayList<String> rowData, DBServer s) throws IOException, InterpreterExceptions {
        int oldRowNum = 0;
        if(rows.size() != 0){
            oldRowNum = rows.size();
        }
        ArrayList<String> rowDataAdj = rowPreProcess(rowData, s);
        DBRow newRow = makeARow(rowDataAdj);
        rows.add(newRow);
        File fileToWrite = new File(storageFolder + File.separator + name + ".tab");
        writeRowsToFile(fileToWrite);
        if(rows.size() == 0){
            return false;
        }
        return rows.size() > oldRowNum;
    }

    private ArrayList<String> rowPreProcess(ArrayList<String> rowData, DBServer s) throws IOException {
        int i = 0, id = 0;
        //for finding the persisitent max ID and updating it
        ArrayList<String> lines = extractId(s);
        id = parseId(lines);
        String idStr;
        while(i < rows.size()){
            idStr = rows.get(i).getCell(0);
            int idInt = Integer.parseInt(idStr);
            if(idInt >= id){
                id = idInt + 1;
            }
            i++;
        }
        if (id == 0){
            id = 1;
        }
        idStr = String.valueOf(id);
        lines = backToLines(lines, idStr);
        saveConfigFile(s, lines);
        rowData.add(0, idStr);
        return rowData;
    }

    private ArrayList<String> extractId(DBServer s) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        int i = 0;
        File configPath = new File(s.currentDirectory + File.separator + "config");
        FileReader reader = new FileReader(configPath);
        BufferedReader buffReader = new BufferedReader(reader);
        while(buffReader.ready()) {
            lines.add(buffReader.readLine());
        }
        buffReader.close();
        reader.close();
        return lines;
    }

    private int parseId(ArrayList<String> lines){
        int i = 0, id = 0;
        while(i < lines.size()){
            if(lines.get(i).contains(getTableName())){
                String[] splitLine = lines.get(i).split("\t");
                id = Integer.parseInt(splitLine[1]);
            }
            i++;
        }
        return id;
    }

    private ArrayList<String> backToLines(ArrayList<String> lines, String id){
        int i = 0, row = 0;
        StringJoiner joiner = null;
        while(i < lines.size()){
            if(lines.get(i).contains(getTableName())){
                row = i;
                String[] splitLine = lines.get(i).split("\t");
                splitLine[1] = id;
                joiner = new StringJoiner("\t");
                for(int n = 0; n < splitLine.length; n++) {
                    joiner.add(splitLine[n]);
                }
            }
            i++;
        }
        lines.set(row, joiner.toString());
        return lines;
    }

    private void saveConfigFile(DBServer s, ArrayList<String> lines) throws IOException{
        FileWriter writer = new FileWriter(s.currentDirectory + File.separator + "config");
        BufferedWriter buffWriter = new BufferedWriter(writer);
        int i = 0;
        while(i < lines.size()){
            buffWriter.write(lines.get(i));
            buffWriter.newLine();
            i++;
        }
        buffWriter.close();
        writer.close();
    }

    private DBRow makeARow(ArrayList<String> rowDataAdj){
        StringJoiner rowInfo = new StringJoiner("\t");
        for (String rowDatum : rowDataAdj) {
            rowInfo.add(rowDatum);
        }
        String tabbedData = rowInfo.toString();
        DBRow newRow = new DBRow(tabbedData);
        return newRow;
    }

    public int NumberOfAttributes(){
        if(attributes.size() == 0){
            return 0;
        }
        return attributes.get(0).getSize();
    }

    public String getAttributes(){
        return attributes.get(0).returnString();
    }

    public boolean removeAttribute(String folderName, ArrayList<String> attribute) throws IOException, InterpreterExceptions {
        if(attributes.get(0).removeAttribute(attribute)){
            if(rows.size() == 0){
                File fileToWrite = new File(folderName + File.separator + name + ".tab");
                writeRowsToFile(fileToWrite);
                return true;
            }
            if(removeRows(attributes.get(0).removedColRet())){
                File fileToWrite = new File(folderName + File.separator + name + ".tab");
                writeRowsToFile(fileToWrite);
                return true;
            } else{
                return false;
            }
        } else{
            return false;
        }
    }

    public boolean removeRows(int i){
        int n = 0;
        if(rows.size() == 0){
            return false;
        }
        int oldWidth = rows.get(n).size();
        if(i == attributes.get(0).getSize()){
            return true;
        }
        while(n < rows.size()){
            rows.get(n).removeCols(i);
            n++;
        }
        n = 0;
        int count = 0;
        while(n < rows.size()){
            if(rows.get(0).size() < oldWidth){
                count++;
            }
            n++;
        }
        if(count == rows.size()){
            return true;
        } else{
            return false;
        }

    }

    public ArrayList<String> getColumnInfo(String columnHeader){
        ArrayList<String> cellsInfo = new ArrayList<String>();
        int column = getAttributeCol(columnHeader);
        int i = 0;
        while(i < rows.size()){
            cellsInfo.add(rows.get(i).getCell(column));
            i++;
        }
        return cellsInfo;
    }

    public String getRowInfo(Integer rowNumber){
        return rows.get(rowNumber).returnString();
    }

    public String getSpecificCells(Integer rowNumber, Integer column){
        return rows.get(rowNumber).getCell(column);
    }

    public Integer getNumberOfRows(){
        return rows.size();
    }
}
