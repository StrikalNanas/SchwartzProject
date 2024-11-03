package com.example.project

import android.content.Intent
import javax.inject.Inject

class LocalService @Inject constructor(
    private val onStart: () -> Unit,
    private val onStop: () -> Unit
) : ILocalService {

    override fun startProjection(result: Int, data: Intent) {
        onStart.invoke()
    }

    override fun stopProjection() {
        onStop.invoke()
    }
}