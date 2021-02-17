package com.engineer.android.media.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.engineer.android.media.R
import com.engineer.android.media.base.Constant
import com.engineer.android.media.databinding.ActivitySimpleVideoViewBinding

class SimpleVideoViewActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySimpleVideoViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySimpleVideoViewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.videoView.setVideoPath(Constant.originalPath)
        val control = MediaController(this)
        viewBinding.videoView.setMediaController(control)
        viewBinding.videoView.requestFocus()
    }
}