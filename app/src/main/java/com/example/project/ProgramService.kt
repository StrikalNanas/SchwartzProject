package com.example.project

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import androidx.annotation.RequiresApi
import com.example.base.extensions.startForegroundMediaProjection

class ProgramService : AccessibilityService() {

    companion object {
        fun isAccessibilityServiceEnabled(context: Context): Boolean {
            val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
            val enabledServices: List<AccessibilityServiceInfo> = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

            for (enabledService in enabledServices) {
                val enabledServiceInfo: ServiceInfo = enabledService.resolveInfo.serviceInfo
                if (enabledServiceInfo.packageName == context.packageName && enabledServiceInfo.name == ProgramService::class.java.name) {
                    return true
                }
            }

            return false
        }
    }

    private lateinit var localService: ILocalService
    private lateinit var serviceNotificationManager: ServiceNotificationManager

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onServiceConnected() {
        super.onServiceConnected()

        serviceNotificationManager = ServiceNotificationManager(this)

        localService = LocalService(
            onStart = {
                startForegroundMediaProjection(
                    notificationId = ServiceNotificationManager.NOTIFICATION_ID,
                    notification = serviceNotificationManager.createNotification(this)
                )
            },
            onStop = {
                stopForeground(STOP_FOREGROUND_REMOVE)
                serviceNotificationManager.destroyNotification()
            }
        )

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) { TODO("Not yet implemented") }
    override fun onInterrupt() { TODO("Not yet implemented") }
}

interface ILocalService {
    fun startProjection(result: Int, data: Intent)
    fun stopProjection()
}
