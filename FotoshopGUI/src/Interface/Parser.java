package Interface;


import java.io.FileInputStream;
import java.util.Scanner;

/**
 * Parser reads user input and tries to interpret it as a command
 *
 * @author James Kite
 * @version 2015.11.09
 */
public class Parser {

    private final CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    public void setInputStream(FileInputStream str) {
        reader = new Scanner(str);
    }

    /**
     * @return The next command from the user.
     */
    public Command getCommand() {
        String inputLine;   // will hold the full input line
        String word1 = null;
        String word2 = null;

        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();

        // Find up to two words on the line.
        Scanner tokenizer = new Scanner(inputLine);
        if (tokenizer.hasNext()) {
            word1 = tokenizer.next();      // get first word
            if (tokenizer.hasNext()) {
                word2 = tokenizer.next();      // get second word
            }
        }

        // Now check whether this word is known. If so, create a command
        // with it. If not, create a "null" command (for unknown command).
        if (commands.isCommand(word1)) {
            return new Command(word1, word2);
        } else {
            return new Command(null, word2);
        }
    }

    /**
     * Retrieves the command words from the CommandWords class
     */
    public void showCommandWords() {
        commands.printCommandWords();
    }

}
