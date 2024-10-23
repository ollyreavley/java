package edu.uob;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;

/** This class implements the DB server. */
public class DBServer {

    private static final char END_OF_TRANSMISSION = 4;
    public String storageFolderPath;
    public DBDatabase data;
    File inDirectory;
    public String currentDirectory;
    public DBServer server;
    public String currentDatabase;

    private File configFile;

    public static void main(String args[]) throws IOException {
        DBServer server = new DBServer();
        server.blockingListenOn(8888);
    }

    /**
    * KEEP this signature otherwise we won't be able to mark your submission correctly.
    */
    public DBServer() {
        storageFolderPath = Paths.get("databases").toAbsolutePath().toString();
        try {
          // Create the database storage folder if it doesn't already exist !
            Files.createDirectories(Paths.get(storageFolderPath));
        } catch(IOException ioe) {
            System.out.println("Can't seem to create database storage folder " + storageFolderPath);
        }
    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.DBServer.handleCommand(String)}) otherwise we won't be
    * able to mark your submission correctly.
    *
    * <p>This method handles all incoming DB commands and carries out the required actions.
    */
    public String handleCommand(String command) {
        if(server == null){
            server = new DBServer();
        }
        DBTokeniser tokens = new DBTokeniser(command);
        ArrayList<String> rawTokens = null;
        try{
            rawTokens = tokens.removeWhiteSpace();
        } catch(ParserExceptions ex){
            return "[ERROR] Please try again." + ex.getMessage();
        }
        DBParser parsedInstructions = new DBParser(rawTokens);
        DBCmd cmd;
        try{
            cmd = parsedInstructions.isCommand(rawTokens);
        } catch(ParserExceptions | IOException ex){
            return "[ERROR]" + ex.getMessage();
        }
        DBCmd.setServer(this);
        if(cmd != null){
            return cmd.query(server);
        } else{
            return "[ERROR] query command response is null.";
        }
    }


    public boolean openDatabase(String databaseName) throws IOException {
        String name = storageFolderPath + File.separator + databaseName + File.separator;
        currentDirectory = name;
        inDirectory = new File(name);
        try {
            // Create the database storage folder if it doesn't already exist !
            Files.createDirectories(Paths.get(storageFolderPath));
        } catch(IOException ioe) {
            System.out.println("Can't seem to create database storage folder " + storageFolderPath);
        }
        if(inDirectory.isDirectory()){
            String[] tableName = inDirectory.list();
            data = new DBDatabase(name, tableName);
            return true;
        } else{
            return false;
        }
    }

    public boolean openFile(String fileName) throws IOException {
        //for opening individual files if using select of insert etc
        String[] newStrArr = new String[] {fileName};
        data = new DBDatabase(currentDirectory, newStrArr);
        return data.tables.get(0) != null;
    }

    public void setConfigFile(File configName){
        configFile = configName;
    }

    public File getConfigFile(){
        return configFile;
    }

    //  === Methods below handle networking aspects of the project - you will not need to change these ! ===

    public void blockingListenOn(int portNumber) throws IOException {
        try (ServerSocket s = new ServerSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);
            while (!Thread.interrupted()) {
                try {
                    blockingHandleConnection(s);
                } catch (IOException e) {
                    System.err.println("Server encountered a non-fatal IO error:");
                    e.printStackTrace();
                    System.err.println("Continuing...");
                }
            }
        }
    }

    private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
        try (Socket s = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {

            System.out.println("Connection established: " + serverSocket.getInetAddress());
            while (!Thread.interrupted()) {
                String incomingCommand = reader.readLine();
                System.out.println("Received message: " + incomingCommand);
                String result = handleCommand(incomingCommand);
                writer.write(result);
                writer.write("\n" + END_OF_TRANSMISSION + "\n");
                writer.flush();
            }
        }
    }
}
