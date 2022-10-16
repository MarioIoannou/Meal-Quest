package com.marioioannou.mealquest.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.marioioannou.mealquest.utils.ScreenState
import com.marioioannou.mealquest.domain.database.RecipesEntity
import com.marioioannou.mealquest.domain.model.FoodRecipe
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

    fun getRecipes(queries: Map<String,String>) = viewModelScope.launch {
        getRecipesConnected(queries)
    }

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

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): ScreenState<FoodRecipe> {
       when{
           response.message().toString().contains("timeout") -> {
               return ScreenState.Error(null,"Timeout")
           }
           response.code() == 402 -> {
               return ScreenState.Error(null,"API Key Limit Achieved.")
           }
           response.body()!!.results.isEmpty() -> {
               return ScreenState.Error(null,"Recipe not found")
           }
           response.isSuccessful -> {
               val foodRecipes = response.body()
               return ScreenState.Success(foodRecipes!!)
           }
           else -> {
               return  ScreenState.Error(null,response.message().toString())
           }
       }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe){
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
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