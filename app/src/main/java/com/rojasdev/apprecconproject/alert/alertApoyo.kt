package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.customSnackBar
import com.rojasdev.apprecconproject.databinding.AlertApoyoBinding

class alertApoyo(): DialogFragment() {
    private lateinit var binding: AlertApoyoBinding

    private var interstitial: InterstitialAd? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertApoyoBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cv)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        adsBanner.initLoadAds(binding.banner)

        initAds()

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btAds.setOnClickListener {
            showAds()
            initAds()
        }

        binding.btFacebook.setOnClickListener {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/profile.php?id=61550862172662")
                )
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                customSnackBar.showCustomSnackBar(requireView(),getString(R.string.noFacebook))
            }
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun initAds() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(), "ca-app-pub-6507674390958957/4557504758",adRequest, object : InterstitialAdLoadCallback(){
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                interstitial = interstitialAd
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                interstitial = null
            }
        })
    }

    fun showAds(){
        interstitial?.show(requireActivity())
    }
}