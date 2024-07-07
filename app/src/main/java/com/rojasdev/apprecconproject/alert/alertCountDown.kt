package com.rojasdev.apprecconproject.alert

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.databinding.AlertCountBinding

class alertCountDown(
    var onClickListener: () -> Unit
): DialogFragment() {
    private lateinit var binding: AlertCountBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCountBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cv)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        countDownTimer()

        adsBanner.initLoadAds(binding.banner)


        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.noDeleteDates))
        return dialog
    }

    private fun countDownTimer(){
        val timer = object: CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000
                binding.viewDelete.rotation = (time * 10).toFloat()
                binding.tvCount.text = time.toString()
            }

            override fun onFinish() {
                onClickListener()
            }
        }
        timer.start()
        binding.btReady.setOnClickListener {
            dismiss()
            timer.cancel()
        }
    }

}