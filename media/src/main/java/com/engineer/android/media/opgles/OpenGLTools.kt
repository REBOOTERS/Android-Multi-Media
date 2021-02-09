package com.engineer.android.media.opgles

import android.opengl.GLES20

object OpenGLTools {
    fun createTextureIds(count: Int): IntArray {
        val texture = IntArray(count)
        GLES20.glGenTextures(count, texture, 0) //生成纹理
        return texture
    }
}