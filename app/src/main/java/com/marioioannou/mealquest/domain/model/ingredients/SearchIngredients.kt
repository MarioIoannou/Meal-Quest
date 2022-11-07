package com.marioioannou.mealquest.domain.model.ingredients


import com.google.gson.annotations.SerializedName

data class SearchIngredients(
    @SerializedName("number")
    val number: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("results")
    val results: List<Ingredient>,
    @SerializedName("totalResults")
    val totalResults: Int
)