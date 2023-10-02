package com.marioioannou.mealquest.ui.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.dataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.databinding.ActivityMainBinding
import com.marioioannou.mealquest.ui.fragments.recipes_fragment.RecipesViewModel
import com.marioioannou.mealquest.utils.NetworkListener
import com.marioioannou.mealquest.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var networkListener: NetworkListener

    //Main BANNER = ca-app-pub-2379578394910008/9460506714
    //Test BANNER = ca-app-pub-3940256099942544/6300978111

    val viewModel: MainViewModel by viewModels()
    val recipesViewModel: RecipesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(this@MainActivity)
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    if (!status){
                        //showNoInternetLayout()
                        disconnected()
                    }else{
                        connected()
                    }
                }
        }

        binding.wifiSettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        MobileAds.initialize(this){}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        val sharedPreference =
            binding.root.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val theme = sharedPreference.getString("theme", "system default")

        themeMode(theme!!)

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.recipesFragment,
                R.id.fridgeFragment,
                R.id.favouriteFragment,
                R.id.triviaFragment
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.recipesFragment -> {
                    showBottomNav()
                    showToolbar()
                }
                R.id.fridgeFragment -> {
                    showBottomNav()
                    showToolbar()
                }
                R.id.favouriteFragment -> {
                    showBottomNav()
                    showToolbar()
                }
                R.id.triviaFragment -> {
                    showBottomNav()
                    showToolbar()
                }
                R.id.recipesBottomSheet -> {
                    showBottomNav()
                    showToolbar()
                }
                R.id.searchIngredientsFragment -> {
                    hideBottomNav()
                    hideToolbar()
                }
                else -> {
                    hideBottomNav()
                    showToolbar()
                }
            }
        }

        binding.bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun showToolbar() {
        binding.toolbar.visibility = View.VISIBLE
    }

    private fun hideToolbar() {
        binding.toolbar.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun disconnected() {
        binding.bottomNavigationView.visibility = View.GONE
        binding.navHostFragment.visibility = View.GONE
        binding.layoutNoInternet.visibility = View.VISIBLE
    }

    private fun connected() {
        binding.bottomNavigationView.visibility = View.VISIBLE
        binding.navHostFragment.visibility = View.VISIBLE
        binding.layoutNoInternet.visibility = View.GONE
    }

    private fun themeMode(string: String){
        when(string.lowercase()){
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "system default" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

//    private fun showNoInternetLayout() {
//        val dialog = layoutInflater.inflate(R.layout.no_internet_dialog,null)
//        val myDialog = Dialog(this)
//        myDialog.setContentView(dialog)
//        myDialog.setCancelable(true)
//        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        myDialog.show()
//
//        val btnClose = myDialog.findViewById<TextView>(R.id.btn_ok_internet)
//        btnClose.setOnClickListener{
//            myDialog.dismiss()
//        }
//    }
}

//navController.addOnDestinationChangedListener { _, destination, _ ->
//    when (destination.id) {
//        R.id.mainFragment -> showBottomNav()
//        R.id.mineFragment -> showBottomNav()
//        else -> hideBottomNav()
//    }
//}
//}
//
//private fun showBottomNav() {
//    bottomNav.visibility = View.VISIBLE
//
//}
//
//private fun hideBottomNav() {
//    bottomNav.visibility = View.GONE
//
//}