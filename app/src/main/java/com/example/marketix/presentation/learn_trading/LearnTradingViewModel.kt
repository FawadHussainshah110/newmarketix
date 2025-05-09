package com.example.marketix.presentation.learn_trading

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.CourseData
import com.example.marketix.domain.model.MarketSignalPurchasedData
import com.example.marketix.domain.repository.CoursesRepository
import com.example.marketix.domain.repository.MarketsRepository
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.util.Constants
import com.example.marketix.util.customLog
import com.example.marketix.util.customToast
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LearnTradingViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val coursesRepository: CoursesRepository,
    private val marketRepository: MarketsRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<LearnTradingActivityListener>() {

    private val TAG = LearnTradingViewModel::class.java.name

    lateinit var listener: LearnTradingActivityListener
    private val compositeDisposable = CompositeDisposable()

    var coursesResponse = MutableLiveData<CourseData>()
    var coursesDetail = MutableLiveData<String>()
    var marketSignalPurchasedData = MutableLiveData<MarketSignalPurchasedData>()

    fun getCoursesList(pageNo: Int, isProgressShow: Boolean = true) {
        progressVisibility.postValue(isProgressShow)
        val hashMap = HashMap<String, String>()
        hashMap["page_no"] = pageNo.toString()

        coursesRepository.coursesListCall(
            preferencesRepository.getDeviceToken(), preferencesRepository.getAccessToken(), hashMap
        ).subscribeOn(schedulersProvider.io()).subscribe({
            it?.let {
                context.customLog(it.toString())
                if (it.status == Constants.STATUS_OK) {
//                        context.customToast(context.resources.getString(R.string.successfully_updated))
                    coursesResponse.postValue(it.data)
                    coursesDetail.postValue(it.message)
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

    fun getCourseLearningList() {
        progressVisibility.postValue(true)

        marketRepository.isMarketSignalPurchased(
            preferencesRepository.getDeviceToken(),
            preferencesRepository.getAccessToken()
        ).subscribeOn(schedulersProvider.io()).subscribe({
            it?.let {
                context.customLog(it.toString())
                if (it.status == Constants.STATUS_OK) {
//                        context.customToast(context.resources.getString(R.string.successfully_updated))
                    marketSignalPurchasedData.postValue(it.data)
                } else {
                    if (it.message.contains(Constants.UNAUTHENTICATED_ACCESS)) {
                        preferencesRepository.setAccessToken("")
                        preferencesRepository.setSplashCheck(1)
                        listener.openLoginActivity()
                    }
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

    fun backPressClick() {
        listener.backPressActivity()
    }
    fun opendashboardActivity(){
        listener.opendashboardActivity()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}