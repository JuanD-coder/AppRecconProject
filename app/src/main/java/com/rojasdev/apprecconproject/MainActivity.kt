package com.rojasdev.apprecconproject

import android.Manifest
import android.content.pm.PackageManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.rojasdev.apprecconproject.legacy.controller.recconApp
import com.rojasdev.apprecconproject.legacy.notification.AlarmNotifications
import android.provider.Settings
import com.rojasdev.apprecconproject.legacy.ActivityMainModule
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, ActivityMainModule::class.java))

        val url = intent.getStringExtra("url")
        if (url != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } else {
            Log.e("ActivityUrl", "No se encontro url")
        }

        askNotificationPermission()
        sheduleNotification()
    }

    private fun sheduleNotification() {
        val intent = Intent(applicationContext, AlarmNotifications::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            recconApp.NOTIFICATION_ID.nextInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        checkAndScheduleAlarm(pendingIntent)

    }

    private fun checkAndScheduleAlarm(pendingIntent: PendingIntent) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms().not()) {
                Log.i("Notification error", "No tienes permiso para programar alarmas exactas")
                val intentSettings = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intentSettings)
                return
            }
        }

        try {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                getNextAlarmTime(),
                pendingIntent
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
            Log.i("Alarm Error", "Error al programar la alarma: ${e.message}")
        }

    }

    private fun getNextAlarmTime(): Long {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysUntilSaturday = if (dayOfWeek < Calendar.SATURDAY) {
            Calendar.SATURDAY - dayOfWeek
        } else {
            7 - dayOfWeek + Calendar.SATURDAY
        }
        calendar.add(Calendar.DAY_OF_YEAR, daysUntilSaturday)
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND,0)
        return calendar.timeInMillis
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            showPermissionDeniedMessage()
        }
    }

    private fun showPermissionDeniedMessage() {
        AlertDialog.Builder(this)
            .setTitle("Permiso de Notificaciones Denegado")
            .setMessage("Para recibir notificaciones, por favor concede el permiso de notificaciones en la configuración de la aplicación.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
