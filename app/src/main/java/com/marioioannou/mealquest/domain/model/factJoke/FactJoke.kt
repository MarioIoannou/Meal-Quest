package com.marioioannou.mealquest.domain.model.factJoke


import com.google.gson.annotations.SerializedName

data class FactJoke(
    @SerializedName("text")
    val text: String
)