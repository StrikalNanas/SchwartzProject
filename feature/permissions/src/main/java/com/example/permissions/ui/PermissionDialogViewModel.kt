package com.example.permissions.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.permissions.PermissionController
import com.example.permissions.R
import com.example.permissions.model.Permission
import com.example.permissions.model.PermissionAccessibilityService
import com.example.permissions.model.PermissionNotification
import com.example.permissions.model.PermissionOverlay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class DialogPermissionViewModel @Inject constructor(
    private val permissionController: PermissionController
) : ViewModel() {

    val permissionDialogUiState: StateFlow<PermissionDialogUiState?> = permissionController.currentRequestPermission
        .map { it?.toPermissionUiDialogState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun isPermissionGranted(context: Context): Boolean {
        val permission = permissionController.currentRequestPermission.value ?: return false
        return permission.checkIsGrantedPermission(context)
    }
    fun initResultLauncherIfNeeded(fragment: Fragment, onResult: (isGranted: Boolean) -> Unit) {
        val permission = permissionController.currentRequestPermission.value ?: return
        if (permission is Permission.DangerousPermission)  {
            permission.initResulLauncher(fragment) { isGranted ->
                onResult(isGranted)
            }
        }
    }

    fun startRequestIfNeeded(context: Context) {
        val permission = permissionController.currentRequestPermission.value ?: return
        permission.onStartPermissionIfNeeded(context)
    }

    fun shouldBeDismissedOnResume(context: Context): Boolean {
        val permission = permissionController.currentRequestPermission.value ?: return true
        return permission is Permission.SpecialPermission && (permission.checkIsGrantedPermission(context))
    }
}

internal data class PermissionDialogUiState(
    val permission: Permission,
    @StringRes val titleRes: Int,
    @StringRes val descRes: Int
)

private fun Permission.toPermissionUiDialogState(): PermissionDialogUiState {
    return when(this) {
        is PermissionOverlay -> PermissionDialogUiState(
            permission = this,
            titleRes = R.string.dialog_permission_overlay_title,
            descRes = R.string.message_permission_desc_overlay
        )

        is PermissionNotification -> PermissionDialogUiState(
            permission = this,
            titleRes = R.string.dialog_permission_notification_title,
            descRes = R.string.message_permission_desc_notification
        )

        is PermissionAccessibilityService -> PermissionDialogUiState(
            permission = this,
            titleRes = R.string.dialog_title_permission_accessibility,
            descRes = R.string.message_permission_desc_accessibility,
        )
    }
}