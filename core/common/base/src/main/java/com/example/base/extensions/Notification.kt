package com.example.base.extensions

import android.app.Notification
import android.app.Service
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.ServiceCompat

fun Service.startForegroundNotification(notificationId: Int, notification: Notification) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
            ServiceCompat.startForeground(
                this,
                notificationId,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION
            )
        else -> startForeground(notificationId, notification)
    }
}