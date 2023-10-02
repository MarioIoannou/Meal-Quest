package com.marioioannou.mealquest.domain.api

import com.marioioannou.mealquest.BuildConfig
import com.marioioannou.mealquest.domain.model.factJoke.FactJoke
import com.marioioannou.mealquest.domain.model.ingredients.SearchIngredients
import com.marioioannou.mealquest.domain.model.randomRecipes.RandomFoodRecipe
import com.marioioannou.mealquest.domain.model.recipe_info_detail_by_ingredients.RecipeByIngredientInfo
import com.marioioannou.mealquest.domain.model.recipes.FoodRecipe
import com.marioioannou.mealquest.domain.model.recipes.Result
import com.marioioannou.mealquest.domain.model.recipes_by_ingredients.RecipesByIngredients
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("/recipes/random")
    suspend fun getRandomRecipes(
        @QueryMap queries: Map<String,String>
    ): Response<RandomFoodRecipe>

    @GET("/recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") recipeId : String,
        @Query("includeNutrition") includeNutrients: String = "false" ,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<RecipeByIngredientInfo>

    @GET("/food/jokes/random")
    suspend fun getFoodJoke(
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<FactJoke>

    @GET("/food/trivia/random")
    suspend fun getFoodFact(
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<FactJoke>
}