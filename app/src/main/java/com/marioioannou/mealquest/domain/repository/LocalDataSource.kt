package com.marioioannou.mealquest.domain.repository

import androidx.lifecycle.LiveData
import com.marioioannou.mealquest.domain.database.recipes_database.RecipesDAO
import com.marioioannou.mealquest.domain.database.recipes_database.RecipesEntity
import com.marioioannou.mealquest.domain.model.ingredients.Ingredient
import com.marioioannou.mealquest.domain.model.ingredients.SearchIngredients
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDAO: RecipesDAO
) {

    // Recipes
    fun readDatabase() : Flow<List<RecipesEntity>> {
        return recipesDAO.readRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity){
        recipesDAO.insertRecipes(recipesEntity)
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
}