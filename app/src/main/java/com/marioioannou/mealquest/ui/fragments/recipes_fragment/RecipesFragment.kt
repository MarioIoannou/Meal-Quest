package com.marioioannou.mealquest.ui.fragments.recipes_fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.mealquest.utils.Constants
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.utils.ScreenState
import com.marioioannou.mealquest.adapters.RecipesAdapter
import com.marioioannou.mealquest.databinding.FragmentRecipesBinding
import com.marioioannou.mealquest.domain.model.recipes.Result
import com.marioioannou.mealquest.utils.observeOnce
import com.marioioannou.mealquest.ui.activities.MainActivity
import com.marioioannou.mealquest.utils.NetworkListener
import com.marioioannou.mealquest.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class RecipesFragment : Fragment() {

    private lateinit var binding: FragmentRecipesBinding

    private lateinit var recipesAdapter: RecipesAdapter
    //private var recipesDatabase: List<RecipesEntity>

    lateinit var viewModel: MainViewModel
    lateinit var recipesViewModel: RecipesViewModel

    private val args by navArgs<RecipesFragmentArgs>()

    private lateinit var networkListener: NetworkListener

    private var TAG = "RecipesFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.shimmerFragRecipesRv.visibility = View.VISIBLE
//        binding.shimmerFragRecipesRv.startShimmer()

        setupRecyclerView()
        readDatabase()

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        binding.shimmerFragRecipesRv.startShimmer()
        requestApiData()

        recipesAdapter.setOnItemClickListener { result: Result ->
            Log.d("onRecipeClick", "${result.title} clicked")
            val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
            findNavController().navigate(action)
        }

        binding.fabFilter.setOnClickListener{
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }
    }

    private fun requestApiData(){
        Log.e(TAG, "requestApiData CALLED")
        viewModel.getRecipes(recipesViewModel.applyQueries())
        viewModel.recipesResponse.observe(viewLifecycleOwner, Observer { recipeResponse ->
            Log.e(TAG, "viewModel.requestApiData.observe")
            when (recipeResponse) {
                is ScreenState.Loading -> {
                    showShimmerEffect()
                    Log.e(TAG, "   requestApiData() Response Loading")
                }
                is ScreenState.Success -> {
                    Log.e(TAG, "   requestApiData() Response Success")
                    binding.noInternetLayout.visibility = View.GONE
                    Handler(Looper.getMainLooper()).postDelayed({
                       hideShimmerEffect()
                    },1000L)
                    //binding.rvCoinRecyclerview.visibility = View.VISIBLE
                    recipeResponse.data?.let { recipes ->
                        recipesAdapter.differ.submitList(recipes.results)
//                        recipeResponse.data.let {
//                            recipesAdapter.setData(it)
//                        }
                    }
                    recipesViewModel.saveMealAndDietType()
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "   requestApiData() Response Error")
                    hideShimmerEffect()
                    loadCachedData()
                    Toast.makeText(
                        requireContext(),
                        recipeResponse.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
//                    if (recipeResponse.data?.results.isNullOrEmpty()){
//                        Log.e(TAG, "is Empty()")
//                        binding.shimmerFragRecipesRv.visibility = View.GONE
//                        binding.rvRecipes.visibility = View.INVISIBLE
//                        binding.noInternetLayout.visibility = View.VISIBLE
//                    }else{
//                        loadCachedData()
//                    }
                }
            }
        })
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            viewModel.readRecipes.observeOnce(viewLifecycleOwner, Observer { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.e(TAG, "readDatabase() CALLED")
                    recipesAdapter.differ.submitList(database[0].foodRecipe.results)
                    //recipesAdapter.setData(database.first().foodRecipe)
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
                    //recipesAdapter.setData(database.first().foodRecipe)
                    binding.shimmerFragRecipesRv.visibility = View.GONE
                }
            })
        }
    }

    private fun setupRecyclerView() {
        recipesAdapter = RecipesAdapter()
        binding.rvRecipes.apply {
            adapter = recipesAdapter
            //layoutManager = GridLayoutManager(activity,2)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun showShimmerEffect() {
        binding.shimmerFragRecipesRv.startShimmer()
        binding.shimmerFragRecipesRv.visibility = View.VISIBLE
        binding.rvRecipes.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFragRecipesRv.stopShimmer()
        binding.shimmerFragRecipesRv.visibility = View.GONE
        binding.rvRecipes.visibility = View.VISIBLE
    }

//    private fun applyQueries():HashMap<String,String>{
//        val queries : HashMap<String,String> = HashMap()
//        queries["number"] = "50"
//        queries["apiKey"] = Constants.API_KEY
//        queries["type"] = "main course"
//        queries["diet"] = "gluten free"
//        queries["addRecipeInformation"] = "true"
//        queries["fillIngredients"] = "true"
//
//        return queries
//    }
}