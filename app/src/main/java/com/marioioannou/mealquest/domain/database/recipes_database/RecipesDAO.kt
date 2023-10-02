package com.marioioannou.mealquest.domain.database.recipes_database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.marioioannou.mealquest.domain.database.recipes_database.entities.FavouritesEntity
import com.marioioannou.mealquest.domain.database.recipes_database.entities.RandomRecipesEntity
import com.marioioannou.mealquest.domain.database.recipes_database.entities.RecipesEntity
import com.marioioannou.mealquest.domain.model.ingredients.Ingredient
import com.marioioannou.mealquest.domain.model.randomRecipes.RandomFoodRecipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDAO {

    // Recipes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    // Random Recipes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRandomRecipes(randomRecipesEntity: RandomRecipesEntity)

    @Query("SELECT * FROM random_recipes_table ORDER BY id ASC")
    fun readRandomRecipes(): Flow<List<RandomRecipesEntity>>

    // Ingredients
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Query("SELECT * FROM ingredients_table ORDER BY id ASC")
    fun readIngredients(): LiveData<List<Ingredient>>

    @Query("SELECT Name FROM ingredients_table")
    fun readIngredientsName(): LiveData<List<String>>

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)

    @Query("DELETE FROM ingredients_table")
    suspend fun deleteAllIngredients()

    //Favorite Recipes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoritesEntity: FavouritesEntity)

    @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes(): Flow<List<FavouritesEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoritesEntity: FavouritesEntity)

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()


    //Food-Fact
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertFoodFact(foodFactEntity: FoodFactEntity)
//
//    @Query("SELECT * FROM food_fact_table ORDER BY id ASC")
//    fun readFoodFact(): Flow<List<FoodFactEntity>>



}