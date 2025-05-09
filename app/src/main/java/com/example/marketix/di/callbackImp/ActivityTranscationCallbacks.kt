package com.example.marketix.di.callbackImp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.example.marketix.di.callback.IActivityTranscation
import javax.inject.Inject

class ActivityTransactionCallbacks @Inject constructor(
    val activityTransaction: IActivityTranscation
) : Application.ActivityLifecycleCallbacks {

    lateinit var context: Activity

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d("1234_transaction", activity.localClassName)
//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this)

        context = activity
    }

    override fun onActivityStarted(activity: Activity) =
        activityTransaction.onActivityStarted(activity)

    override fun onActivityResumed(p0: Activity) {
        Log.d("1234_transaction", p0.localClassName)
    }

    override fun onActivityPaused(p0: Activity) {
        Log.d("1234_transaction", p0.localClassName)
    }

    override fun onActivityStopped(p0: Activity) {
        Log.d("1234_transaction", p0.localClassName)
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        Log.d("1234_transaction", p0.localClassName)
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityTransaction.onActivityDestroyed(activity)

        Log.d("1234_transaction", activity.localClassName)
    }

//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    fun onMessageEvent(event: MessageEvent) {
////        notificationDialog(context, event.toString())
//    }

}

