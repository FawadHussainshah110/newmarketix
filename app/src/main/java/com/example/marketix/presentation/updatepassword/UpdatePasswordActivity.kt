package com.example.marketix.presentation.updatepassword

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.marketix.R
import com.example.marketix.databinding.ActivityUpdatePasswordBinding
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.util.hideKeyboard
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class UpdatePasswordActivity : DaggerAppCompatActivity(), UpdatePasswordActivityListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityUpdatePasswordBinding
    private var uri: Uri? = null
    private var referalCode: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: UpdatePasswordViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(UpdatePasswordViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_update_password)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

    }

    override fun openLoginActivity() {
        intentCall<LoginActivity> { }
    }

    override fun onBackPressListener() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun hideKeyboardListener() {
        hideKeyboard()
    }
}