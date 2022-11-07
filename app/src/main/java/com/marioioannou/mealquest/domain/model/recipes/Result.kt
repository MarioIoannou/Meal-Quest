package com.marioioannou.mealquest.domain.model.recipes


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Result(
    @SerializedName("aggregateLikes")
    val aggregateLikes: Int?,
    @SerializedName("cheap")
    val cheap: Boolean?,
    @SerializedName("cookingMinutes")
    val cookingMinutes: Int?,
    @SerializedName("creditsText")
    val creditsText: String?,
    @SerializedName("cuisines")
    val cuisines: List<String>?,
    @SerializedName("dairyFree")
    val dairyFree: Boolean?,
    @SerializedName("extendedIngredients")
    val extendedIngredients: @RawValue List<ExtendedIngredient>?,
    @SerializedName("gaps")
    val gaps: String?,
    @SerializedName("glutenFree")
    val glutenFree: Boolean?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("imageType")
    val imageType: String?,
    @SerializedName("license")
    val license: String?,
    @SerializedName("likes")
    val likes: Int?,
    @SerializedName("lowFodmap")
    val lowFodmap: Boolean?,
    @SerializedName("missedIngredientCount")
    val missedIngredientCount: Int?,
    @SerializedName("preparationMinutes")
    val preparationMinutes: Int?,
    @SerializedName("pricePerServing")
    val pricePerServing: Double?,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int?,
    @SerializedName("servings")
    val servings: Int?,
    @SerializedName("sourceName")
    val sourceName: String?,
    @SerializedName("sourceUrl")
    val sourceUrl: String?,
    @SerializedName("spoonacularSourceUrl")
    val spoonacularSourceUrl: String?,
    @SerializedName("summary")
    val summary: String?,
    @SerializedName("sustainable")
    val sustainable: Boolean?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("usedIngredientCount")
    val usedIngredientCount: Int?,
    @SerializedName("vegan")
    val vegan: Boolean?,
    @SerializedName("vegetarian")
    val vegetarian: Boolean?,
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean?,
    @SerializedName("veryPopular")
    val veryPopular: Boolean?,
    @SerializedName("weightWatcherSmartPoints")
    val weightWatcherSmartPoints: Int?
):Parcelable