package com.marioioannou.mealquest.domain.model.recipes_by_ingredients


import com.google.gson.annotations.SerializedName

data class RecipesByIngredientsItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("imageType")
    val imageType: String,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("missedIngredientCount")
    val missedIngredientCount: Int,
    @SerializedName("missedIngredients")
    val missedIngredients: List<MissedIngredient>,
    @SerializedName("title")
    val title: String,
    @SerializedName("unusedIngredients")
    val unusedIngredients: List<UnusedIngredient>,
    @SerializedName("usedIngredientCount")
    val usedIngredientCount: Int,
    @SerializedName("usedIngredients")
    val usedIngredients: List<UsedIngredient>
)