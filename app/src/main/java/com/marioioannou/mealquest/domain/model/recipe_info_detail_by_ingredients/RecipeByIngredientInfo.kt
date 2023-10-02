package com.marioioannou.mealquest.domain.model.recipe_info_detail_by_ingredients


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeByIngredientInfo(

    @SerializedName("id")
    val id: Int?,
    @SerializedName("sourceName")
    val sourceName: String?,
    @SerializedName("sourceUrl")
    val sourceUrl: String?,
    @SerializedName("spoonacularSourceUrl")
    val spoonacularSourceUrl: String?,
    @SerializedName("title")
    val title: String?

):Parcelable