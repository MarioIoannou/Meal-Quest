package com.marioioannou.mealquest.ui.fragments.recipes_fragment

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.marioioannou.mealquest.domain.datastore.DataStoreRepository
import com.marioioannou.mealquest.domain.datastore.MealDietAndCuisine
import com.marioioannou.mealquest.utils.Constants.API_KEY
import com.marioioannou.mealquest.utils.Constants.DEFAULT_CUISINE_TYPE
import com.marioioannou.mealquest.utils.Constants.DEFAULT_DIET_TYPE
import com.marioioannou.mealquest.utils.Constants.DEFAULT_MEAL_TYPE
import com.marioioannou.mealquest.utils.Constants.DEFAULT_RECIPES_NUMBER
import com.marioioannou.mealquest.utils.Constants.QUERY_ADD_RECIPE_INFO
import com.marioioannou.mealquest.utils.Constants.QUERY_API_KEY
import com.marioioannou.mealquest.utils.Constants.QUERY_CUISINE
import com.marioioannou.mealquest.utils.Constants.QUERY_DIET
import com.marioioannou.mealquest.utils.Constants.QUERY_FILL_INGREDIENTS
import com.marioioannou.mealquest.utils.Constants.QUERY_INGREDIENT
import com.marioioannou.mealquest.utils.Constants.QUERY_NUMBER
import com.marioioannou.mealquest.utils.Constants.QUERY_MEAL_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private lateinit var mealDietAndCuisine: MealDietAndCuisine

    var networkStatus = false
    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMealAndDietType() =
        viewModelScope.launch(Dispatchers.IO) {
            if (this@RecipesViewModel::mealDietAndCuisine.isInitialized) {
                dataStoreRepository.saveMealAndDietType(
                    mealDietAndCuisine.selectedMealType,
                    mealDietAndCuisine.selectedMealTypeId,
                    mealDietAndCuisine.selectedDietType,
                    mealDietAndCuisine.selectedDietTypeId,
                    mealDietAndCuisine.selectedCuisineType,
                    mealDietAndCuisine.selectedCuisineTypeId
                )
            }
        }

    fun saveMealAndDietTypeTemp(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int,
        cuisineType: String,
        cuisineTypeId: Int
    ) {
        mealDietAndCuisine = MealDietAndCuisine(
            mealType,
            mealTypeId,
            dietType,
            dietTypeId,
            cuisineType,
            cuisineTypeId
        )
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFO] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        if (this@RecipesViewModel::mealDietAndCuisine.isInitialized) {
            if (mealDietAndCuisine.selectedCuisineType.lowercase() == "all" && mealDietAndCuisine.selectedDietType.lowercase() != "all"){
                queries[QUERY_MEAL_TYPE] = mealDietAndCuisine.selectedMealType
                queries[QUERY_DIET] = mealDietAndCuisine.selectedDietType
            }else if (mealDietAndCuisine.selectedCuisineType.lowercase() != "all" && mealDietAndCuisine.selectedDietType.lowercase() == "all"){
                queries[QUERY_MEAL_TYPE] = mealDietAndCuisine.selectedMealType
                queries[QUERY_CUISINE] = mealDietAndCuisine.selectedCuisineType
            }else if (mealDietAndCuisine.selectedCuisineType.lowercase() == "all" && mealDietAndCuisine.selectedDietType.lowercase() == "all"){
                queries[QUERY_MEAL_TYPE] = mealDietAndCuisine.selectedMealType
            }else{
                queries[QUERY_MEAL_TYPE] = mealDietAndCuisine.selectedMealType
                queries[QUERY_DIET] = mealDietAndCuisine.selectedDietType
                queries[QUERY_CUISINE] = mealDietAndCuisine.selectedCuisineType
            }
        } else {
            queries[QUERY_MEAL_TYPE] = DEFAULT_MEAL_TYPE
//            queries[QUERY_DIET] = DEFAULT_DIET_TYPE
//            queries[QUERY_CUISINE] = DEFAULT_CUISINE_TYPE
        }

        return queries
    }

    fun searchIngredientQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_INGREDIENT] = searchQuery
        queries[QUERY_API_KEY] = API_KEY
        return queries
    }


//    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
//        val queries: HashMap<String, String> = HashMap()
//        queries[QUERY_SEARCH] = searchQuery
//        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
//        queries[QUERY_API_KEY] = API_KEY
//        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
//        queries[QUERY_FILL_INGREDIENTS] = "true"
//        return queries
//    }

    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

}