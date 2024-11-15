package com.example.domain.repository

import com.example.domain.model.UserKey

interface FireStoreRepository {
    suspend fun checkUserKey(userKey: UserKey): Result<Boolean>
}