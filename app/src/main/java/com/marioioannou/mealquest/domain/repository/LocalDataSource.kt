package com.marioioannou.mealquest.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.marioioannou.mealquest.domain.database.recipes_database.RecipesDAO
import com.marioioannou.mealquest.domain.database.recipes_database.entities.FavouritesEntity
import com.marioioannou.mealquest.domain.database.recipes_database.entities.RandomRecipesEntity
import com.marioioannou.mealquest.domain.database.recipes_database.entities.RecipesEntity
import com.marioioannou.mealquest.domain.model.ingredients.Ingredient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDAO: RecipesDAO
) {

    // Recipes
    fun readRecipes() : Flow<List<RecipesEntity>> {
        return recipesDAO.readRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity){
        Log.e("LocalDataSource","insertRecipes()")
        recipesDAO.insertRecipes(recipesEntity)
    }

    // Random Recipes
    fun readRandomRecipes() : Flow<List<RandomRecipesEntity>> {
        return recipesDAO.readRandomRecipes()
    }

    suspend fun insertRandomRecipes(randomRecipesEntity: RandomRecipesEntity){
        Log.e("LocalDataSource","insertRandomRecipes()")
        recipesDAO.insertRandomRecipes(randomRecipesEntity)
    }

    // Ingredients
    fun readIngredients() : LiveData<List<Ingredient>>{
        return recipesDAO.readIngredients()
    }

    fun readIngredientsName() : LiveData<List<String>>{
        return recipesDAO.readIngredientsName()
    }

    suspend fun insertIngredient(ingredient: Ingredient){
        recipesDAO.insertIngredient(ingredient)
    }

    suspend fun deleteIngredient(ingredient: Ingredient){
        recipesDAO.deleteIngredient(ingredient)
    }

    suspend fun deleteAllIngredients(){
        recipesDAO.deleteAllIngredients()
    }

    //Favorite Recipes
    fun readFavoriteRecipes() : Flow<List<FavouritesEntity>> {
        return recipesDAO.readFavoriteRecipes()
    }

    suspend fun insertFavoriteRecipe(favouritesEntity: FavouritesEntity){
        recipesDAO.insertFavoriteRecipe(favouritesEntity)
    }

    suspend fun deleteFavoriteRecipe(favouritesEntity: FavouritesEntity){
        recipesDAO.deleteFavoriteRecipe(favouritesEntity)
    }
    suspend fun deleteAllFavouriteRecipes(){
        recipesDAO.deleteAllFavoriteRecipes()
    }
}