package com.example.marketix.util

import android.app.Activity
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import com.example.marketix.R
import io.github.inflationx.calligraphy3.CalligraphyTypefaceSpan
import io.github.inflationx.calligraphy3.TypefaceUtils

fun Activity.customTextWithColor(
    str: String,
    isBold: Boolean = false,
    underline: Int = 0,
    color: Int = R.color.default_text_color
): SpannableString {
    val spannableStr = SpannableString(str)

    val underlineSpan = UnderlineSpan()
    if (underline != 0)
        spannableStr.setSpan(
            underlineSpan,
            0,
            str.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )

    val textColorSpan =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ForegroundColorSpan(this.resources.getColor(color, theme))
        } else {
            ForegroundColorSpan(this.resources.getColor(color))
        }
    spannableStr.setSpan(
        textColorSpan,
        0,
        str.length,
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
    )

    if (isBold) {
        val typefaceSpan = CalligraphyTypefaceSpan(TypefaceUtils.load(assets, "fonts/NexaBold.otf"))
        spannableStr.setSpan(typefaceSpan, 0, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStr.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            str.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

    } else {
        val typefaceSpan =
            CalligraphyTypefaceSpan(TypefaceUtils.load(assets, "fonts/NexaLight.otf"))
        spannableStr.setSpan(typefaceSpan, 0, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    return spannableStr
}