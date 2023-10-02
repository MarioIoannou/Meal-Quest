package com.marioioannou.mealquest.domain.repository

import com.marioioannou.mealquest.domain.api.FoodRecipesApi
import com.marioioannou.mealquest.domain.model.factJoke.FactJoke
import com.marioioannou.mealquest.domain.model.ingredients.SearchIngredients
import com.marioioannou.mealquest.domain.model.randomRecipes.RandomFoodRecipe
import com.marioioannou.mealquest.domain.model.recipe_info_detail_by_ingredients.RecipeByIngredientInfo
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

    suspend fun getRandomRecipes(queries: Map<String,String>) : Response<RandomFoodRecipe>{
        return foodRecipesApi.getRandomRecipes(queries)
    }

    suspend fun getRecipesInformation(id: String) : Response<RecipeByIngredientInfo>{
        return foodRecipesApi.getRecipeInformation(id)
    }

    suspend fun getFoodFact() : Response<FactJoke>{
        return foodRecipesApi.getFoodFact()
    }

    suspend fun getFoodJoke() : Response<FactJoke>{
        return foodRecipesApi.getFoodJoke()
    }
}