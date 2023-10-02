package com.marioioannou.mealquest.ui.fragments

import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.FragmentTriviaBinding
import com.marioioannou.mealquest.ui.fragments.recipes_fragment.RecipesViewModel
import com.marioioannou.mealquest.utils.NetworkListener
import com.marioioannou.mealquest.utils.ScreenState
import com.marioioannou.mealquest.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class TriviaFragment : Fragment() {

    private lateinit var binding: FragmentTriviaBinding

    lateinit var viewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    private lateinit var networkListener: NetworkListener

    private var TAG = "TriviaFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTriviaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        Log.e(TAG, "Fact is ${viewModel.fact}")
        Handler(Looper.getMainLooper()).postDelayed({
            //hideLottie()
            binding.layoutLottie.visibility = View.GONE
            binding.layoutFactJoke.visibility = View.VISIBLE
        }, 8000L)

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    if (viewModel.fact) {
                        //showLottie()
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            requestFoodFact()
//                        }, 8000L)
                        requestFoodFact()
                    } else {
                        //showLottie()
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            requestFoodJoke()
//                        }, 8000L)
                        requestFoodJoke()
                    }
                }
        }
    }

    private fun requestFoodFact() {
        viewModel.getFoodFact()
        viewModel.foodFactResponse.observe(viewLifecycleOwner, Observer { foodFactResponse ->
            Log.e(TAG, "viewModel.requestApiData.observe")
            when (foodFactResponse) {
                is ScreenState.Loading -> {
                    //hideNoInternet()
                }
                is ScreenState.Success -> {
                    //hideNoInternet()
                    //hideLottie()
                    foodFactResponse.data?.let { fact ->
                        binding.tvFactJoke.text = getString(R.string.did_you_know)
                        binding.tvJokeOrFact.text = fact.text
                    }
                }
                is ScreenState.Error -> {
                    //showNoInternet()
                }
            }
        })
        viewModel.fact = false
    }

    private fun requestFoodJoke() {
        viewModel.getFoodJoke()
        viewModel.foodJokeResponse.observe(viewLifecycleOwner, Observer { foodJokeResponse ->
            Log.e(TAG, "viewModel.requestApiData.observe")
            when (foodJokeResponse) {
                is ScreenState.Loading -> {
                    //hideNoInternet()
                }
                is ScreenState.Success -> {
                    //hideNoInternet()
                    //hideLottie()
                    foodJokeResponse.data?.let { joke ->
                        binding.tvFactJoke.text = getString(R.string.a_joke_for_you)
                        binding.tvJokeOrFact.text = joke.text
                    }
                }
                is ScreenState.Error -> {
                    //showNoInternet()
                }
            }
        })
        viewModel.fact = true
    }

    private fun showLottie() {
        binding.ltLottie.playAnimation()
        binding.layoutFactJoke.visibility = View.GONE
        binding.layoutLottie.visibility = View.VISIBLE
    }

    private fun hideLottie() {
        binding.ltLottie.pauseAnimation()
        binding.layoutFactJoke.visibility = View.VISIBLE
        binding.layoutLottie.visibility = View.GONE

    }

    private fun showLayout() {
        binding.ltLottie.playAnimation()
        binding.layoutFactJoke.visibility = View.GONE
        binding.layoutNoInternet.visibility = View.GONE
    }

    private fun hideLayout() {
        binding.ltLottie.pauseAnimation()
        binding.layoutNoInternet.visibility = View.GONE
        binding.layoutLottie.visibility = View.GONE
        binding.layoutFactJoke.visibility = View.VISIBLE

    }


    private fun showNoInternet() {
        binding.lottieNoInternet.playAnimation()
        binding.layoutFactJoke.visibility = View.GONE
        binding.layoutLottie.visibility = View.GONE
        binding.layoutNoInternet.visibility = View.VISIBLE
    }

    private fun hideNoInternet() {
        binding.layoutNoInternet.visibility = View.GONE
        binding.layoutLottie.visibility = View.GONE
        binding.layoutFactJoke.visibility = View.VISIBLE
        binding.lottieNoInternet.pauseAnimation()
    }
}