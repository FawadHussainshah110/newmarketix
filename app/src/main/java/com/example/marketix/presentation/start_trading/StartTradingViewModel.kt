package com.example.marketix.presentation.start_trading

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.MarketsData
import com.example.marketix.domain.repository.MarketsRepository
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.util.Constants
import com.example.marketix.util.customLog
import com.example.marketix.util.customToast
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class StartTradingViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val marketsRepository: MarketsRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<StartTradingActivityListener>() {

    private val TAG = StartTradingViewModel::class.java.name

    lateinit var listener: StartTradingActivityListener
    private val compositeDisposable = CompositeDisposable()

    var marketsResponse = MutableLiveData<MarketsData>()
    var marketsDetail = MutableLiveData<String>()

    fun getMarketsList(pageNo: Int, isProgressShow: Boolean = true) {
        progressVisibility.postValue(isProgressShow)
        val hashMap = HashMap<String, String>()
        hashMap["page_no"] = pageNo.toString()

        marketsRepository.marketsListCall(
            preferencesRepository.getDeviceToken(), preferencesRepository.getAccessToken(), hashMap
        ).subscribeOn(schedulersProvider.io()).subscribe({
            it?.let {
                context.customLog(it.toString())
                if (it.status == Constants.STATUS_OK) {
//                        context.customToast(context.resources.getString(R.string.successfully_updated))
                    marketsResponse.postValue(it.data)
                    marketsDetail.postValue(it.message)
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

    fun openProfileClick() {
        listener.openProfileActivity()
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
    fun dashboardclik(){
        listener.dashboardActivity()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}