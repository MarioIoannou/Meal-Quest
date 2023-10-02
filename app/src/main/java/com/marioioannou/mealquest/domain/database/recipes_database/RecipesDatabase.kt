package com.marioioannou.mealquest.domain.database.recipes_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.marioioannou.mealquest.domain.database.recipes_database.entities.FavouritesEntity
import com.marioioannou.mealquest.domain.database.recipes_database.entities.RandomRecipesEntity
import com.marioioannou.mealquest.domain.database.recipes_database.entities.RecipesEntity
import com.marioioannou.mealquest.domain.model.ingredients.Ingredient

@Database(
    entities = [RecipesEntity::class , RandomRecipesEntity::class, Ingredient::class, FavouritesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDAO
}