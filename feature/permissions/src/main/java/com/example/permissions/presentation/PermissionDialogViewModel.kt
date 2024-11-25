package com.example.permissions.presentation

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.permissions.PermissionController
import com.example.permissions.R
import com.example.permissions.domain.model.Permission
import com.example.permissions.domain.model.PermissionAccessibilityService
import com.example.permissions.domain.model.PermissionNotification
import com.example.permissions.domain.model.PermissionOverlay
import com.example.permissions.domain.usecase.PermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class DialogPermissionViewModel @Inject constructor(
    private val permissionController: PermissionController,
    private val permissionUseCase: PermissionUseCase
) : ViewModel() {

    val permissionDialogUiState: StateFlow<PermissionDialogUiState?> = permissionController.currentRequestPermission
        .map { it?.toPermissionUiDialogState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun isPermissionGranted(context: Context): Boolean {
        val permission = permissionController.currentRequestPermission.value ?: return false
        return permissionUseCase.isPermissionGranted(permission, context)
    }

    fun initResultLauncherIfNeeded(fragment: Fragment, onResult: (isGranted: Boolean) -> Unit) {
        val permission = permissionController.currentRequestPermission.value ?: return
        if (permission is Permission.DangerousPermission)  {
            permission.initResulLauncher(fragment) { isGranted ->
                onResult(isGranted)
            }
        }
    }

    fun requestPermission(context: Context) {
        val permission = permissionController.currentRequestPermission.value ?: return
        permissionUseCase.requestPermission(permission, context)
    }

    fun shouldBeDismissedOnResume(context: Context): Boolean {
        val permission = permissionController.currentRequestPermission.value ?: return true
        return permission is Permission.SpecialPermission && (permissionUseCase.isPermissionGranted(permission, context))
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