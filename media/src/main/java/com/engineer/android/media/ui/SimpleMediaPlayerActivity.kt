package com.engineer.android.media.ui

import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.widget.ScrollView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.engineer.android.media.base.Constant
import com.engineer.android.media.base.MediaPlayerHolder
import com.engineer.android.media.base.PlaybackInfoListener
import com.engineer.android.media.base.PlayerAdapter
import com.engineer.android.media.databinding.ActivitySimpleMediaPlayerBinding
import com.engineer.android.media.show
import com.engineer.android.media.toast


/**
 * https://www.cnblogs.com/senior-engineer/p/7867626.html
 *
 * https://developer.android.google.cn/guide/topics/media/mediaplayer
 */

class SimpleMediaPlayerActivity : AppCompatActivity() {
    private val TAG = "media"
    private lateinit var mPlayerAdapter: PlayerAdapter
    private var mUserIsSeeking = false

    private lateinit var viewBinding: ActivitySimpleMediaPlayerBinding

    private lateinit var surfaceHolder: SurfaceHolder
    private var resId: Int =
        intArrayOf(Constant.AUDIO_RES_ID, Constant.VIDEO_RES_ID).random()
    private val playVideo
        get() = (resId == Constant.VIDEO_RES_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySimpleMediaPlayerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        surfaceHolder = viewBinding.surfaceView.holder
        initUI()
        initializeSeekbar()
        initializePlaybackController()

    }

    private fun initUI() {
        viewBinding.buttonPlay.setOnClickListener {
            if (playVideo) {
                mPlayerAdapter.setDisplay(surfaceHolder)
            }
            mPlayerAdapter.play()
        }
        viewBinding.buttonPause.setOnClickListener { mPlayerAdapter.pause() }
        viewBinding.buttonReset.setOnClickListener { mPlayerAdapter.reset() }
    }


    private fun initializePlaybackController() {
        val mMediaPlayerHolder = MediaPlayerHolder(this)
        Log.d(TAG, "initializePlaybackController: created MediaPlayerHolder")
        mMediaPlayerHolder.setPlaybackInfoListener(PlaybackListener())
        mPlayerAdapter = mMediaPlayerHolder
        Log.d(TAG, "initializePlaybackController: MediaPlayerHolder progress callback set")
    }


    inner class PlaybackListener : PlaybackInfoListener() {

        override fun onDurationChanged(duration: Int) {
            viewBinding.seekbarAudio.max = duration
            Log.d(TAG, String.format("setPlaybackDuration: setMax(%d)", duration))
        }

        override fun onPositionChanged(position: Int) {
            if (!mUserIsSeeking) {
                viewBinding.seekbarAudio.setProgress(position, true)
                Log.d(TAG, String.format("setPlaybackPosition: setProgress(%d)", position))
            }
        }

        override fun onStateChanged(@State state: Int) {
            val stateToString = convertStateToString(state)
            onLogUpdated(String.format("onStateChanged(%s)", stateToString))
        }

        override fun onPlaybackCompleted() {}

        override fun onLogUpdated(message: String) {
            viewBinding.textDebug.append(message)
            viewBinding.textDebug.append("\n")
            // Moves the scrollContainer focus to the end.
            viewBinding.scrollContainer.post { viewBinding.scrollContainer.fullScroll(ScrollView.FOCUS_DOWN) }
        }
    }

    private fun initializeSeekbar() {
        viewBinding.seekbarAudio.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                var userSelectedPosition = 0

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    mUserIsSeeking = true
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        userSelectedPosition = progress
                    }
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    mUserIsSeeking = false
                    mPlayerAdapter.seekTo(userSelectedPosition)
                }
            })
    }

    override fun onStart() {
        super.onStart()
        "resId =${resources.getResourceEntryName(resId)}: ${resources.getResourceName(resId)}".toast(
            this
        )
        mPlayerAdapter.loadMedia(resId)
        Log.d(TAG, "onStart: create MediaPlayer")
        if (playVideo) {
            viewBinding.surfaceView.show()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isChangingConfigurations && mPlayerAdapter.isPlaying) {
            Log.d(TAG, "onStop: don't release MediaPlayer as screen is rotating & playing")
        } else {
            mPlayerAdapter.release()
            Log.d(TAG, "onStop: release MediaPlayer")
        }
    }
}
