package com.marioioannou.mealquest.adapters.recyclerview_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.LayoutIngredientsBinding
import com.marioioannou.mealquest.domain.model.ingredients.Ingredient
import com.marioioannou.mealquest.viewmodel.MainViewModel
import java.util.*

class IngredientsAdapter(
    private val viewModel: MainViewModel
): RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: LayoutIngredientsBinding) :
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
            LayoutIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ingredient = differ.currentList[position]
        val ingredientName = ingredient.name
        holder.binding.apply {
            imgFridgeIngredient.load("https://spoonacular.com/cdn/ingredients_500x500/${ingredient.image}") {
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            tvFridgeIngredient.text = ingredientName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
            imgDelete.setOnClickListener {
                viewModel.deleteIngredient(ingredient)
                Snackbar.make(root,
                    "$ingredientName successfully deleted.",
                    Snackbar.LENGTH_SHORT).apply {

                    setAction("Undo") {
                        viewModel.saveIngredient(ingredient)
                    }
                    show()
                }
            }
        }
//        checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//            checkBoxStateArray.put(bindingAdapterPosition, isChecked)
//        })
//        holder.binding.cbFridgeIngredient.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener{ buttonView, isChecked ->
//            selectedList.add(ingredient)
//            this.isChecked = true
//        })
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(ingredient) }
        }
    }

    private var onItemClickListener: ((Ingredient) -> Unit)? = null

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}