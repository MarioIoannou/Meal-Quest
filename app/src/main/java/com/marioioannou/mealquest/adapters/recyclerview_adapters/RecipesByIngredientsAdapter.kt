package com.marioioannou.mealquest.adapters.recyclerview_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.FridgeRecipesLayoutRow4Binding
import com.marioioannou.mealquest.domain.model.recipes.Result
import com.marioioannou.mealquest.domain.model.recipes_by_ingredients.RecipesByIngredientsItem
import java.util.*

class RecipesByIngredientsAdapter : RecyclerView.Adapter<RecipesByIngredientsAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: FridgeRecipesLayoutRow4Binding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<RecipesByIngredientsItem>() {

        override fun areItemsTheSame(oldItem: RecipesByIngredientsItem, newItem: RecipesByIngredientsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecipesByIngredientsItem, newItem: RecipesByIngredientsItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FridgeRecipesLayoutRow4Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        val usedIngredient = recipe.usedIngredients
        val recipesSize = recipe.usedIngredients?.size
        //val recipe = recipes[position]
        holder.binding.apply {
            imgFood.load(recipe.image) {
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            tvRecipeTitle.text = recipe.title
            if (recipesSize!! >= 1) {
                imgItem1.visibility = View.VISIBLE
                tvItem1.visibility = View.VISIBLE
                tvItem1.text = usedIngredient?.get(0)?.name?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
                if (recipesSize >= 2) {
                    imgItem2.visibility = View.VISIBLE
                    tvItem2.visibility = View.VISIBLE
                    tvItem2.text = usedIngredient?.get(1)?.name?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    }
                    if (recipesSize >= 3) {
                        imgItem3.visibility = View.VISIBLE
                        tvItem3.visibility = View.VISIBLE
                        tvItem3.text = usedIngredient?.get(2)?.name?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                        }
                        if (recipesSize >= 4) {
                            tvAndMore.visibility = View.VISIBLE
                        }
                    }
                }
            }
//            when(usedIngredient.size){
//                1 -> {
//                    imgItem1.visibility = View.VISIBLE
//                    tvItem1.visibility = View.VISIBLE
//                    tvItem1.text = usedIngredient[0].name.replaceFirstChar {
//                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
//                    }
//                }
//                2 -> {
//                    imgItem2.visibility = View.VISIBLE
//                    tvItem2.visibility = View.VISIBLE
//                    tvItem2.text = usedIngredient[1].name.replaceFirstChar {
//                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
//                    }
//                }
//                3 -> {
//                    imgItem3.visibility = View.VISIBLE
//                    tvItem3.visibility = View.VISIBLE
//                    tvItem3.text = usedIngredient[2].name.replaceFirstChar {
//                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
//                    }
//                }
//                4 -> {
//                    tvAndMore.visibility = View.VISIBLE
//                }
//            }
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(recipe) }
            }
        }
    }

        override fun getItemCount(): Int {
            return differ.currentList.size
            //return recipes.size
        }

        private var onItemClickListener: ((RecipesByIngredientsItem) -> Unit)? = null
        fun setOnItemClickListener(listener: (RecipesByIngredientsItem) -> Unit) {
            onItemClickListener = listener
        }
}