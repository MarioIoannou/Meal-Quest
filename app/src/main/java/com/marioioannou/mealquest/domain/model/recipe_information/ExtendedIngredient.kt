package com.marioioannou.mealquest.domain.model.recipe_information


import com.google.gson.annotations.SerializedName

data class ExtendedIngredient(
    @SerializedName("aisle")
    val aisle: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("consistency")
    val consistency: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("meta")
    val meta: List<String>,
    @SerializedName("name")
    val name: String,
    @SerializedName("nameClean")
    val nameClean: String,
    @SerializedName("original")
    val original: String,
    @SerializedName("originalName")
    val originalName: String,
    @SerializedName("unit")
    val unit: String
)