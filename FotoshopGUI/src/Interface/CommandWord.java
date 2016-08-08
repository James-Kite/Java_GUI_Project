package Interface;


/**
 * Representations of all the valid commands in fotoshop.
 *
 * @author James
 * @version 08/11/15
 */
public enum CommandWord {

    //a value for each command and one for any unknown command
    OPEN("open"), SAVE("save"), LOOK("look"), MONO("mono"), ROT90("rot90"), FLIPH("fliph"), SCRIPT("script"), PUT("put"), GET("get"), UNDO("undo"), HELP("help"), QUIT("quit"), UNKNOWN("?");

    //the command string - already private
    String commandString;

    /**
     * Initialise the command word with corresponding string
     * @param commandString
     */
    CommandWord(String commandString) {
        this.commandString = commandString;
    }

    /**
     * Method to return the command word as a string
     *
     * @return
     */
    public String sendToString() {
        return commandString;
    }

}
