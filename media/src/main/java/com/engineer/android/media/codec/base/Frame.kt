package com.engineer.android.media.codec.base

import android.media.MediaCodec
import java.nio.ByteBuffer

/**
 * Created on 2021/2/2.
 * @author rookie
 */
class Frame {
    var buffer: ByteBuffer? = null

    var bufferInfo = MediaCodec.BufferInfo()
        private set

    fun setBufferInfo(info: MediaCodec.BufferInfo) {
        bufferInfo.set(info.offset, info.size, info.presentationTimeUs, info.flags)
    }
}