package com.example.permissions.domain.model

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.Settings

data class PermissionAccessibilityService(
    private val isServiceGranted: (context: Context) -> Boolean,
) : Permission.SpecialPermission() {

    override fun isPermissionGranted(context: Context): Boolean =
        isServiceGranted(context)

    override fun onStartRequest(context: Context): Boolean {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)

        return try {
            context.startActivity(intent)
            true
        } catch (error: ActivityNotFoundException) {
            false
        }
    }
}