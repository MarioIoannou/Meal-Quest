package com.marioioannou.mealquest.utils

import com.marioioannou.mealquest.BuildConfig


object Constants {

    const val BASE_URL = "https://api.spoonacular.com/"
    const val API_KEY = BuildConfig.API_KEY

    const val PREFERENCES_BACK_ONLINE = "backOnline"

    const val RECIPE_RESULT_KEY = "recipeBundle"

    // API Queries - Recipes
    const val QUERY_NUMBER = "number"
    const val QUERY_API_KEY = "apiKey"
    const val QUERY_MEAL_TYPE = "type"
    const val QUERY_CUISINE = "cuisine"
    const val QUERY_DIET = "diet"
    const val QUERY_ADD_RECIPE_INFO = "addRecipeInformation"
    const val QUERY_FILL_INGREDIENTS = "fillIngredients"

    // API Queries - Recipes
    const val QUERY_INGREDIENT = "query"

    // API Queries - Recipes By Ingredients
    const val QUERY_RECIPE_BY_INGREDIENTS_INGREDIENTS = "ingredients"
    const val QUERY_RECIPE_BY_INGREDIENTS_API_KEY = "apiKey"
    const val QUERY_RECIPE_BY_INGREDIENTS_NUMBER = "number"
    const val QUERY_RECIPE_BY_INGREDIENTS_IGNORE_PANTRY = "ignorePantry"

    const val PREFERENCES_INGREDIENTS_NAME = "Ingredients"

//    @Query("ingredients") ingredients: String,
//    @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
//    @Query("number") recipesNumber : Int,
//    @Query("ignorePantry") ignorePantry: Boolean

    /*queries["number"] = "50"
    queries["apiKey"] = Constants.API_KEY
    queries["type"] = "main"
    queries["diet"] = "vegan"
    queries["addRecipeInformation"] = "true"
    queries["fillIngredients"] = "true"*/

    //Room
    const val DATABASE_NAME = "recipes_database"
    const val RECIPES_TABLE = "recipes_table"
    const val INGREDIENTS_TABLE = "ingredients_table"

    //Bottom Sheet and Preferences
    const val DEFAULT_RECIPES_NUMBER = "50"
    const val DEFAULT_MEAL_TYPE = "main course"
    const val DEFAULT_CUISINE_TYPE = "all"
    const val DEFAULT_DIET_TYPE = "all"

    const val PREFERENCES_NAME = "meal_preferences"
    const val PREFERENCES_MEAL_TYPE = "mealType"
    const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
    const val PREFERENCES_DIET_TYPE = "dietType"
    const val PREFERENCES_DIET_TYPE_ID= "dietTypeId"
    const val PREFERENCES_CUISINE_TYPE = "cuisineType"
    const val PREFERENCES_CUISINE_TYPE_ID= "cuisineIdType"
    //const val DEFAULT_ = ""
    //const val DEFAULT_ = ""
}