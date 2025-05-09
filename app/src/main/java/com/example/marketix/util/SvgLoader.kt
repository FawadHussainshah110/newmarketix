package com.example.marketix.util

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.caverock.androidsvg.SVG
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


//class SvgTarget(private val imageView: ImageView) : CustomTarget<SVG>() {
//    override fun onResourceReady(resource: SVG, transition: Transition<in SVG>?) {
//        val pictureDrawable = PictureDrawable(resource.renderToPicture())
//        imageView.setImageDrawable(pictureDrawable)
//    }
//
//    override fun onLoadCleared(placeholder: Drawable?) {
//        imageView.setImageDrawable(placeholder)
//    }
//}

object SvgLoader {

    private val client = OkHttpClient()

    fun loadSvg(context: Context, url: String, imageView: ImageView) {
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.byteStream()?.use { inputStream ->
                    val svg = SVG.getFromInputStream(inputStream)
                    val drawable = PictureDrawable(svg.renderToPicture())
                    imageView.post {
                        imageView.setImageDrawable(drawable)
                    }
                }
            }
        })
    }

//    fun loadSvgZoomable(context: Context, url: String, imageView: SubsamplingScaleImageView) {
//        val request = Request.Builder().url(url).build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                response.body?.byteStream()?.use { inputStream ->
//                    val svg = SVG.getFromInputStream(inputStream)
//                    val drawable = PictureDrawable(svg.renderToPicture())
//                    val bitmapsvg = BitmapFactory.decodeResource(context.resources, drawable)
//
//                    imageView.post {
//                        imageView.setImage(b)
//                    }
//                }
//            }
//        })
//    }
}