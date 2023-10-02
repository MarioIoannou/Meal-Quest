package com.marioioannou.mealquest.domain.model.randomRecipes

import com.google.gson.annotations.SerializedName
import com.marioioannou.mealquest.domain.model.recipes.Result

data class RandomFoodRecipe(
    @SerializedName("recipes")
    val results: List<Result>
)
