package com.example.marketix.di.module

import android.app.Application
import com.example.marketix.di.callback.IActivityTranscation
import com.example.marketix.di.callbackImp.ActivityTransactionCallbacks
import com.example.marketix.di.callbackImp.ImpActivityTransction
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
class ActivityTransitionModule {

    @Provides
    fun provideIActivityTranscation(): IActivityTranscation {
        return ImpActivityTransction()
    }

    @Provides
    @IntoSet
    fun activityTranscationCallbacks(callbacks: ActivityTransactionCallbacks): Application.ActivityLifecycleCallbacks =
        callbacks
}