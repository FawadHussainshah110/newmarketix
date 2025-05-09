package com.example.marketix.presentation.onboarding

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.marketix.R
import com.example.marketix.databinding.ActivityOnBoardingBinding
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.util.intentCall
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class OnBoardingActivity : DaggerAppCompatActivity(), OnBoardingActivityListener {
    private val TAG = OnBoardingActivity::class.java.name
    private lateinit var activity: ActivityOnBoardingBinding

    private lateinit var mViewPager: ViewPager2

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: OnBoardingViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OnBoardingViewModel::class.java]
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activity = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        mViewPager = activity.viewPager
        mViewPager.adapter = OnboardingViewPagerAdapter(this, this)
        TabLayoutMediator(activity.pageIndicator, mViewPager) { _, _ -> }.attach()

//        btnNextStep.setOnClickListener {
//            if (getItem() > mViewPager.childCount) {
//                finish()
//                val intent =
//                    Intent(applicationContext, LoginActivity::class.java)
//                startActivity(intent)
////                Animatoo.animateSlideLeft(this)
//            } else {
//                mViewPager.setCurrentItem(getItem() + 1, true)
//            }
//        }

    }

    private fun getItem(): Int {
        return mViewPager.currentItem
    }

    override fun openLoginActivity() {
        this.intentCall<LoginActivity>(1) { }

    }

}