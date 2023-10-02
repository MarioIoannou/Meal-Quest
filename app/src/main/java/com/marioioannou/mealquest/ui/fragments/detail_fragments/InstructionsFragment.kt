package com.marioioannou.mealquest.ui.fragments.detail_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.mealquest.adapters.recyclerview_adapters.InstructionsAdapter
import com.marioioannou.mealquest.databinding.FragmentInstructionsBinding
import com.marioioannou.mealquest.domain.model.recipes.Result
import com.marioioannou.mealquest.domain.model.recipes.Step
import com.marioioannou.mealquest.utils.Constants
import kotlinx.parcelize.RawValue

class InstructionsFragment : Fragment() {

    private lateinit var binding: FragmentInstructionsBinding

    private lateinit var instructionsAdapter: InstructionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentInstructionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>(Constants.RECIPE_RESULT_KEY) as Result

        setupRecyclerView()

        val list = myBundle.analyzedInstructions 
        if (myBundle.analyzedInstructions == null || list?.size == 0){
            binding.rvInstructions.visibility = View.GONE
            binding.layoutNoInstructions.visibility = View.VISIBLE
        }else{
            binding.rvInstructions.visibility = View.VISIBLE
            binding.layoutNoInstructions.visibility = View.GONE
            instructionsAdapter.differ.submitList(myBundle.analyzedInstructions.get(0).steps as @RawValue List<Step>)
        }
    }

    private fun setupRecyclerView() {
        instructionsAdapter = InstructionsAdapter()
        binding.rvInstructions.apply {
            adapter = instructionsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

}