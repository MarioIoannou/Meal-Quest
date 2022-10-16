package com.marioioannou.mealquest.ui.fragments.recipesfragment.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.adapters.RecipesAdapter
import com.marioioannou.mealquest.databinding.FragmentRecipesBinding
import com.marioioannou.mealquest.databinding.RecipesBottomSheetBinding
import com.marioioannou.mealquest.ui.fragments.recipesfragment.RecipesViewModel
import com.marioioannou.mealquest.utils.Constants
import com.marioioannou.mealquest.viewmodel.MainViewModel
import java.util.*

class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: RecipesBottomSheetBinding
    //private lateinit var recipesAdapter: RecipesAdapter
    //private var recipesDatabase: List<RecipesEntity>
    private lateinit var recipesViewModel: RecipesViewModel

    private var mealTypeChip = Constants.DEFAULT_MEAL_TYPE
    private var mealTypeIdChip = 0
    private var dietTypeChip = Constants.DEFAULT_DIET_TYPE
    private var dietTypeIdChip = 0
    private var TAG = "RecipesBottomSheet"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = RecipesBottomSheetBinding.inflate(inflater, container, false)
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.infoDiets.setOnClickListener{
            findNavController().navigate(R.id.action_recipesBottomSheet_to_dietInfoFragment)
        }

                                        // asLiveData because is a flow()
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, Observer { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            //updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
            //updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)
        })

        binding.chipGroupMealType.setOnCheckedStateChangeListener{ group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId.first())
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeIdChip = selectedChipId.first()
        }

        binding.chipGroupDietType.setOnCheckedStateChangeListener{ group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId.first())
            val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeIdChip = selectedChipId.first()
        }

        binding.btnApply.setOnClickListener {
            recipesViewModel.saveMealAndDietTypeTemp (mealTypeChip,mealTypeIdChip,dietTypeChip,dietTypeIdChip)
            val action = RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment()
        }
    }

}