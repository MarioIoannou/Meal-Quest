package com.marioioannou.mealquest.domain.database.recipes_database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marioioannou.mealquest.domain.model.recipes_by_ingredients.RecipesByIngredientsItem
import com.marioioannou.mealquest.utils.Constants.FAVORITE_RECIPES_TABLE

@Entity(
    tableName = FAVORITE_RECIPES_TABLE
)
class FavouritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: com.marioioannou.mealquest.domain.model.recipes.Result,
)

//class RecipesByIngredientsEntity(
//    @PrimaryKey(autoGenerate = true)
//    var id: Int,
//    var recipesByIngredient: RecipesByIngredientsItem
//)