package com.example.project.ui.menu

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.permissions.PermissionController
import com.example.permissions.model.PermissionAccessibilityService
import com.example.permissions.model.PermissionNotification
import com.example.permissions.model.PermissionOverlay
import com.example.project.service.capture.ScreenCaptureService
import com.example.project.service.touch.TouchService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MenuViewModel @Inject constructor(
    private val applicationContext: Application,
    private val permissionController: PermissionController
) : ViewModel() {

    fun onStartPermissions(activity: AppCompatActivity, onAllGranted: () -> Unit) {
        permissionController.startPermission(
            activity = activity,
            permissions = listOf(
                PermissionOverlay,
                PermissionAccessibilityService(
                    componentName = ComponentName(activity, TouchService::class.java),
                    isServiceGranted = { context ->
                        TouchService.isAccessibilityServiceEnabled(context)
                    }
                ),
                PermissionNotification
            ),
            onAllGranted = onAllGranted
        )
    }

    fun startScreenCaptureService(resultCode: Int, data: Intent) {
        ContextCompat.startForegroundService(
            applicationContext,
            ScreenCaptureService.getIntent(applicationContext)
        )
    }
}

