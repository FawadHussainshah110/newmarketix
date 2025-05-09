package com.example.marketix.util

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object VideoLoader {

    private val client = OkHttpClient()

    fun loadVideo(context: Context, url: String, videoView: VideoView) {
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.byteStream()?.use { inputStream ->
                        val tempFile = File.createTempFile("temp_video", "mp4", context.cacheDir)
                        FileOutputStream(tempFile).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                        val videoUri = Uri.fromFile(tempFile)
                        (context as Activity).runOnUiThread {
                            videoView.setVideoURI(videoUri)
                            val mediaController = MediaController(context)
                            mediaController.setAnchorView(videoView)
                            videoView.setMediaController(mediaController)
                            videoView.start()
                        }
                    }
                }
            }
        })
    }
}
