package com.marioioannou.mealquest.ui.fragments.recipesfragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.marioioannou.mealquest.domain.datastore.DataStoreRepository
import com.marioioannou.mealquest.domain.datastore.MealAndDietType
import com.marioioannou.mealquest.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class RecipesViewModel(
    application: Application,
    private val dataStoreRepository: DataStoreRepository,
) : AndroidViewModel(application) {

    private lateinit var mealAndDiet: MealAndDietType

    var networkStatus = false
    var backOnline = false

    private var mealType = Constants.DEFAULT_MEAL_TYPE
    private var dietType = Constants.DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    //val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

//    fun saveMealAndDietType(
//        mealType: String,
//        mealTypeId: Int,
//        dietType: String,
//        dietTypeId: Int,
//    ) = viewModelScope.launch(Dispatchers.IO) {
//        dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
//    }

        fun saveMealAndDietType() =
            viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveMealAndDietType(
            mealAndDiet.selectedMealType,
            mealAndDiet.selectedMealTypeId,
            mealAndDiet.selectedDietType,
            mealAndDiet.selectedDietTypeId
        )
    }

    fun saveMealAndDietTypeTemp(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int,
    ) {
        mealAndDiet = MealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        queries[Constants.QUERY_NUMBER] = Constants.DEFAULT_RECIPES_NUMBER
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_TYPE] = mealType //ex Constants.DEFAULT_MEAL_TYPE
        queries[Constants.QUERY_DIET] = dietType //ex Constants.DEFAULT_DIET_TYPE
        queries[Constants.QUERY_ADD_RECIPE_INFO] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

}