package com.example.marketix.presentation.learn_trading

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.marketix.databinding.ActivityLearnTradingBinding
import com.example.marketix.domain.model.CourseItem
import com.example.marketix.domain.repository.PaymentRepository
import com.example.marketix.presentation.account.AccountActivity
import com.example.marketix.presentation.announcement.AnnouncementActivity
import com.example.marketix.presentation.courselearning.CourseLearningActivity
import com.example.marketix.presentation.coursepayment.CoursePaymentActivity
import com.example.marketix.presentation.dashboard.DashboardActivity
import com.example.marketix.presentation.history.HistoryActivity
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

class LearnTradingActivity : DaggerAppCompatActivity(), LearnTradingActivityListener,
    OnCoursesListItemClickListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityLearnTradingBinding

    lateinit var coursesAdapter: CoursesListAdapter
    lateinit var coursesArrayList: ArrayList<CourseItem>
    var totalPages: Int = 0
    var currentPages: Int = 1
    var isNeedMore: Boolean = true
    private var lastPurchasedCourseId: String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LearnTradingViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(LearnTradingViewModel::class.java)
    }

    @Inject
    lateinit var paymentRepository: PaymentRepository

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Constants.COURSES_PAYMENT -> {
                    val intent: Intent? = result.data
                    if (intent?.getIntExtra("data", 0) == 1) {
                        // Payment was successful
                        val courseId = intent.getStringExtra("courseId") ?: lastPurchasedCourseId
                        courseId?.let {
                            // Refresh the course list
                            coursesArrayList = ArrayList()
                            viewModel.getCoursesList(1)

                            // Navigate to the purchased course
                            viewModel.coursesResponse.value?.course?.find { it.id == courseId }?.let { course ->
                                navigateToCourseLearning(course)
                            }
                        }
                    }
                }
                Constants.MARKETS_PAYMENT -> {
                    val intent: Intent? = result.data
                    if (intent?.getIntExtra("data", 0) == 1) {
                        viewModel.getCourseLearningList()
                    }
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_learn_trading)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.getCoursesList(1)

        coursesArrayList = ArrayList()
        coursesAdapter = CoursesListAdapter(this, this)

        activity.rvCourses.setLayoutManager(GridLayoutManager(this, 2))

        val layoutManager =
            LinearLayoutManager(this@LearnTradingActivity, LinearLayoutManager.VERTICAL, false)
        activity.rvCourses.setLayoutManager(GridLayoutManager(this, 1))
        activity.rvCourses.adapter = coursesAdapter

        coursesAdapter.addData(coursesArrayList, false)

        viewModel.coursesResponse.observe(this) { response ->
            response?.let {
                totalPages = it.pagination.totalpages
                isNeedMore = totalPages != currentPages
                coursesArrayList.addAll(it.course)
                coursesAdapter.addData(coursesArrayList, isNeedMore)

                // After refresh, check if we need to navigate to a purchased course
                lastPurchasedCourseId?.let { courseId ->
                    it.course.find { course -> course.id == courseId }?.let { course ->
                        if (course.purchased == true) {
                            navigateToCourseLearning(course)
                            lastPurchasedCourseId = null // Reset after navigation
                        }
                    }
                }
            }
        }

        activity.rvCourses.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!activity.rvCourses.canScrollVertically(1) && (currentPages < totalPages && isNeedMore)) {
                    viewModel.getCoursesList(currentPages + 1, false)
                    isNeedMore = false
                }
            }
        })

        viewModel.marketSignalPurchasedData.observe(this) {
            it?.let {
                if (it.purchased) {
                    intentCall<StartTradingActivity>(1) { }
                } else {
                    intentCallForResult<MarketPaymentActivity>(startForResult) {}
                }
            }
        }
    }

    private fun navigateToCourseLearning(course: CourseItem) {
        intentCall<CourseLearningActivity> {
            putString("title", course.name)
            putString("courseid", course.id)
            // Add any other necessary extras
        }
    }

    override fun clickCoursesListItem(model: CourseItem, position: Int) {
//        if (model.purchased == true) {
        if (paymentRepository.hasCourseAccess(model.id)) {
            navigateToCourseLearning(model)
        } else {
            Log.d("PRICEFINDING ", "${model.price}")
            lastPurchasedCourseId = model.id // Store the course ID in case payment succeeds
            intentCallForResult<CoursePaymentActivity>(startForResult) {
                putString("title", model.name)
                putString("courseid", model.id)
                putString("price", model.price)
                putString("paymentdetail", model.description)
            }
        }
    }

    override fun backPressActivity() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun openLoginActivity() {
        intentCall<LoginActivity> { }
    }

    override fun announcementActivity() {
        intentCall<AnnouncementActivity>(1) { }
    }

    override fun historyActivity() {
        intentCall<HistoryActivity>(1) { }
    }

    override fun startTradingActivity() {
        viewModel.getCourseLearningList()
    }

    override fun opendashboardActivity() {
        intentCall<DashboardActivity>(1) { }
    }

    override fun openProfileActivity() {
        intentCall<AccountActivity> { }
    }

    override fun displayMessageListener(message: String) {
        this@LearnTradingActivity.alertMessageDialog(message) {
            openLoginActivity()
        }
    }
}