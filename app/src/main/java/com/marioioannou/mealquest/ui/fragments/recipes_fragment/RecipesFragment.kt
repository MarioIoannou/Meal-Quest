package com.marioioannou.mealquest.ui.fragments.recipes_fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.adapters.recyclerview_adapters.RandomRecipesAdapter
import com.marioioannou.mealquest.utils.ScreenState
import com.marioioannou.mealquest.adapters.recyclerview_adapters.RecipesAdapter
import com.marioioannou.mealquest.databinding.FragmentRecipesBinding
import com.marioioannou.mealquest.domain.model.recipes.Result
import com.marioioannou.mealquest.utils.observeOnce
import com.marioioannou.mealquest.ui.activities.MainActivity
import com.marioioannou.mealquest.utils.AdMobUtil
import com.marioioannou.mealquest.utils.Constants.firstTime
import com.marioioannou.mealquest.utils.NetworkListener
import com.marioioannou.mealquest.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class RecipesFragment : Fragment() {

    private lateinit var binding: FragmentRecipesBinding

    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var randomRecipesAdapter: RandomRecipesAdapter
    //private var recipesDatabase: List<RecipesEntity>

    lateinit var viewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    private val args by navArgs<RecipesFragmentArgs>()

    private lateinit var networkListener: NetworkListener

    private var TAG = "RecipesFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
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

        Log.e(TAG, "Start firstTime is $firstTime")

        //readDatabase()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.recipes_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when (menuItem.itemId) {
                    R.id.menu_refresh -> {
                        if (recipesViewModel.networkStatus) {
                            requestRandomRecipes()
                        } else {
                            recipesViewModel.showNetworkStatus()
                        }
                    }

                    R.id.menu_settings -> {
                        findNavController().navigate(R.id.action_global_settingsFragment)
                    }

                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }

        setupRecyclerView()
        setupRandomRecipesRecyclerView()

        readDatabase()
        readRandomDatabase()

//        lifecycleScope.launchWhenStarted {
//            networkListener = NetworkListener()
//            networkListener.checkNetworkAvailability(requireContext())
//                .collect { status ->
//                    Log.d("NetworkListener", status.toString())
//                    recipesViewModel.networkStatus = status
//                    recipesViewModel.showNetworkStatus()
//                    readDatabase()
//                    readRandomDatabase()
//                }
//        }

        binding.shimmerFragRecipesRv.startShimmer()
        binding.shimmerFragRandomRecipesRv.startShimmer()
        //requestApiData()

        recipesAdapter.setOnItemClickListener { result: Result ->
            Log.d("onRecipeClick", "${result.title} clicked")
            val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
            findNavController().navigate(action)
        }

        randomRecipesAdapter.setOnItemClickListener { result: Result ->
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
            val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
            findNavController().navigate(action)
        }

        binding.fabFilter.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }
    }

    private fun requestApiData() {
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
                    hideShimmerEffect()
                    recipeResponse.data?.let { recipes ->
                        recipesAdapter.differ.submitList(recipes.results)
                    }
                    recipesViewModel.saveMealDietAndCuisineType()
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "   requestApiData() Response Error")
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

    private fun requestRandomRecipes() {
        Log.e(TAG, "requestApiData CALLED")
        viewModel.getRandomRecipes(recipesViewModel.applyRandomRecipesQueries())
        viewModel.randomRecipesResponse.observe(viewLifecycleOwner, Observer { recipeResponse ->
            Log.e(TAG, "viewModel.requestApiData.observe")
            when (recipeResponse) {
                is ScreenState.Loading -> {
                    showRandomShimmerEffect()
                    Log.e(TAG, "   requestApiData() Response Loading")
                }
                is ScreenState.Success -> {
                    Log.e(TAG, "   requestApiData() Response Success")
                    hideRandomShimmerEffect()
                    //binding.rvCoinRecyclerview.visibility = View.VISIBLE
                    recipeResponse.data?.let { recipes ->
                        randomRecipesAdapter.differ.submitList(recipes.results)
//                        recipeResponse.data.let {
//                            recipesAdapter.setData(it)
//                        }
                    }
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "   requestApiData() Response Error")
                    loadCachedRandomData()
                    Toast.makeText(
                        requireContext(),
                        recipeResponse.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
//                    Log.e(TAG, "   requestApiData() Response Error")
//                    hideRandomShimmerEffect()
//                    if(!recipesViewModel.networkStatus){
//                        //showNoInternetLayout()
//                    }else{
//                        hideNoInternetLayout()
//                    }
//                    Toast.makeText(
//                        requireContext(),
//                        recipeResponse.message.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()

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
                Log.e(TAG, "backFromBottomSheet: ${recipesViewModel.backFromBottomSheet}")
                if (database.isNotEmpty() && !recipesViewModel.backFromBottomSheet) {
                    Log.e(TAG, "readDatabase() CALLED")
                    recipesAdapter.differ.submitList(database[0].foodRecipe.results)
                    recipesViewModel.backFromBottomSheet = false
                } else {
                    requestApiData()
                }
            })
        }
    }

    private fun readRandomDatabase() {
        lifecycleScope.launch {
            viewModel.readRandomRecipes.observeOnce(viewLifecycleOwner, Observer { database ->
                if (database.isNotEmpty()) {
                    Log.e(TAG, "readDatabase() CALLED")
                    hideRandomShimmerEffect()
                    randomRecipesAdapter.differ.submitList(database[0].randomFoodRecipe.results)
                    //recipesAdapter.setData(database.first().foodRecipe)
                } else {
                    //recipesViewModel.backFromBottomSheet = false
                    requestRandomRecipes()
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
                }
            })
        }
    }

    private fun loadCachedRandomData() {
        lifecycleScope.launch {
            viewModel.readRandomRecipes.observe(viewLifecycleOwner, Observer { database ->
                if (database.isNotEmpty()) {
                    Log.e(TAG, "loadCachedData() CALLED")
                    randomRecipesAdapter.differ.submitList(database[0].randomFoodRecipe.results)
                    //recipesAdapter.setData(database.first().foodRecipe)
                    //binding.shimmerFragRecipesRv.visibility = View.GONE
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

    private fun setupRandomRecipesRecyclerView() {
        randomRecipesAdapter = RandomRecipesAdapter()
        binding.rvRandomRecipes.apply {
            adapter = randomRecipesAdapter
            //layoutManager = GridLayoutManager(activity,2)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun showNoInternetLayout() {
        val dialog = layoutInflater.inflate(R.layout.no_internet_dialog,null)
        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialog)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val btnClose = myDialog.findViewById<TextView>(R.id.btn_ok_internet)
        btnClose.setOnClickListener{
            myDialog.dismiss()
        }
        firstTime = false
        Log.e(TAG, "firstTime is $firstTime")
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

    private fun showRandomShimmerEffect() {
        binding.shimmerFragRandomRecipesRv.startShimmer()
        binding.shimmerFragRandomRecipesRv.visibility = View.VISIBLE
    }

    private fun hideRandomShimmerEffect() {
        binding.shimmerFragRandomRecipesRv.stopShimmer()
        binding.shimmerFragRandomRecipesRv.visibility = View.GONE
    }


//    private fun applyQueries():HashMap<Sctring,String>{
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