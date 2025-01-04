import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;

/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * @author Catie Baker & Bonsen Yusuf
 *
 */
public class AACMappings implements AACPage {

	/*Fields */
	/**
     * Maps image locations on the home page to their respective categories.
     */
    private AssociativeArray<String, AACCategory> homeMappings;

    /**
     * The name of the currently selected catgory. 
     */
    private String currentCategory;
	
	/**
	 * Creates a set of mappings for the AAC based on the provided
	 * file. The file is read in to create categories and fill each
	 * of the categories with initial items. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) {
		homeMappings = new AssociativeArray<>();
        currentCategory = ""; // Start on the home page

        // Read from the file and populate the mappings
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            AACCategory currentCat = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(">")) {
                    // Add an item to the current category
                    String[] parts = line.substring(1).split(" ", 2);
                    currentCat.addItem(parts[0], parts[1]);
                } else {
                    // Create a new category
                    String[] parts = line.split(" ", 2);
                    currentCat = new AACCategory(parts[1]);
					try {
                      homeMappings.set(parts[0], currentCat);
					} catch (NullKeyException e) {
					  System.err.println("Error: Null key encountered while adding a category.");
					} //added try/catch because my set method in AssociativeArrays throws exception.
                } // if/else
            } // while
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } // try/catch
	} //AACMappings(String)
	
	/**
	 * Given the image location selected, it determines the action to be
	 * taken. This can be updating the information that should be displayed
	 * or returning text to be spoken. If the image provided is a category, 
	 * it updates the AAC's current category to be the category associated 
	 * with that image and returns the empty string. If the AAC is currently
	 * in a category and the image provided is in that category, it returns
	 * the text to be spoken.
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise
	 * it returns the empty string
	 * @throws NoSuchElementException if the image provided is not in the current 
	 * category
	 */
	public String select(String imageLoc) {
		// Check if the user is currently on the home page
    	if (currentCategory.isEmpty()) {
          // If the image does not correspond to a category on the home page, throw an exception
          if (!homeMappings.hasKey(imageLoc)) {
            throw new NoSuchElementException("Category image not found: " + imageLoc);
        }
          // Update the current category to the selected category and return an empty string
          currentCategory = imageLoc;
          return "";
    	} else {
          // The user is in a specific category, so we need to handle item selection
          try {
            // Retrieve the current category object from the home mappings
            AACCategory category = homeMappings.get(currentCategory); // May throw KeyNotFoundException
           
            // Use the current category to retrieve the text associated with the selected image
            return category.select(imageLoc);
        } catch (KeyNotFoundException e) {
            // If the current category is not found in the home mappings, throw a NoSuchElementException
            throw new NoSuchElementException("Key not found: " + currentCategory);
        }
      } // if/else
	}//select(String)
	
	/**
	 * Provides an array of all the images in the current category
	 * @return the array of images in the current category; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		if (currentCategory.isEmpty()) {
			// Convert Object[] to String[]
			Object[] keys = homeMappings.getKeys(); // Assuming getKeys() returns Object[]
			String[] stringKeys = new String[keys.length];
			for (int i = 0; i < keys.length; i++) {
				stringKeys[i] = (String) keys[i]; // Explicitly cast each element
			}
			return stringKeys;
		} else {
			try {
				// Delegate to the current category's getImageLocs method
				return homeMappings.get(currentCategory).getImageLocs();
			} catch (KeyNotFoundException e) {
				return new String[0];
			}
		} // if/else
	} //getImageLocs()
	
	/**
	 * Resets the current category of the AAC back to the default
	 * category
	 */
	public void reset() {
		currentCategory = "";
	} //reset()
	
	
	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * 
	 * @param filename the name of the file to write the
	 * AAC mapping to
	 */
	public void writeToFile(String filename) {
		try (FileWriter writer = new FileWriter(filename)) {
			for (String homeImage : homeMappings.getKeys()) {
				try {
					// Retrieve the category associated with the home page image
					AACCategory category = homeMappings.get(homeImage);
					writer.write(homeImage + " " + category.getCategory() + "\n");
	
					// Write all items in the category
					for (String itemImage : category.getImageLocs()) {
						writer.write(">" + itemImage + " " + category.select(itemImage) + "\n");
					} // for
				} catch (KeyNotFoundException e) {
					// Log an error if the key is missing
					System.err.println("Error: Key not found for home image: " + homeImage);
				} // try/catch
			} // for
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		} // try/catch
    } // writeToFile(String)	
	
	/**
	 * Adds the mapping to the current category (or the default category if
	 * that is the current category)
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 */
	@Override
	public void addItem(String imageLoc, String text) {
		if (currentCategory.isEmpty()) {
			// Add a new category on the home page
			AACCategory newCategory = new AACCategory(text);
			try {
				homeMappings.set(imageLoc, newCategory); // Handle NullKeyException
			} catch (NullKeyException e) {
				System.err.println("Error: Null key provided for image location.");
			}
		} else {
			// Add an item to the current category
			try {
				AACCategory category = homeMappings.get(currentCategory); // Handle KeyNotFoundException
				category.addItem(imageLoc, text);
			} catch (KeyNotFoundException e) {
				System.err.println("Error: Current category key not found in home mappings.");
			}
		} // if/else
	} //addItem()


	/**
	 * Gets the name of the current category
	 * @return returns the current category or the empty string if 
	 * on the default category
	 */
	@Override
	public String getCategory() {
		return currentCategory;
	} //getCatgory()

	/**
     * Determines if the provided image is a category image.
     *
     * @param imageLoc the location of the image
     * @return true if the image is a category, false otherwise
     */
    public boolean isCategory(String imageLoc) { // isCategory(String)
        return homeMappings.hasKey(imageLoc);
    } // isCategory(String)


	/**
	 * Determines if the provided image is in the set of images that
	 * can be displayed and false otherwise
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that
	 * can be displayed, false otherwise
	 */
	@Override
	public boolean hasImage(String imageLoc) {
		if (currentCategory.isEmpty()) {
			// Check if the image exists on the home page
			return homeMappings.hasKey(imageLoc);
		} else {
			try {
				// Check if the image exists in the current category
				AACCategory category = homeMappings.get(currentCategory);
				return category.hasImage(imageLoc);
			} catch (KeyNotFoundException e) {
				// If the current category key is missing, return false
				return false;
			} // try/catch
		} // if/else
	} //hasImage(string)
}
