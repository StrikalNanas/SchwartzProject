package com.example.project.service.capture

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.example.base.extensions.startForegroundNotification
import com.example.project.NotificationManager

class ScreenCaptureService : Service() {

    private lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        notificationManager = NotificationManager(this)
        val notification = notificationManager.createNotification(this)

        startForegroundNotification(
            NotificationManager.NOTIFICATION_ID,
            notification
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.destroyNotification()
    }

    companion object {
        fun getIntent(context: Context) =
            Intent(context, ScreenCaptureService::class.java)
    }
}