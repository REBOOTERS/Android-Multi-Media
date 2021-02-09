package com.engineer.android.media.muxer

import android.media.MediaCodec
import android.util.Log
import com.engineer.android.media.codec.buz.AudioExtractor
import com.engineer.android.media.codec.buz.VideoExtractor
import java.nio.ByteBuffer

class MP4Repack(path: String) {
    private val TAG = "MP4Repack"

    //初始化音视频分离器
    private val mAExtractor: AudioExtractor = AudioExtractor(path)
    private val mVExtractor: VideoExtractor = VideoExtractor(path)

    //初始化封装器
    private val mMuxer: MMuxer = MMuxer()

    fun startGenVideo(block: (String) -> Unit) {
        val audioFormat = mAExtractor.getFormat()
        val videoFormat = mVExtractor.getFormat()
        val resultPath = mMuxer.getResultPath()
        if (audioFormat != null) {
            mMuxer.addAudioTrack(audioFormat)
        } else {
            mMuxer.setNoAudio()
        }
        if (videoFormat != null) {
            mMuxer.addVideoTrack(videoFormat)
        } else {
            mMuxer.setNoVideo()
        }

        Thread {
            val buffer = ByteBuffer.allocate(500 * 1024)
            val bufferInfo = MediaCodec.BufferInfo()

            if (audioFormat != null) {
                var size = mAExtractor.readBuffer(buffer)
                while (size > 0) {
                    Log.d(TAG, "audio size is $size")
                    bufferInfo.set(
                        0, size, mAExtractor.getCurrentTimestamp(),
                        mAExtractor.getSampleFlag()
                    )
                    mMuxer.writeAudioData(buffer, bufferInfo)
                    size = mAExtractor.readBuffer(buffer)
                }
            }
            if (videoFormat != null) {
                var size = mVExtractor.readBuffer(buffer)
                while (size > 0) {
                    Log.d(TAG, "video size is $size")
                    bufferInfo.set(
                        0, size, mVExtractor.getCurrentTimestamp(),
                        mVExtractor.getSampleFlag()
                    )
                    mMuxer.writeVideoData(buffer, bufferInfo)
                    size = mVExtractor.readBuffer(buffer)
                }
            }
            mAExtractor.stop()
            mAExtractor.stop()
            mMuxer.releaseAudioTrack()
            mMuxer.releaseVideoTrack()
            block(resultPath)
        }.start()
    }
}