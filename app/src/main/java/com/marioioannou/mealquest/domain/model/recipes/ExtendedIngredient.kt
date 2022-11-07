package com.marioioannou.mealquest.domain.model.recipes


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExtendedIngredient(
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("consistency")
    val consistency: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("meta")
    val meta: List<String>?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("nameClean")
    val nameClean: String?,
    @SerializedName("unit")
    val unit: String?
):Parcelable