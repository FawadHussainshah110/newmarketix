/*
 * The MIT License (MIT)
 *
 * Copyright 2019 Yaroslav Berezanskyi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.marketix.localehelper

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log

internal class LingverActivityLifecycleCallbacks(private val lingver: Lingver) :
    ActivityLifecycleCallbacks {

    private val TAG = LingverActivityLifecycleCallbacks::class.java.name

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        lingver.setLocaleInternal(activity)
        lingver.resetActivityTitle(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(TAG, "Activity started.")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(TAG, "Activity in resume state.")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(TAG, "Activity in pause state.")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(TAG, "Activity stopped.")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.d(TAG, "Activity save state: $outState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(TAG, "Activity closed successfully.")
    }
}
