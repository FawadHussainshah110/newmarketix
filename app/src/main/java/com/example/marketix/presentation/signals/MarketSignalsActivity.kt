package com.example.marketix.presentation.signals

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketix.R
import com.example.marketix.databinding.ActivityMarketSignalsBinding
import com.example.marketix.domain.model.SignalItem
import com.example.marketix.presentation.account.AccountActivity
import com.example.marketix.presentation.announcement.AnnouncementActivity
import com.example.marketix.presentation.dashboard.DashboardActivity
import com.example.marketix.presentation.history.HistoryActivity
import com.example.marketix.presentation.learn_trading.LearnTradingActivity
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.util.Constants.MARKETS_ACTIVE
import com.example.marketix.util.SvgLoader
import com.example.marketix.util.alertMessageDialog
import com.example.marketix.util.getPlaceHolder
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class MarketSignalsActivity : DaggerAppCompatActivity(), MarketSignalsActivityListener,
    OnMarketSignalsListItemClickListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityMarketSignalsBinding
    lateinit var marketSignalsListAdapter: MarketSignalsListAdapter
    lateinit var activeMarketSignalArrayList: ArrayList<SignalItem>
    lateinit var finishMarketSignalArrayList: ArrayList<SignalItem>

    var totalPages: Int = 0
    var currentPages: Int = 1
    var isNeedMore: Boolean = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MarketSignalsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MarketSignalsViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_market_signals)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.noDataFound.value = ""
        viewModel.courseName.value = ""
        viewModel.courseLearningDetail.value = ""
        viewModel.isActiveTrade.value = true

        if (intent.hasExtra("title")) viewModel.courseName.postValue(intent.getStringExtra("title")!!)
        if (intent.hasExtra("courseid")) viewModel.courseid = intent.getStringExtra("courseid")!!

        viewModel.tradeType = MARKETS_ACTIVE
        viewModel.getCourseLearningList(1, true)

        activeMarketSignalArrayList = ArrayList()
        finishMarketSignalArrayList = ArrayList()

        marketSignalsListAdapter = MarketSignalsListAdapter(this, this)

        val layoutManager =
            LinearLayoutManager(this@MarketSignalsActivity, LinearLayoutManager.VERTICAL, false)
        activity.rvCourseLearning.layoutManager = layoutManager
        activity.rvCourseLearning.adapter = marketSignalsListAdapter

        viewModel.courseLearningResponse.observe(this) {
            it.let {
                totalPages = it.pagination.totalpages
                isNeedMore = totalPages != currentPages
                viewModel.isDataFound.postValue(it.signal.isNotEmpty())
                if (it.signal.isNullOrEmpty())
                    viewModel.loadSignalsContentData()
                if (viewModel.tradeType == MARKETS_ACTIVE) {
                    viewModel.activeHaveData = true
                    activeMarketSignalArrayList.addAll(it.signal)
                    marketSignalsListAdapter.addData(activeMarketSignalArrayList, isNeedMore)
                } else {
                    viewModel.finishHaveData = true
                    finishMarketSignalArrayList.addAll(it.signal)
                    marketSignalsListAdapter.addData(finishMarketSignalArrayList, isNeedMore)
                }
            }
        }

        viewModel.noDataImage.observe(this) {
            it.let {
                if (it.endsWith(".svg") || it.endsWith(".SVG")) SvgLoader.loadSvg(
                    this,
                    it,
                    activity.ivNoSignalFound
                )
                else Glide.with(this@MarketSignalsActivity).load(it).fitCenter().placeholder(getPlaceHolder())
                    .into(activity.ivNoSignalFound)

            }
        }
        marketSignalsListAdapter.addData(activeMarketSignalArrayList, false)

        activity.rvCourseLearning.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // already load new data if there are only 2 items left to scroll
                if (!activity.rvCourseLearning.canScrollVertically(1) && (currentPages < totalPages && isNeedMore)) {
                    viewModel.getCourseLearningList(currentPages + 1, false)
                    isNeedMore = false
                }
            }
        })
    }

    override fun loadFinishList() {
        marketSignalsListAdapter.addData(finishMarketSignalArrayList, isNeedMore)
    }

    override fun loadActiveList() {
        marketSignalsListAdapter.addData(activeMarketSignalArrayList, isNeedMore)
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
        intentCall<DashboardActivity>(1) {  }
    }

    override fun clickMarketSignalsListItem(model: SignalItem, position: Int) {
    }

    override fun displayMessageListener(message: String) {
        this@MarketSignalsActivity.alertMessageDialog(message) {
            openLoginActivity()
        }
    }

}