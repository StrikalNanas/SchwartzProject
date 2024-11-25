package com.example.permissions.domain.repository

import android.content.Context
import com.example.permissions.domain.model.Permission

interface PermissionRepository {

    fun requestPermission(permission: Permission, context: Context): Boolean
    fun isPermissionGranted(permission: Permission, context: Context): Boolean
}