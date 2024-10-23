package edu.uob;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import static edu.uob.TokenShortHand.*;

public class DBCreateCmd extends DBCmd {

    public ArrayList<String> tableNames;
    public ArrayList<String> attributeNames;
    public String databaseName;
    public ArrayList<TokenShortHand> tokens;

    public DBCreateCmd() {
        tokens = new ArrayList<TokenShortHand>();
        tableNames = new ArrayList<String>();
        attributeNames = new ArrayList<String>();
    }

    public void addTokens(ArrayList<TokenShortHand> tokenStream){
        tokens.addAll(tokenStream);
    }

    public void addTableName(ArrayList<String> tableNamesFromParse){
        tableNames.addAll(tableNamesFromParse);
    }

    public void addDatabaseName(String databaseNameFromParse){
        databaseName = databaseNameFromParse;
    }

    public void addAttributes(ArrayList<String> attributeNamesFromParse){
        attributeNames.addAll(attributeNamesFromParse);
    }

    public String query(DBServer s) {
        if(tokens.contains(TABLE)){
            if(tokens.contains(ATTRIBUTE)){
                fileCreationWtAttrib(s);
                return "[OK]";
            } else {
                createFileTop(s);
                return "[OK]";
            }
        } else if(tokens.contains(DATABASE)){
            try {
                s.currentDatabase = databaseName;
                createDirectory(s.storageFolderPath + File.separator + databaseName + File.separator, s);
                return "[OK]";
            } catch (IOException ex){
                return "[ERROR] " + ex.getMessage();
            }
        }
        return "Unknown failure.";
    }

    private String fileCreationWtAttrib(DBServer s){
        String fileCreation = createFileTop(s);
        try{
            if(addAttributes(s)){
                return "[OK] " + fileCreation + "attributes added";
            } else{
                return "[ERROR] " + fileCreation + " attributes not added";
            }
        } catch(IOException | InterpreterExceptions ex){
            return ex.getMessage();
        }
    }

    private String createFileTop(DBServer s) {
        try {
            if(s != null){
                return createFile(s.currentDirectory + File.separator + tableNames.get(0), s);
            } else{
                return "s is null";
            }
        } catch (IOException ex){
            return ex.getMessage();
        }
    }

    private String createFile(String name, DBServer s) throws IOException{
        if(s.storageFolderPath == null){
            return "Folder path is null";
        }
        if (createFileNow(name)) {
            readFromWriteToConfig(s);
            return "File created.";
        } else {
            return "File already exists.";
        }
    }

    //adds the name of the table to the config file, see below
    private void readFromWriteToConfig(DBServer s) throws IOException {
        FileReader newInfo = new FileReader(s.currentDirectory + File.separator + "config");
        BufferedReader readFrom = new BufferedReader(newInfo);
        ArrayList<String> fromFile = new ArrayList<>();
        while(readFrom.ready()){
            fromFile.add(readFrom.readLine());
        }
        fromFile.add(tableNames.get(0) + "\t" + "0");
        readFrom.close();
        newInfo.close();
        File info = new File(s.currentDirectory + File.separator + "config");
        FileWriter newFile = new FileWriter(s.currentDirectory + File.separator + "config");
        BufferedWriter newBuffFile = new BufferedWriter(newFile);
        if (!info.canWrite()) {
            return;
        } else {
            int i = 0;
            while(i < fromFile.size()){
                newBuffFile.write(fromFile.get(i));
                newBuffFile.newLine();
                i++;
            }
            newBuffFile.close();
            newFile.close();
        }
    }

    private boolean createFileNow(String name) throws IOException {
        File newFile = new File(name + ".tab");
        return newFile.createNewFile();
    }

    private String createDirectory(String name, DBServer s) throws IOException{
        if(name == null){
            return "Folder path is null";
        }
        try {
            // Create the database storage folder if it doesn't already exist !
            Files.createDirectories(Paths.get(name));
            //makes the config file for persistent row ids
            File newConfig = new File(name + File.separator + "config");
            newConfig.createNewFile();
            s.setConfigFile(newConfig);
            return name;
        } catch(IOException ioe) {
            return "Can't seem to create database storage folder " + name;
        }
    }

    //adds attributes if provided
    private boolean addAttributes(DBServer s) throws IOException, InterpreterExceptions {
        String[] names = new String[] {tableNames.get(0)};
        DBDatabase openTable = new DBDatabase(s.currentDirectory, names);
        return openTable.addAttributes(s.currentDirectory, attributeNames);
    }
}
