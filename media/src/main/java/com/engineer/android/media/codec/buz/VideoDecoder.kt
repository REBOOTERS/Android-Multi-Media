package com.engineer.android.media.codec.buz

import android.media.MediaCodec
import android.media.MediaFormat
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.engineer.android.media.codec.base.BaseDecoder
import com.engineer.android.media.codec.interfaces.IDecoderStateListener
import com.engineer.android.media.codec.interfaces.IExtractor
import java.nio.ByteBuffer

/**
 * Created on 2021/2/2.
 * @author rookie
 */
class VideoDecoder(path: String,
                   sfv: SurfaceView?,
                   surface: Surface?): BaseDecoder(path) {

    private val TAG = "VideoDecoder"

    private val mSurfaceView = sfv
    private var mSurface = surface

    override fun render(outputBuffers: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {

    }

    override fun doneDecode() {
        TODO("Not yet implemented")
    }

    override fun check(): Boolean {
        if (mSurfaceView == null && mSurface == null) {
            Log.w(TAG, "SurfaceView和Surface都为空，至少需要一个不为空")
            mStateListener?.decoderError(this, "显示器为空")
            return false
        }
        return true
    }

    override fun initExtractor(path: String): IExtractor {
        return VideoExtractor(path)
    }

    override fun initSpecParams(format: MediaFormat) {

    }

    override fun initRender(): Boolean {
        return true
    }

    override fun configCodec(codec: MediaCodec, format: MediaFormat): Boolean {
        if (mSurface != null) {
            codec.configure(format,mSurface,null,0)
            notifyDecode()
        } else {
            mSurfaceView?.holder?.addCallback(object :SurfaceHolder.Callback2 {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    mSurface = holder.surface
                    configCodec(codec, format)
                }

                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                }

                override fun surfaceRedrawNeeded(holder: SurfaceHolder) {

                }

            })
            return false
        }
        return true
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun goOn() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun isDecoding(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isSeeking(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isStop(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setStateListener(l: IDecoderStateListener?) {
        TODO("Not yet implemented")
    }

    override fun getWidth(): Int {
        TODO("Not yet implemented")
    }

    override fun getHeight(): Int {
        TODO("Not yet implemented")
    }

    override fun getDuration(): Long {
        TODO("Not yet implemented")
    }

    override fun getRotationAngle(): Int {
        TODO("Not yet implemented")
    }

    override fun getMediaFormat(): MediaFormat? {
        TODO("Not yet implemented")
    }

    override fun getTrack(): Int {
        TODO("Not yet implemented")
    }

    override fun getFilePath(): String {
        TODO("Not yet implemented")
    }

}