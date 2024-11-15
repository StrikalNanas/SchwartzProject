package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.base.extensions.isNetworkAvailable
import com.example.domain.model.UserKey
import com.example.domain.repository.FireStoreRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireStore @Inject constructor(
    private val context: Context
) : FireStoreRepository {

    override suspend fun checkUserKey(userKey: UserKey): Result<Boolean> {
        return if (!context.isNetworkAvailable()) {
            Result.failure(NoInternetException())
        } else {
            val querySnapshot = Firebase.firestore.collection("keys").get().await()
            val keyFound = querySnapshot.documents.any { document ->
                document.getString("key") == userKey.userKey
            }

            if (keyFound) {
                Result.success(true)
            } else {
                Result.failure(KeyNotFoundException())
            }
        }
    }
}

class KeyNotFoundException : Exception()
class NoInternetException : Exception()