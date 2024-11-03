package com.example.data.repositoty

import android.content.Context
import android.util.Log
import com.example.base.extensions.isNetworkAvailable
import com.example.data.model.userKeyMapToData
import com.example.domain.model.UserKey
import com.example.domain.repository.FireStoreRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FireStore @Inject constructor(
    @ApplicationContext private val context: Context
) : FireStoreRepository {

    override suspend fun checkUserKey(userKey: UserKey, resultListener: (isValid: Boolean) -> Unit) {
        if (!context.isNetworkAvailable()) {
            resultListener(false)
            return
        }

        userKey.userKeyMapToData()

        Firebase.firestore
            .collection("keys")
            .get()
            .addOnSuccessListener { querySnapshot ->
                var keyFound = false
                for (document in querySnapshot.documents) {
                    val key = document.getString("key")
                    if (key == userKey.userKey) {
                        keyFound = true
                        break
                    }
                }
                resultListener(keyFound)
                return@addOnSuccessListener
            }
            .addOnFailureListener { error ->
                Log.e(TAG, "Error:  $error")
                resultListener(false)
            }
    }
}

internal const val TAG = "MyLog"