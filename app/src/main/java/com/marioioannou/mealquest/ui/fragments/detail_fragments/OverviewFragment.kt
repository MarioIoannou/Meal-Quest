package com.marioioannou.mealquest.ui.fragments.detail_fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.FragmentOverviewBinding
import com.marioioannou.mealquest.databinding.FragmentRecipesBinding
import com.marioioannou.mealquest.domain.model.recipes.Result
import com.marioioannou.mealquest.utils.Constants.RECIPE_RESULT_KEY
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>(RECIPE_RESULT_KEY) as Result

        binding.apply {
            imgOverview.load(myBundle.image)
            tvOverviewName.text = myBundle.title
            tvServingNumber.text = myBundle.servings.toString()
            tvMinutesNumber.text = myBundle.readyInMinutes.toString()
            tvLikesNumber.text = myBundle.aggregateLikes.toString()
            tvSummary.text = Jsoup.parse(myBundle.summary!!).text()
            showCorrect(myBundle.vegan,binding.imgCorrectVegan)
            showCorrect(myBundle.vegetarian,binding.imgCorrectVegeterian)
            showCorrect(myBundle.dairyFree,binding.imgCorrectDairyFree)
            showCorrect(myBundle.glutenFree,binding.imgCorrectGlutenFree)
            showCorrect(myBundle.veryHealthy,binding.imgCorrectHealthy)
            showCorrect(myBundle.cheap,binding.imgCorrectCheap)
            tvSource.text = myBundle.sourceName
            tvAuthor.text = myBundle.creditsText
            cvSource.setOnClickListener {
                val url: Uri = Uri.parse(myBundle.sourceUrl)
                val intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(intent)
            }
//            if (myBundle.vegan == true){
//                imgCorrectVegan.visibility = View.VISIBLE
//            }
//            if (myBundle.vegetarian == true){
//                imgCorrectVegeterian.visibility = View.VISIBLE
//            }
//            if (myBundle.dairyFree == true){
//                imgCorrectDairyFree.visibility = View.VISIBLE
//            }
//            if (myBundle.glutenFree == true){
//                imgCorrectGlutenFree.visibility = View.VISIBLE
//            }
//            if (myBundle.veryHealthy == true){
//                imgCorrectHealthy.visibility = View.VISIBLE
//            }
//            if (myBundle.cheap == true){
//                imgCorrectCheap.visibility = View.VISIBLE
//            }
        }
    }

    private fun showCorrect(isTrue: Boolean?,imageView: ImageView){
        if (isTrue == true){
            imageView.visibility = View.VISIBLE
        }
    }
}