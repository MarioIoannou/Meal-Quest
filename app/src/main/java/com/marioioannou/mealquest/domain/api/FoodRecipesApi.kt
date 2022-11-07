package com.marioioannou.mealquest.domain.api

import com.marioioannou.mealquest.BuildConfig
import com.marioioannou.mealquest.domain.model.ingredients.SearchIngredients
import com.marioioannou.mealquest.domain.model.recipes.FoodRecipe
import com.marioioannou.mealquest.domain.model.recipes_by_ingredients.RecipesByIngredients
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipesApi {

    @GET("/recipes/complexSearch")
     suspend fun getRecipes(
        @QueryMap queries: Map<String,String>
     ): Response<FoodRecipe>

     @GET("/food/ingredients/search")
     suspend fun getSearchedIngredients(
         @QueryMap ingredientQuery: Map<String,String>
     ):Response<SearchIngredients>

     @GET("/recipes/findByIngredients")
     suspend fun getRecipesByIngredients(
         @QueryMap recipesByIngredientsQuery: Map<String,String>
     ):Response<RecipesByIngredients>
}