package com.marioioannou.mealquest.domain.model.recipes


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Step(
    @SerializedName("number")
    val number: Int?,
    @SerializedName("step")
    val step: String?
):Parcelable