package com.marioioannou.mealquest.domain.model.ingredients


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.marioioannou.mealquest.utils.Constants

@Entity(tableName = Constants.INGREDIENTS_TABLE)
data class Ingredient(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "Id")
    @SerializedName("id")
    val id: Int,
    @ColumnInfo(name = "Image")
    @SerializedName("image")
    val image: String,
    @ColumnInfo(name = "Name")
    @SerializedName("name")
    val name: String
)