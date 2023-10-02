package com.marioioannou.mealquest.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.adapters.PagerAdapter
import com.marioioannou.mealquest.databinding.ActivityDetailsBinding
import com.marioioannou.mealquest.domain.database.recipes_database.entities.FavouritesEntity
import com.marioioannou.mealquest.ui.fragments.detail_fragments.InstructionsFragment
import com.marioioannou.mealquest.ui.fragments.detail_fragments.OverviewFragment
import com.marioioannou.mealquest.ui.fragments.detail_fragments.UsedIngredientsFragment
import com.marioioannou.mealquest.utils.Constants.RECIPE_RESULT_KEY
import com.marioioannou.mealquest.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    val args: DetailsActivityArgs by navArgs()
    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId = 0

    private var mInterstitialAd: InterstitialAd? = null

    //Main
//    private var interstitialAdUnitId = "ca-app-pub-2379578394910008/3557173841"
//    //Banner = ca-app-pub-2379578394910008/4668727927

    //Test
    private var interstitialAdUnitId = "ca-app-pub-3940256099942544/1033173712"
    //Banner = ca-app-pub-3940256099942544/6300978111

    private lateinit var menuItem: MenuItem

    private var TAG = "DetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadAdmobInterstitialAd(this, TAG)
        Handler(Looper.getMainLooper()).postDelayed({
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
            }
        }, 1000L)
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adViewDetails.loadAd(adRequest)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.theme_primary))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(UsedIngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add(R.string.overview.toString())
        titles.add(R.string.ingredients.toString())
        titles.add(R.string.instruction.toString())

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )

        //binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.apply {
            offscreenPageLimit = 3
            adapter = pagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            //setCurrentItem(3,true)
            beginFakeDrag()
            fakeDragBy(-10f)
            endFakeDrag()
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_favorites_menu)
        checkSavedRecipes(menuItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorites_menu && !recipeSaved) {
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_to_favorites_menu && recipeSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this) { favoritesEntity ->
            try {
                for (recipe in favoritesEntity) {
                    if (recipe.result.id == args.result.id) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeId = recipe.id
                        recipeSaved = true
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        }
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity =
            FavouritesEntity(
                0,
                args.result
            )
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe", "successfully saved.", favoritesEntity.result.title.toString())
        recipeSaved = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoritesEntity =
            FavouritesEntity(
                savedRecipeId,
                args.result
            )
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.my_grey)
        showSnackBar("", "removed from Favorites.", favoritesEntity.result.title.toString())
        recipeSaved = false
    }

    private fun showSnackBar(messageFront: String, messageBack: String, recipeName: String) {
        Snackbar.make(binding.root,
            "$messageFront $recipeName $messageBack.",
            Snackbar.LENGTH_SHORT).setAction("Okay") {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon?.setTint(ContextCompat.getColor(this, color))
    }

    private fun loadAdmobInterstitialAd(context: Context, TAG: String) {

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(context, interstitialAdUnitId, adRequest, object :
            InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem, R.color.my_grey)
    }
}