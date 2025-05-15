package com.example.marketix.presentation.webpage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.*
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.marketix.R
import com.example.marketix.databinding.ActivityPaymentWebPageBinding
import com.example.marketix.di.builder.BaseViewModel
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class PaymentWebPageActivity : DaggerAppCompatActivity() {

    companion object {
        private const val EXTRA_PAYMENT_URL = "payment_url"
        private const val EXTRA_COURSE_ID = "course_id"
        private const val EXTRA_PRICE = "price"
        private const val EXTRA_COURSE_NAME = "course_name"
        private const val EXTRA_MARKET_ID = "market_id"

    }

    private lateinit var binding: ActivityPaymentWebPageBinding
    private lateinit var progressBar: ProgressBar
    private var webView: WebView? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_web_page)
        binding.lifecycleOwner = this

        progressBar = binding.progressBar

        val paymentUrl = intent.getStringExtra(EXTRA_PAYMENT_URL) ?: run {
            finish()
            return
        }

        initializeWebView()
        loadPaymentPage(paymentUrl)
    }

    private fun initializeWebView() {
        binding.wvMainContainer.removeAllViews()
        webView = WebView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        binding.wvMainContainer.addView(webView)
        configureWebViewSettings()
        setupWebViewClients()
    }

    private fun configureWebViewSettings() {
        webView?.settings?.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            loadWithOverviewMode = true
            useWideViewPort = true
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                safeBrowsingEnabled = false
            }
        }
    }

    private fun setupWebViewClients() {
        webView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return when {
                    url.contains("/payment/success") -> {
                        handlePaymentSuccess(url)
                        true
                    }

                    url.contains("/payment/cancel") -> {
                        handlePaymentCancelled()
                        true
                    }

                    else -> super.shouldOverrideUrlLoading(view, url)
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = ProgressBar.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = ProgressBar.GONE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                progressBar.visibility = ProgressBar.GONE
            }
        }

        webView?.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress < 100 && progressBar.visibility == ProgressBar.GONE) {
                    progressBar.visibility = ProgressBar.VISIBLE
                }
                progressBar.progress = newProgress
            }
        }
    }

    private fun loadPaymentPage(url: String) {
        try {
            webView?.loadUrl(url)
        } catch (e: Exception) {
            Log.e("PaymentWebPage", "Error loading URL: ${e.message}")
            finish()
        }
    }

    private fun handlePaymentSuccess(url: String) {
        val paymentId = extractPaymentIdFromUrl(url)
        val resultIntent = Intent()
        resultIntent.putExtra("paymentId", paymentId)

        if (intent.hasExtra(EXTRA_COURSE_ID)) {
            resultIntent.putExtra("courseId", intent.getStringExtra(EXTRA_COURSE_ID))
        } else if (intent.hasExtra(EXTRA_MARKET_ID)) {
            resultIntent.putExtra("marketId", intent.getStringExtra(EXTRA_MARKET_ID))
        }

        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun handlePaymentCancelled() {
        setResult(RESULT_CANCELED)
        finish()
    }

    private fun extractPaymentIdFromUrl(url: String): String {
        return Uri.parse(url).getQueryParameter("payment_id") ?: ""
    }

    override fun onBackPressed() {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        webView?.let {
            it.stopLoading()
            it.destroy()
            webView = null
        }
        super.onDestroy()
    }

}