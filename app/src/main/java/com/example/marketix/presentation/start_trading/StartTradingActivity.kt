package com.example.marketix.presentation.start_trading

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketix.R
import com.example.marketix.databinding.ActivityStartTradingBinding
import com.example.marketix.domain.model.MarketItem
import com.example.marketix.presentation.account.AccountActivity
import com.example.marketix.presentation.announcement.AnnouncementActivity
import com.example.marketix.presentation.dashboard.DashboardActivity
import com.example.marketix.presentation.history.HistoryActivity
import com.example.marketix.presentation.learn_trading.LearnTradingActivity
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.signals.MarketSignalsActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.presentation.webpage.PaymentWebPageActivity
import com.example.marketix.util.alertMessageDialog
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class StartTradingActivity : DaggerAppCompatActivity(), StartTradingActivityListener,
    OnMarketsListItemClickListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityStartTradingBinding

    lateinit var marketsAdapter: MarketsListAdapter
    lateinit var marketsArrayList: ArrayList<MarketItem>
    var totalPages: Int = 0
    var currentPages: Int = 1
    var isNeedMore: Boolean = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: StartTradingViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(StartTradingViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
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


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_start_trading)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.getMarketsList(1)
        marketsArrayList = ArrayList()
        marketsAdapter = MarketsListAdapter(this, this)

        activity.rvCourses.setLayoutManager(LinearLayoutManager(this))
        activity.rvCourses.adapter = marketsAdapter
        marketsAdapter.addData(marketsArrayList, false)

        viewModel.marketsResponse.observe(this) {
            it.let {
                totalPages = it.pagination.totalpages
                isNeedMore = totalPages != currentPages
                marketsArrayList.addAll(it.market)
                marketsAdapter.addData(marketsArrayList, isNeedMore)
            }
        }

        viewModel.invoiceData.observe(this) { paymentData ->
            paymentData?.let {
                openPaymentWebPage(it.invoice_url, viewModel.selectedMarketId, viewModel.selectedMarketPrice)
            }
        }

        viewModel.paymentStatus.observe(this) { status ->
            when (status?.lowercase()) {
                "success" -> {
                    // Payment successful, open market signals
                    viewModel.marketsResponse.value?.market?.find { it.id == viewModel.selectedMarketId }?.let {
                        openMarketSignals(it)
                    }
                }
                "failed", "expired" -> {
                    alertMessageDialog("Payment failed. Please try again.") {}
                }
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let { alertMessageDialog(it) {} }
        }

        activity.rvCourses.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // already load new data if there are only 2 items left to scroll
                if (!activity.rvCourses.canScrollVertically(1) && (currentPages < totalPages && isNeedMore)) {
                    viewModel.getMarketsList(currentPages + 1, false)
                    isNeedMore = false
                }
            }
        })

    }

    override fun openMarketSignals(market: MarketItem) {
        intentCall<MarketSignalsActivity> {
            putString("title", market.name)
            putString("courseid", market.id)
        }
    }

    override fun openPaymentWebPage(url: String, marketId: String, price: String) {
        val intent = Intent(this, PaymentWebPageActivity::class.java).apply {
            putExtra("payment_url", url)
            putExtra("marketId", marketId)
            putExtra("price", price)
        }
        paymentResultLauncher.launch(intent)
    }

//    override fun clickMarketsListItem(model: MarketItem, position: Int) {
//        Log.d("MarketPriceDebug", "Market price: ${model.price}, ID: ${model.id}")
//        viewModel.handleMarketSelection(model)
//    }

    override fun clickMarketsListItem(model: MarketItem, position: Int) {
        viewModel.handleMarketSelection(model)
    }

    override fun backPressActivity() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun openLoginActivity() {
        intentCall<LoginActivity>(2) { }
    }

    override fun openProfileActivity() {
        intentCall<AccountActivity> { }
    }

    override fun announcementActivity() {
        intentCall<AnnouncementActivity>(1) { }
    }

    override fun historyActivity() {
        intentCall<HistoryActivity>(1) { }
    }

    override fun learnTradingActivity() {
        intentCall<LearnTradingActivity>(1) { }
    }

    override fun dashboardActivity() {
        intentCall<DashboardActivity>(1){}
    }

//    override fun clickMarketsListItem(model: MarketItem, position: Int) {
//        intentCall<MarketSignalsActivity> {
//            putString("title", model.name)
//            putString("courseid", model.id)
//        }
//    }

    override fun displayMessageListener(message: String) {
        this@StartTradingActivity.alertMessageDialog(message) {
            openLoginActivity()
        }
    }


}