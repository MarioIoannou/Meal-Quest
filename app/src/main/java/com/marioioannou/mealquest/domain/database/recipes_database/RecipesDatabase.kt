package com.marioioannou.mealquest.domain.database.recipes_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.marioioannou.mealquest.domain.model.ingredients.Ingredient

@Database(
    entities = [RecipesEntity::class , Ingredient::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDAO
}