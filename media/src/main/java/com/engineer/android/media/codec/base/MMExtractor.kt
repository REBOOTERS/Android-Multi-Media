package com.engineer.android.media.codec.base

import android.media.MediaExtractor
import android.media.MediaFormat
import java.nio.ByteBuffer

/**
 * Created on 2021/2/2.
 * @author rookie
 */
class MMExtractor(path: String) {

    private var mExtractor: MediaExtractor? = null


    /**音频通道索引*/
    private var mAudioTrack = -1

    /**视频通道索引*/
    private var mVideoTrack = -1

    /**当前帧时间戳*/
    private var mCurSampleTime: Long = 0

    /**开始解码时间点*/
    private var mStartPos: Long = 0

    init {
        mExtractor = MediaExtractor()
        mExtractor?.setDataSource(path)
    }

    fun readBuffer(byteBuffer: ByteBuffer): Int {
        byteBuffer.clear()
        selectSourceTrack()
        val readSampleCount = mExtractor!!.readSampleData(byteBuffer, 0)
        if (readSampleCount < 0) {
            return -1
        }
        mCurSampleTime = mExtractor?.sampleTime ?: 0
        mExtractor?.advance()
        return readSampleCount
    }

    private fun selectSourceTrack() {
        if (mVideoTrack >= 0) {
            mExtractor?.selectTrack(mVideoTrack)
        } else if (mAudioTrack >= 0) {
            mExtractor?.selectTrack(mAudioTrack)
        }
    }

    /**
     * Seek到指定位置，并返回实际帧的时间戳
     */
    fun seek(pos: Long): Long {
        mExtractor!!.seekTo(pos, MediaExtractor.SEEK_TO_PREVIOUS_SYNC)
        return mExtractor!!.sampleTime
    }

    /**
     * 停止读取数据
     */
    fun stop() {
        //【4，释放提取器】
        mExtractor?.release()
        mExtractor = null
    }

    fun getVideoTrack(): Int {
        return mVideoTrack
    }

    fun getAudioTrack(): Int {
        return mAudioTrack
    }

    fun setStartPos(pos: Long) {
        mStartPos = pos
    }

    /**
     * 获取当前帧时间
     */
    fun getCurrentTimestamp(): Long {
        return mCurSampleTime
    }

    /**
     * 获取视频格式
     */
    fun getVideoFormat(): MediaFormat? {
        mExtractor?.let {
            for (i in 0 until it.trackCount) {
                val mediaFormat = it.getTrackFormat(i)
                val mine = mediaFormat.getString(MediaFormat.KEY_MIME) ?: ""
                if (mine.startsWith("video/")) {
                    mVideoTrack = i
                    break
                }
            }
        }
        if (mVideoTrack >= 0) {
            return mExtractor!!.getTrackFormat(mVideoTrack)
        }
        return null
    }

    /**
     * 获取视频格式
     */
    fun getAudioFormat(): MediaFormat? {
        mExtractor?.let {
            for (i in 0 until it.trackCount) {
                val mediaFormat = it.getTrackFormat(i)
                val mine = mediaFormat.getString(MediaFormat.KEY_MIME) ?: ""
                if (mine.startsWith("audio/")) {
                    mAudioTrack = i
                    break
                }
            }
        }
        if (mAudioTrack >= 0) {
            return mExtractor!!.getTrackFormat(mAudioTrack)
        }
        return null
    }
}