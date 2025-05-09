package com.example.marketix.presentation.signupverification

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.marketix.R
import com.example.marketix.databinding.ActivitySignupVerificationBinding
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.presentation.welcome.WelcomeActivity
import com.example.marketix.util.alertMessageDialog
import com.example.marketix.util.hideKeyboard
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class SignupVerificationActivity : DaggerAppCompatActivity(), SignupVerificationActivityListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivitySignupVerificationBinding
    private var uri: Uri? = null
    private var referalCode: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SignupVerificationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SignupVerificationViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_signup_verification)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.getOTPCode()

    }

    override fun openWelcomeActivity() {
        intentCall<WelcomeActivity> { }
    }

    override fun hideKeyboardListener() {
        hideKeyboard()
    }

    override fun displayMessageListener(message: String) {
        this@SignupVerificationActivity.alertMessageDialog(message, {})
    }
}