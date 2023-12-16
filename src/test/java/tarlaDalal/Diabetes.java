package tarlaDalal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import PojoClass.RecipePojo;
import utilities.BaseClass;
import utilities.CommonUtils;
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
			Assert.assertTrue(recipeIdEleList.size() > 0, "recipeID should be available"); // chk if recipeid available
																							// to retreive recipe ID
			String recipeId = recipeIdEleList.get(0).firstElementChild().text();
//				System.out.println(recipeId);
			Elements recipeNameEleList = recipeCard.select(".rcc_rcpcore > span > a");
			Assert.assertTrue(recipeNameEleList.size() > 0, "recipeName should be available");
			String recipeURL = recipeNameEleList.get(0).attr("href");
			String recipeName = recipeNameEleList.get(0).text();
//				System.out.println(recipeURL);
//				System.out.println(recipeName);

			recipe.setRecipeID(recipeId);
			recipe.setRecipeName(recipeName);
			recipe.setRecipeURL(recipeURL);

			openSpecificPage(recipeURL);
			Document recipeDocument = getJsoupDocument();
			// System.out.println(title());
			String cookingTime = recipeDocument.select("time[itemprop=prepTime]").get(0).text();
			String prepTime = recipeDocument.select("time[itemprop=cookTime]").get(0).text();
			String prepMethod = recipeDocument.select("#recipe_small_steps  ol[itemprop='recipeInstructions'] li")
					.text();
			Elements ingredientListofElements = recipeDocument.select("span[itemprop=recipeIngredient]");
			List<String> ingredientList = new ArrayList<String>();
			for (Element ingredientEle : ingredientListofElements) {
				String ingredient = ingredientEle.text();
				ingredientList.add(ingredient);
			}

			recipe.setIngredients(ingredientList);
			recipe.setCookingTime(cookingTime);
			recipe.setPreparationTime(prepTime);
			recipe.setPreparationMethod(prepMethod);
			recipe.setMorbidConditions("diabetes");

			Elements nutrientRowListEle = recipeDocument.select("table[id=rcpnutrients] tr");
			List<String> nutrientList = new ArrayList<String>();
			for (Element nutrientRow : nutrientRowListEle) {
				String nutrientValue = nutrientRow.text();
				nutrientList.add(nutrientValue);
			}
			recipe.setNutrientValues(nutrientList);

			String recipeCategory = recipeDocument.select("div[id=recipe_tags]").text();

			List<String> recipeCategoryList= Arrays.asList("Breakfast", "Lunch", "Snack", "Dinner"); 
			String addRecipeCategory =  "";
			for (String availableRecipeCategory: recipeCategoryList)
			{
				
				boolean chkRecipeCategory = recipeCategory.contains(availableRecipeCategory);
				if (chkRecipeCategory== true)
				{
					addRecipeCategory = addRecipeCategory + availableRecipeCategory + "/";
					//recipe.setRecipeCategory(availableRecipeCategory);
				}
			}
			recipe.setRecipeCategory(addRecipeCategory);
			

			recipeList.add(recipe);
		}
		// List<String> eliminationList = Arrays.asList("mint", "quinoa"); // TODO -
		// remove after list is extracted from
		// excel

		List<RecipePojo> filteredList = CommonUtils.getFilteredList(recipeList, CommonUtils.getEliminationAddList(
				"src/test/resources/excelReader/Elimination&AddList.xlsx", "Diabetes Elimination", 0));

		ExcelWriter.writeDataToExcel(filteredList, "target/recipies1.xlsx");

	}

}
