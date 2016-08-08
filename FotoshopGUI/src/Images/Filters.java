package Images;


import Locale.Language;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * This class contains information about the filters applied to an image
 * and the relevant operations, checks, accessors and mutators.
 * 
 * @author James Kite
 * @version 2015.11.09
 */
public class Filters {

    private final ArrayList<String> filters = new ArrayList();

    private final ResourceBundle messages = Language.getBundle();

    /**
     * Constructor initialises all filters to be empty
     */
    public Filters() {
        
        int i;
        for(i = 0 ; i < 4; i++){
            filters.add(i, null);
        }
    }

    /**
     * Method to print all the filters
     */
    public void printFilters() {
        String filterList = returnFilterList();

        if (filters.get(0) == null) {
            System.out.println(messages.getString("no_filters_active"));
        } else {
            
            System.out.println(filterList);
        }
    }

    /**
     * Accessor method to get all the filters
     *
     * @return
     */
    public ArrayList<String> getFilters() {
        return filters;
    }
    
    public String returnFilterList(){
        if(filters.get(0) == null){
            return null;
        }
        else {            
            String filterList = " ";
            
            String test = filters.stream()
                    .collect(Collectors.joining(" "));
            
            System.out.println(test);
            
            for (String filter : filters) {
                if (filter != null) {
                    filterList = filterList + " " + filter;
                }
            }
            return filterList;
        }
    }

    /**
     * Mutator to change a particular filter to provided string
     *
     * @param i
     * @param filterName
     */
    public void setFilter(int i, String filterName) {
        filters.set(i, filterName);
    }
    
    public String getFilter(int i){
        return filters.get(i);
    }

    /**
     * Append filter list
     *
     * @param filter
     */
    public void appendFilters(String filter) {
        
        for(int i = 0; i < filters.size() ; i++){
            if(filters.get(i) == null){
                filters.add(i, filter);
                return;
            }
        }
        
    }

    /**
     * Method to check if filter pipeline has been exceeded
     *
     * @return
     */
    public boolean filterPipelineExceeded() {
        if (filters.get(3) != null) {
            System.out.println(messages.getString("filter_pipeline"));
            return true;
        }
        return false;
    }

    /**
     * Return size of field filters
     *
     * @return
     */
    public int getNumFilters() {
        int size = filters.size();
        return size;
    }
    
    /**
     * Clear filters
     * @return 
     */
    public String returnRot90Filter(){
        String filt = filters.stream()
                .filter(s -> "rot90".equals(s))
                .findFirst()
                .get();
        return filt;
    }
}
