package com.engineer.android.media.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ScrollView
import android.widget.SeekBar
import com.engineer.android.media.R
import com.engineer.android.media.base.Constant.Companion.MEDIA_RES_ID
import com.engineer.android.media.base.MediaPlayerHolder
import com.engineer.android.media.base.PlaybackInfoListener
import com.engineer.android.media.base.PlayerAdapter
import kotlinx.android.synthetic.main.activity_simple_media_player.*

const val TAG = "media"

/**
 * https://www.cnblogs.com/senior-engineer/p/7867626.html
 *
 * https://developer.android.google.cn/guide/topics/media/mediaplayer
 */

class SimpleMediaPlayerActivity : AppCompatActivity() {

    private lateinit var mPlayerAdapter: PlayerAdapter
    private var mUserIsSeeking = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_media_player)

        initUI()
        initializeSeekbar()
        initializePlaybackController()

    }

    private fun initUI() {
        button_play.setOnClickListener { mPlayerAdapter.play() }
        button_pause.setOnClickListener { mPlayerAdapter.pause() }
        button_reset.setOnClickListener { mPlayerAdapter.reset() }
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
            seekbar_audio.max = duration
            Log.d(TAG, String.format("setPlaybackDuration: setMax(%d)", duration))
        }

        override fun onPositionChanged(position: Int) {
            if (!mUserIsSeeking) {
                seekbar_audio.setProgress(position, true)
                Log.d(TAG, String.format("setPlaybackPosition: setProgress(%d)", position))
            }
        }

        override fun onStateChanged(@State state: Int) {
            val stateToString = convertStateToString(state)
            onLogUpdated(String.format("onStateChanged(%s)", stateToString))
        }

        override fun onPlaybackCompleted() {}

        override fun onLogUpdated(message: String) {
            if (text_debug != null) {
                text_debug.append(message)
                text_debug.append("\n")
                // Moves the scrollContainer focus to the end.
                scroll_container.post { scroll_container.fullScroll(ScrollView.FOCUS_DOWN) }
            }
        }
    }

    private fun initializeSeekbar() {
        seekbar_audio.setOnSeekBarChangeListener(
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
        mPlayerAdapter.loadMedia(MEDIA_RES_ID)
        Log.d(TAG, "onStart: create MediaPlayer")
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
