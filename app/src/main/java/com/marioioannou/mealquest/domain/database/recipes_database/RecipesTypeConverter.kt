package com.marioioannou.mealquest.domain.database.recipes_database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marioioannou.mealquest.domain.model.randomRecipes.RandomFoodRecipe
import com.marioioannou.mealquest.domain.model.recipes.FoodRecipe
import com.marioioannou.mealquest.domain.model.recipes.Result
import com.marioioannou.mealquest.domain.model.recipes_by_ingredients.RecipesByIngredientsItem

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe):String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipe {
        val listType = object : TypeToken<FoodRecipe>(){}.type
        return gson.fromJson(data,listType)
    }

    @TypeConverter
    fun randomRecipeToString(randomFoodRecipe: RandomFoodRecipe):String {
        return gson.toJson(randomFoodRecipe)
    }

    @TypeConverter
    fun stringToRandomRecipe(data: String): RandomFoodRecipe {
        val listType = object : TypeToken<RandomFoodRecipe>(){}.type
        return gson.fromJson(data,listType)
    }

    @TypeConverter
    fun resultToString(result: Result):String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String): Result {
        val listType = object : TypeToken<Result>(){}.type
        return gson.fromJson(data,listType)
    }

    @TypeConverter
    fun recipesByIngredientToString(recipesByIngredient: RecipesByIngredientsItem):String {
        return gson.toJson(recipesByIngredient)
    }

    @TypeConverter
    fun stringToRecipesByIngredient(data: String): RecipesByIngredientsItem {
        val listType = object : TypeToken<RecipesByIngredientsItem>(){}.type
        return gson.fromJson(data,listType)
    }
}