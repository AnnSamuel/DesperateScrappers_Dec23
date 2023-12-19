package tarlaDalal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.UnhandledAlertException;
import org.testng.Assert;
import org.testng.annotations.Test;

import PojoClass.RecipePojo;
import utilities.BaseClass;
import utilities.CommonUtils;
import utilities.ExcelWriter;

public class DataScrapping extends BaseClass {
	List<RecipePojo> recipeList = new ArrayList<RecipePojo>();

	@Test
	public void diabetesScrape() {
		recipeList.clear();
		scrapeAcrossPages("DIABETES");
	}

	@Test
	public void hypertensionScrape() {
		recipeList.clear();
		scrapeAcrossPages("HYPERTENSION");
	}

	@Test
	public void thyroidScrape() {
		recipeList.clear();
		scrapeAcrossPages("HYPOTHYROIDISM");

	}

	@Test
	public void pcos() {
		recipeList.clear();
		scrapeAcrossPages("PCOS");

	}

	// public method to do pagination and stuff

	public void scrapeAcrossPages(String type) {
		openSpecificPage(CommonUtils.getURL(type));
		boolean hasNextPageAvailable = true;
		do {
			Document document = getJsoupDocument();

			scrapeAPage(document, type);
			Elements nextPage = document.select("#pagination a.rescurrpg").next();
			hasNextPageAvailable = nextPage.size() > 0;
			if (hasNextPageAvailable) {
				System.out.println("Opening new page --->>> " + nextPage.get(0).attr("href"));
				openSpecificPage(nextPage.get(0).attr("href"));
			}

//			hasNextPage = document.select("id=)
		} while (hasNextPageAvailable);

		// List<String> eliminationList = Arrays.asList("mint", "quinoa"); // TODO -
		// remove after list is extracted from
		// excel

		List<RecipePojo> filteredList = CommonUtils.getFilteredList(recipeList,
				CommonUtils.getEliminationList("src/test/resources/excelReader/EliminationList.xlsx",
						CommonUtils.getEliminationSheetName(type), 0));

		ExcelWriter.writeDataToExcel(filteredList, "target/recipies.xlsx", CommonUtils.getSheetName(type));
	}

	public void scrapeAPage(Document document, String type) {

		List<Element> recipeCards = document.select("article.rcc_recipecard");

		for (Element recipeCard : recipeCards) {
			try {
				RecipePojo recipe = new RecipePojo();
				Elements recipeIdEleList = recipeCard.select(".rcc_rcpno");
				Assert.assertTrue(recipeIdEleList.size() > 0, "recipeID should be available"); // chk if recipeid
																								// available
//				12345678910111213																				// to retreive recipe ID
//				RECEPIE# 1 2 3 4 #
				String recipeId = recipeIdEleList.get(0).firstElementChild().text();
				int firstOccurance = recipeId.indexOf(" ") + 1;
				recipeId = recipeId.substring(firstOccurance, recipeId.indexOf(" ", firstOccurance));
//			System.out.println(recipeId);
				Elements recipeNameEleList = recipeCard.select(".rcc_rcpcore > span > a");
				Assert.assertTrue(recipeNameEleList.size() > 0, "recipeName should be available");
				String recipeURL = recipeNameEleList.get(0).attr("href");
				String recipeName = recipeNameEleList.get(0).text();
//			System.out.println(recipeURL);
//			System.out.println(recipeName);

				recipe.setRecipeID(recipeId);
				recipe.setRecipeName(recipeName);
				recipe.setRecipeURL(recipeURL);

				openSpecificPage("/" + recipeURL);
				Document recipeDocument = getJsoupDocument();
				System.out.println(title() + " -->> " + recipeURL);

				Elements cookingTimeElements = recipeDocument.select("time[itemprop=cookTime]");
				if (cookingTimeElements.size() > 0) {
					String cookingTime = cookingTimeElements.get(0).text();
					recipe.setCookingTime(cookingTime);
				}
				Elements prepTimeElements = recipeDocument.select("time[itemprop=prepTime]");
				if (prepTimeElements.size() > 0) {
					String prepTime = prepTimeElements.get(0).text();
					recipe.setPreparationTime(prepTime);
				}

				String prepMethod = recipeDocument.select("#recipe_small_steps  ol[itemprop='recipeInstructions'] li")
						.text();
				Elements ingredientListofElements = recipeDocument.select("span[itemprop=recipeIngredient]");
				List<String> ingredientList = new ArrayList<String>();
				for (Element ingredientEle : ingredientListofElements) {
					String ingredient = ingredientEle.text();
					ingredientList.add(ingredient);
				}

				recipe.setIngredients(ingredientList);

				recipe.setPreparationMethod(prepMethod);
				recipe.setMorbidConditions(CommonUtils.getMorbidConditions(type));

				Elements nutrientRowListEle = recipeDocument.select("table[id=rcpnutrients] tr");
				List<String> nutrientList = new ArrayList<String>();
				for (Element nutrientRow : nutrientRowListEle) {
					String nutrientValue = nutrientRow.text();
					nutrientList.add(nutrientValue);
				}
				recipe.setNutrientValues(nutrientList);

				String recipeCategory = recipeDocument.select("div[id=recipe_tags]").text();

				List<String> recipeCategoryList = Arrays.asList("Breakfast", "Lunch", "Snack", "Dinner");
				String addRecipeCategory = "";
				for (String availableRecipeCategory : recipeCategoryList) {

					boolean chkRecipeCategory = recipeCategory.contains(availableRecipeCategory);
					if (chkRecipeCategory == true) {
						addRecipeCategory = addRecipeCategory + availableRecipeCategory + "/";
						// recipe.setRecipeCategory(availableRecipeCategory);
					}
				}
				recipe.setRecipeCategory(addRecipeCategory);

				recipeList.add(recipe);
			} catch (UnhandledAlertException e) {
				e.printStackTrace();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
