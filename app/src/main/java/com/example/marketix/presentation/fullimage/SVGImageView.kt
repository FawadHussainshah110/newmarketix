package com.example.marketix.presentation.fullimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

class SVGImageView(context: Context, attrs: AttributeSet?) : SubsamplingScaleImageView(context, attrs) {
    private var svgBitmap: Bitmap? = null

    fun setSVG(bitmap: Bitmap) {
        svgBitmap = bitmap
        setImage(ImageSource.bitmap(svgBitmap!!))
    }

//    fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas!!)
//        svgBitmap?.let { canvas?.drawBitmap(it, 0f, 0f, null) }
//    }
}
