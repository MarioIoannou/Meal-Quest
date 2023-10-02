package com.marioioannou.mealquest.ui.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.marioioannou.mealquest.adapters.recyclerview_adapters.RecipesByIngredientsAdapter
import com.marioioannou.mealquest.databinding.FragmentRecipesByIngredientsBinding
import com.marioioannou.mealquest.domain.model.recipes.Result
import com.marioioannou.mealquest.domain.model.recipes_by_ingredients.RecipesByIngredientsItem
import com.marioioannou.mealquest.ui.activities.MainActivity
import com.marioioannou.mealquest.ui.fragments.recipes_fragment.RecipesFragmentDirections
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

    //- MAIN - Interstitial Id = ca-app-pub-2379578394910008/5285366091
    //- test - Interstitial Id = ca-app-pub-3940256099942544/1033173712

    private var mInterstitialAd: InterstitialAd? = null

    private var interstitialAdUnitId = "ca-app-pub-3940256099942544/1033173712"


    private var TAG = "RecipesByIngredientsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        loadAdmobInterstitialAd(requireContext(),TAG)
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

        Handler(Looper.getMainLooper()).postDelayed({
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
            }
        }, 1000L)

        recipesAdapter.setOnItemClickListener { recipe: RecipesByIngredientsItem ->
            Log.d("onRecipeClick", "${recipe.title} clicked")
            val action = RecipesByIngredientsFragmentDirections.actionRecipesByIngredientsFragmentToRecipesByIngredientsDetailsFragment(recipe)
            findNavController().navigate(action)
        }
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

    private fun loadAdmobInterstitialAd(context: Context, TAG: String) {

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(context, interstitialAdUnitId, adRequest, object :
            InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })

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