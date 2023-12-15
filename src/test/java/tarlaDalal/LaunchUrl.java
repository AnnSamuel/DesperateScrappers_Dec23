/**
 * 
 */
package tarlaDalal;

import org.testng.annotations.Test;
import org.testng.Assert;

import utilities.BaseClass;

/**
 * 
 */
public class LaunchUrl extends BaseClass {

//	@Test
	public void testTitle() {
		openPage();
		String pageTitle = title();
		Assert.assertEquals(pageTitle, "Indian Recipes | Indian Vegetarian Recipes | Top Indian Veg Dishes");
		//openRecipePage();
	}

}
