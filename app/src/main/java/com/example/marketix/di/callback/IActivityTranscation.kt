package com.example.marketix.di.callback

import android.app.Activity
import com.example.marketix.di.module.ActivityTransitionModule
import dagger.Module

@Module(includes = [ActivityTransitionModule::class])
interface IActivityTranscation {
    fun onActivityStarted(activity: Activity)
    fun onActivityDestroyed(activity: Activity)
}