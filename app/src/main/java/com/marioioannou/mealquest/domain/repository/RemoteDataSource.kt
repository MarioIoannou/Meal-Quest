package com.marioioannou.mealquest.domain.repository

import com.marioioannou.mealquest.domain.api.FoodRecipesApi
import com.marioioannou.mealquest.domain.model.ingredients.SearchIngredients
import com.marioioannou.mealquest.domain.model.recipes.FoodRecipe
import com.marioioannou.mealquest.domain.model.recipes_by_ingredients.RecipesByIngredients
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {

    suspend fun getRecipes(queries: Map<String,String>) : Response<FoodRecipe>{
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun getSearchedIngredients(queries: Map<String, String>) : Response<SearchIngredients>{
        return foodRecipesApi.getSearchedIngredients(queries)
    }

    suspend fun getRecipesByIngredients(queries: Map<String, String>) : Response<RecipesByIngredients>{
        return foodRecipesApi.getRecipesByIngredients(queries)
    }
}