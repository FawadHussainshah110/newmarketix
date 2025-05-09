package com.example.marketix.presentation.aboutus

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.marketix.R
import com.example.marketix.databinding.ActivityAboutUsBinding
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.util.getPlaceHolder
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class AboutUsActivity : DaggerAppCompatActivity(), AboutUsActivityListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityAboutUsBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AboutUsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AboutUsViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_about_us)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.loadAboutUsData()

        viewModel.aboutUsContent.observe(this) {
            it.let {
                Glide
                    .with(this@AboutUsActivity)
                    .load(it)
                    .fitCenter()
                    .placeholder(getPlaceHolder())
                    .into(activity.ivFeature)
            }
        }

    }

    override fun openBackPressListener() {
        onBackPressedDispatcher.onBackPressed()
    }
}