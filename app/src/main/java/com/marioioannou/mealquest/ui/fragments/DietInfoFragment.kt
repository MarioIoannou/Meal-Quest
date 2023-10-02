package com.marioioannou.mealquest.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marioioannou.mealquest.databinding.FragmentDietInfoBinding

class DietInfoFragment : Fragment() {

    private lateinit var binding: FragmentDietInfoBinding
    private var TAG = "DietInfoFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDietInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

}