package com.engineer.android.media.ui

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.engineer.android.media.R
import com.engineer.android.media.codec.buz.AudioDecoder
import com.engineer.android.media.codec.buz.VideoDecoder
import kotlinx.android.synthetic.main.activity_simple_codec.*
import java.io.File
import java.util.concurrent.Executors


class SimpleCodecActivity : AppCompatActivity() {
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
            return
        }
        //创建线程池
        val threadPool = Executors.newFixedThreadPool(2)

        //创建视频解码器
        videoDecoder = VideoDecoder(path, sfv, null)
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