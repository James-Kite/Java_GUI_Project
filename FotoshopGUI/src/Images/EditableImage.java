/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Images;

/**
 *
 * @author James
 */
public class EditableImage {
    
    private final ColorImage colImage;
    private final Filters filters;
    
    public EditableImage(ColorImage img, Filters filts){
        colImage = img;
        filters = filts;
    }
    
    public ColorImage returnColImage(){
        if(colImage != null){
            return colImage;
        }
        else{
            System.out.println("EditableImage colImage null.");
            return null;
        }
    }
    
    public Filters returnFilters(){
        if(filters != null){
            return filters;
        }
        else{
            System.out.println("EditableImage filters null");
            return null;
        }
    }
    
}
