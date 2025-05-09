package com.example.marketix.util

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.marketix.R

fun exitDialog(context: Activity) {

    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_exit)
    val yesBtn = dialog.findViewById<Button>(R.id.btnYes)
    val noBtn = dialog.findViewById<Button>(R.id.btnNo)
    yesBtn.setOnClickListener {
        context.finish()

        dialog.dismiss()
    }
    noBtn.setOnClickListener { dialog.dismiss() }
    dialog.show()

}

fun <U> Activity.exitDialogOpenNewActivity(
    activity: Class<U>,
    newActivity: Int = 0
) {

    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_exit)
    val yesBtn = dialog.findViewById<Button>(R.id.btnYes)
    val noBtn = dialog.findViewById<Button>(R.id.btnNo)
    yesBtn.setOnClickListener {
        this.finish()

        if (newActivity != 0) {
            startActivity(Intent(this, activity))
        }
        dialog.dismiss()
    }
    noBtn.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()

}

fun Activity.alertMessageDialog(message: String, callback: (() -> Unit)) {
    runOnUiThread {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_alert_message)
        val detail = dialog.findViewById<TextView>(R.id.tvDetail)
        detail.text = message
        val tvDone = dialog.findViewById<TextView>(R.id.tvDone)
        tvDone.setOnClickListener {
            callback.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }

}
