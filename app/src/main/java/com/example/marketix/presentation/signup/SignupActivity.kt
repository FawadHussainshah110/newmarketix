package com.example.marketix.presentation.signup

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.marketix.R
import com.example.marketix.databinding.ActivitySignupBinding
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.util.hideKeyboard
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class SignupActivity : DaggerAppCompatActivity(), SignupActivityListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivitySignupBinding
    private var uri: Uri? = null
    private var referalCode: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SignupViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SignupViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.email.value = ""
        viewModel.name.value = ""
        viewModel.phone.value = ""
        viewModel.address.value = ""
        viewModel.password.value = ""
        viewModel.confirmPassword.value = ""

        deeplinkData()
    }

    override fun onSupportNavigateUp(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
        }
        return true
    }

    private fun deeplinkData() {
        // getting the data from our intent in our uri.
        uri = intent.data

        // checking if the uri is null or not.
        if (uri != null) {
            // if the uri is not null then we are getting
            // the path segments and storing it in list.
            val parameters = uri!!.pathSegments
            // after that we are extracting string
            // from that parameters.
            val param = parameters[0]
            referalCode = param.toString()
        }
    }

    override fun openLoginActivity() {
        intentCall<LoginActivity>(2) { }
    }

    override fun hideKeyboardListener() {
        hideKeyboard()
    }

    override fun loginActiviry() {
        intentCall<LoginActivity>(1) {  }
    }
}