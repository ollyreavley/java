package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class DBTokeniser {

    public ArrayList<String> instructions;
    public String query;
    public String[] specialCharacters = {"(",")",",",";",">","<","=","!"};
    public String[] combinedCharacters = {"> =","< =","= =","! ="};
    public String[] correctCombination = {">=", "<=", "==", "!="};
    public ArrayList<String> tokens;


    public DBTokeniser(String command){
        query = new String();
        query = query.concat(command);
    }

    //Courtesy of Dr Simon Lock, https://github.com/drslock/JAVA2022/blob/main/Weekly%20Lectures/07-Tokeniser.java#L9, accessed 13 March 2023.
    public ArrayList<String> removeWhiteSpace() throws ParserExceptions
    {
        // Remove any whitespace at the beginning and end of the query
        query = query.trim();
        tokens = new ArrayList<String>();
        int count = 0;
        if(query.contains("'")){
            for(int i = 0; i < query.length(); i++){
                String testChar = String.valueOf(query.charAt(i));
                if(Objects.equals(testChar, "'")){
                    count++;
                }
            }
            if(count % 2 != 0){
                throw new ParserExceptions.UnevenQuotes();
            }
        }
        // Split the query on single quotes (to separate out query characters from string literals)
        String[] fragments = query.split("'");

        for (int i=0; i<fragments.length; i++) {
            // Every odd fragment is a string literal, so just append it without any alterations
            if (i%2 != 0) tokens.add("'" + fragments[i] + "'");
                // If it's not a string literal, it must be query characters (which need further processing)
            else {
                // Tokenise the fragments into an array of strings
                String[] nextBatchOfTokens = tokenise(fragments[i]);
                // Then add these to the "result" array list (needs a bit of conversion)
                tokens.addAll(Arrays.asList(nextBatchOfTokens));
            }
        }
        // Finally, loop through the result array list, printing out each token a line at a time
        return tokens;
    }

    private String[] tokenise(String input)
    {
        // Add in some extra padding spaces around the "special characters"
        // so we can be sure that they are separated by AT LEAST one space (possibly more)
        for(int i=0; i<specialCharacters.length ;i++) {
            input = input.replace(specialCharacters[i], " " + specialCharacters[i] + " ");
        }
        // Remove all double spaces (the previous replacements may have added some)
        // This is "blind" replacement - replacing if they exist, doing nothing if they don't
        while (input.contains("  ")) input = input.replaceAll("  ", " ");
        // Again, remove any whitespace from the beginning and end that might have been introduced

        for(int i = 0; i < combinedCharacters.length; i++) {
            input = input.replace(combinedCharacters[i], correctCombination[i]);
        }
        input = input.trim();
        // Finally split on the space char (since there will now ALWAYS be a space between tokens)
        return input.split(" ");
    }
}
