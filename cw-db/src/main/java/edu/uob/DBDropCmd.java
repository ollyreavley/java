package edu.uob;

import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;

import static edu.uob.TokenShortHand.*;

public class DBDropCmd extends DBCmd{

    public DBDropCmd(){
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

    public String query(DBServer s) {
        if(tokens.contains(TABLE)){
            File filePath = new File(s.currentDirectory + tableNames.get(0) + ".tab");
            if(filePath.delete()){
                return "[OK] File has been deleted.";
            } else{
                return "[ERROR] File has not been deleted, it may not exist.";
            }
        } else if(tokens.contains(DATABASE)){
            File filePath = new File(s.currentDirectory + File.separator);
            if(filePath.isDirectory()){
                return ifDirectory(filePath, s);
            } else{
                return "[ERROR] Error token suggests database but file path is not a directory." + filePath.toString();
            }
        }
        return "An unknown error has occurred";
    }

    private String ifDirectory(File filePath, DBServer s){
        File configFilePath = new File (s.currentDirectory + File.separator + "config");
        configFilePath.delete();
        String[] arr = filePath.list();
        if(arr == null){
            return "[ERROR] error with file path.";
        }
        if(arr.length > 0){
            return "[ERROR] Directory is not empty, must delete all files first before deleting the directory";
        } else {
            if (filePath.delete()) {
                return "[OK] Folder has been deleted";
            } else {
                return "[ERROR] Folder has not been deleted";
            }
        }
    }
}
