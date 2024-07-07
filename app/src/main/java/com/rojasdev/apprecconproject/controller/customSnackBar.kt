package com.rojasdev.apprecconproject.controller

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.SnackbarBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object customSnackBar {
    @SuppressLint("SuspiciousIndentation")
    fun showCustomSnackBar(view: android.view.View, message: String) {
        val snackBar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)

        // Personaliza la vista de la Snackbar
        val snackBarView = snackBar.view
        val snackBarLayout = snackBarView as Snackbar.SnackbarLayout
            snackBarLayout.setBackgroundColor(ContextCompat.getColor(view.context, R.color.transparent))

        // Infla el diseño de la Snackbar personalizada utilizando ViewBinding
        val binding = SnackbarBinding.inflate(LayoutInflater.from(view.context))
            binding.tv.text = message

        // Agrega la vista personalizada a la Snackbar
        snackBarLayout.addView(binding.root)

        val mp = MediaPlayer.create(view.context, R.raw.notification_all_tasks_completed)
            mp.start()

        snackBar.show()
    }
}