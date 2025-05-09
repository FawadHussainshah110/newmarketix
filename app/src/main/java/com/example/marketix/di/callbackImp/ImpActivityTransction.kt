package com.example.marketix.di.callbackImp

import android.app.Activity
import com.example.marketix.R
import com.example.marketix.di.callback.IActivityTranscation

class ImpActivityTransction : IActivityTranscation {
    override fun onActivityStarted(activity: Activity) {
        activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }

    override fun onActivityDestroyed(activity: Activity) {
        activity.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

}