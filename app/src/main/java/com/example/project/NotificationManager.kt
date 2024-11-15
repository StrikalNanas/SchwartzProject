package com.example.project

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationManager(context: Context){

    companion object {
        internal const val NOTIFICATION_ID = 42
        private const val NOTIFICATION_CHANNEL_ID = "SchwartzService"
    }

    private val notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    fun createNotification(
        context: Context,
        notificationTitle: String,
        notificationText: String,
        notificationIntent: PendingIntent
    ): Notification {
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

    fun destroyNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context): NotificationChannel =
        NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
}