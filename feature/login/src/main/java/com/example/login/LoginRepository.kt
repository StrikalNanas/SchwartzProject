package com.example.login

import com.example.login.model.UserKey

internal interface LoginRepository {
    suspend fun checkKey(userKey: UserKey, resultListener: (isValid: Boolean) -> Unit)
}