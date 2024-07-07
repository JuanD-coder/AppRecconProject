package com.rojasdev.apprecconproject.controller

import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

object adsBanner {
    fun initLoadAds(banner: AdView) {
        val adRequest = AdRequest.Builder().build()
        banner.loadAd(adRequest)
        banner.adListener = object : AdListener(){
            override fun onAdLoaded() {
               banner.visibility = View.VISIBLE
            }
            override fun onAdFailedToLoad(adError : LoadAdError) {
            }
            override fun onAdOpened() {
            }
            override fun onAdClicked() {
            }
            override fun onAdClosed() {
                initLoadAds(banner)
            }
        }
    }
}