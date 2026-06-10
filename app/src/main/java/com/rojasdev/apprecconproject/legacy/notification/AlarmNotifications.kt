package com.rojasdev.apprecconproject.legacy.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.rojasdev.apprecconproject.R

class AlarmNotifications : android.content.BroadcastReceiver() {

    override fun onReceive(context: Context, p1: Intent?) {
        createSimpleNotification(context)
    }

    private fun createSimpleNotification(context: Context) {
        val intent = Intent(context, _root_ide_package_.com.rojasdev.apprecconproject.legacy.ActivityInformes::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        val notification = NotificationCompat.Builder(context, _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.recconApp.Companion.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_reccon_round)
            .setContentTitle("Recuerda generear tu infome de recolecion")
            .setContentText("NO pierdad tus datos y genera un hisorial de recoleciones")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Genera tu informe al teminar la recoleccion para no perder tus datos y generear un historial de tus gastos")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
            .setContentIntent(pendingIntent)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(_root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.recconApp.Companion.NOTIFICATION_ID.nextInt(), notification)

    }

}