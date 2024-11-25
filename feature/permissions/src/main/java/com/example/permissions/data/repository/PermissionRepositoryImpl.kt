package com.example.permissions.data.repository

import android.content.Context
import com.example.permissions.domain.model.Permission
import com.example.permissions.domain.repository.PermissionRepository
import javax.inject.Inject

class PermissionRepositoryImpl @Inject constructor() : PermissionRepository {

    override fun requestPermission(permission: Permission, context: Context): Boolean {
        return permission.onStartPermissionIfNeeded(context)
    }

    override fun isPermissionGranted(permission: Permission, context: Context): Boolean {
        return permission.checkIsGrantedPermission(context)
    }
}