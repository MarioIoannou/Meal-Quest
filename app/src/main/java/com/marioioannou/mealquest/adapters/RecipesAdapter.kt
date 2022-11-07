package com.marioioannou.mealquest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.RecipesLayoutRow4Binding
import com.marioioannou.mealquest.domain.model.recipes.Result

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: RecipesLayoutRow4Binding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: com.marioioannou.mealquest.domain.model.recipes.Result, newItem: Result): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    //private var recipes = emptyList<com.marioioannou.mealquest.domain.model.recipes.Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RecipesLayoutRow4Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        //val recipe = recipes[position]
        holder.binding.apply {
            imgFood.load(recipe.image){
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            tvRecipeTitle.text = recipe.title
            tvLikesNumber?.text = recipe.aggregateLikes.toString()
            //tvRecipeDescription.text = recipe.summary
            //tvLoveNumber.text = recipe.likes.toString()
            tvClockNumber.text = recipe.readyInMinutes.toString()
            if (recipe.vegan!!){
                //tvVegan.setTextColor(Color.parseColor("#7FB069"))
                imgVegan?.visibility = View.VISIBLE
            }else{
                //tvVegan.setTextColor(Color.WHITE)
                imgVegan?.visibility = View.INVISIBLE
            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(recipe) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
        //return recipes.size
    }

    private var onItemClickListener: ((Result) -> Unit)? = null
    fun setOnItemClickListener(listener: (Result) -> Unit){
        onItemClickListener = listener
    }

//    fun setData(newData: FoodRecipe){
//        val recipesDiffUtil =
//            RecipesDiffUtil(recipes, newData.results)
//        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
//        recipes = newData.results
//        diffUtilResult.dispatchUpdatesTo(this)
//    }
}