package com.example.marketix.presentation.history

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketix.R
import com.example.marketix.databinding.ActivityHistoryBinding
import com.example.marketix.domain.model.HistoryItem
import com.example.marketix.presentation.announcement.AnnouncementActivity
import com.example.marketix.presentation.dashboard.DashboardActivity
import com.example.marketix.presentation.fullimage.FullImageActivity
import com.example.marketix.presentation.learn_trading.LearnTradingActivity
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.marketpayment.MarketPaymentActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.presentation.start_trading.StartTradingActivity
import com.example.marketix.util.Constants
import com.example.marketix.util.alertMessageDialog
import com.example.marketix.util.intentCall
import com.example.marketix.util.intentCallForResult
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject


class HistoryActivity : DaggerAppCompatActivity(), HistoryActivityListener,
    OnHistoryListItemClickListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityHistoryBinding

    lateinit var historyAdapter: HistoryListAdapter
    lateinit var historyArrayList: ArrayList<HistoryItem>
    var totalPages: Int = 0
    var currentPages: Int = 1
    var isNeedMore: Boolean = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(HistoryViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Constants.MARKETS_PAYMENT -> {
                    val intent: Intent = result.data!!
                    if (intent.getIntExtra("data", 0) == 1)
                        viewModel.getCourseLearningList()
                }

            }
        }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_history)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.historyDetail.value = ""
        viewModel.getHistoryList(1, true)
        historyArrayList = ArrayList()
        historyAdapter = HistoryListAdapter(this, this)

        activity.rvHistory.setLayoutManager(GridLayoutManager(this, 2))

        val layoutManager =
            LinearLayoutManager(this@HistoryActivity, LinearLayoutManager.VERTICAL, false)
        activity.rvHistory.setLayoutManager(GridLayoutManager(this, 2))
//        activity.rvHistory.layoutManager = layoutManager
        activity.rvHistory.adapter = historyAdapter

        viewModel.historyResponse.observe(this) {
            it.let {
                totalPages = it.pagination.totalpages
//                    if (it.currentPageNo.isEmpty())
//                        it.currentPageNo = "0"
//                    currentPages = it.currentPageNo.toInt()
                isNeedMore = totalPages != currentPages
                historyArrayList.addAll(it.history)
                historyAdapter.addData(historyArrayList, isNeedMore)
            }
        }

        historyAdapter.addData(historyArrayList, false)

        activity.rvHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // already load new data if there are only 2 items left to scroll
                if (!activity.rvHistory.canScrollVertically(1) && (currentPages < totalPages && isNeedMore)) {
                    viewModel.getHistoryList(currentPages + 1, false)
                    isNeedMore = false
                    currentPages++
                }
            }
        })

        viewModel.marketSignalPurchasedData.observe(this) {
            it.let {
                if (it.purchased) {
                    intentCall<StartTradingActivity>(1) { }
                } else
                    intentCallForResult<MarketPaymentActivity>(startForResult) {}
            }
        }

    }

    override fun backPressActivity() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun announcementActivity() {
        intentCall<AnnouncementActivity>(1) { }
    }

    override fun learnTradingActivity() {
        intentCall<LearnTradingActivity>(1) { }
    }

    override fun startTradingActivity() {
//        intentCall<StartTradingActivity>(1) { }
        viewModel.getCourseLearningList()

    }

    override fun dashboardclick() {
        intentCall<DashboardActivity>(1) {}
    }

    override fun openLoginActivity() {
        intentCall<LoginActivity>(2) { }
    }

    override fun clickHistoryListItem(model: HistoryItem, position: Int) {
        intentCall<FullImageActivity> {
            putString("imageurl", model.image)
        }
    }

    override fun displayMessageListener(message: String) {
        this@HistoryActivity.alertMessageDialog(message) {
            openLoginActivity()
        }
    }

}