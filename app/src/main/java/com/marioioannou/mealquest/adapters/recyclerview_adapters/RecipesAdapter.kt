package com.marioioannou.mealquest.adapters.recyclerview_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.RecipesLayoutRow4Binding
import com.marioioannou.mealquest.domain.model.recipes.Result
import me.bush.translator.Translator

class RecipesAdapter() : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    //private var recipes = emptyList<Result>()

    inner class MyViewHolder(val binding: RecipesLayoutRow4Binding) :
        RecyclerView.ViewHolder(binding.root)

//    class MyViewHolder(val binding: RecipesLayoutRow4Binding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(result: Result) {
//            binding.result = result
//            binding.executePendingBindings()
//        }
//
//        companion object {
//            fun from(parent: ViewGroup): MyViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = RecipesLayoutRow4Binding.inflate(layoutInflater, parent, false)
//                return MyViewHolder(binding)
//            }
//        }
//    }

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    //private var recipes = emptyList<com.marioioannou.mealquest.domain.model.recipes.Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //return MyViewHolder.from(parent)
        return MyViewHolder(
            RecipesLayoutRow4Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        val translation = Translator()
        //val recipe = recipes[position]
        //holder.bind(recipe)

//        val loader = ImageLoader(holder.binding.imgFood.context)
//        val req = ImageRequest.Builder(holder.binding.imgFood.context)
//            .data(recipe.image)
//            .target{result ->
//                val bitmap = (result as BitmapDrawable).bitmap
//            }
//            .build()

        holder.binding.apply {

            imgFood.load(recipe.image) {
//                transformations(object : Transformation {
//                    override fun key() = "paletteTransformer"
//
//                    override suspend fun transform(
//                        pool: BitmapPool,
//                        input: Bitmap,
//                        size: Size,
//                    ): Bitmap {
//                        val p = Palette.from(input).generate()
//                        val colorPalette = p.getDominantColor(1)
//                        //cvRecipe.setCardBackgroundColor(p.getDominantColor(1))
//                        val drawable: GradientDrawable = cvRecipe.background as GradientDrawable
//                        drawable.color = colorPalette.v
//                        return input
//                    }
//
//                })
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }

            //imgFood.background(recipe.image.)
            tvRecipeTitle.text = recipe.title
            tvLikesNumber?.text = recipe.aggregateLikes.toString()
            //tvRecipeDescription.text = recipe.summary
            //tvLoveNumber.text = recipe.likes.toString()
            tvClockNumber.text = recipe.readyInMinutes.toString()
            if (recipe.vegan!!) {
                //tvVegan.setTextColor(Color.parseColor("#7FB069"))
                imgVegan?.visibility = View.VISIBLE
            } else {
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
    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }

    fun translate(){

//        val translator = Translator()
//        binding.tvText.text = translator.translateBlocking("Cannellini Bean and Asparagus Salad with Mushrooms",
//            Language.GREEK,
//            Language.ENGLISH).translatedText


        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.GREEK)
            .build()
        val englishGermanTranslator = Translation.getClient(options)

    }
//    fun setData(newData: FoodRecipe){
//        val recipesDiffUtil =
//            RecipesDiffUtil(recipes, newData.results)
//        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
//        recipes = newData.results
//        diffUtilResult.dispatchUpdatesTo(this)
//    }
}