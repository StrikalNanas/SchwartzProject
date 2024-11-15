package com.example.base.extensions

import android.content.Context
import android.health.connect.datatypes.units.Length
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Message
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(
    message: Any,
    length: Int = Toast.LENGTH_SHORT
) {
    val text = when(message) {
        is String -> message
        is Int -> getString(message)
        else -> throw IllegalStateException()
    }
    Toast.makeText(this, text, length).show()
}