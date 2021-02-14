package com.engineer.android.multimedia

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.engineer.android.media.opgles.MultiGLVideoRenderActivity
import com.engineer.android.media.opgles.SimpleGLVideoRenderActivity
import com.engineer.android.media.opgles.SimpleRenderActivity
import com.engineer.android.media.ui.FFmpegActivity
import com.engineer.android.media.ui.SimpleCodecActivity
import com.engineer.android.media.ui.SimpleMediaPlayerActivity
import com.engineer.android.media.ui.SimpleVideoViewActivity
import com.engineer.android.multimedia.databinding.ActivityMainBinding
import com.engineer.android.multimedia.ui.CreatorRootActivity
import com.permissionx.guolindev.PermissionX


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.audio.setOnClickListener {
            startActivity(Intent(this, SimpleMediaPlayerActivity::class.java))
        }

        viewBinding.video.setOnClickListener {
            startActivity(Intent(this, SimpleVideoViewActivity::class.java))
        }

        viewBinding.decoder.setOnClickListener {
            startActivity(Intent(this, SimpleCodecActivity::class.java))
        }
        viewBinding.creator.setOnClickListener {
            startActivity(Intent(this, CreatorRootActivity::class.java))
        }
        viewBinding.opgl.setOnClickListener {
            startActivity(Intent(this, SimpleRenderActivity::class.java))
        }

        viewBinding.opglVideo.setOnClickListener {
            startActivity(Intent(this, SimpleGLVideoRenderActivity::class.java))
        }

        viewBinding.opglVideoMulti.setOnClickListener {
            startActivity(Intent(this, MultiGLVideoRenderActivity::class.java))
        }

        viewBinding.ffmpeg.setOnClickListener {
            startActivity(Intent(this, FFmpegActivity::class.java))
        }


        requestPermission()
    }

    private fun requestPermission() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .request { _, _, _ -> }
    }
}
