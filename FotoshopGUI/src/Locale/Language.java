package Locale;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *  Class to set language
 * @author James
 */
public class Language {
    
    private static String language = "en";
    private static String country = "GB";
    private static ResourceBundle bundle;
    private static Locale locale;
    private static String currentBundle;
    
    /**
     * Method to get language
     * @return 
     */
    public static String getLanguage() {
        return Language.language;
    }
    
    /**
     * Method to set language
     * @param language
     */
    public static void setLanguage(String language) {
        Language.language = language;
    }
    
    /**
     * Method to get country
     * @return 
     */
    public static String getCountry() {
        return Language.country;
    }
    
    /**
     * Method to set country
     * @param country
     */
    public static void setCountry(String country) {
        Language.country = country;
    }
    
    /**
     * Method to set resource bundle
     */
    public static void setBundle() {
        Language.bundle = ResourceBundle.getBundle(currentBundle, locale);
    }
    
    /**
     * Method to acquire resource bundle
     * @return 
     */
    public static ResourceBundle getBundle() {
        return Language.bundle;        
    }
    
    /**
     * Method to set locale
     */
    public static void setLocale() {
        locale = new Locale(language, country);
    }
    
    /**
     * Method to select bundle
     * @param bundle
     */
    public static void selectBundle(String bundle) {
    
        currentBundle = bundle;
    
    }
    
}
