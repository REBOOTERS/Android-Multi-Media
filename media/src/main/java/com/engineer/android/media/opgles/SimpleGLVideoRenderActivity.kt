package com.engineer.android.media.opgles

import android.graphics.SurfaceTexture
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Surface
import com.engineer.android.media.R
import com.engineer.android.media.base.Constant.Companion.originalPath
import com.engineer.android.media.codec.buz.AudioDecoder
import com.engineer.android.media.codec.buz.VideoDecoder
import com.engineer.android.media.databinding.ActivitySimpleGLVideoRenderBinding
import com.engineer.android.media.opgles.drawer.IDrawer
import com.engineer.android.media.opgles.drawer.VideoDrawer
import java.util.concurrent.Executors

class SimpleGLVideoRenderActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivitySimpleGLVideoRenderBinding

    private lateinit var drawer: IDrawer

    private var videoDecoder: VideoDecoder? = null
    private var audioDecoder: AudioDecoder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySimpleGLVideoRenderBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        drawer = VideoDrawer()
        drawer.setVideoSize(1920, 1080)
        drawer.getSurfaceTexture {
            initPlayer(it)
        }
        viewBinding.glSurfaceView.setEGLContextClientVersion(2)
        val render = SimpleRender()
        render.addDrawer(drawer)
        viewBinding.glSurfaceView.setRenderer(render)
    }

    private fun initPlayer(it: SurfaceTexture) {
        val surface = Surface(it)
        val threadPool = Executors.newFixedThreadPool(2)
        videoDecoder = VideoDecoder(originalPath, null, surface)
        threadPool.execute(videoDecoder)

        audioDecoder = AudioDecoder(originalPath)
        threadPool.execute(audioDecoder)

        videoDecoder?.goOn()
        audioDecoder?.goOn()
    }

    override fun onDestroy() {
        videoDecoder?.stop()
        audioDecoder?.stop()
        super.onDestroy()
    }
}