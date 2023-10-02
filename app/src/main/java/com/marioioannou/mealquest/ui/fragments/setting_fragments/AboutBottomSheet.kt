package com.marioioannou.mealquest.ui.fragments.setting_fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.FragmentAboutBottomSheetBinding
import com.marioioannou.mealquest.databinding.FragmentRecipesBinding
import com.marioioannou.mealquest.databinding.FragmentSettingsBinding

class AboutBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAboutBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAboutBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            aboutSpoonacular.setOnClickListener {
                val url: Uri = Uri.parse("https://spoonacular.com/food-api")
                val intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(intent)
//                if (intent.resolveActivity(requireContext().packageManager) != null) {
//                    startActivity(intent)
//                }
            }
            aboutLinkedin.setOnClickListener{
                val url: Uri = Uri.parse("https://www.linkedin.com/in/marios-ioannou-8326b8174")
                val intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(intent)

            }
            aboutGithub.setOnClickListener {
                val url: Uri = Uri.parse("https://github.com/MarioIoannou")
                val intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(intent)
            }
        }

    }

}