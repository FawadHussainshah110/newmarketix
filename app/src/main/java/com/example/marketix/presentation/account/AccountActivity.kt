package com.example.marketix.presentation.account

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.marketix.R
import com.example.marketix.databinding.ActivityAccountBinding
import com.example.marketix.presentation.aboutus.AboutUsActivity
import com.example.marketix.presentation.editaccount.EditAccountActivity
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.presentation.updatepassword.UpdatePasswordActivity
import com.example.marketix.util.Constants
import com.example.marketix.util.SvgLoader
import com.example.marketix.util.getPlaceHolder
import com.example.marketix.util.intentCall
import com.example.marketix.util.intentCallForResult
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class AccountActivity : DaggerAppCompatActivity(), AccountActivityListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityAccountBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AccountViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AccountViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Constants.EDIT_PROFILE_DATA -> {
                    val intent: Intent = result.data!!
                    if (intent.getIntExtra("data", 0) == 1) {
                        viewModel.loadProfileData()
                    }
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_account)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.email.value = ""
        viewModel.address.value = ""
        viewModel.userName.value = ""
        viewModel.phoneNumber.value = ""
        viewModel.profileImage.value = ""

        viewModel.loadProfileData()

        viewModel.profileImage.observe(this) {
            it.let {
                if (it.isNotEmpty()) {
                    if (it.endsWith(".svg") || it.endsWith(".SVG"))
                        SvgLoader.loadSvg(this, it, activity.ivProfile)
                    else {
                        Glide.with(this@AccountActivity).load(it)
                            .transform(CenterCrop(), RoundedCorners(32))
                            .placeholder(getPlaceHolder())
                            .into(activity.ivProfile)
                    }
                }
            }
        }

    }

    override fun openChangePasswordActivity() {
        intentCall<UpdatePasswordActivity> { }
    }

    override fun openAboutUskActivity() {
        intentCall<AboutUsActivity> { }
    }

    override fun editProfileActivity() {
        intentCallForResult<EditAccountActivity>(startForResult) {
        }
    }

    override fun openLogoutActivity() {
        intentCall<LoginActivity>(2) { }
    }

    override fun openBackActivity() {
        onBackPressedDispatcher.onBackPressed()
    }

}