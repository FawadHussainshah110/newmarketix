package com.example.marketix.presentation.webpage

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.marketix.R
import com.example.marketix.databinding.ActivityPaymentWebPageBinding
import com.example.marketix.presentation.signup.SignupActivity
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class PaymentWebPageActivity : DaggerAppCompatActivity() {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityPaymentWebPageBinding

//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory
//    private val viewModel: MarketPaymentViewModel by lazy {
//        ViewModelProvider(this, viewModelFactory).get(MarketPaymentViewModel::class.java)
//    }

    var url: String = ""

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_payment_web_page)
//        activity.viewModel = viewModel
//        viewModel.listener = this
        activity.lifecycleOwner = this

        if (intent.hasExtra("payment_url"))
            url = intent.getStringExtra("payment_url")!!


        if (url.isNotEmpty())
            activity.wvMain.loadUrl(url)
        else
            finish()

        activity.wvMain.webViewClient = object : WebViewClient() {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onRenderProcessGone(
                view: WebView?,
                detail: RenderProcessGoneDetail?
            ): Boolean {

                if (view == activity.wvMain && detail?.didCrash() == true) {
                    recreateWebView()
                    return true
                }

                return super.onRenderProcessGone(view, detail)
            }

        }
    }

    private fun recreateWebView() {
        activity.wvMain.loadUrl(url)

    }
}