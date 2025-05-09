package com.example.marketix.presentation.fullvideo

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.marketix.R
import com.example.marketix.databinding.ActivityFullVideoBinding
import com.example.marketix.presentation.fullimage.FullImageActivity
import com.example.marketix.util.Constants
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class FullVideoActivity : DaggerAppCompatActivity(), FullVideoActivityListener {

    private val TAG = FullImageActivity::class.java.name
    lateinit var activity: ActivityFullVideoBinding

    private lateinit var playerView: PlayerView
    private lateinit var player: ExoPlayer
    private lateinit var bufferingProgress: View

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: FullVideoViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(FullVideoViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        hideStatusBarIfLandscape()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_full_video)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        hideStatusBarIfLandscape()

        playerView = findViewById(R.id.vvPreview)
        bufferingProgress = findViewById(R.id.buffering_progress)

        var posturl = ""
        var isSecure = false
        if (intent.hasExtra("imageurl"))
            posturl = intent.getStringExtra("imageurl")!!
        if (intent.hasExtra("isSecure"))
            isSecure = intent.getBooleanExtra("isSecure", false)

//        var temp = VideoLoader.loadVideo(this, posturl, activity.vvPreview)
//        Log.d(TAG, "onCreate: ${temp.toString()}")
        initializePlayer(posturl, isSecure)
    }

    private fun initializePlayer(posturl: String, isSecure: Boolean) {
        // Create an ExoPlayer instance
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val dataSourceFactory = if (isSecure) {
            // Create HTTP data source with headers
            DefaultHttpDataSource.Factory().apply {
                setDefaultRequestProperties(
                    mapOf(
                        Constants.ACCESS_AUTHORIZATION_TOKEN to viewModel.getAccessToken(),
                        Constants.DEVICE_TOKEN to viewModel.getDeviceToken()
                    )
                )
            }
//            DefaultDataSourceFactory(this, httpDataSourceFactory)
        } else {
            DefaultDataSource.Factory(this)
        }

        // Prepare the media item with the video URL
        val mediaItem = MediaItem.fromUri(Uri.parse(posturl))
        // Set the media source to the player
        player.setMediaSource(
            ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        )

//        // Add listener for buffering state
//        player.addListener(object : Player.Listener {
//            override fun onIsLoadingChanged(isLoading: Boolean) {
//                bufferingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
//            }
//        })

        // Prepare the player
        player.prepare()
        player.playWhenReady = true
        player.seekTo(viewModel.playbackPosition)

        if (player.isPlaying)
            bufferingProgress.visibility = View.GONE

        // Handle play and pause on click
        playerView.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
    }

    private fun hideStatusBarIfLandscape() {
        // Check if the app is in landscape mode
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Hide status bar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // For Android 11 and higher
                window.insetsController?.hide(android.view.WindowInsets.Type.statusBars())
            } else {
                // For Android versions below 11
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            }
        } else {
            // Show status bar if it's not in landscape mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.show(android.view.WindowInsets.Type.statusBars())
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        player.play()
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onStop() {
        super.onStop()
        viewModel.playbackPosition = player.currentPosition
        player.release()
    }

    override fun playPauseClick() {
//        if (activity.vvPreview.isPlaying)
//            activity.vvPreview.pause()
//        else
//            activity.vvPreview.start()
    }

    override fun backPressClick() {
        onBackPressedDispatcher.onBackPressed()
    }
}