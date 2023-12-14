package tarlaDalal;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import PojoClass.RecipePojo;
import utilities.BaseClass;
import utilities.ExcelWriter;

public class Diabetes extends BaseClass {
	WebDriver driver;
//	 static String url = "https://www.tarladalal.com/recipes-for-indian-diabetic-recipes-370";

	@Test
	public void scrapeDiabeticRecipes() {
		openSpecificPage("recipes-for-indian-diabetic-recipes-370");
		String pageTitle = title();
		Assert.assertEquals(pageTitle, "Diabetic Recipes, 300 Indian Diabetic Recipes, Tarladalal.com");

		Document document = getJsoupDocument();
		List<Element> recipeCards = document.select("article.rcc_recipecard");
		List<RecipePojo> recipeList = new ArrayList<RecipePojo>();
		for (Element recipeCard : recipeCards) {

			RecipePojo recipe = new RecipePojo();
			Elements recipeIdEleList = recipeCard.select(".rcc_rcpno");
			Assert.assertTrue(recipeIdEleList.size() > 0, "recipeID should be available"); // chk of recipeid available
																							// to retreive recipe ID
			String recipeId = recipeIdEleList.get(0).firstElementChild().text();
//				System.out.println(recipeId);
			Elements recipeNameEleList = recipeCard.select(".rcc_rcpcore > span > a");
			Assert.assertTrue(recipeNameEleList.size() > 0, "recipeName should be available");
			String recipeURL = recipeNameEleList.get(0).attr("href");
			String recipeName = recipeNameEleList.get(0).text();
//				System.out.println(recipeURL);
//				System.out.println(recipeName);

//				openSpecificPage(recipeURL);
//				Document recipeDocument =  getJsoupDocument();

			// System.out.println(title());

			recipe.setRecipeID(recipeId);
			recipe.setRecipeName(recipeName);
			recipe.setRecipeURL(recipeURL);
			recipeList.add(recipe);
		}

		ExcelWriter.writeDataToExcel(recipeList, "target/recipies1.xlsx");

	}

}
