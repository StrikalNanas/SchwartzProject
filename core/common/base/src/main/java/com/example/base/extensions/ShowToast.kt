package com.example.base.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showShortToast(@StringRes textResId: Int) {
    Toast.makeText(this, textResId, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(@StringRes textResId: Int) {
    Toast.makeText(this, textResId, Toast.LENGTH_LONG).show()
}