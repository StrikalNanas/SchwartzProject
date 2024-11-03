package com.example.permissions.model

import android.content.Context
import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager

@SuppressLint("InlinedApi")
data object PermissionNotification : Permission.DangerousPermission() {

    override val permissionString: String
        get() = Manifest.permission.POST_NOTIFICATIONS

    override fun isPermissionGranted(context: Context): Boolean =
        context.getSystemService(NotificationManager::class.java).areNotificationsEnabled()
}