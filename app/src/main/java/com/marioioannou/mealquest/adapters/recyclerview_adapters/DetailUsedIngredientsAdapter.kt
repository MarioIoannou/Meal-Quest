package com.marioioannou.mealquest.adapters.recyclerview_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.DetailRecipesIngredientsLayoutBinding
import com.marioioannou.mealquest.domain.model.recipes.ExtendedIngredient
import java.util.*

class DetailUsedIngredientsAdapter : RecyclerView.Adapter<DetailUsedIngredientsAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: DetailRecipesIngredientsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<ExtendedIngredient>() {

        override fun areItemsTheSame(oldItem: ExtendedIngredient, newItem: ExtendedIngredient): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExtendedIngredient, newItem: ExtendedIngredient): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DetailRecipesIngredientsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ingredient = differ.currentList[position]
        holder.binding.apply {
            tvIngredientTitle.text = ingredient.name.toString().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
            tvAmount.text = ingredient.amount.toString()
            tvUnit.text = ingredient.unit.toString()
            tvOriginal.text = ingredient.original.toString().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
            imgFood.load("https://spoonacular.com/cdn/ingredients_500x500/${ingredient.image}"){
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}