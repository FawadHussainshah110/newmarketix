package com.example.marketix.presentation.courselearning

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.CourseLessonData
import com.example.marketix.domain.repository.CoursesRepository
import com.example.marketix.domain.repository.PaymentRepository
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.util.Constants
import com.example.marketix.util.customLog
import com.example.marketix.util.customToast
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CourseLearningViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val coursesRepository: CoursesRepository,
    private val schedulersProvider: SchedulersProvider,
    private val paymentRepository: PaymentRepository
) : BaseViewModel<CourseLearningActivityListener>() {

    private val TAG = CourseLearningViewModel::class.java.name

    lateinit var listener: CourseLearningActivityListener
    private val compositeDisposable = CompositeDisposable()

    var courseName = MutableLiveData<String>()
    var courseid = ""
    var courseLearningDetail = MutableLiveData<String>()
    var isDataEmpty = MutableLiveData<Boolean>()
    var noDataFound = MutableLiveData<String>()
    var courseLearningResponse = MutableLiveData<CourseLessonData>()

    fun checkCourseAccess(): Boolean {
        return paymentRepository.hasCourseAccess(courseid)
    }

    fun getCourseLearningList(pageNo: Int, isProgressShow: Boolean = true) {
        if (!checkCourseAccess()) {
            listener.displayMessageListener("You don't have access to this course. Please purchase it first.")
            return
        }


        progressVisibility.postValue(isProgressShow)
        val hashMap = HashMap<String, String>()
        hashMap["page_no"] = pageNo.toString()

        coursesRepository.courseLearningListCall(
            preferencesRepository.getDeviceToken(),
            preferencesRepository.getAccessToken(),
            courseid,
            hashMap
        ).subscribeOn(schedulersProvider.io()).subscribe({
            it?.let {
                context.customLog(it.toString())
                if (it.status == Constants.STATUS_OK) {
                    courseLearningResponse.postValue(it.data)
                    courseLearningDetail.postValue(it.message)
                    noDataFound.postValue(it.message)
                } else {
                    if (it.message.contains(Constants.UNAUTHENTICATED_ACCESS)) {
                        preferencesRepository.setAccessToken("")
                        preferencesRepository.setSplashCheck(1)
                        listener.displayMessageListener(it.message)
                    } else
                        context.customToast(it.message)
                }
                progressVisibility.postValue(false)
            }
        }, {
            if (it.toString().contains(Constants.UNAUTHENTICATED_ACCESS)) {
                preferencesRepository.setAccessToken("")
                preferencesRepository.setSplashCheck(1)
                listener.openLoginActivity()
            }
            context.customLog(it.toString())
            progressVisibility.postValue(false)
        }).let {
            compositeDisposable.add(it)
        }
    }

    fun setCourseAccess(courseId: String, days: Int) {
        paymentRepository.setCourseAccess(courseId, days)
    }

    fun backPressClick() {
        listener.backPressActivity()
    }

    fun openProfileClick() {
        listener.openProfileActivity()
    }

    fun announcementClick() {
        listener.announcementActivity()
    }

    fun historyClick() {
        listener.historyActivity()
    }

    fun startTradingClick() {
        listener.startTradingActivity()
    }

    fun dashboardClick() {
        listener.dashboardActivity()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}