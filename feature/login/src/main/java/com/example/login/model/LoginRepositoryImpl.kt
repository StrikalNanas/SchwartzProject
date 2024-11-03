package com.example.login.model

import com.example.login.LoginRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

internal class LoginRepositoryImpl : LoginRepository {

    override suspend fun checkKey(userKey: UserKey, resultListener: (isValid: Boolean) -> Unit) {
        val database = Firebase.firestore
            .collection("passwords")
            .document("keys")
            .get()
            .addOnSuccessListener { keys ->
                if (keys.exists()) {
                    val keysList = keys.data?.get("keys") as? List<String>
                    val keyExists = keysList?.contains(userKey.userKey) ?: false
                    resultListener(keyExists)
                } else {
                    resultListener(false)
                }
            }
            .addOnFailureListener {
                resultListener(false)
            }
    }
}