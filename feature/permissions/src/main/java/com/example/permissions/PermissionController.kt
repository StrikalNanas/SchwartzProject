package com.example.permissions

import androidx.appcompat.app.AppCompatActivity
import com.example.permissions.domain.model.Permission
import com.example.permissions.presentation.PermissionDialogFragment
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@ActivityRetainedScoped
class PermissionController @Inject constructor() {

    private val _currentRequestPermission: MutableStateFlow<Permission?> = MutableStateFlow(null)
    val currentRequestPermission: StateFlow<Permission?> = _currentRequestPermission

    private var onAllGrantedCallback: (() -> Unit)? = null

    fun startPermission(
        activity: AppCompatActivity,
        permissions: List<Permission>,
        onAllGranted: () -> Unit
    ) {
        onAllGrantedCallback = onAllGranted
        val requestNeededPermission = permissions.filter { !it.checkIsGrantedPermission(activity) }

        if (requestNeededPermission.isEmpty()) {
            onAllGranted()
        } else {
            requestNextPermission(activity, requestNeededPermission)
        }
    }

    private fun requestNextPermission(activity: AppCompatActivity, permissions: List<Permission>) {
        val nextPermission = permissions.first()
        activity.showPermissionDialogFragment(nextPermission) { isGranted ->
            if (isGranted) {
                val remainingPermissions = permissions.drop(1)
                if (remainingPermissions.isNotEmpty()) {
                    requestNextPermission(activity, remainingPermissions)
                } else {
                    onAllGrantedCallback?.invoke()
                    onAllGrantedCallback = null
                    _currentRequestPermission.value = null
                }
            }
        }
    }

    private fun AppCompatActivity.showPermissionDialogFragment(permission: Permission, resultListener: (isGranted: Boolean) -> Unit) {
        supportFragmentManager.setFragmentResultListener(FRAGMENT_RESULT_KEY_PERMISSION_STATE, this) { _, bundle ->
            _currentRequestPermission.value = null
            resultListener(bundle.getBoolean(EXTRA_RESULT_KEY_PERMISSION_STATE))
        }

        _currentRequestPermission.value = permission
        PermissionDialogFragment().show(supportFragmentManager, FRAGMENT_TAG_PERMISSION_DIALOG)
    }
}

const val FRAGMENT_TAG_PERMISSION_DIALOG = "PermissionDialog"
const val FRAGMENT_RESULT_KEY_PERMISSION_STATE = ":$FRAGMENT_TAG_PERMISSION_DIALOG:state"
const val EXTRA_RESULT_KEY_PERMISSION_STATE = "$FRAGMENT_RESULT_KEY_PERMISSION_STATE:isGranted"
