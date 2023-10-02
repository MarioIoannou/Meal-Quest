package com.marioioannou.mealquest.adapters.binding_adapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.domain.model.recipes.Result
import com.marioioannou.mealquest.ui.fragments.recipes_fragment.RecipesFragmentDirections

object RecipesRowBinding {

//    @BindingAdapter("onRecipeClickListener")
//    @JvmStatic
//    fun onRecipeClickListener(recipeRowLayout: ConstraintLayout, result: Result) {
//        Log.d("onRecipeClickListener", "CALLED")
//        recipeRowLayout.setOnClickListener {
//            try {
//                val action =
//                    RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
//                recipeRowLayout.findNavController().navigate(action)
//            } catch (e: Exception) {
//                Log.d("onRecipeClickListener", e.toString())
//            }
//        }
//    }

    @BindingAdapter("loadImageFromUrl")
    @JvmStatic
    fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
        imageView.load(imageUrl) {
            crossfade(600)
            error(R.drawable.ic_image_placeholder)
        }
    }

    @BindingAdapter("applyVeganImage")
    @JvmStatic
    fun applyVeganImage(view: View, vegan: Boolean) {
        if (vegan) {
            when (view) {
                is ImageView -> {
                    view.visibility = View.VISIBLE
                }
            }
        }
    }
}