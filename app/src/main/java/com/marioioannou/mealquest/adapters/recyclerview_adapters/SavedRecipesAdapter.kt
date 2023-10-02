package com.marioioannou.mealquest.adapters.recyclerview_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.LayoutIngredientsBinding
import com.marioioannou.mealquest.databinding.RecipesLayoutRow4Binding
import com.marioioannou.mealquest.databinding.SavedRecipeLayoutRowBinding
import com.marioioannou.mealquest.domain.database.recipes_database.entities.FavouritesEntity
import com.marioioannou.mealquest.domain.model.ingredients.Ingredient
import com.marioioannou.mealquest.domain.model.recipes.Result
import com.marioioannou.mealquest.viewmodel.MainViewModel
import java.util.*

class SavedRecipesAdapter(private val viewModel: MainViewModel): RecyclerView.Adapter<SavedRecipesAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: SavedRecipeLayoutRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<FavouritesEntity>() {

        override fun areItemsTheSame(oldItem: FavouritesEntity, newItem: FavouritesEntity): Boolean {
            return oldItem.result.title == newItem.result.title
        }

        override fun areContentsTheSame(oldItem: FavouritesEntity, newItem: FavouritesEntity): Boolean {
            return oldItem.result == newItem.result
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            SavedRecipeLayoutRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipeList = differ.currentList[position]
        val recipe = recipeList.result
        val recipeName = recipeList.result.title.toString()
        holder.binding.apply {
            imgFood.load(recipe.image){
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            tvRecipeTitle.text = recipe.title
            tvLikesNumber.text = recipe.aggregateLikes.toString()
            //tvRecipeDescription.text = recipe.summary
            //tvLoveNumber.text = recipe.likes.toString()
            tvClockNumber.text = recipe.readyInMinutes.toString()
            if (recipe.vegan!!) {
                //tvVegan.setTextColor(Color.parseColor("#7FB069"))
                imgVegan.visibility = View.VISIBLE
            } else {
                //tvVegan.setTextColor(Color.WHITE)
                imgVegan.visibility = View.INVISIBLE
            }
            btnDelete.setOnClickListener {
                    viewModel.deleteFavoriteRecipe(recipeList)
                    Snackbar.make(root,
                        "$recipeName successfully deleted.",
                        Snackbar.LENGTH_SHORT).apply {

                        setAction("Undo") {
                            viewModel.insertFavoriteRecipe(recipeList)
                        }
                        show()
                    }

            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(recipeList) }
        }
    }

    private var onItemClickListener: ((FavouritesEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (FavouritesEntity) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}