import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.*;

/**
 * Represents the mappings for a single category of items that should
 * be displayed
 * 
 * @author Catie Baker & Bonsen Yusuf
 *
 */
public class AACCategory implements AACPage {

	/*Fields */

	/**
     * The name of the category.
     */
    private String name; // The name of the category

    /**
     * The mappings from image locations to text for this category.
     */
    private AssociativeArray<String, String> items; // Map of image locations to text

	
	/**
	 * Creates a new empty category with the given name
	 * @param name the name of the category
	 */
	public AACCategory(String name) {
		this.name = name;
        this.items = new AssociativeArray<>();
	} // AACCategory(String)
	
    /*Public Methods */

	/**
	 * Adds the image location, text pairing to the category
	 * @param imageLoc the location of the image
	 * @param text the text that image should speak
	 */
	public void addItem(String imageLoc, String text) {
		try {
            items.set(imageLoc, text);
        } catch (Exception e) {
            // Handle exceptions like NullKeyException if needed
            System.err.println("Error adding item: " + e.getMessage());
        } // try/catch
	}//addItem(String imageLoc, String text)

	/**
	 * Returns an array of all the images in the category
	 * @return the array of image locations; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		Object[] keys = items.getKeys(); // Get keys as Object[]
    	String[] stringKeys = new String[keys.length]; // Create a String array
    	for (int i = 0; i < keys.length; i++) {
        	stringKeys[i] = (String) keys[i]; // Cast each key to String
    	} // for
    	return stringKeys;
	} //getImageLocs()

	/**
	 * Returns the name of the category
	 * @return the name of the category
	 */
	public String getCategory() {
		return name;
	}//getCategory()

	/**
	 * Returns the text associated with the given image in this category
	 * @param imageLoc the location of the image
	 * @return the text associated with the image
	 * @throws NoSuchElementException if the image provided is not in the current
	 * 		   category
	 */
	public String select(String imageLoc) {
		try {
            return items.get(imageLoc);
        } catch (KeyNotFoundException e) {
            throw new NoSuchElementException("Image not found: " + imageLoc);
        } // try/catch
	} //select()

	/**
	 * Determines if the provided images is stored in the category
	 * @param imageLoc the location of the category
	 * @return true if it is in the category, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return items.hasKey(imageLoc);
	} //hasImage()
} //AACCategory
