package com.marioioannou.mealquest.adapters.recyclerview_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.RandomRecipesLayoutRowBinding
import com.marioioannou.mealquest.databinding.RecipesLayoutRow4Binding
import com.marioioannou.mealquest.domain.model.recipes.Result

class RandomRecipesAdapter() : RecyclerView.Adapter<RandomRecipesAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: RandomRecipesLayoutRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //return MyViewHolder.from(parent)
        return MyViewHolder(
            RandomRecipesLayoutRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = differ.currentList[position]

        holder.binding.apply {

            imgFood.load(recipe.image) {

                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }


            tvRecipeTitle.text = recipe.title
            tvLikesNumber.text = recipe.aggregateLikes.toString()

            tvClockNumber.text = recipe.readyInMinutes.toString()
            if (recipe.vegan!!) {
                imgVegan.visibility = View.VISIBLE
            } else {
                imgVegan.visibility = View.INVISIBLE
            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(recipe) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Result) -> Unit)? = null
    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }
}