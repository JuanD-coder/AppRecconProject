package com.rojasdev.apprecconproject.controller

import android.app.Application
import android.content.Context
import com.google.android.gms.ads.MobileAds
import io.monedata.Monedata

class recconApp: Application(){
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)

    }
}