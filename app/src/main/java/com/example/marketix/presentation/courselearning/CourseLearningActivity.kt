package com.example.marketix.presentation.courselearning

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketix.R
import com.example.marketix.databinding.ActivityCourseLearningBinding
import com.example.marketix.domain.model.LessonItem
import com.example.marketix.presentation.account.AccountActivity
import com.example.marketix.presentation.announcement.AnnouncementActivity
import com.example.marketix.presentation.coursepayment.CoursePaymentActivity
import com.example.marketix.presentation.dashboard.DashboardActivity
import com.example.marketix.presentation.fullvideo.FullVideoActivity
import com.example.marketix.presentation.history.HistoryActivity
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.presentation.start_trading.StartTradingActivity
import com.example.marketix.util.alertMessageDialog
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class CourseLearningActivity : DaggerAppCompatActivity(), CourseLearningActivityListener,
    OnCourseLessonListItemClickListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityCourseLearningBinding
    lateinit var courseLessonAdapter: CourseLessonListAdapter
    lateinit var courseLessonArrayList: ArrayList<LessonItem>

    var totalPages: Int = 0
    var currentPages: Int = 1
    var isNeedMore: Boolean = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CourseLearningViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CourseLearningViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_course_learning)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.noDataFound.value = ""
        viewModel.courseName.value = ""
        viewModel.courseLearningDetail.value = ""

        if (intent.hasExtra("title"))
            viewModel.courseName.postValue(intent.getStringExtra("title")!!)
        if (intent.hasExtra("courseid"))
            viewModel.courseid = intent.getStringExtra("courseid")!!

        if (!viewModel.checkCourseAccess()) {
            val price = intent.getStringExtra("price") ?: "0.0"
            if (price == "0.0") {
                viewModel.setCourseAccess(viewModel.courseid, 30)

                    viewModel.getCourseLearningList(1, true)
            } else {
                intentCall<CoursePaymentActivity> {
                    putString("title", viewModel.courseName.value)
                    putString("courseid", viewModel.courseid)
                    putString("price", price)
                    putString("paymentdetail", intent.getStringExtra("paymentdetail") ?: "")
                }
                finish()
                return
            }
        }

        viewModel.getCourseLearningList(1, true)

        courseLessonArrayList = ArrayList()
        courseLessonAdapter = CourseLessonListAdapter(this, this)

        val layoutManager =
            LinearLayoutManager(this@CourseLearningActivity, LinearLayoutManager.VERTICAL, false)
        activity.rvCourseLearning.layoutManager = layoutManager
        activity.rvCourseLearning.adapter = courseLessonAdapter

        courseLessonAdapter.addData(courseLessonArrayList, false)
        viewModel.courseLearningResponse.observe(this) {
            it.let {
                totalPages = it.pagination.totalpages
                isNeedMore = totalPages != currentPages
                viewModel.isDataEmpty.postValue(it.lesson.isEmpty())
                Log.d(TAG, "onCreate: ${viewModel.isDataEmpty.value}")
                courseLessonArrayList.addAll(it.lesson)
                courseLessonAdapter.addData(courseLessonArrayList, isNeedMore)
            }
        }

        courseLessonAdapter.addData(courseLessonArrayList, false)

        activity.rvCourseLearning.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!activity.rvCourseLearning.canScrollVertically(1) && (currentPages < totalPages && isNeedMore)) {
                    viewModel.getCourseLearningList(currentPages + 1, false)
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

    override fun clickCourseLessonListItem(model: LessonItem, position: Int) {
        intentCall<FullVideoActivity> {
            putString("imageurl", model.link)
            putBoolean("isSecure", true)
        }
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

    override fun startTradingActivity() {
        intentCall<StartTradingActivity>(1) { }
    }

    override fun dashboardActivity() {
        intentCall<DashboardActivity>(1) { }
    }

    override fun displayMessageListener(message: String) {
        this@CourseLearningActivity.alertMessageDialog(message) {
            openLoginActivity()
        }
    }
}