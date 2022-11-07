package com.marioioannou.mealquest.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.marioioannou.mealquest.utils.ScreenState
import com.marioioannou.mealquest.domain.database.recipes_database.RecipesEntity
import com.marioioannou.mealquest.domain.model.ingredients.Ingredient
import com.marioioannou.mealquest.domain.model.ingredients.SearchIngredients
import com.marioioannou.mealquest.domain.model.recipes.FoodRecipe
import com.marioioannou.mealquest.domain.model.recipes_by_ingredients.RecipesByIngredients
import com.marioioannou.mealquest.domain.repository.LocalDataSource
import com.marioioannou.mealquest.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
):AndroidViewModel(application) {

    /* ROOM DATABASE */

    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertRecipes(recipesEntity)
    }

    /* RETROFIT */

    private val _recipesResponse: MutableLiveData<ScreenState<FoodRecipe>> = MutableLiveData()
    val recipesResponse = _recipesResponse

    private val _searchedIngredientsResponse: MutableLiveData<ScreenState<SearchIngredients>> = MutableLiveData()
    val searchedIngredientsResponse = _searchedIngredientsResponse

    private val _recipeByIngredientsResponse: MutableLiveData<ScreenState<RecipesByIngredients>> = MutableLiveData()
    val recipeByIngredientsResponse = _recipeByIngredientsResponse

    fun getRecipes(queries: Map<String,String>) = viewModelScope.launch {
        getRecipesConnected(queries)
    }

    fun getSearchedIngredients(queries: Map<String,String>) = viewModelScope.launch {
        getSearchedIngredientsConnected(queries)
    }

    fun getRecipesByIngredients(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesByIngredientsConnected(queries)
    }

    // Connected Responses //

    private suspend fun getRecipesConnected(queries: Map<String,String>){
        recipesResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()){
                val response = repository.remote.getRecipes(queries)
                recipesResponse.postValue(handleFoodRecipesResponse(response))
                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe != null){
                    offlineCacheRecipes(foodRecipe)
                }
            }else{
                recipesResponse.postValue(ScreenState.Error(null,"No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> recipesResponse.postValue(ScreenState.Error(null,"Network Failure"))
                else -> recipesResponse.postValue(ScreenState.Error(null,"Something went wrong"))
            }
        }
    }

    private suspend fun getSearchedIngredientsConnected(queries: Map<String,String>){
        searchedIngredientsResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()){
                val response = repository.remote.getSearchedIngredients(queries)
                searchedIngredientsResponse.postValue(handleSearchedIngredientsResponse(response))
            }else{
                searchedIngredientsResponse.postValue(ScreenState.Error(null,"No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> searchedIngredientsResponse.postValue(ScreenState.Error(null,"Network Failure"))
                else -> searchedIngredientsResponse.postValue(ScreenState.Error(null,"Something went wrong"))
            }
        }
    }

    private suspend fun getRecipesByIngredientsConnected(queries: Map<String,String>){
        recipeByIngredientsResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()){
                val response = repository.remote.getRecipesByIngredients(queries)
                recipeByIngredientsResponse.postValue(handleRecipesByIngredientsResponse(response))
            }else{
                recipeByIngredientsResponse.postValue(ScreenState.Error(null,"No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> recipeByIngredientsResponse.postValue(ScreenState.Error(null,"Network Failure"))
                else -> recipeByIngredientsResponse.postValue(ScreenState.Error(null,"Something went wrong"))
            }
        }
    }

   // Handle Responses //

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): ScreenState<FoodRecipe> {
       when{
           response.message().toString().contains("timeout") -> {
               Log.d("Error1",response.message().toString())
               return ScreenState.Error(null,"Timeout")
           }
           response.code() == 402 -> {
               Log.d("Error2",response.code().toString())
               return ScreenState.Error(null,"API Key Limit Achieved.")
           }
           response.body()!!.results.isEmpty() -> {
               Log.d("Error3",response.message().toString())
               return ScreenState.Error(null,"Recipe not found")
           }
           response.isSuccessful -> {
               val foodRecipes = response.body()
               return ScreenState.Success(foodRecipes!!)
           }
           else -> {
               Log.d("Error4",response.message().toString())
               return  ScreenState.Error(null,response.message().toString())
           }
       }
    }

    private fun handleSearchedIngredientsResponse(response: Response<SearchIngredients>): ScreenState<SearchIngredients> {
        when{
            response.message().toString().contains("timeout") -> {
                Log.d("Error1",response.message().toString())
                return ScreenState.Error(null,"Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2",response.code().toString())
                return ScreenState.Error(null,"API Key Limit Achieved.")
            }
            response.body()!!.results.isEmpty() -> {
                Log.d("Error3",response.message().toString())
                return ScreenState.Error(null,"Recipe not found")
            }
            response.isSuccessful -> {
                val ingredients = response.body()
                return ScreenState.Success(ingredients!!)
            }
            else -> {
                Log.d("Error4",response.message().toString())
                return  ScreenState.Error(null,response.message().toString())
            }
        }
    }

    private fun handleRecipesByIngredientsResponse(response: Response<RecipesByIngredients>): ScreenState<RecipesByIngredients> {
        when{
            response.message().toString().contains("timeout") -> {
                Log.d("Error1",response.message().toString())
                return ScreenState.Error(null,"Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2",response.code().toString())
                return ScreenState.Error(null,"API Key Limit Achieved.")
            }
//            response.body()!!.isEmpty() -> {
//                Log.d("Error3",response.message().toString())
//                return ScreenState.Error(null,"Recipe not found")
//            }
            response.isSuccessful -> {
                val ingredients = response.body()
                return ScreenState.Success(ingredients!!)
            }
            else -> {
                Log.d("Error4",response.message().toString())
                return  ScreenState.Error(null,response.message().toString())
            }
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe){
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    // Database
    fun saveIngredient(ingredients: Ingredient) = viewModelScope.launch {
        repository.local.insertIngredient(ingredients)
    }

    fun readIngredient() = repository.local.readIngredients()

    fun readIngredientsName() = repository.local.readIngredientsName()

    fun deleteIngredient(ingredients: Ingredient) = viewModelScope.launch {
        repository.local.deleteIngredient(ingredients)
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}