package com.rojasdev.apprecconproject

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.rojasdev.apprecconproject.controller.recconApp
import com.rojasdev.apprecconproject.notification.AlarmNotifications
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupPermissionLaunchers()
        checkPermissions()

        val url = intent.getStringExtra("url")
        if (url != null) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } else {
            Log.e("ActivityUrl", "No se encontró URL")
        }

        startActivity(Intent(this, ActivityMainModule::class.java))
        finish()
    }

    private fun setupPermissionLaunchers() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            val allGranted = result.all { it.value }
            if (allGranted) {
                scheduleNotification()
            } else {
                showAlertPermissions()
            }
        }

        notificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (!isGranted) {
                showPermissionDeniedMessage()
            }
        }
    }

    private fun checkPermissions() {
        val requiredPermissions = mutableListOf<String>()

        // Solo para Android 9 o menor
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requiredPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        // Notificaciones (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (requiredPermissions.isNotEmpty()) {
            permissionLauncher.launch(requiredPermissions.toTypedArray())
        } else {
            scheduleNotification()
        }
    }

    private fun scheduleNotification() {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) {
            Log.i("Notification", "No tienes permiso para alarmas exactas.")
            val intentSettings = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intentSettings)
            return
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
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun showPermissionDeniedMessage() {
        AlertDialog.Builder(this)
            .setTitle("Permiso de Notificaciones Denegado")
            .setMessage("Para recibir notificaciones, por favor concede el permiso en la configuración.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showAlertPermissions() {
        AlertDialog.Builder(this)
            .setTitle("Permisos Requeridos")
            .setMessage("Esta aplicación necesita permisos de lectura/escritura para funcionar correctamente.")
            .setPositiveButton("Ir a configuración") { _, _ ->
                startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    }
                )
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

}

