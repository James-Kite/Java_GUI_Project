package Interface;


import Interface.CommandWord;
import java.util.HashMap;

/**
 * This class holds a HashMap of all the command words and some operations
 * relevant to these
 *
 * @author James Kite
 * @version 2015.11.09
 */
public class CommandWords {

    private final HashMap<String, CommandWord> validCommands;

    /**
     * Constructor - fill validCommands with all the command words
     */
    public CommandWords() {

        validCommands = new HashMap<>();
        for (CommandWord word : CommandWord.values()) {
            if (word != CommandWord.UNKNOWN) {
                validCommands.put(word.sendToString(), word);
            }
        }

    }

    /**
     * Method to print command words
     */
    public void printCommandWords() {
        String printString = "";
        for (CommandWord word : CommandWord.values()) {
            if (!"?".equals(word.sendToString())) {
                printString = printString + " " + word.sendToString();
            }
        }
        System.out.println(printString);
    }

    /**
     * Check whether a given String is a valid command word.
     *
     * @param aString
     * @return true if a given string is a valid command, false if it isn't.
     */
    public boolean isCommand(String aString) {
        for (CommandWord word : CommandWord.values()) {
            if (word.sendToString().equals(aString)) {
                return true;
            }
        }
        return false;
    }

}
