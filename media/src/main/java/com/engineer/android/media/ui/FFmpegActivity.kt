package com.engineer.android.media.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.engineer.android.media.R
import com.engineer.android.media.databinding.ActivityFFmpegBinding
import com.engineer.android.media.toast

class FFmpegActivity : AppCompatActivity() {


    private lateinit var viewBinding: ActivityFFmpegBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityFFmpegBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.tv.text = getFFmpegInfo()
        getHello().toast(this)
    }

    private external fun getHello(): String
    private external fun getFFmpegInfo(): String

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
}