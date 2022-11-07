package com.marioioannou.mealquest.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.marioioannou.mealquest.adapters.SearchIngredientsAdapter
import com.marioioannou.mealquest.databinding.FragmentSearchIngredientsBinding
import com.marioioannou.mealquest.ui.fragments.recipes_fragment.RecipesViewModel
import com.marioioannou.mealquest.utils.ScreenState
import com.marioioannou.mealquest.utils.hideKeyboard
import com.marioioannou.mealquest.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SearchIngredientsFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentSearchIngredientsBinding

    private lateinit var searchIngredientsAdapter: SearchIngredientsAdapter

    lateinit var viewModel: MainViewModel
    lateinit var recipesViewModel: RecipesViewModel

    private var TAG = "SearchIngredientsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchIngredientsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            //binding.shimmerIngredientsSearchRv.startShimmer()
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        requestSearchedApiData(editable.toString())
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        setupRecyclerView()

        searchIngredientsAdapter.setOnItemClickListener {
            val ingredientName = it.name.replaceFirstChar { ingredient ->
                if (ingredient.isLowerCase()) ingredient.titlecase(Locale.getDefault()) else ingredient.toString()
            }
            viewModel.saveIngredient(it)
            Snackbar.make(view, "$ingredientName successfully added", Snackbar. LENGTH_SHORT).apply {
                setAction("Okay") {
                    dismiss()
                }
                show()
            }
//            val action = SearchIngredientsFragmentDirections.actionSearchIngredientsFragmentToFridgeFragment()
//            findNavController().navigate(action)
        }

    }

    private fun requestSearchedApiData(searchedIngredients: String) {
        Log.e(TAG, "requestApiData CALLED")
        viewModel.getSearchedIngredients(recipesViewModel.searchIngredientQuery(searchedIngredients))
        viewModel.searchedIngredientsResponse.observe(viewLifecycleOwner, Observer { recipeResponse ->
            Log.e(TAG, "viewModel.requestApiData.observe")
            when (recipeResponse) {
                is ScreenState.Loading -> {
                    showShimmerEffect()
                    binding.layoutNoResult.visibility = View.GONE
                    //Log.e(TAG, "   requestApiData() Response Loading")
                }
                is ScreenState.Success -> {
                    //Log.e(TAG, "   requestApiData() Response Success")
                    binding.layoutNoResult.visibility = View.GONE
                    Handler(Looper.getMainLooper()).postDelayed({
                        hideShimmerEffect()
                    },100L)
                    hideKeyboard()
                    recipeResponse.data?.let { recipes ->
                        searchIngredientsAdapter.differ.submitList(recipes.results)
                    }
                }
                is ScreenState.Error -> {
                    searchError()

                    //Log.e(TAG, "   requestApiData() Response Error")
                }
            }
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){
            requestSearchedApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun showShimmerEffect() {
        binding.shimmerIngredientsSearchRv.startShimmer()
        binding.shimmerIngredientsSearchRv.visibility = View.VISIBLE
        binding.rvSearchIngredient.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerIngredientsSearchRv.stopShimmer()
        binding.shimmerIngredientsSearchRv.visibility = View.GONE
        binding.rvSearchIngredient.visibility = View.VISIBLE
    }

    private fun searchError() {
        binding.shimmerIngredientsSearchRv.stopShimmer()
        binding.shimmerIngredientsSearchRv.visibility = View.GONE
        binding.rvSearchIngredient.visibility = View.GONE
        binding.layoutNoResult.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        searchIngredientsAdapter = SearchIngredientsAdapter()
        binding.rvSearchIngredient.apply {
            adapter = searchIngredientsAdapter
            layoutManager = GridLayoutManager(activity,2)
            //layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }
}