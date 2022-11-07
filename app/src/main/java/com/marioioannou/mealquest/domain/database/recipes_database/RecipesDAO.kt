package com.marioioannou.mealquest.domain.database.recipes_database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.marioioannou.mealquest.domain.model.ingredients.Ingredient
import com.marioioannou.mealquest.domain.model.ingredients.SearchIngredients
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDAO {

    // Recipes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    // Ingredients
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Query("SELECT * FROM ingredients_table ORDER BY id ASC")
    fun readIngredients(): LiveData<List<Ingredient>>

    @Query("SELECT Name FROM ingredients_table")
    fun readIngredientsName(): LiveData<List<String>>

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)
}