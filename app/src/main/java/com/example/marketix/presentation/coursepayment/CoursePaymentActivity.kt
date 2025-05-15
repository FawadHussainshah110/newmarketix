package com.example.marketix.presentation.coursepayment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.marketix.R
import com.example.marketix.databinding.ActivityCoursePaymentBinding
import com.example.marketix.domain.model.Result
import com.example.marketix.presentation.account.AccountActivity
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.webpage.PaymentWebPageActivity
import com.example.marketix.util.Constants
import com.example.marketix.util.alertMessageDialog
import com.example.marketix.util.customToast
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class CoursePaymentActivity : DaggerAppCompatActivity(), CoursePaymentActivityListener {

    private val TAG = CoursePaymentActivity::class.java.name
    private lateinit var binding: ActivityCoursePaymentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CoursePaymentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CoursePaymentViewModel::class.java)
    }

    private val paymentResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val paymentId = result.data?.getStringExtra("paymentId")
            paymentId?.let {
                viewModel.checkPaymentStatus(it)
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_payment)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.listener = this

        intent.extras?.let { bundle ->
            viewModel.courseName.value = bundle.getString("title", "")
            viewModel.courseID = bundle.getString("courseid", "")
            viewModel.price.value = bundle.getString("price", "0.0")
            Log.d("PRICEFINDING ", "${viewModel.price}")
            Log.d("PRICEFINDING ", "${viewModel.price.value}")
            viewModel.paymentDetail.value = bundle.getString("paymentdetail", "")
        }

        //for testing
//        intent.extras?.let { bundle ->
//            viewModel.courseName.value = bundle.getString("title", "")
//            viewModel.courseID = bundle.getString("courseid", "")
//            viewModel.price.value = bundle.getString("price", "0.0")
//            viewModel.paymentDetail.value = bundle.getString("paymentdetail", "")
//        }

        setupObservers()
      //  viewModel.initiatePayment()
    }

    private fun setupObservers() {
        viewModel.invoiceData.observe(this) { paymentData ->
            paymentData?.let {
                openPaymentPage(it.invoice_url, viewModel.courseID)
            }
        }

        viewModel.paymentStatus.observe(this) { status ->
            when (status?.lowercase()) {
                "completed", "finished" -> {
                    customToast("Payment successful!")
                    setResult(Constants.COURSES_PAYMENT, Intent().putExtra("data", 1))
                    finish()
                }

                "failed", "expired" -> {
                    customToast("Payment failed")
                }

                else -> {
                    status?.let {
                        customToast("Payment status: $it")
                    }
                }
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                customToast(it)
            }
        }
    }

    private fun openPaymentPage(paymentUrl: String, courseId: String) {
        val intent = Intent(this, PaymentWebPageActivity::class.java).apply {
            putExtra("payment_url", paymentUrl)
            putExtra("courseId", courseId)
            putExtra("price", viewModel.price.value)
        }
        paymentResultLauncher.launch(intent)
    }

//    private fun handlePaymentSuccess(url: String) {
//        val paymentId = extractPaymentIdFromUrl(url)
//        setResult(RESULT_OK, Intent().apply {
//            putExtra("payment_id", paymentId)
//            putExtra("market_id", intent.getStringExtra("courseid"))
//        })
//
//        // Grant 30-day access to the market
//        val marketId = intent.getStringExtra("courseid") ?: return
//        val expiryTime = System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000L)
//        preferencesRepository.setMarketAccess(marketId, expiryTime.toString())
//
//        finish()
//    }

    override fun backPressActivity() {
        setResult(Constants.COURSES_PAYMENT, Intent().putExtra("data", 0))
        finish()
    }

    override fun openProfileActivity() {
        intentCall<AccountActivity> { }
    }

    override fun openLoginActivity() {
        intentCall<LoginActivity> { }
    }

    override fun displayMessageListener(message: String) {
        alertMessageDialog(message) {
            openLoginActivity()
        }
    }
}