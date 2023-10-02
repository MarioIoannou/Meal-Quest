package com.marioioannou.mealquest.adapters.recyclerview_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.SearchedIngredientsRowBinding
import com.marioioannou.mealquest.domain.model.ingredients.Ingredient
import java.util.*

class SearchIngredientsAdapter : RecyclerView.Adapter<SearchIngredientsAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: SearchedIngredientsRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Ingredient>() {

        override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            SearchedIngredientsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ingredient = differ.currentList[position]
        holder.binding.apply {
            imgSearchedFood.load("https://spoonacular.com/cdn/ingredients_500x500/${ingredient.image}"){
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            tvSearchedIngredient.text = ingredient.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(ingredient) }
        }
    }

    private var onItemClickListener: ((Ingredient) -> Unit)? = null
    fun setOnItemClickListener(listener: (Ingredient) -> Unit){
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}