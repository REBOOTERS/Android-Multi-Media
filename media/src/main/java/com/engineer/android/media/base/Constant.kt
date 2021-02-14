package com.engineer.android.media.base

import android.os.Environment
import com.engineer.android.media.R

/**
 * @author rookie
 * @since 09-05-2019
 */
class Constant {
    companion object {
        val AUDIO_RES_ID = R.raw.jazz_in_paris
        val VIDEO_RES_ID = R.raw.video

        val originalPath = Environment.getExternalStorageDirectory().absolutePath + "/hero.mp4"
        val videoPath = Environment.getExternalStorageDirectory().absolutePath + "/star.mp4"
    }
}