package com.marioioannou.mealquest.domain.database.recipes_database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marioioannou.mealquest.domain.model.recipes.FoodRecipe
import com.marioioannou.mealquest.utils.Constants


@Entity(tableName = Constants.RECIPES_TABLE)
data class RecipesEntity(
    var foodRecipe: FoodRecipe
){
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
    // RecipesEntity table will contain only one row for the recipe. Whenever
    //   we fetch a new data from API, we are going to replace all data from db with new data
}
