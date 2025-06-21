package com.rojasdev.apprecconproject.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rojasdev.apprecconproject.ActivityMainModule
import com.rojasdev.apprecconproject.MainActivity
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.recconApp
import java.net.HttpURLConnection
import java.net.URL

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class CloudMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        showNotification(message)
    }

    private fun showNotification(message: RemoteMessage) {
        val pendingIntent = createPendingIntent(message.data["url"])
        val bitmap = downloadImage(message.data["imageUrl"])
        val notificationBuilder =
            NotificationCompat.Builder(this, recconApp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo_notification)
                .setContentTitle(message.notification?.title)
                .setContentText(message.notification?.body)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
                .setAutoCancel(true)

        if (bitmap != null) {
            val style = NotificationCompat.BigPictureStyle().bigPicture(bitmap)
            notificationBuilder.setStyle(style)
        } else Log.e("Imagen", "no se encontro la imagen de las notificaciones $bitmap")

        val notification = notificationBuilder.build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(recconApp.NOTIFICATION_ID.nextInt(), notification)
    }

    private fun createPendingIntent(url: String?): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("url", url)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        return PendingIntent.getActivity(this, 0, intent, flag)
    }

    private fun downloadImage(imageUrl: String?): Bitmap? {
        return try {
            imageUrl?.let {
                val url = URL(it)
                val connection = url.openConnection() as HttpURLConnection
                connection.apply {
                    doInput = true
                    connect()
                }
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}