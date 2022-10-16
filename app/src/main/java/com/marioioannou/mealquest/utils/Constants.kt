package com.marioioannou.mealquest.utils

import com.marioioannou.mealquest.BuildConfig


object Constants {

    //https://api.coingecko.com/api/v3/coins/
    const val BASE_URL = "https://api.spoonacular.com/"
    const val API_KEY = BuildConfig.API_KEY

    // AOI Queries
    const val QUERY_NUMBER = "number"
    const val QUERY_API_KEY = "api_key"
    const val QUERY_TYPE = "type"
    const val QUERY_DIET = "diet"
    const val QUERY_ADD_RECIPE_INFO = "addRecipeInformation"
    const val QUERY_FILL_INGREDIENTS = "fillIngredients"

    //Room
    const val DATABASE_NAME = "recipes_database"
    const val RECIPES_TABLE = "recipes_table"

    //Bottom Sheet and Preferences
    const val DEFAULT_RECIPES_NUMBER = "50"
    const val DEFAULT_MEAL_TYPE = "main course"
    const val DEFAULT_DIET_TYPE = "gluten free"

    const val PREFERENCES_NAME = "meal_preferences"
    const val PREFERENCES_MEAL_TYPE = "mealType"
    const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
    const val PREFERENCES_DIET_TYPE = "dietType"
    const val PREFERENCES_DIET_TYPE_ID= "dietTypeId"
    //const val DEFAULT_ = ""
    //const val DEFAULT_ = ""
}