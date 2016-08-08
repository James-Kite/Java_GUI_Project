package Interface;


/**
 * This class holds information about commands issued by the user
 * A command consists of two words: the first being a command word, the
 * second being a filename or parameter.
 *
 * @author James Kite
 * @version 2015.11.09
 */
public class Command {

    private final String commandWord;
    private final String secondWord;

    /**
     * Create a command object. First and second word must be supplied, but
     * either one (or both) can be null.
     *
     * @param firstWord The first word of the command. Null if the command was
     * not recognised.
     * @param secondWord The second word of the command.
     */
    public Command(String firstWord, String secondWord) {
        commandWord = firstWord;
        this.secondWord = secondWord;
    }

    /**
     * Return the command word (the first word) of this command. If the command
     * was not understood, the result is null.
     *
     * @return The command word.
     */
    public String getCommandWord() {
        return commandWord;
    }

    /**
     * @return The second word of this command. Returns null if there was no
     * second word.
     */
    public String getSecondWord() {
        return secondWord;
    }

    /**
     * @return true if this command was not understood.
     */
    public boolean nothingEntered() {
        return (commandWord == null);
    }

    /**
     * @return true if the command has a second word.
     */
    public boolean hasSecondWord() {
        return (secondWord != null);
    }

}
