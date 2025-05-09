package com.example.marketix.presentation.announcement

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketix.R
import com.example.marketix.databinding.ActivityAnnouncementBinding
import com.example.marketix.domain.model.AnnouncementItem
import com.example.marketix.presentation.dashboard.DashboardActivity
import com.example.marketix.presentation.fullimage.FullImageActivity
import com.example.marketix.presentation.fullvideo.FullVideoActivity
import com.example.marketix.presentation.history.HistoryActivity
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

class AnnouncementActivity : DaggerAppCompatActivity(), AnnouncementActivityListener,
    OnAnnouncementListItemClickListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityAnnouncementBinding
    lateinit var announcementAdapter: AnnouncementListAdapter
    lateinit var announcementArrayList: ArrayList<AnnouncementItem>

    var totalPages: Int = 0
    var currentPages: Int = 1
    var isNeedMore: Boolean = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AnnouncementViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AnnouncementViewModel::class.java)
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
        activity = DataBindingUtil.setContentView(this, R.layout.activity_announcement)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.announcementDetail.value = ""
        viewModel.getAnnouncementList(1, true)
        announcementArrayList = ArrayList()
        announcementAdapter = AnnouncementListAdapter(this, this)

        val layoutManager =
            LinearLayoutManager(this@AnnouncementActivity, LinearLayoutManager.VERTICAL, false)
        activity.rvAnnouncement.layoutManager = layoutManager
        activity.rvAnnouncement.adapter = announcementAdapter

        announcementAdapter.addData(announcementArrayList, false)
        viewModel.announcementResponse.observe(this) {
            it.let {
                totalPages = it.pagination.totalpages
//                    if (it.currentPageNo.isEmpty())
//                        it.currentPageNo = "0"
//                    currentPages = it.currentPageNo.toInt()
                isNeedMore = totalPages != currentPages
                announcementArrayList.addAll(it.announcement)
                announcementAdapter.addData(announcementArrayList, isNeedMore)
            }
        }

        announcementAdapter.addData(announcementArrayList, false)

        activity.rvAnnouncement.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // already load new data if there are only 2 items left to scroll
                if (!activity.rvAnnouncement.canScrollVertically(1) && (currentPages < totalPages && isNeedMore)) {
                    viewModel.getAnnouncementList(currentPages + 1, false)
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

    override fun openLoginActivity() {
        intentCall<LoginActivity>(2) { }
    }

    override fun historyActivity() {
        intentCall<HistoryActivity>(1) { }
    }


    override fun learnTradingActivity() {
        intentCall<LearnTradingActivity>(1) { }
    }

    override fun startTradingActivity() {
        viewModel.getCourseLearningList()
//        intentCall<StartTradingActivity>(1) { }
    }

    override fun dashboardActivity() {
        intentCall<DashboardActivity>(1) {  }
    }

    override fun clickAnnouncementListItem(model: AnnouncementItem, position: Int) {
        if (model.filetype != null && model.filetype.contains("video")) {
            intentCall<FullVideoActivity> {
                putString("imageurl", model.image)
                putBoolean("isSecure", false)
            }
        } else {
            intentCall<FullImageActivity> {
                putString("imageurl", model.image)
            }
        }
    }

    override fun displayMessageListener(message: String) {
        this@AnnouncementActivity.alertMessageDialog(message) {
            openLoginActivity()
        }
    }

}