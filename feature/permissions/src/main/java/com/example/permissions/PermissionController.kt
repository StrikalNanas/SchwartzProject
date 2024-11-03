package com.example.permissions

import androidx.appcompat.app.AppCompatActivity
import com.example.permissions.model.Permission
import com.example.permissions.ui.PermissionDialogFragment
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@ActivityRetainedScoped
class PermissionController @Inject constructor() {

    private val requestNeededPermission: MutableSet<Permission> = mutableSetOf()

    private val _currentRequestPermission: MutableStateFlow<Permission?> = MutableStateFlow(null)
    val currentRequestPermission: StateFlow<Permission?> = _currentRequestPermission

    private var onAllGrantedCallback: (() -> Unit)? = null

    fun startPermission(
        activity: AppCompatActivity,
        permissions: List<Permission>,
        onAllGranted: () -> Unit
    ) {
        onAllGrantedCallback = onAllGranted
        requestNeededPermission.clear()

        if (permissions.isEmpty()) {
            notifyOnAllGranted()
            return
        }

        permissions.forEach { permission ->
            if (!permission.checkIsGrantedPermission(activity)) requestNeededPermission.add(permission)
        }

        handleNextPermission(activity)
    }

    private fun handleNextPermission(activity: AppCompatActivity) {
        if (requestNeededPermission.isEmpty()) {
            notifyOnAllGranted()
            return
        }

        val nextPermission = requestNeededPermission.pupFirst()
        activity.showPermissionDialogFragment(nextPermission) { isGranted ->
            if (isGranted) handleNextPermission(activity)
        }
    }

    private fun MutableSet<Permission>.pupFirst() =
        first().also(::remove)

    private fun AppCompatActivity.showPermissionDialogFragment(permission: Permission, resultListener: (isGranted: Boolean) -> Unit) {
        supportFragmentManager.setFragmentResultListener(FRAGMENT_RESULT_KEY_PERMISSION_STATE, this) { _, bundle ->
            _currentRequestPermission.value = null
            resultListener(bundle.getBoolean(EXTRA_RESULT_KEY_PERMISSION_STATE))
        }

        _currentRequestPermission.value = permission
        PermissionDialogFragment().show(supportFragmentManager, FRAGMENT_TAG_PERMISSION_DIALOG)
    }

    private fun clear() {
        onAllGrantedCallback = null
        _currentRequestPermission.value = null
        requestNeededPermission.clear()
    }

    private fun notifyOnAllGranted() {
        onAllGrantedCallback?.invoke()
        clear()
    }
}

internal const val FRAGMENT_TAG_PERMISSION_DIALOG = "PermissionDialog"
internal const val FRAGMENT_RESULT_KEY_PERMISSION_STATE = ":$FRAGMENT_TAG_PERMISSION_DIALOG:state"
internal const val EXTRA_RESULT_KEY_PERMISSION_STATE = "$FRAGMENT_RESULT_KEY_PERMISSION_STATE:isGranted"
