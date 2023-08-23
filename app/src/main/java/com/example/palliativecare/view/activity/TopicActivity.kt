package com.example.palliativecare.view.activity

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import com.example.palliativecare.R
import com.example.palliativecare.databinding.ActivityTopicBinding

class TopicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTopicBinding
    private var playbackPosition = 0
    private lateinit var mediaController: MediaController
    var videoURL: String = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic)
        binding = ActivityTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaController = MediaController(this)

        binding.topicVideoView.setOnPreparedListener {
            mediaController.setAnchorView(binding.topicVideoContainer)
            binding.topicVideoView.setMediaController(mediaController)
            binding.topicVideoView.seekTo(playbackPosition)
            binding.topicVideoView.start()
        }

        binding.topicVideoView.setOnInfoListener { player, what, extras ->
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                binding.topicProgressBar.visibility = View.VISIBLE
            true
        }
    }

    override fun onStart() {
        super.onStart()

        val uri = Uri.parse(videoURL)
        binding.topicVideoView.setVideoURI(uri)
        binding.topicProgressBar.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()

        binding.topicVideoView.pause()
        playbackPosition = binding.topicVideoView.currentPosition
    }

    override fun onStop() {
        binding.topicVideoView.stopPlayback()
        super.onStop()
    }
}