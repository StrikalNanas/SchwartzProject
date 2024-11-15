package com.example.project.service.touch

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import androidx.annotation.RequiresApi

class TouchService : AccessibilityService()  {

    companion object {
        fun isAccessibilityServiceEnabled(context: Context): Boolean {
            val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
            val enabledServices: List<AccessibilityServiceInfo> = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

            return enabledServices.any { serviceInfo ->
                serviceInfo.resolveInfo.serviceInfo.packageName == context.packageName &&
                        serviceInfo.resolveInfo.serviceInfo.name == TouchService::class.java.name
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) { TODO("Not yet implemented") }
    override fun onInterrupt() { TODO("Not yet implemented") }
}