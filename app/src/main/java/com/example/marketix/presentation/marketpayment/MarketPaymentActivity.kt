package com.example.marketix.presentation.marketpayment

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.MediaController
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.marketix.R
import com.example.marketix.databinding.ActivityMarketsPaymentBinding
import com.example.marketix.presentation.account.AccountActivity
import com.example.marketix.presentation.fullvideo.FullVideoActivity
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.presentation.webpage.PaymentWebPageActivity
import com.example.marketix.util.Constants
import com.example.marketix.util.SvgLoader
import com.example.marketix.util.alertMessageDialog
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class MarketPaymentActivity : DaggerAppCompatActivity(), MarketPaymentActivityListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityMarketsPaymentBinding

    var isPaymentCall = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MarketPaymentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MarketPaymentViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_markets_payment)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.loadMarketData()

        viewModel.marketPaymentLinkData.observe(this) {
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

        viewModel.isVideoDataFound.observe(this) {
            it.let {
                if (it) {
                    val uri: Uri = Uri.parse(viewModel.marketurl.value)
                    activity.vvVideoView.setVideoURI(uri)
                    val mediaController = MediaController(this)
                    mediaController.setAnchorView(activity.vvVideoView)
                    mediaController.setMediaPlayer(activity.vvVideoView)
                    activity.vvVideoView.setMediaController(mediaController)
                } else {
                    SvgLoader.loadSvg(this, viewModel.marketurl.value!!, activity.ivNoImage)
                }
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

    override fun playVideoActivity() {
        intentCall<FullVideoActivity> {
            putString("imageurl", viewModel.marketurl.value)
            putBoolean("isSecure", true)
        }
    }

    override fun backPressActivity() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun openLoginActivity() {
        intentCall<LoginActivity>(2) { }
    }

    override fun displayMessageListener(message: String) {
        this@MarketPaymentActivity.alertMessageDialog(message) {
            openLoginActivity()
        }
    }

    override fun openWebViewWithUrl(url: String) {
        intentCall<PaymentWebPageActivity> {
            putString("payment_url", url)
        }
    }

}