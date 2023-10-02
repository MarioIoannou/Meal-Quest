package com.marioioannou.mealquest.ui.fragments.recipes_fragment.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.RecipesBottomSheetBinding
import com.marioioannou.mealquest.ui.fragments.recipes_fragment.RecipesViewModel
import com.marioioannou.mealquest.utils.Constants
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
    private var cuisineTypeChip = Constants.DEFAULT_CUISINE_TYPE
    private var cuisineTypeIdChip = 0
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
            cuisineTypeChip = value.selectedCuisineType
            updateChip(value.selectedMealTypeId, binding.chipGroupMealType)
            updateChip(value.selectedDietTypeId, binding.chipGroupDietType)
            updateChip(value.selectedCuisineTypeId, binding.chipGroupCuisineType)
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

        binding.chipGroupCuisineType.setOnCheckedStateChangeListener{ group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId.first())
            val selectedCuisineType = chip.text.toString().lowercase(Locale.ROOT)
            cuisineTypeChip = selectedCuisineType
            cuisineTypeIdChip = selectedChipId.first()
        }

        binding.btnApply.setOnClickListener {
            recipesViewModel.backFromBottomSheet = true
            recipesViewModel.saveMealAndDietTypeTemp(mealTypeChip,mealTypeIdChip,dietTypeChip,dietTypeIdChip,cuisineTypeChip,cuisineTypeIdChip)
            //recipesViewModel.saveMealAndDietType (mealTypeChip,mealTypeIdChip,dietTypeChip,dietTypeIdChip)
            val action = RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment()
            findNavController().navigate(action)
        }
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup){
        if(chipId != 0){
            try {
                val focusChip = chipGroup.findViewById<Chip>(chipId)
                focusChip.isChecked = true
                chipGroup.requestChildFocus(focusChip,focusChip)
            }catch (e: Exception){
                Log.e(TAG,e.message.toString())
            }
        }
    }

}