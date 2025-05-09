package com.example.marketix.presentation.forgetpassword

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.marketix.R
import com.example.marketix.databinding.ActivityForgetPasswordBinding
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.util.alertMessageDialog
import com.example.marketix.util.hideKeyboard
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class ForgetPasswordActivity : DaggerAppCompatActivity(), ForgetPasswordActivityListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityForgetPasswordBinding
    private var uri: Uri? = null
    private var referalCode: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ForgetPasswordViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ForgetPasswordViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_forget_password)
        activity.viewModel = viewModel
        activity.lifecycleOwner = this
        viewModel.listener = this

        viewModel.progressVisibility.postValue(false)

//        viewModel.editTextTitle.postValue(resources.getString(R.string.text_email))
//        viewModel.title.value = resources.getString(R.string.text_email_here)
//        viewModel.detailDescription.value = resources.getString(R.string.text_forget_password_dec)
        viewModel.viewCondition.value = 1
        viewModel.otpCode.value = ""
        viewModel.newPassword.value = ""
        viewModel.newConfirmPassword.value = ""
        viewModel.email.value = ""

    }

    override fun openLoginActivity() {
        intentCall<LoginActivity>(2) { }
    }

    override fun hideKeyboardListener() {
        hideKeyboard()
    }

    override fun displayMessageListener(message: String) {
        this@ForgetPasswordActivity.alertMessageDialog(message, {})
    }

}