package com.example.project

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.project.activity.SchwartzActivity

class ServiceNotificationManager(context: Context){

    companion object {
        internal const val NOTIFICATION_ID = 42
        private const val NOTIFICATION_CHANNEL_ID = "SchwartzService"
    }

    private val notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    private val notificationTitle: String =
        context.getString(R.string.notification_app_name)
    private val notificationText: String =
        context.getString(R.string.notification_content_text)

    private val notificationIntent: PendingIntent =
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, SchwartzActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

    private var notificationBuilder: Notification? = null

    fun createNotification(context: Context): Notification {
        val builder = notificationBuilder ?: notificationBuilder(context)
        notificationBuilder = builder
        return builder
    }

    fun destroyNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
        notificationBuilder = null
    }

    private fun notificationBuilder(context: Context): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createNotificationChannel(context))
        }

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setContentIntent(notificationIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setLocalOnly(true)
            .setOngoing(true)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context): NotificationChannel =
        NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
}