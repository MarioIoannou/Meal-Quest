package com.marioioannou.mealquest.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.mealquest.adapters.RecipesAdapter
import com.marioioannou.mealquest.adapters.RecipesByIngredientsAdapter
import com.marioioannou.mealquest.databinding.FragmentRecipesByIngredientsBinding
import com.marioioannou.mealquest.ui.activities.MainActivity
import com.marioioannou.mealquest.utils.Constants
import com.marioioannou.mealquest.utils.Constants.QUERY_RECIPE_BY_INGREDIENTS_API_KEY
import com.marioioannou.mealquest.utils.Constants.QUERY_RECIPE_BY_INGREDIENTS_IGNORE_PANTRY
import com.marioioannou.mealquest.utils.Constants.QUERY_RECIPE_BY_INGREDIENTS_INGREDIENTS
import com.marioioannou.mealquest.utils.Constants.QUERY_RECIPE_BY_INGREDIENTS_NUMBER
import com.marioioannou.mealquest.utils.NetworkListener
import com.marioioannou.mealquest.utils.ScreenState
import com.marioioannou.mealquest.viewmodel.MainViewModel

class RecipesByIngredientsFragment : Fragment() {

    private lateinit var binding: FragmentRecipesByIngredientsBinding

    private lateinit var recipesAdapter: RecipesByIngredientsAdapter
    //private var recipesDatabase: List<RecipesEntity>

    lateinit var viewModel: MainViewModel

    private val args by navArgs<RecipesByIngredientsFragmentArgs>()

    private lateinit var networkListener: NetworkListener

    private var TAG = "RecipesByIngredientsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRecipesByIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ingredients = args.ingredients
        Log.d(TAG,"Ingredients $ingredients")

        setupRecyclerView()
        requestApiData(ingredients)
    }

    private fun requestApiData(ingredients: String) {
        Log.e(TAG, "requestApiData CALLED")
        viewModel.getRecipesByIngredients(applyQueries(ingredients))
        viewModel.recipeByIngredientsResponse.observe(viewLifecycleOwner, Observer { recipeResponse ->
            Log.e(TAG, "viewModel.getRecipesByIngredients.observe")
            when (recipeResponse) {
                is ScreenState.Loading -> {
                    //binding.shimmerFragRecipesRv.visibility = View.VISIBLE
                    //Log.e(TAG, "   requestApiData() Response Loading")
                }
                is ScreenState.Success -> {
                    Log.e(TAG, "   getRecipesByIngredients() Response Success")
                    //binding.noInternetLayout.visibility = View.GONE
                    //binding.rvRecipes.visibility = View.VISIBLE
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        binding.shimmerFragRecipesRv.stopShimmer()
//                        binding.shimmerFragRecipesRv.visibility = View.GONE
//                    },100L)
                    //binding.rvCoinRecyclerview.visibility = View.VISIBLE
                    recipeResponse.data?.let { recipes ->
                        recipesAdapter.differ.submitList(recipes)
                    }
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "   getRecipesByIngredients() Response Error")
                }
            }
        })
    }

    private fun setupRecyclerView() {
        recipesAdapter = RecipesByIngredientsAdapter()
        binding.rvRecipesByIngredients.apply {
            adapter = recipesAdapter
            //layoutManager = GridLayoutManager(activity,2)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun applyQueries(names: String):HashMap<String,String>{
        val queries : HashMap<String,String> = HashMap()
        //val nameList = names.joinToString(",+")
        queries[QUERY_RECIPE_BY_INGREDIENTS_INGREDIENTS] = names
        queries[QUERY_RECIPE_BY_INGREDIENTS_API_KEY] = Constants.API_KEY
        queries[QUERY_RECIPE_BY_INGREDIENTS_NUMBER] = "50"
        queries[QUERY_RECIPE_BY_INGREDIENTS_IGNORE_PANTRY] = "false"

        return queries
    }
}