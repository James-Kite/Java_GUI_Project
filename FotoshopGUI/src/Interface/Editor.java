package Interface;


import Images.Cache;
import Images.ColorImage;
import Images.EditableImage;
import Images.Filters;
import Locale.Language;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;

/**
 * This class is the main processing class of the Fotoshop application. Fotoshop
 * is a very simple image editing tool. Users can apply a number of filters to
 * an image.
 *
 * This class creates and initialises all the others: it creates the parser and
 * evaluates and executes the commands that the parser returns.
 *
 * @author James Kite
 * @version 2015.11.09
 */
public class Editor {

    private final Parser parser;
    private final Cache cache;
    private ColorImage currentImage;
    private EditableImage edtImg;
    private String name;
    private Filters filters;
    private final ResourceBundle messages = Language.getBundle();

    /**
     * Create the editor and initialise its parser.
     */
    public Editor() {
        parser = new Parser();
        cache = new Cache();
        filters = new Filters();
    }
    
    public Cache getCache(){
        return cache;
    }

    /**
     * Main edit routine. Loops until the end of the editing session.
     */
    public void edit() {
        printWelcome();
        getCurrentImage();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the editing session is over.
        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println(messages.getString("thank_you")
        );
    }

    /**
     * Print out the opening message for the user.
     */
    public void printWelcome() {
        System.out.println();
        System.out.println(messages.getString("welcome1"));
        System.out.println(messages.getString("welcome2"));
        System.out.println(messages.getString("welcome3"));
        System.out.println();
    }

    /**
     * Print out current image and a list of currently applied filters
     */
    public void getCurrentImage() {
        if (imageOpenCheck(name)) {
            System.out.println(messages.getString("current_image") + name);
            System.out.print(messages.getString("filters_applied"));
            filters.printFilters();
        }

        System.out.println();
    }

    /**
     * Given a command, edit (that is: execute) the command.
     *
     * @param command The command to be processed.
     * @return true If the command ends the editing session, false otherwise.
     */
    public boolean processCommand(Command command) {

        boolean wantToQuit = false;

        if (command.nothingEntered()) {
            System.out.println(messages.getString("i_don't_know"));
            return false;
        }

        CommandWord word = CommandWord.UNKNOWN;

        for (CommandWord commandWord : CommandWord.values()) {
            if (commandWord.commandString.equals(command.getCommandWord())) {
                word = commandWord;
            }
        }

        switch (word) {
            case HELP:
                printHelp();
                break;
            case OPEN:
                open(command);
                break;
            case SAVE:
                save(command);
                break;
            case MONO:
                mono();
                break;
            case ROT90:
                rot90();
                break;
            case LOOK:
                look();
                break;
            case SCRIPT:
                wantToQuit = script(command);
                break;
            case QUIT:
                wantToQuit = quit(command);
                break;
            case PUT:
                edtImg = new EditableImage(currentImage, filters);
                cache.addToCache(name, edtImg);
                break;
            case GET:
                if(!cache.isEmpty()){
                    currentImage = getFromCache(command).returnColImage();
                    filters = getFromCache(command).returnFilters();
                    break;
                }
                else{
                    System.out.println("Cache empty. Could not get.");
                    break;
                }
            case UNDO:
                undoLastFilter();
                break;
        }
        return wantToQuit;
    }
//----------------------------------
// Implementations of user commands:
//----------------------------------

    /**
     * Print out some help information. Here we print some useless, cryptic
     * message and a list of the command words.
     */
    public void printHelp() {
        System.out.println(messages.getString("you_are_using"));
        System.out.println();
        System.out.println(messages.getString("your_command_words"));
        parser.showCommandWords();
    }

    /**
     * Load an image from a file.
     *
     * @param name The name of the image file
     * @return a ColorImage containing the image
     */
    public ColorImage loadImage(String name) {
        ColorImage img = null;
        try {
            img = new ColorImage(ImageIO.read(new File(name)));
        } catch (IOException e) {
            System.out.println(messages.getString("cannot_find_image") + name);
            System.out.println(messages.getString("cwd_is") + System.getProperty("user.dir"));
        }
        return img;
    }

    /**
     * "open" was entered. Open the file given as the second word of the command
     * and use as the current image.
     *
     * @param command the command given.
     */
    public void open(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know what to open...
            System.out.println(messages.getString("open_what"));
            return;
        }

        String inputName = command.getSecondWord();
        ColorImage img = loadImage(inputName);
        if (img == null) {
            System.out.println(messages.getString("please_enter_valid_filename"));
        } else {
            currentImage = img;
            name = inputName;
            filters = new Filters();
            edtImg = new EditableImage(currentImage, filters);
            System.out.println(messages.getString("loaded") + name);
        }
    }

    /**
     * "save" was entered. Save the current image to the file given as the
     * second word of the command.
     *
     * @param command the command given
     */
    public void save(Command command) {

        if (!imageOpenCheck(name)) {
            System.out.println(messages.getString("could_not_save"));
            return;
        }
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to save...
            System.out.println(messages.getString("save_where"));
            return;
        }

        String outputName = command.getSecondWord();
        try {
            File outputFile = new File(outputName);
            ImageIO.write(currentImage, "jpg", outputFile);
            System.out.println(messages.getString("image_saved_to") + outputName);
            name = outputName;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            printHelp();
        }
    }

    /**
     * "look" was entered. Report the status of the work bench.
     */
    public void look() {
        getCurrentImage();
    }

    /**
     * "mono" was entered. Convert the current image to monochrome.
     */
    public void mono() {

        if (!canApplyFilter(name, filters)) {
            return;
        }

        ColorImage tmpImage = new ColorImage(currentImage);
        //Graphics2D g2 = currentImage.createGraphics();
        int height = tmpImage.getHeight();
        int width = tmpImage.getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pix = tmpImage.getPixel(x, y);
                int lum = (int) Math.round(0.299 * pix.getRed()
                        + 0.587 * pix.getGreen()
                        + 0.114 * pix.getBlue());
                tmpImage.setPixel(x, y, new Color(lum, lum, lum));
            }
        }
        currentImage = tmpImage;

        filters.appendFilters(messages.getString("mono"));

    }

    /**
     * "rot90" was entered. Rotate the current image 90 degrees.
     */
    public void rot90() {

        if (!canApplyFilter(name, filters)) {
            return;
        }

        // R90 = [0 -1, 1 0] rotates around origin
        // (x,y) -> (-y,x)
        // then transate -> (height-y, x)
        int height = currentImage.getHeight();
        int width = currentImage.getWidth();
        ColorImage rotImage = new ColorImage(height, width);
        for (int y = 0; y < height; y++) { // in the rotated image
            for (int x = 0; x < width; x++) {
                Color pix = currentImage.getPixel(x, y);
                rotImage.setPixel(height - y - 1, x, pix);
            }
        }
        currentImage = rotImage;

        filters.appendFilters(messages.getString("rot90"));

    }

    /**
     * The 'script' command runs a sequence of commands from a text file.
     *
     * IT IS IMPORTANT THAT THIS COMMAND WORKS AS IT CAN BE USED FOR TESTING
     *
     * @param command the script command, second word of which is the name of
     * the script file.
     * @return whether to quit.
     */
    public boolean script(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know what to open...
            System.out.println(messages.getString("which_script"));
            return false;
        }

        String scriptName = command.getSecondWord();
        Parser scriptParser = new Parser();
        try (FileInputStream inputStream = new FileInputStream(scriptName)) {
            scriptParser.setInputStream(inputStream);
            boolean finished = false;
            while (!finished) {
                try {
                    Command cmd = scriptParser.getCommand();
                    finished = processCommand(cmd);
                } catch (Exception ex) {
                    return finished;
                }
            }
            return finished;
        } catch (FileNotFoundException ex) {
            System.out.println(messages.getString("cannot_find") + scriptName);
            return false;
        } catch (IOException ex) {
            throw new RuntimeException(messages.getString("panic"));
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see whether we
     * really quit the editor.
     *
     * @param command the command given.
     * @return true, if this command quits the editor, false otherwise.
     */
    public boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println(messages.getString("enter_quit"));
            return false;
        } else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Check if there is an image open
     *
     * @param name
     * @return
     */
    public boolean imageOpenCheck(String name) {
        boolean result = false;
        if (name != null) {
            result = true;
            return result;
        }
        System.out.println(messages.getString("no_image_open"));
        return result;
    }

    /**
     * Method to check if the criteria for applying filters has been met i.e
     * image open and filter pipeline not full
     *
     * @param name
     * @param filters
     * @return
     */
    public boolean canApplyFilter(String name, Filters filters) {
        return imageOpenCheck(name) && !filters.filterPipelineExceeded();
    }

    /**
     * "get" was entered, want to retrieve and image from the cache
     *
     * @param command
     * @return 
     */
    public EditableImage getFromCache(Command command) {

        if (!command.hasSecondWord()) {
            System.out.println(messages.getString("get_what"));
            return null;
        }
        else {
            if (!cache.isEmpty()) {
                if(cache.containsImg(command.getSecondWord())){
                    name = command.getSecondWord();
                    edtImg = cache.loadCached(name);
                    return edtImg;
                }
                else{
                    System.out.println("Image " + command.getSecondWord() + " not found in cache.");
                }
            }
            else{
                System.out.println(messages.getString("error_cache_empty"));
                return null;        
            }
            return null;
        }
    }

    /**
     * Method to undo last filter
     */
    public void undoLastFilter() {
        for(int i = 3; i > -1; i-- ){
            if("rot90".equals(filters.getFilters().get(i))){
                for(int j = 0; j < 4; j++){
                    filters.getFilters().set(j, null);
                }
                rot90();
                rot90();
                rot90();
                for(int j = 0; j < 4; j++){
                    filters.getFilters().set(j, null);
                }
            }
            else {
                System.out.println("Filter not undoable.");
            }
        }
        System.out.println("No undoable filters.");
    }

    /**
     * Check if filter is undoable
     *
     * @param filter
     * @return
     */
    public boolean isUndoable(String filter) {
        if (filter != null) {
            if (filter.equals(messages.getString("mono"))) {
                System.out.println(messages.getString("cannot_undo_filter"));
                return false;
            }
        }
        System.out.println(messages.getString("filter_undone"));
        return true;
    }
    
    public String returnImgName(){
        return name;
    }
    
    public Filters getFilters(){
        return filters;
    }
    
    public ColorImage returnCurrentImage(){
        return currentImage;
    }
    
    public EditableImage returnEdtImg(){
        return edtImg;
    }
    
    public void setEdtImg(EditableImage img){
        edtImg = img;
    }
    
    public Cache returnCache(){
        return cache;
    }

}
