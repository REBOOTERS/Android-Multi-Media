package com.engineer.android.media.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.engineer.android.media.R
import com.engineer.android.media.codec.base.BaseDecoder
import com.engineer.android.media.codec.base.Frame
import com.engineer.android.media.codec.buz.AudioDecoder
import com.engineer.android.media.codec.buz.VideoDecoder
import com.engineer.android.media.codec.interfaces.IDecoderStateListener
import com.engineer.android.media.toHumanTime
import com.engineer.android.media.toast
import kotlinx.android.synthetic.main.activity_simple_codec.*
import java.io.File
import java.lang.StringBuilder
import java.util.concurrent.Executors


class SimpleCodecActivity : AppCompatActivity() {
    private val TAG = "SimpleCodecActivity"

    private var videoDecoder: VideoDecoder? = null
    private var audioDecoder: AudioDecoder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_codec)
        initPlayer()
    }

    private fun initPlayer() {
        val path = Environment.getExternalStorageDirectory().absolutePath + "/hero.mp4"
        if (File(path).exists().not()) {
            "$path no exist".toast(this)
            return
        }
        //创建线程池
        val threadPool = Executors.newFixedThreadPool(2)

        //创建视频解码器
        videoDecoder = VideoDecoder(path, sfv, null)
        videoDecoder?.setStateListener(object : IDecoderStateListener {
            override fun decoderPrepare(decodeJob: BaseDecoder?) {
                Log.d(TAG, "decoderPrepare() called with: decodeJob = $decodeJob")
            }

            override fun decoderReady(decodeJob: BaseDecoder?) {
                Log.d(TAG, "decoderReady() called with: decodeJob = $decodeJob")

                val info = StringBuilder()
                info.append("path:").append(videoDecoder?.getFilePath()).append("\n")
                val time = videoDecoder?.getDuration()?.toHumanTime()
                info.append("duration:").append(time).append("\n")
                runOnUiThread {
                    media_info.text = info.toString()
                }
            }

            override fun decoderRunning(decodeJob: BaseDecoder?) {
                Log.d(TAG, "decoderRunning() called with: decodeJob = $decodeJob")
            }

            override fun decoderPause(decodeJob: BaseDecoder?) {
                Log.d(TAG, "decoderPause() called with: decodeJob = $decodeJob")
            }

            override fun decodeOneFrame(decodeJob: BaseDecoder?, frame: Frame) {
                Log.d(TAG, "decodeOneFrame() called with: decodeJob = $decodeJob, frame = $frame")
            }

            override fun decoderFinish(decodeJob: BaseDecoder?) {
                Log.d(TAG, "decoderFinish() called with: decodeJob = $decodeJob")
            }

            override fun decoderDestroy(decodeJob: BaseDecoder?) {
                Log.d(TAG, "decoderDestroy() called with: decodeJob = $decodeJob")
            }

            override fun decoderError(decodeJob: BaseDecoder?, msg: String) {
                Log.d(TAG, "decoderError() called with: decodeJob = $decodeJob, msg = $msg")
            }

        })
        threadPool.execute(videoDecoder)

//        //创建音频解码器
        audioDecoder = AudioDecoder(path)
        threadPool.execute(audioDecoder)

        //开启播放
        videoDecoder?.goOn()
        audioDecoder?.goOn()
    }

    override fun onDestroy() {
        videoDecoder?.stop()
        audioDecoder?.stop()
        super.onDestroy()
    }
}