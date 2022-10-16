package com.marioioannou.mealquest.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.RecipesLayoutRowBinding
import com.marioioannou.mealquest.databinding.RecipesLayoutRowNewBinding

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: RecipesLayoutRowNewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<com.marioioannou.mealquest.domain.model.Result>() {

        override fun areItemsTheSame(oldItem: com.marioioannou.mealquest.domain.model.Result, newItem: com.marioioannou.mealquest.domain.model.Result): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: com.marioioannou.mealquest.domain.model.Result, newItem: com.marioioannou.mealquest.domain.model.Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RecipesLayoutRowNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        holder.binding.apply {
            imgFood.load(recipe.image){
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            tvRecipeTitle.text = recipe.title
            //tvRecipeDescription.text = recipe.summary
            tvLoveNumber.text = recipe.likes.toString()
            tvClockNumber.text = recipe.readyInMinutes.toString()
            if (recipe.vegan){
                tvVegan.setTextColor(Color.parseColor("#7FB069"))
            }else{
                tvVegan.setTextColor(Color.WHITE)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}