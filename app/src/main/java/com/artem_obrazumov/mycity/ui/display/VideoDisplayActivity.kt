package com.artem_obrazumov.mycity.ui.display

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.artem_obrazumov.mycity.databinding.ActivityVideoDisplayBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer

class VideoDisplayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoDisplayBinding
    private lateinit var videoPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeExoPlayer(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        videoPlayer.run {
            outState.putLong("currentPosition", this.currentPosition)
            outState.putBoolean("playWhenReady", this.playWhenReady)
            release()
        }
    }

    private fun initializeExoPlayer(savedInstanceState: Bundle?) {
        videoPlayer = SimpleExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                binding.viewingVideo.player = exoPlayer
                val url = intent.extras!!.getString("URL")
                val mediaItem = MediaItem.fromUri(url!!)
                exoPlayer.setMediaItem(mediaItem)
            }
        if (savedInstanceState != null) {
            videoPlayer.seekTo(savedInstanceState.getLong("currentPosition"))
            videoPlayer.playWhenReady = savedInstanceState.getBoolean("playWhenReady")
        }
    }

    @SuppressLint("InlinedApi")
    @Suppress("DEPRECATION")
    private fun hideSystemUi() {
        binding.viewingVideo.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
}