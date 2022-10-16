package com.marioioannou.mealquest.ui.fragments.recipesfragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.marioioannou.mealquest.utils.Constants
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.utils.ScreenState
import com.marioioannou.mealquest.adapters.RecipesAdapter
import com.marioioannou.mealquest.databinding.FragmentRecipesBinding
import com.marioioannou.mealquest.utils.observeOnce
import com.marioioannou.mealquest.ui.activities.MainActivity
import com.marioioannou.mealquest.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class RecipesFragment : Fragment() {

    private lateinit var binding: FragmentRecipesBinding
    private lateinit var recipesAdapter: RecipesAdapter
    //private var recipesDatabase: List<RecipesEntity>
    lateinit var viewModel: MainViewModel
    lateinit var recipesViewModel: RecipesViewModel
    private var TAG = "RecipesFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.shimmerFragRecipesRv.visibility = View.VISIBLE
//        binding.shimmerFragRecipesRv.startShimmer()
        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()
        readDatabase()

        binding.shimmerFragRecipesRv.startShimmer()
        requestApiData()

        binding.fabFilter.setOnClickListener{
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        }
    }

    private fun requestApiData(){
        Log.e(TAG, "requestApiData CALLED")
        viewModel.getRecipes(applyQueries())
        viewModel.recipesResponse.observe(viewLifecycleOwner, Observer { coinResponse ->
            Log.e(TAG, "viewModel.requestApiData.observe")
            when (coinResponse) {
                is ScreenState.Loading -> {
                    binding.shimmerFragRecipesRv.visibility = View.VISIBLE
                    Log.e(TAG, "   requestApiData() Response Loading")
                }
                is ScreenState.Success -> {
                    Log.e(TAG, "   requestApiData() Response Success")
                    binding.noInternetLayout.visibility = View.INVISIBLE
                    binding.rvRecipes.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerFragRecipesRv.stopShimmer()
                        binding.shimmerFragRecipesRv.visibility = View.GONE
                    },100L)
                    //binding.rvCoinRecyclerview.visibility = View.VISIBLE
                    coinResponse.data?.let { recipes ->
                        recipesAdapter.differ.submitList(recipes.results)
                    }
                    recipesViewModel.saveMealAndDietType( )
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "   requestApiData() Response Error")
                    if (coinResponse.data?.results.isNullOrEmpty()){
                        Log.e(TAG, "is Empty()")
                        binding.rvRecipes.visibility = View.INVISIBLE
                        binding.noInternetLayout.visibility = View.VISIBLE
                    }else{
                        loadCachedData()
                    }
                }
            }
        })
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            viewModel.readRecipes.observeOnce(viewLifecycleOwner, Observer { database ->
                if (database.isNotEmpty()) {
                    Log.e(TAG, "readDatabase() CALLED")
                    recipesAdapter.differ.submitList(database[0].foodRecipe.results)
                    binding.shimmerFragRecipesRv.visibility = View.GONE
                } else {
                    requestApiData()
                }
            })
        }
    }

    private fun loadCachedData() {
        lifecycleScope.launch {
            viewModel.readRecipes.observe(viewLifecycleOwner, Observer { database ->
                if (database.isNotEmpty()) {
                    Log.e(TAG, "loadCachedData() CALLED")
                    recipesAdapter.differ.submitList(database[0].foodRecipe.results)
                    binding.shimmerFragRecipesRv.visibility = View.GONE
                }
            })
        }
    }

    private fun setupRecyclerView() {
        recipesAdapter = RecipesAdapter()
        binding.rvRecipes.apply {
            adapter = recipesAdapter
            layoutManager = GridLayoutManager(activity,2)
            //layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun applyQueries():HashMap<String,String>{
        val queries : HashMap<String,String> = HashMap()
        queries["number"] = "50"
        queries["apiKey"] = Constants.API_KEY
        queries["type"] = "main"
        queries["diet"] = "vegan"
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"

        return queries
    }
}