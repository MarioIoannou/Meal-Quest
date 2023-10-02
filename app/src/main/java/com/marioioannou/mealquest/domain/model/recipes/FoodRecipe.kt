package com.marioioannou.mealquest.domain.model.recipes


import com.google.gson.annotations.SerializedName

data class FoodRecipe(
    @SerializedName("results")
    val results: List<Result>
)