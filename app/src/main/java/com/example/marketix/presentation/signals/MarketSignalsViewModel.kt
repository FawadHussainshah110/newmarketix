package com.example.marketix.presentation.signals

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.AppSettingsResponse
import com.example.marketix.domain.model.MarketSignalData
import com.example.marketix.domain.repository.MarketsRepository
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.util.Constants
import com.example.marketix.util.customLog
import com.example.marketix.util.customToast
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MarketSignalsViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val marketsRepository: MarketsRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<MarketSignalsActivityListener>() {

    private val TAG = MarketSignalsViewModel::class.java.name

    lateinit var listener: MarketSignalsActivityListener
    private val compositeDisposable = CompositeDisposable()

    var courseName = MutableLiveData<String>()
    var courseid = ""
    var tradeType = ""
    var courseLearningDetail = MutableLiveData<String>()
    var isDataFound = MutableLiveData<Boolean>()
    var isActiveTrade = MutableLiveData<Boolean>()
    var noDataFound = MutableLiveData<String>()
    var noDataImage = MutableLiveData<String>()
    var courseLearningResponse = MutableLiveData<MarketSignalData>()

    var activeHaveData: Boolean = false
    var finishHaveData: Boolean = false

    fun loadSignalsContentData() {
        val marketModel =
            Gson().fromJson(preferencesRepository.getAppSettings(), AppSettingsResponse::class.java)
        for (item in marketModel.data.setting) {
            if (item.name == "${Constants.NO_SIGNAL_CONTENT}$courseid") {
                noDataFound.postValue(item.description)
                noDataImage.postValue(item.image)
            }
        }
    }

    fun getCourseLearningList(pageNo: Int, isProgressShow: Boolean = true) {
        progressVisibility.postValue(isProgressShow)
        val hashMap = HashMap<String, String>()
        hashMap["page_no"] = pageNo.toString()

        marketsRepository.marketSignalListCall(
            preferencesRepository.getDeviceToken(),
            preferencesRepository.getAccessToken(),
            courseid,
            tradeType
        ).subscribeOn(schedulersProvider.io()).subscribe({
            it?.let {
                if (it.status == Constants.STATUS_OK) {
                    courseLearningResponse.postValue(it.data)
                    courseLearningDetail.postValue(it.message)
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

    fun activeClick() {
        isActiveTrade.value = true
        tradeType = Constants.MARKETS_ACTIVE
        if (activeHaveData)
            listener.loadActiveList()
        else
            getCourseLearningList(1, true)
    }

    fun finishedTradesClick() {
        isActiveTrade.value = false
        tradeType = Constants.MARKETS_FINISHED
        if (finishHaveData)
            listener.loadFinishList()
        else
            getCourseLearningList(1, true)
    }

    fun backPressClick() {
        listener.backPressActivity()
    }

    fun historyClick() {
        listener.historyActivity()
    }

    fun learnTradingClick() {
        listener.learnTradingActivity()
    }

    fun announcementClick() {
        listener.announcementActivity()
    }
    fun dashboardClick(){
        listener.dashboardActivity()
    }


    fun openProfileClick() {
        listener.openProfileActivity()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}