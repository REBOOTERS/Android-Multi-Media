package com.engineer.android.media

import android.content.Context
import android.view.Gravity
import android.widget.Toast

fun String?.toast(context: Context?) {
    context?.let { ctx ->
        this?.let {
            val toast = Toast.makeText(ctx, this, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 100)
            toast.show()
        }
    }
}