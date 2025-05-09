package com.example.marketix.presentation.login

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.marketix.R
import com.example.marketix.databinding.ActivityLoginBinding
import com.example.marketix.presentation.forgetpassword.ForgetPasswordActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.presentation.signupverification.SignupVerificationActivity
import com.example.marketix.presentation.welcome.WelcomeActivity
import com.example.marketix.util.hideKeyboard
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity(), LoginActivityListener {

    private val TAG = LoginActivity::class.java.name
    lateinit var activity: ActivityLoginBinding
    private var uri: Uri? = null
    private var referalCode: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_login)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.email.value = ""
        viewModel.password.value = ""

    }

    override fun openSignupActivity() {
        intentCall<SignupActivity> { }
    }

    override fun openForgetPasswordActivity() {
        intentCall<ForgetPasswordActivity> { }
    }

    override fun openWelcomeActivity() {
        intentCall<WelcomeActivity>(1) { }
    }

    override fun openUserVerificationActivity() {
        intentCall<SignupVerificationActivity> { }
    }

    override fun hideKeyboardListener() {
        hideKeyboard()
    }

}