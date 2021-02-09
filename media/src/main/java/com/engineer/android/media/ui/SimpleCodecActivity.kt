package com.engineer.android.media.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.engineer.android.media.codec.base.BaseDecoder
import com.engineer.android.media.codec.base.Frame
import com.engineer.android.media.codec.buz.AudioDecoder
import com.engineer.android.media.codec.buz.VideoDecoder
import com.engineer.android.media.codec.interfaces.IDecoderStateListener
import com.engineer.android.media.databinding.ActivitySimpleCodecBinding
import com.engineer.android.media.gone
import com.engineer.android.media.muxer.MP4Repack
import com.engineer.android.media.show
import com.engineer.android.media.toHumanTime
import com.engineer.android.media.toast
import java.io.File
import java.lang.StringBuilder
import java.util.concurrent.Executors


class SimpleCodecActivity : AppCompatActivity() {
    private val TAG = "SimpleCodecActivity"

    private val originalPath = Environment.getExternalStorageDirectory().absolutePath + "/hero.mp4"
    private var repackPath = ""
    private var videoDecoder: VideoDecoder? = null
    private var audioDecoder: AudioDecoder? = null

    private lateinit var viewBinding: ActivitySimpleCodecBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySimpleCodecBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initPlayer()
    }

    private fun initPlayer() {
        if (File(originalPath).exists().not()) {
            "$originalPath no exist".toast(this)
            return
        }
        //创建线程池
        val threadPool = Executors.newFixedThreadPool(2)

        //创建视频解码器
        videoDecoder = VideoDecoder(originalPath, viewBinding.sfv, null)
        videoDecoder?.setStateListener(object : IDecoderStateListener {
            override fun decoderPrepare(decodeJob: BaseDecoder?) {
                Log.d(TAG, "decoderPrepare() called with: decodeJob = $decodeJob")
            }

            override fun decoderReady(decodeJob: BaseDecoder?) {
                Log.d(TAG, "decoderReady() called with: decodeJob = $decodeJob")

                val info = StringBuilder()
                info.append("path:").append(videoDecoder?.getFilePath()).append("\n")
                val time = videoDecoder?.getDuration() ?: 0
                val timeStr = (time / 1000).toHumanTime()
                info.append("duration:").append(timeStr).append("\n")
                runOnUiThread {
                    viewBinding.mediaInfo.text = info.toString()
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
        audioDecoder = AudioDecoder(originalPath)
        threadPool.execute(audioDecoder)

        //开启播放
        videoDecoder?.goOn()
        audioDecoder?.goOn()

        viewBinding.repack.setOnClickListener {
            viewBinding.loading.show()
            val repack = MP4Repack(originalPath)
            repack.startGenVideo {
                runOnUiThread {
                    viewBinding.loading.gone()
                    repackPath = it
                    viewBinding.repackResult.text = it
                    "success ".toast(this)
                }
            }
        }
        viewBinding.repackResult.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("视频操作")
                .setNegativeButton(
                    "删除"
                ) { dialog, _ ->
                    val file = File(repackPath)
                    if (file.exists()) {
                        file.delete()
                    }
                    dialog?.dismiss()
                }
                .setPositiveButton(
                    "播放"
                ) { dialog, _ -> dialog?.dismiss() }
                .setNeutralButton(
                    "取消"
                ) { dialog, _ -> dialog.dismiss() }.show()
        }
    }

    override fun onDestroy() {
        videoDecoder?.stop()
        audioDecoder?.stop()
        super.onDestroy()
    }
}