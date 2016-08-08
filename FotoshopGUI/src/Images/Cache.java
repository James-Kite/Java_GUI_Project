package Images;


import Locale.Language;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Cache of images containing info about the filters applied.
 *
 * @author James
 */
public class Cache {

    private final HashMap<String, EditableImage> cachedImages = new HashMap<>();
    private final ResourceBundle messages = Language.getBundle();

    /**
     * Constructor of class Cache
     */
    public Cache() {
        //some code pls
    }
    
    public HashMap<String, EditableImage> returnCache(){
        return cachedImages;
    }
    
    public ArrayList<String> returnCacheArray(){
        ArrayList<String> keyCollection = new ArrayList();
        keyCollection.addAll(cachedImages.keySet());
        return keyCollection;
    }

    /**
     * Method to add an image to the cache
     *
     * @param name
     * @param edtImg
     */
    public void addToCache(String name, EditableImage edtImg) {
        if(name != null){
            cachedImages.put(name, edtImg);
        }
        else {
            System.out.println("No image loaded to put in cache.");
        }
    }

    /**
     * Method to get an image from the cache
     *
     * @param name
     * @return
     */
    public EditableImage loadCached(String name) {
        return cachedImages.get(name);
    }

    /**
     * Method to check if cache is empty
     *
     * @return
     */
    public boolean isEmpty() {
        return cachedImages.isEmpty();
    }
    
    public boolean containsImg(String name){
        return cachedImages.containsKey(name);
    }

}
