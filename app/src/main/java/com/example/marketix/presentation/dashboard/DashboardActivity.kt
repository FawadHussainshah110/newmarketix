package com.example.marketix.presentation.dashboard

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.marketix.R
import com.example.marketix.databinding.ActivityDashboardBinding
import com.example.marketix.presentation.account.AccountActivity
import com.example.marketix.presentation.announcement.AnnouncementActivity
import com.example.marketix.presentation.history.HistoryActivity
import com.example.marketix.presentation.learn_trading.LearnTradingActivity
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.marketpayment.MarketPaymentActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.presentation.start_trading.StartTradingActivity
import com.example.marketix.util.Constants
import com.example.marketix.util.intentCall
import com.example.marketix.util.intentCallForResult
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class DashboardActivity : DaggerAppCompatActivity(), DashboardActivityListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityDashboardBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: DashboardViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DashboardViewModel::class.java)
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
        activity = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.marketSignalPurchasedData.observe(this) {
            it.let {
                if (it.purchased) {
                    intentCall<StartTradingActivity> { }
                } else
                    intentCallForResult<MarketPaymentActivity>(startForResult) {}
            }
        }
    }

    override fun backPressClick() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun openProfileActivity() {
        intentCall<AccountActivity> { }
    }

    override fun openLoginActivity() {
        intentCall<LoginActivity>(2) { }
    }

    override fun openAnnouncementActivity() {
        intentCall<AnnouncementActivity> { }
    }

    override fun openHistoryActivity() {
        intentCall<HistoryActivity> { }
    }

    override fun openLearnTradingActivity() {
        intentCall<LearnTradingActivity> { }
    }

    override fun openStartTradingActivity() {
        viewModel.getCourseLearningList()
    }
}