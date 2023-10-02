package com.marioioannou.mealquest.domain.model.recipes


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class AnalyzedInstruction(
    @SerializedName("name")
    val name: String?,
    @SerializedName("steps")
    val steps:  @RawValue List<Step>?
):Parcelable