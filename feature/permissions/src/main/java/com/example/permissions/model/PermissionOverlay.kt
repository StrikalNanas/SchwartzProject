package com.example.permissions.model

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("InlinedApi")
data object PermissionOverlay : Permission.SpecialPermission() {

    override fun onStartRequest(context: Context): Boolean {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)

        return try {
            context.startActivity(intent)
            true
        } catch (error: ActivityNotFoundException) {
            Log.e(TAG, error.toString())
            false
        }
    }

    override fun isPermissionGranted(context: Context): Boolean =
        Settings.canDrawOverlays(context)
}