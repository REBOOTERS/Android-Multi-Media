package com.engineer.android.media.codec.buz

import android.media.MediaFormat
import com.engineer.android.media.codec.base.MMExtractor
import com.engineer.android.media.codec.interfaces.IExtractor
import java.nio.ByteBuffer

/**
 * Created on 2021/2/2.
 * @author rookie
 */
class AudioExtractor(path:String):IExtractor {
    private val mmExtractor = MMExtractor(path)

    override fun getFormat(): MediaFormat? {
        return  mmExtractor.getAudioFormat()
    }

    override fun readBuffer(byteBuffer: ByteBuffer): Int {
        return  mmExtractor.readBuffer(byteBuffer)
    }

    override fun getCurrentTimestamp(): Long {
        return  mmExtractor.getCurrentTimestamp()
    }

    override fun seek(pos: Long): Long {
        return  mmExtractor.seek(pos)
    }

    override fun setStartPos(pos: Long) {
        mmExtractor.setStartPos(pos)
    }

    override fun stop() {
        mmExtractor.stop()
    }
}