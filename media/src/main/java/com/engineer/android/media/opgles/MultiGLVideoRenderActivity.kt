package com.engineer.android.media.opgles

import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import com.engineer.android.media.base.Constant
import com.engineer.android.media.codec.buz.AudioDecoder
import com.engineer.android.media.codec.buz.VideoDecoder
import com.engineer.android.media.databinding.ActivityMultiGLVideoRenderBinding
import com.engineer.android.media.opgles.drawer.VideoDrawer
import java.util.concurrent.Executors

class MultiGLVideoRenderActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMultiGLVideoRenderBinding

    private val path1 = Constant.originalPath
    private val path2 = Constant.videoPath

    private val threadPool = Executors.newFixedThreadPool(10)
    private val render = SimpleRender()

    private var videoDecoder: VideoDecoder? = null
    private var audioDecoder: AudioDecoder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMultiGLVideoRenderBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initVideo(path1, mute = false)
        initVideo(path2, alpha = 0.5f, mute = true)

        viewBinding.glSurfaceView.setEGLContextClientVersion(2)
        viewBinding.glSurfaceView.setRenderer(render)
    }

    private fun initVideo(videoPath: String, alpha: Float = 1.0f, mute: Boolean = false) {
        val drawer = VideoDrawer()
        drawer.setAlpha(alpha)
        drawer.setVideoSize(1920, 1080)
        drawer.getSurfaceTexture {
            initPlayer(it, videoPath, mute)
        }
        render.addDrawer(drawer)

        if (alpha < 1) {
            viewBinding.glSurfaceView.addDrawer(drawer)
            viewBinding.root.postDelayed({
                drawer.scale(0.5f, 0.5f)
            }, 1000)
        }
    }

    private fun initPlayer(
        surfaceTexture: SurfaceTexture,
        videoPath: String,
        mute: Boolean = false
    ) {
        val surface = Surface(surfaceTexture)
        videoDecoder = VideoDecoder(videoPath, null, surface)
        threadPool.execute(videoDecoder)
        videoDecoder?.goOn()

        if (mute.not()) {
            audioDecoder = AudioDecoder(videoPath)
            threadPool.execute(audioDecoder)
            audioDecoder?.goOn()
        }

    }

    override fun onDestroy() {
        threadPool?.shutdown()
        videoDecoder?.stop()
        audioDecoder?.stop()
        super.onDestroy()
    }
}