package com.marioioannou.mealquest.ui.fragments.setting_fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.FragmentRecipesBinding
import com.marioioannou.mealquest.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            cvThemeMode.setOnClickListener {
                showAlertDialog()
            }
            val sharedPreference =
                binding.root.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            when (sharedPreference.getString("theme", "system default")) {
                "light" -> tvMode.text = "Light"
                "dark" -> tvMode.text = "Dark"
                "system default" -> tvMode.text = "System Default"
            }
            cvAbout.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_aboutBottomSheet)
            }

            imgBuyMeACoffee.setOnClickListener {
                val url: Uri = Uri.parse("https://www.buymeacoffee.com/marioioannou")
                val intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(intent)
            }
        }
    }

    private fun showAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
        ).setTitle("Theme")
        val items = arrayOf("Light", "Dark", "System Default")
        val sharedPreference =
            binding.root.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        var checkedItem = 2
        when (sharedPreference.getString("theme", "system default")) {
            "light" -> checkedItem = 0
            "dark" -> checkedItem = 1
            "system default" -> checkedItem = 2
        }
        alertDialog.setSingleChoiceItems(
            items, checkedItem
        ) { dialog, which ->
            when (which) {
                0 -> {
                    dialog.dismiss()
                    editor.putString("theme", "light")
                    editor.apply()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                1 -> {
                    dialog.dismiss()
                    editor.putString("theme", "dark")
                    editor.apply()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                2 -> {
                    dialog.dismiss()
                    editor.putString("theme", "system default")
                    editor.apply()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
        val alert: AlertDialog = alertDialog.create()
        alert.show()
    }

}