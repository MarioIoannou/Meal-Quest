package com.marioioannou.mealquest.ui.fragments.detail_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.mealquest.adapters.recyclerview_adapters.DetailUsedIngredientsAdapter
import com.marioioannou.mealquest.databinding.FragmentUsedIngredientsBinding
import com.marioioannou.mealquest.domain.model.recipes.Result
import com.marioioannou.mealquest.utils.Constants

class UsedIngredientsFragment : Fragment() {

    private lateinit var binding: FragmentUsedIngredientsBinding

    private lateinit var usedIngredientsAdapter: DetailUsedIngredientsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentUsedIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>(Constants.RECIPE_RESULT_KEY) as Result
        setupRecyclerView()
        usedIngredientsAdapter.differ.submitList(myBundle.extendedIngredients)
    }

    private fun setupRecyclerView() {
        usedIngredientsAdapter = DetailUsedIngredientsAdapter()
        binding.rvUsedIngredients.apply {
            adapter = usedIngredientsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }
}