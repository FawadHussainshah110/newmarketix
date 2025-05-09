package com.example.marketix.presentation.history

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.HistoryData
import com.example.marketix.domain.model.MarketSignalPurchasedData
import com.example.marketix.domain.repository.HistoryRepository
import com.example.marketix.domain.repository.MarketsRepository
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.util.Constants
import com.example.marketix.util.customLog
import com.example.marketix.util.customToast
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val historyRepository: HistoryRepository,
    private val marketRepository: MarketsRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<HistoryActivityListener>() {

    private val TAG = HistoryViewModel::class.java.name

    lateinit var listener: HistoryActivityListener
    private val compositeDisposable = CompositeDisposable()

    var historyResponse = MutableLiveData<HistoryData>()
    var historyDetail = MutableLiveData<String>()
    var marketSignalPurchasedData = MutableLiveData<MarketSignalPurchasedData>()

    fun getHistoryList(pageNo: Int, isProgressShow: Boolean = true) {
        progressVisibility.postValue(isProgressShow)
        val hashMap = HashMap<String, String>()
        hashMap["page_no"] = pageNo.toString()

        historyRepository.historyListCall(
            preferencesRepository.getDeviceToken(), preferencesRepository.getAccessToken(), hashMap
        ).subscribeOn(schedulersProvider.io()).subscribe({
            it?.let {
                context.customLog(it.toString())
                if (it.status == Constants.STATUS_OK) {
                    historyResponse.postValue(it.data)
                    historyDetail.postValue(it.message)
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
    fun clearPreference(message: String) {
        if (message.contains(Constants.UNAUTHENTICATED_ACCESS)) {
            preferencesRepository.setAccessToken("")
            preferencesRepository.setSplashCheck(1)
            listener.openLoginActivity()
        }

    }

    fun backPressClick() {
        listener.backPressActivity()
    }

    fun announcementClick() {
        listener.announcementActivity()
    }

    fun learnTradingClick() {
        listener.learnTradingActivity()
    }

    fun startTradingClick() {
        listener.startTradingActivity()
    }
    fun dashboardclick(){
        listener.dashboardclick()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}