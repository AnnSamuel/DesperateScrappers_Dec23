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

	public class Diabetes extends BaseClass {
		List<RecipePojo> recipeList = new ArrayList<RecipePojo>();

		@Test
		public void diabetesScrape() {

			scrapeAcrossPages("/recipes-for-indian-diabetic-recipes-370?pageindex=24","Diabetes Elimination", "DiabeteSheet");

		}

		@Test
		public void hypertensionScrape() {
			
			scrapeAcrossPages("/recipes-for-high-blood-pressure-644?pageindex=5","Hypertension Elimination", "HypertensionSheet");
		}

		public void thyroidScrape() {
			
			scrapeAcrossPages("/recipes-for-hypothyroidism-veg-diet-indian-recipes-849","Hypothyroidism Elimination", "HyperthyroidismSheet");

		}

		public void pcos() {
			
			scrapeAcrossPages("/recipes-for-pcos-1040","PCOS Elimination", "pcosSheet");

		}

		// public method to do pagination and stuff
		// change signature
		public void scrapeAcrossPages(String siteUrl, String eliminationSheetName,String createSheetName) {
			openSpecificPage(siteUrl);
			boolean hasNextPageAvailable = true;
			do {
				Document document = getJsoupDocument();
				Elements nextPage = document.select("#pagination a.rescurrpg").next();
				scrapeAPage(document);
				hasNextPageAvailable = nextPage.size() > 0;
				if (hasNextPageAvailable) {
					System.out.println("Opening new page --->>> " + nextPage.get(0).attr("href"));
					openSpecificPage(nextPage.get(0).attr("href"));
				}
//				hasNextPage = document.select("id=)
			} while (hasNextPageAvailable);

			// List<String> eliminationList = Arrays.asList("mint", "quinoa"); // TODO -
			// remove after list is extracted from
			// excel

			List<RecipePojo> filteredList = CommonUtils.getFilteredList(recipeList, CommonUtils.getEliminationAddList(
					"src/test/resources/excelReader/Elimination&AddList.xlsx", eliminationSheetName, 0));

			//ExcelWriter.writeDataToExcelExistCheck(filteredList, "target/recipies.xlsx", createSheetName);	
			ExcelWriter.writeDataToExcel(filteredList, "/Users/Naveena/git/DesperateScrappers_23/src/test/java/utilities/recipes1.xlsx");
		}

		public void scrapeAPage(Document document) {

			List<Element> recipeCards = document.select("article.rcc_recipecard");

			for (Element recipeCard : recipeCards) {
				try {
					RecipePojo recipe = new RecipePojo();
					Elements recipeIdEleList = recipeCard.select(".rcc_rcpno");
					Assert.assertTrue(recipeIdEleList.size() > 0, "recipeID should be available"); // chk if recipeid
																									// available
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
					recipe.setMorbidConditions("diabetes");

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
