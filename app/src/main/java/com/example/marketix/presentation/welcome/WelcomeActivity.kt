package com.example.marketix.presentation.welcome

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.marketix.R
import com.example.marketix.databinding.ActivityWelcomeBinding
import com.example.marketix.presentation.dashboard.DashboardActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.util.getPlaceHolder
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class WelcomeActivity : DaggerAppCompatActivity(), WelcomeActivityListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityWelcomeBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: WelcomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(WelcomeViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.loadWelcomeData()

        viewModel.welcomeContent.observe(this) {
            it.let {
                Glide
                    .with(this@WelcomeActivity)
                    .load(it)
                    .fitCenter()
                    .placeholder(getPlaceHolder())
                    .into(activity.ivFeature)
            }
        }
    }

    override fun openDashboardActivity() {
        intentCall<DashboardActivity> { }
    }
}