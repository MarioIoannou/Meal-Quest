package com.marioioannou.mealquest.domain.model.recipes


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class FoodRecipe(
    @SerializedName("results")
    val results: List<Result>
)