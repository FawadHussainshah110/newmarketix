package com.example.marketix.presentation.coursepayment

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.marketix.R
import com.example.marketix.databinding.ActivityCoursePaymentBinding
import com.example.marketix.presentation.account.AccountActivity
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.presentation.webpage.PaymentWebPageActivity
import com.example.marketix.util.Constants
import com.example.marketix.util.alertMessageDialog
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class CoursePaymentActivity : DaggerAppCompatActivity(), CoursePaymentActivityListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityCoursePaymentBinding

    var isPaymentCall = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CoursePaymentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CoursePaymentViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_course_payment)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.courseName.value = ""
        viewModel.payment.value = ""
        viewModel.paymentdetail.value = ""

        if (intent.hasExtra("title")) viewModel.courseName.postValue(intent.getStringExtra("title")!!)
        if (intent.hasExtra("courseid")) viewModel.courseID = intent.getStringExtra("courseid")!!
        if (intent.hasExtra("price")) viewModel.payment.postValue(intent.getStringExtra("price")!!)
        if (intent.hasExtra("paymentdetail"))
            viewModel.paymentdetail.postValue(intent.getStringExtra("paymentdetail")!!)

        viewModel.coursePaymentLinkData.observe(this) {
            it.let {
                var url = it.link
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://$url"
                }
                isPaymentCall = true
                intentCall<PaymentWebPageActivity> {
                    putString("payment_url", url)
                }

//                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                startActivity(browserIntent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPaymentCall) {
            intent.putExtra("data", 1)
            setResult(Constants.COURSES_PAYMENT, intent)
            finish()
        }
    }

    override fun openProfileActivity() {
        intentCall<AccountActivity> { }
    }

    override fun backPressActivity() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun openLoginActivity() {
        intentCall<LoginActivity>(2) { }
    }

    override fun displayMessageListener(message: String) {
        this@CoursePaymentActivity.alertMessageDialog(message) {
            openLoginActivity()
        }
    }

}