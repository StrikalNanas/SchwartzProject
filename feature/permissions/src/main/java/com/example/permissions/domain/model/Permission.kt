package com.example.permissions.domain.model

import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

sealed class Permission {

    protected abstract fun onStartRequest(context: Context): Boolean
    protected abstract fun isPermissionGranted(context: Context): Boolean

    internal fun onStartPermissionIfNeeded(context: Context): Boolean {
        return onStartRequest(context)
    }

    internal fun checkIsGrantedPermission(context: Context): Boolean {
        return isPermissionGranted(context)
    }

    sealed class SpecialPermission : Permission()

    sealed class DangerousPermission : Permission() {
        private var permissionLauncher: ActivityResultLauncher<String>? = null
        protected abstract val permissionString: String

        internal fun initResulLauncher(fragment: Fragment, onResult: (isGranted: Boolean) -> Unit) {
            permissionLauncher = fragment.registerForActivityResult(
                ActivityResultContracts.RequestPermission()) { granted ->
                onResult(granted)
            }
        }

        override fun onStartRequest(context: Context): Boolean =
            permissionLauncher?.let { activityResultLauncher ->
                try {
                    activityResultLauncher.launch(permissionString)
                    true
                } catch (error: IllegalStateException) {
                    false
                }
            } ?: false
    }
}