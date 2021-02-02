package com.engineer.android.media.ui

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.engineer.android.media.R
import com.engineer.android.media.codec.buz.VideoDecoder
import kotlinx.android.synthetic.main.activity_simple_codec.*
import java.io.File
import java.util.concurrent.Executors


class SimpleCodecActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_codec)
    }

    private fun initPlayer() {
        val uri: Uri = Uri.parse("android:resource://包名/" + R.raw.video)
        val path = Environment.getExternalStorageDirectory().absolutePath + "/mvtest.mp4"
        if (File(path).exists().not()) {
            return
        }
        //创建线程池
        val threadPool = Executors.newFixedThreadPool(2)

        //创建视频解码器
        val videoDecoder = VideoDecoder(path, sfv, null)
        threadPool.execute(videoDecoder)

//        //创建音频解码器
//        val audioDecoder = AudioDecoder(path)
//        threadPool.execute(audioDecoder)

        //开启播放
        videoDecoder.goOn()
//        audioDecoder.goOn()
    }
}