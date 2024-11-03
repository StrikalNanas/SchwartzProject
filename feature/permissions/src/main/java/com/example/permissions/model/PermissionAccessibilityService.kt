package com.example.permissions.model

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log

data class PermissionAccessibilityService(
    private val componentName: ComponentName,
    private val isServiceGranted: (context: Context) -> Boolean,
) : Permission.SpecialPermission() {

    override fun isPermissionGranted(context: Context): Boolean =
        isServiceGranted(context)

    override fun onStartRequest(context: Context): Boolean {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)

        val bundle = Bundle()
        val showArgs = componentName.flattenToString()
        bundle.putString(EXTRA_ACCESSIBILITY_FRAGMENT_ARG_KEY, showArgs)
        intent.putExtra(EXTRA_ACCESSIBILITY_FRAGMENT_ARG_KEY, showArgs)
        intent.putExtra(EXTRA_SHOW_ACCESSIBILITY_FRAGMENT_ARGUMENTS, bundle)

        return try {
            context.startActivity(intent)
            true
        } catch (error: ActivityNotFoundException) {
            false
        }
    }
}

private const val EXTRA_ACCESSIBILITY_FRAGMENT_ARG_KEY = ":settings:fragment_args_key"
private const val EXTRA_SHOW_ACCESSIBILITY_FRAGMENT_ARGUMENTS = ":settings:show_fragment_args"