package com.marioioannou.mealquest.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.*
import com.marioioannou.mealquest.domain.database.recipes_database.entities.FavouritesEntity
import com.marioioannou.mealquest.domain.database.recipes_database.entities.RandomRecipesEntity
import com.marioioannou.mealquest.utils.ScreenState
import com.marioioannou.mealquest.domain.database.recipes_database.entities.RecipesEntity
import com.marioioannou.mealquest.domain.datastore.DatastoreRepo
import com.marioioannou.mealquest.domain.model.factJoke.FactJoke
import com.marioioannou.mealquest.domain.model.ingredients.Ingredient
import com.marioioannou.mealquest.domain.model.ingredients.SearchIngredients
import com.marioioannou.mealquest.domain.model.randomRecipes.RandomFoodRecipe
import com.marioioannou.mealquest.domain.model.recipe_info_detail_by_ingredients.RecipeByIngredientInfo
import com.marioioannou.mealquest.domain.model.recipes.FoodRecipe
import com.marioioannou.mealquest.domain.model.recipes_by_ingredients.RecipesByIngredients
import com.marioioannou.mealquest.domain.model.recipes_by_ingredients.RecipesByIngredientsItem
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
    //private val dataStoreRepo: DatastoreRepo,
    application: Application,
) : AndroidViewModel(application) {

    var fact = true


    /* ROOM DATABASE */

    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readRandomRecipes: LiveData<List<RandomRecipesEntity>> =
        repository.local.readRandomRecipes().asLiveData()
    val readFavoriteRecipes: LiveData<List<FavouritesEntity>> =
        repository.local.readFavoriteRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("MainViewModel", "insertRecipes()")
            repository.local.insertRecipes(recipesEntity)
        }

    private fun insertRandomRecipes(randomRecipesEntity: RandomRecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("MainViewModel", "insertRandomRecipes()")
            repository.local.insertRandomRecipes(randomRecipesEntity)
        }

    fun insertFavoriteRecipe(favouritesEntity: FavouritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("MainViewModel", "insertFavoriteRecipe()")
            repository.local.insertFavoriteRecipe(favouritesEntity)
        }

    fun deleteFavoriteRecipe(favouritesEntity: FavouritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("MainViewModel", "deleteFavoriteRecipe()")
            repository.local.deleteFavoriteRecipe(favouritesEntity)
        }

    fun deleteAllFavoriteRecipes() = viewModelScope.launch(Dispatchers.IO) {
        Log.e("MainViewModel", "deleteAllFavouriteRecipes()")
        repository.local.deleteAllFavouriteRecipes()
    }

    /* RETROFIT */

    private val _recipesResponse: MutableLiveData<ScreenState<FoodRecipe>> = MutableLiveData()
    val recipesResponse = _recipesResponse

    private val _randomRecipesResponse: MutableLiveData<ScreenState<RandomFoodRecipe>> =
        MutableLiveData()
    val randomRecipesResponse = _randomRecipesResponse

    private val _searchedIngredientsResponse: MutableLiveData<ScreenState<SearchIngredients>> =
        MutableLiveData()
    val searchedIngredientsResponse = _searchedIngredientsResponse

    private val _recipeByIngredientsResponse: MutableLiveData<ScreenState<RecipesByIngredients>> =
        MutableLiveData()
    val recipeByIngredientsResponse = _recipeByIngredientsResponse

    private val _recipeByIngredientsDetailsResponse: MutableLiveData<ScreenState<RecipeByIngredientInfo>> =
        MutableLiveData()
    val recipeByIngredientsDetailsResponse = _recipeByIngredientsDetailsResponse

    private val _foodFactResponse: MutableLiveData<ScreenState<FactJoke>> =
        MutableLiveData()
    val foodFactResponse = _foodFactResponse

    private val _foodJokeResponse: MutableLiveData<ScreenState<FactJoke>> =
        MutableLiveData()
    val foodJokeResponse = _foodJokeResponse



    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesConnected(queries)
    }

    fun getSearchedIngredients(queries: Map<String, String>) = viewModelScope.launch {
        getSearchedIngredientsConnected(queries)
    }

    fun getRecipesByIngredients(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesByIngredientsConnected(queries)
    }

    fun getRandomRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRandomRecipesConnected(queries)
    }

    fun getRecipeByIngredientsDetail(id:String) = viewModelScope.launch {
        getRecipeByIngredientsDetailConnected(id)
    }

    fun getFoodFact() = viewModelScope.launch {
        getFoodFactConnected()
    }

    fun getFoodJoke() = viewModelScope.launch {
        getFoodJokeConnected()
    }

    // Connected Responses //

    private suspend fun getRecipesConnected(queries: Map<String, String>) {
        recipesResponse.postValue(ScreenState.Loading())
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if (foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                //recipesResponse.value = NetworkResult.Error("Recipes not found.")
                recipesResponse.postValue(ScreenState.Error(null, "Recipes not found."))
            }
        } else {
            //recipesResponse.value = NetworkResult.Error("No Internet Connection.")
            recipesResponse.postValue(ScreenState.Error(null, "No Internet Connection"))
        }
//        try {
//            if (hasInternetConnection()){
//                val response = repository.remote.getRecipes(queries)
//                recipesResponse.postValue(handleFoodRecipesResponse(response))
//                val foodRecipe = recipesResponse.value?.data
//                if(foodRecipe != null){
//                    Log.e("offlineCacheRecipes NOT null","offlineCacheRecipes()")
//                    offlineCacheRecipes(foodRecipe)
//                }
//            }else{
//                recipesResponse.postValue(ScreenState.Error(null,"No Internet Connection"))
//            }
//        }catch (t: Throwable){
//            when(t){
//                is IOException -> recipesResponse.postValue(ScreenState.Error(null,"Network Failure"))
//                else -> recipesResponse.postValue(ScreenState.Error(null,"Something went wrong"))
//            }
//        }
    }

    private suspend fun getSearchedIngredientsConnected(queries: Map<String, String>) {
        searchedIngredientsResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.remote.getSearchedIngredients(queries)
                searchedIngredientsResponse.postValue(handleSearchedIngredientsResponse(response))
            } else {
                searchedIngredientsResponse.postValue(ScreenState.Error(null,
                    "No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchedIngredientsResponse.postValue(ScreenState.Error(null,
                    "Network Failure"))
                else -> searchedIngredientsResponse.postValue(ScreenState.Error(null,
                    "Something went wrong"))
            }
        }
    }

    private suspend fun getRecipesByIngredientsConnected(queries: Map<String, String>) {
        recipeByIngredientsResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.remote.getRecipesByIngredients(queries)
                recipeByIngredientsResponse.postValue(handleRecipesByIngredientsResponse(response))
            } else {
                recipeByIngredientsResponse.postValue(ScreenState.Error(null,
                    "No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> recipeByIngredientsResponse.postValue(ScreenState.Error(null,
                    "Network Failure"))
                else -> recipeByIngredientsResponse.postValue(ScreenState.Error(null,
                    "Something went wrong"))
            }
        }
    }

    private suspend fun getRecipeByIngredientsDetailConnected(id: String) {
        recipeByIngredientsDetailsResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.remote.getRecipesInformation(id)
                recipeByIngredientsDetailsResponse.postValue(handleRecipesByIngredientsDetailResponse(response))
            } else {
                recipeByIngredientsDetailsResponse.postValue(ScreenState.Error(null,
                    "No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> recipeByIngredientsDetailsResponse.postValue(ScreenState.Error(null,
                    "Network Failure"))
                else -> recipeByIngredientsDetailsResponse.postValue(ScreenState.Error(null,
                    "Something went wrong"))
            }
        }
    }

    private suspend fun getRandomRecipesConnected(queries: Map<String, String>) {
        randomRecipesResponse.postValue(ScreenState.Loading())
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRandomRecipes(queries)
                randomRecipesResponse.value = handleRandomFoodRecipesResponse(response)

                val randomRecipe = randomRecipesResponse.value!!.data
                if (randomRecipe != null) {
                    offlineCacheRandomRecipes(randomRecipe)
                }
            } catch (e: Exception) {
                //recipesResponse.value = NetworkResult.Error("Recipes not found.")
                recipesResponse.postValue(ScreenState.Error(null, "Recipes not found."))
            }
        } else {
            //recipesResponse.value = NetworkResult.Error("No Internet Connection.")
            recipesResponse.postValue(ScreenState.Error(null, "No Internet Connection"))
        }
    }

    private suspend fun getFoodFactConnected() {
        foodFactResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.remote.getFoodFact()
                foodFactResponse.postValue(handleFoodFactResponse(response))
            } else {
                foodFactResponse.postValue(ScreenState.Error(null,
                    "No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> foodFactResponse.postValue(ScreenState.Error(null,
                    "Network Failure"))
                else -> foodFactResponse.postValue(ScreenState.Error(null,
                    "Something went wrong"))
            }
        }
    }

    private suspend fun getFoodJokeConnected() {
        foodJokeResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.remote.getFoodJoke()
                foodJokeResponse.postValue(handleFoodJokeResponse(response))
            } else {
                foodJokeResponse.postValue(ScreenState.Error(null,
                    "No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> foodJokeResponse.postValue(ScreenState.Error(null,
                    "Network Failure"))
                else -> foodJokeResponse.postValue(ScreenState.Error(null,
                    "Something went wrong"))
            }
        }
    }

//    private suspend fun getRandomRecipesConnected(queries: Map<String, String>) {
//        randomRecipesResponse.postValue(ScreenState.Loading())
//        try {
//            if (hasInternetConnection()) {
//                val response = repository.remote.getRandomRecipes(queries)
//                randomRecipesResponse.postValue(handleRandomFoodRecipesResponse(response))
//            } else {
//                randomRecipesResponse.postValue(ScreenState.Error(null, "No Internet Connection"))
//            }
//        } catch (t: Throwable) {
//            when (t) {
//                is IOException -> randomRecipesResponse.postValue(ScreenState.Error(null,
//                    "Network Failure"))
//                else -> randomRecipesResponse.postValue(ScreenState.Error(null,
//                    "Something went wrong"))
//            }
//        }
//    }

    // Handle Responses //

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): ScreenState<FoodRecipe> {
        when {
            response.message().toString().contains("timeout") -> {
                Log.d("Error1", response.message().toString())
                return ScreenState.Error(null, "Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2", response.code().toString())
                return ScreenState.Error(null, "API Key Limit Achieved.")
            }
            response.body()!!.results.isEmpty() -> {
                Log.d("Error3", response.message().toString())
                return ScreenState.Error(null, "Recipe not found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return ScreenState.Success(foodRecipes!!)
            }
            else -> {
                Log.d("Error4", response.message().toString())
                return ScreenState.Error(null, response.message().toString())
            }
        }
    }

    private fun handleSearchedIngredientsResponse(response: Response<SearchIngredients>): ScreenState<SearchIngredients> {
        when {
            response.message().toString().contains("timeout") -> {
                Log.d("Error1", response.message().toString())
                return ScreenState.Error(null, "Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2", response.code().toString())
                return ScreenState.Error(null, "API Key Limit Achieved.")
            }
            response.body()!!.results.isEmpty() -> {
                Log.d("Error3", response.message().toString())
                return ScreenState.Error(null, "Recipe not found")
            }
            response.isSuccessful -> {
                val ingredients = response.body()
                return ScreenState.Success(ingredients!!)
            }
            else -> {
                Log.d("Error4", response.message().toString())
                return ScreenState.Error(null, response.message().toString())
            }
        }
    }

    private fun handleRecipesByIngredientsResponse(response: Response<RecipesByIngredients>): ScreenState<RecipesByIngredients> {
        when {
            response.message().toString().contains("timeout") -> {
                Log.d("Error1", response.message().toString())
                return ScreenState.Error(null, "Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2", response.code().toString())
                return ScreenState.Error(null, "API Key Limit Achieved.")
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
                Log.d("Error4", response.message().toString())
                return ScreenState.Error(null, response.message().toString())
            }
        }
    }

    private fun handleRecipesByIngredientsDetailResponse(response: Response<RecipeByIngredientInfo>): ScreenState<RecipeByIngredientInfo> {
        when {
            response.message().toString().contains("timeout") -> {
                Log.d("Error1", response.message().toString())
                return ScreenState.Error(null, "Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2", response.code().toString())
                return ScreenState.Error(null, "API Key Limit Achieved.")
            }
            response.body() == null-> {
                Log.d("Error3",response.message().toString())
                return ScreenState.Error(null,"Recipe not found")
            }
            response.isSuccessful -> {
                val recipe = response.body()
                return ScreenState.Success(recipe!!)
            }
            else -> {
                Log.d("Error4", response.message().toString())
                return ScreenState.Error(null, response.message().toString())
            }
        }
    }

    private fun handleRandomFoodRecipesResponse(response: Response<RandomFoodRecipe>): ScreenState<RandomFoodRecipe> {
        when {
            response.message().toString().contains("timeout") -> {
                Log.d("Error1", response.message().toString())
                return ScreenState.Error(null, "Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2", response.code().toString())
                return ScreenState.Error(null, "API Key Limit Achieved.")
            }
            response.body()!!.results.isEmpty() -> {
                Log.d("Error3", response.message().toString())
                return ScreenState.Error(null, "Recipe not found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return ScreenState.Success(foodRecipes!!)
            }
            else -> {
                Log.d("Error4", response.message().toString())
                return ScreenState.Error(null, response.message().toString())
            }
//            response.message().toString().contains("timeout") -> {
//                Log.d("Error1", response.message().toString())
//                return ScreenState.Error(null, "Timeout")
//            }
//            response.code() == 402 -> {
//                Log.d("Error2", response.code().toString())
//                return ScreenState.Error(null, "API Key Limit Achieved.")
//            }
//            response.body()!!.results.isEmpty() -> {
//                Log.d("Error3", response.message().toString())
//                return ScreenState.Error(null, "Recipe not found")
//            }
//            response.isSuccessful -> {
//                val foodRecipes = response.body()
//                return ScreenState.Success(foodRecipes!!)
//            }
//            else -> {
//                Log.d("Error4", response.message().toString())
//                return ScreenState.Error(null, response.message().toString())
//            }
        }
    }

    private fun handleFoodFactResponse(response: Response<FactJoke>): ScreenState<FactJoke> {
        when {
            response.message().toString().contains("timeout") -> {
                Log.d("Error1", response.message().toString())
                return ScreenState.Error(null, "Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2", response.code().toString())
                return ScreenState.Error(null, "API Key Limit Achieved.")
            }
            response.body() == null -> {
                Log.d("Error3",response.message().toString())
                return ScreenState.Error(null,"Fact not found")
            }
            response.isSuccessful -> {
                val fact = response.body()
                return ScreenState.Success(fact!!)
            }
            else -> {
                Log.d("Error4", response.message().toString())
                return ScreenState.Error(null, response.message().toString())
            }
        }
    }

    private fun handleFoodJokeResponse(response: Response<FactJoke>): ScreenState<FactJoke> {
        when {
            response.message().toString().contains("timeout") -> {
                Log.d("Error1", response.message().toString())
                return ScreenState.Error(null, "Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2", response.code().toString())
                return ScreenState.Error(null, "API Key Limit Achieved.")
            }
            response.body() == null -> {
                Log.d("Error3",response.message().toString())
                return ScreenState.Error(null,"Joke not found")
            }
            response.isSuccessful -> {
                val joke = response.body()
                return ScreenState.Success(joke!!)
            }
            else -> {
                Log.d("Error4", response.message().toString())
                return ScreenState.Error(null, response.message().toString())
            }
        }
    }

    // Offline Caching

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        Log.e("offlineCacheRecipes", "insertRecipes()")
        insertRecipes(recipesEntity)
    }

    private fun offlineCacheRandomRecipes(randomFoodRecipe: RandomFoodRecipe) {
        val randomRecipesEntity = RandomRecipesEntity(randomFoodRecipe)
        Log.e("offlineCacheRandomRecipes", "insertRandomRecipes()")
        insertRandomRecipes(randomRecipesEntity)
    }

//    private fun offlineCacheRecipes(foodRecipe: FoodRecipe, randomFoodRecipe: RandomFoodRecipe) {
//        val recipesEntity = RecipesEntity(foodRecipe)
//        val randomRecipesEntity = RandomRecipesEntity(randomFoodRecipe)
//        Log.e("offlineCacheRecipes", "insertRecipes()")
//        insertRecipes(recipesEntity)
//        insertRandomRecipes(randomRecipesEntity)
//    }

    // Database
    fun saveIngredient(ingredients: Ingredient) = viewModelScope.launch {
        repository.local.insertIngredient(ingredients)
    }

    fun readIngredient() = repository.local.readIngredients()

    fun readIngredientsName() = repository.local.readIngredientsName()

    fun deleteIngredient(ingredients: Ingredient) = viewModelScope.launch {
        repository.local.deleteIngredient(ingredients)
    }

    fun deleteAllIngredients() = viewModelScope.launch {
        repository.local.deleteAllIngredients()
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
