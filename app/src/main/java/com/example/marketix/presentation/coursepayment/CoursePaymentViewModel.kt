package com.example.marketix.presentation.coursepayment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.CoursePaymentLinkData
import com.example.marketix.domain.repository.CoursesRepository
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.util.Constants
import com.example.marketix.util.customLog
import com.example.marketix.util.customToast
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CoursePaymentViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val coursesRepository: CoursesRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<CoursePaymentActivityListener>() {

    private val TAG = CoursePaymentViewModel::class.java.name

    lateinit var listener: CoursePaymentActivityListener
    private val compositeDisposable = CompositeDisposable()

    var courseID = ""
    var courseName = MutableLiveData<String>()
    var payment = MutableLiveData<String>()
    var paymentdetail = MutableLiveData<String>()
    var coursePaymentLinkData = MutableLiveData<CoursePaymentLinkData>()

    fun getCoursePaymentLink() {
        if (courseID.isNotEmpty()) {
            progressVisibility.postValue(true)

            coursesRepository.coursePaymentLink(
                preferencesRepository.getDeviceToken(),
                preferencesRepository.getAccessToken(),
                courseID
            ).subscribeOn(schedulersProvider.io()).subscribe({
                it?.let {
                    context.customLog(it.toString())
                    if (it.status == Constants.STATUS_OK) {
                        coursePaymentLinkData.postValue(it.data)
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
    }

    fun backPressClick() {
        listener.backPressActivity()
    }

    fun openProfileClick() {
        listener.openProfileActivity()
    }

    fun paymentClick() {
        getCoursePaymentLink()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}