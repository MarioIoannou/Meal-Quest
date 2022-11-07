package com.marioioannou.mealquest.domain.datastore

data class MealDietAndCuisine(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int,
    val selectedCuisineType: String,
    val selectedCuisineTypeId: Int
)
