package com.engineer.android.media

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast

const val TAG = "KotlinExt"

fun String?.toast(context: Context?) {
    context?.let { ctx ->
        this?.let {
            val toast = Toast.makeText(ctx, this, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 100)
            toast.show()
        }
    }
}

fun Long.toHumanTime(): String {
    Log.d(TAG, "toHumanTime() called,receiver is $this")
    val base = 60
    val hour: Long = this / (base * base)
    val minute: Long = this % (base * base) / base
    val second: Long = this % base

    return "${formatInt(hour)}:${formatInt(minute)}:${formatInt(second)}"
}

fun Int.toHumanTime(): String {
    return this.toLong().toHumanTime()
}

private fun formatInt(input: Long): String {
    return if (input < 10) "0$input" else input.toString()
}

fun View?.show() {
    if (this != null) {
        this.visibility = View.VISIBLE
    }
}

fun View?.gone() {
    if (this != null) {
        this.visibility = View.GONE
    }
}