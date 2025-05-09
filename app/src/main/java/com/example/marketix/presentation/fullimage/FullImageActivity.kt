package com.example.marketix.presentation.fullimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.caverock.androidsvg.SVG
import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.marketix.R
import com.example.marketix.databinding.ActivityFullImageBinding
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlin.math.ceil


class FullImageActivity : DaggerAppCompatActivity(), FullImageActivityListener {

    private val TAG = FullImageActivity::class.java.name
    lateinit var activity: ActivityFullImageBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: FullImageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(FullImageViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_full_image)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        var posturl = ""
        if (intent.hasExtra("imageurl")) posturl = intent.getStringExtra("imageurl")!!

        if (posturl.endsWith(".svg") || posturl.endsWith(".SVG")) {
//            SvgLoader.loadSvgZoomable(this, posturl, activity.ivPostPreview)
            activity.ivPostPreviewSVG.visibility = View.VISIBLE
            activity.ivPostPreview.visibility = View.GONE

            Glide.with(this)
                .load(posturl)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        // Convert the downloaded SVG to Bitmap
                        val bitmap = imageFromString(posturl)
                        if (bitmap != null) {
                            activity.ivPostPreview.setImage(ImageSource.bitmap(bitmap))
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle cleanup if needed
                    }
                })
//            Glide
//                .with(this@FullImageActivity)
//                .asBitmap()
////                .decode(SvgBitmapDecoder(this@FullImageActivity))
//                .load(posturl)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(
//                        resource: Bitmap, transition: Transition<in Bitmap>?
//                    ) {
//                        activity.ivPostPreview.setImage(ImageSource.bitmap(resource))
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                        // Handle any cleanup
//                    }
//                })
//            val bitmap = imageFromString(posturl)
//            activity.ivPostPreviewSVG.setImageBitmap(bitmap)

//            Glide.with(this)
//                .asBitmap()
//                .load(posturl)
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: Transition<in Bitmap>?
//                    ) {
//                        activity.ivPostPreviewSVG.setImageBitmap(resource)
////                        activity.ivPostPreviewSVG.setSVG(resource)
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                        // Handle cleanup
//                    }
//                })
        } else {
            activity.ivPostPreviewSVG.visibility = View.GONE
            activity.ivPostPreview.visibility = View.VISIBLE
            Glide.with(this)
                .asBitmap()
                .load(posturl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap, transition: Transition<in Bitmap>?
                    ) {
                        activity.ivPostPreview.setImage(ImageSource.bitmap(resource))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle any cleanup
                    }
                })
        }
//            Glide
//                .with(this@FullImageActivity)
//                .load(posturl)
//                .fitCenter()
//                .placeholder(this@FullImageActivity.getPlaceHolder())
//                .error(R.drawable.logo)
//                .into(activity.ivPostPreview)

    }

    override fun backPressClick() {
        onBackPressedDispatcher.onBackPressed()
    }

    fun imageFromString(imageData: String): Bitmap {
        val data = imageData.substring(imageData.indexOf(",") + 1)
        val imageAsBytes: ByteArray = Base64.decode(data.toByteArray(), Base64.DEFAULT)
        val svgAsString = String(imageAsBytes, StandardCharsets.UTF_8)

        val svg = SVG.getFromString(svgAsString)

        // Create a bitmap and canvas to draw onto
        val svgWidth = if ((svg.documentWidth != -1f)) svg.documentWidth else 500f
        val svgHeight = if ((svg.documentHeight != -1f)) svg.documentHeight else 500f

        val newBM = Bitmap.createBitmap(
            ceil(svgWidth.toDouble()).toInt(),
            ceil(svgHeight.toDouble()).toInt(),
            Bitmap.Config.ARGB_8888
        )
        val bmcanvas = Canvas(newBM)

        // Clear background to white if you want
        bmcanvas.drawRGB(255, 255, 255)

        // Render our document onto our canvas
        svg.renderToCanvas(bmcanvas)

        return newBM
    }
}
