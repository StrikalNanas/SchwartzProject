package com.example.project.activity.menu

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.base.extensions.showShortToast
import com.example.permissions.PermissionController
import com.example.permissions.model.PermissionAccessibilityService
import com.example.permissions.model.PermissionNotification
import com.example.permissions.model.PermissionOverlay
import com.example.project.ILocalService
import com.example.project.ProgramService
import com.example.project.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MenuViewModel @Inject constructor(
    private val permissionController: PermissionController,
    private var localService: ILocalService
) : ViewModel() {

    fun onStartPermissions(activity: AppCompatActivity, onAllGranted: () -> Unit) {
        permissionController.startPermission(
            activity = activity,
            permissions = listOf(
                PermissionOverlay,
                PermissionAccessibilityService(
                    componentName = ComponentName(activity, ProgramService::class.java),
                    isServiceGranted = { context ->
                        ProgramService.isAccessibilityServiceEnabled(context)
                    }
                ),
                PermissionNotification
            ),
            onAllGranted = onAllGranted
        )
    }

    fun startSchwartzService(context: Context, resultCode: Int, data: Intent) {
        localService.startProjection(resultCode, data)
        context.showShortToast(R.string.app_name)
    }
}