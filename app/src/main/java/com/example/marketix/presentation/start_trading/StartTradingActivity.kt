package com.example.marketix.presentation.start_trading

import android.content.Context
import android.os.Build
import android.os.Bundle
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

        activity.rvCourses.setLayoutManager(GridLayoutManager(this, 2))

        val layoutManager =
            LinearLayoutManager(this@StartTradingActivity, LinearLayoutManager.VERTICAL, false)
        activity.rvCourses.setLayoutManager(GridLayoutManager(this, 1))
//        activity.rvHistory.layoutManager = layoutManager
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

    override fun clickMarketsListItem(model: MarketItem, position: Int) {
        intentCall<MarketSignalsActivity> {
            putString("title", model.name)
            putString("courseid", model.id)
        }
    }

    override fun displayMessageListener(message: String) {
        this@StartTradingActivity.alertMessageDialog(message) {
            openLoginActivity()
        }
    }

}