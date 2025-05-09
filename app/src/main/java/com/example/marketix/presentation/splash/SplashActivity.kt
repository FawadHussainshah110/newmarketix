package com.example.marketix.presentation.splash

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Secure
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.marketix.R
import com.example.marketix.databinding.ActivitySplashBinding
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.onboarding.OnBoardingActivity
import com.example.marketix.presentation.welcome.WelcomeActivity
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

/*
* This Activity class created to load introduction UI (Smile Creator) to user with some animations.
* @SmileCreatorViewModel is injected
* @ActivitySmileCreatorBinding is used to call binded UI
* @SmileCreatorListener is interface listener and used to implement for communication between
* activity and viewmodel.
*/
class SplashActivity : DaggerAppCompatActivity(), SplashActivityListener {
    private val TAG = SplashActivity::class.java.name
    private lateinit var activity: ActivitySplashBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        activity = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        activity.splashViewModel = viewModel
        viewModel.activityListener = this
        activity.lifecycleOwner = this

        viewModel.loadAllSettings()

        val deviceToken = Secure.getString(contentResolver, Secure.ANDROID_ID)
        viewModel.setDeviceToken(deviceToken)

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getStartedClick()
        }, 4800)

//        object : CountDownTimer(4000, 500) {
//            override fun onTick(millisUntilFinished: Long) {
//                if (MainApplication.chatChannels?.isNotEmpty() == true) {
//                    viewModel.getStartedClick()
//                    cancel()
//                    Log.d(TAG, "onTick: chat loaded successfully")
//                }
//            }
//
//            override fun onFinish() {
//                viewModel.getStartedClick()
//                Log.d(TAG, "onFinish: error while loading chat")
//            }
//        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
        }
        return true
    }

    override fun openLoginActivity() {
        this.intentCall<LoginActivity>(2) { }
    }

    override fun openWelcomeActivity() {
        this.intentCall<WelcomeActivity>(1) { }
    }

    override fun openOnBoardingActivity() {
        this.intentCall<OnBoardingActivity>(1) { }
    }

}
