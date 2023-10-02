package com.marioioannou.mealquest.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.FragmentRecipesByIngredientsBinding
import com.marioioannou.mealquest.databinding.FragmentRecipesByIngredientsDetailsBinding
import com.marioioannou.mealquest.databinding.FragmentTriviaBinding
import com.marioioannou.mealquest.ui.fragments.recipes_fragment.RecipesViewModel
import com.marioioannou.mealquest.utils.NetworkListener
import com.marioioannou.mealquest.utils.ScreenState
import com.marioioannou.mealquest.viewmodel.MainViewModel

class RecipesByIngredientsDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRecipesByIngredientsDetailsBinding

    lateinit var viewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    private lateinit var networkListener: NetworkListener

    val args: RecipesByIngredientsDetailsFragmentArgs by navArgs()

    private var TAG = "TriviaFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRecipesByIngredientsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.recipeId.id

        binding.recipeWebView.webViewClient = object : WebViewClient() {}
        requestApiData(id.toString())

    }

    private fun requestApiData(id : String){
        Log.e(TAG, "requestApiData CALLED")
        viewModel.getRecipeByIngredientsDetail(id)
        viewModel.recipeByIngredientsDetailsResponse.observe(viewLifecycleOwner, Observer { recipeResponse ->
            Log.e(TAG, "viewModel.requestApiData.observe")
            when (recipeResponse) {
                is ScreenState.Loading -> {
                    hideNoInternet()
                    showLottie()
                    Log.e(TAG, "   requestApiData() Response Loading")
                }
                is ScreenState.Success -> {
                    Log.e(TAG, "   requestApiData() Response Success")
                    Handler(Looper.getMainLooper()).postDelayed({
                        hideLottie()
                    },3000L)
                    recipeResponse.data?.let { recipes ->
                        binding.recipeWebView.loadUrl(recipes.sourceUrl.toString())
                    }
                    recipesViewModel.saveMealDietAndCuisineType()
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "   requestApiData() Response Error")

                    showNoInternet()

                    Toast.makeText(
                        requireContext(),
                        recipeResponse.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        })
    }

    private fun showLottie() {
        binding.recipeWebView.visibility = View.GONE
        binding.webviewLtRecipe.playAnimation()
        binding.webviewLtRecipe.visibility = View.VISIBLE
    }

    private fun hideLottie() {
        binding.webviewLtRecipe.visibility = View.GONE
        binding.recipeWebView.visibility = View.VISIBLE
        binding.webviewLtRecipe.pauseAnimation()
    }

    private fun showNoInternet() {
        binding.recipeWebView.visibility = View.GONE
        binding.webviewLtNoInternet.playAnimation()
        binding.webviewLtNoInternet.visibility = View.VISIBLE
    }

    private fun hideNoInternet() {
        binding.webviewLtNoInternet.visibility = View.GONE
        binding.recipeWebView.visibility = View.VISIBLE
        binding.webviewLtNoInternet.pauseAnimation()
    }

}