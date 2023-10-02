package com.marioioannou.mealquest.utils

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdMobUtil {

    private var mInterstitialAd: InterstitialAd? = null
    private var interstitialAdUnitId = "ca-app-pub-3940256099942544/1033173712"

    fun loadAdmobInterstitialAd(context: Context,TAG: String) {

        var adRequest = AdRequest.Builder().build()
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

//        interstitialAd = InterstitialAd(context)
//
//        interstitialAd.adUnitId = interstitialAdUnitId
//
//        interstitialAd.load(AdRequest.Builder().build())

    }

//    fun showInterstitialAd() {
//        if (interstitialAd.isLoaded) interstitialAd.show()
//    }
}