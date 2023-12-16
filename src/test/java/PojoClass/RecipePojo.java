package PojoClass;

import java.util.List;

public class RecipePojo {

	String recipeID;
	String recipeName;
	String recipeCategory;
	String foodCategory;
	List<String> ingredients;
	String preparationTime;
	String cookingTime;
	String preparationMethod;
	List<String> nutrientValues;

	 String morbidConditions;
	String recipeURL;

	public List<String> getNutrientValues() {
		return nutrientValues;
	}

	public void setNutrientValues(List<String> nutrientValues) {
		this.nutrientValues = nutrientValues;
	}

	public String getRecipeID() {
		return recipeID;
	}

	public void setRecipeID(String recipeID) {
		this.recipeID = recipeID;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public String getRecipeCategory() {
		return recipeCategory;
	}
	public void setRecipeCategory(String recipeCategory) {
		this.recipeCategory = recipeCategory;
	}
	public String getFoodCategory() {
		return foodCategory;
	}
	public void setFoodCategory(String foodCategory) {
		this.foodCategory = foodCategory;
	}

	public String getPreparationTime() {
		return preparationTime;
	}
	public void setPreparationTime(String preparationTime) {
		this.preparationTime = preparationTime;
	}
	public String getCookingTime() {
		return cookingTime;
	}
	public void setCookingTime(String cookingTime) {
		this.cookingTime = cookingTime;
	}
	public String getPreparationMethod() {
		return preparationMethod;
	}
	public void setPreparationMethod(String preparationMethod) {
		this.preparationMethod = preparationMethod;
	}

	public String getMorbidConditions() {
		return morbidConditions;
	}
	public void setMorbidConditions(String morbidConditions) {
		this.morbidConditions = morbidConditions;
	}
	public String getRecipeURL() {
		return recipeURL;
	}

	public void setRecipeURL(String recipeURL) {
		this.recipeURL = recipeURL;
	}

}
