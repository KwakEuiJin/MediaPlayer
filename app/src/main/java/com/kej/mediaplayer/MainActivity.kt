package com.kej.mediaplayer

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kej.mediaplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        const val MEDIA_PLAY = "mediaPlay"
        const val MEDIA_PAUSE = "mediaPause"
        const val MEDIA_STOP = "mediaStop"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.playButton.setOnClickListener {
            mediaPlay()
        }
        binding.pauseButton.setOnClickListener {
            mediaPause()
        }
        binding.stopButton.setOnClickListener {
            mediaStop()
        }

    }

    private fun mediaPlayerServiceStart(mediaAction: String) {
        val intent = Intent(this, MediaPlayerService::class.java).apply {
            action = mediaAction
        }
        startService(intent)
    }

    private fun mediaPlay() {
        mediaPlayerServiceStart(MEDIA_PLAY)
    }

    private fun mediaPause() {
        mediaPlayerServiceStart(MEDIA_PAUSE)
    }

    private fun mediaStop() {
        mediaPlayerServiceStart(MEDIA_STOP)
    }
}