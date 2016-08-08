/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Images;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author James
 */
public class ColorImageTest {
    
    public ColorImageTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of setPixel method, of class ColorImage.
     */
    @Test(expected = NullPointerException.class)
    public void testSetPixelShouldThrowExceptionWhenSetPixelToColorNull() {
        System.out.println("Testing method setPixel for NullPointerException");
        int x = 0;
        int y = 0;
        Color col = null;
        ColorImage instance = null;
        instance.setPixel(x, y, col);
    }

    /**
     * Test of getPixel method, of class ColorImage.
     */
    @Test(expected = NullPointerException.class)
    public void testGetPixelShouldThrowNullPointExceptionIfPixelColorIsNull() {
        System.out.println("Testing method getPixel for NullPointerException");
        int x = 0;
        int y = 0;
        ColorImage instance = null;
        Color expResult = null;
        Color result = instance.getPixel(x, y);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of setPixel method, of class ColorImage.
     */
    @Test
    public void testSetPixelShouldSucceedWhenSettingPixelValueToPositiveIntegerBetween() {
        System.out.println("Testing method setPixel with r,g,b values that are positive integers between 0 and 255");
        
        int r = 0;
        int g = 125;
        int b = 255;
        
        int x = 0;
        int y = 0;
        
        int col = r | g | b;
        
        BufferedImage buffImg = new BufferedImage(10, 10, (10 | 10 | 10));
        
        ColorImage instance = new ColorImage(buffImg);
        
        instance.setRGB(x, y, col);
    }
    
    /**
    * Test of setPixel method, of class ColorImage.
    */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPixelShouldThrowOutoFBoundsExceptionForRGBValuesNotInRange0To255() {
        System.out.println("Testing method setPixel with r,g,b values that are positive integers above 255");

        int r = 0;
        int g = 0;
        int b = 300;

        int x = 0;
        int y = 0;

        int col = 0;
        
        try{
            col = r | g | b;
            System.out.println(col); //doesn't throw exception here though, does something weird with the values provided.
            fail("No exception thrown. Test fails.");
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        BufferedImage buffImg = new BufferedImage(10, 10, (10 | 10 | 10));

        ColorImage instance = new ColorImage(buffImg);

        instance.setRGB(x, y, col);
        
        System.out.println(instance.getPixel(0,0));
    }   
    
}
