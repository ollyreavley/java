package edu.uob;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DBUseCmd extends DBCmd{

    public String databaseName;
    public ArrayList<TokenShortHand> tokens;


    public DBUseCmd() {
        tokens = new ArrayList<TokenShortHand>();
    }

    public void addTokens(ArrayList<TokenShortHand> tokenStream){
        tokens.addAll(tokenStream);
    }

    public String query(DBServer s) {
        try {
            if (s.openDatabase(databaseName)) {
                s.currentDatabase = databaseName;
                File config = new File(s.currentDatabase + File.separator + "config");
                s.setConfigFile(config);
                return "[OK] Database open.";
            } else {
                return "[ERROR] Database does not exist";
            }
        }
        catch (IOException ex){
            return "[ERROR] " + ex.getMessage();
        }
    }
}
