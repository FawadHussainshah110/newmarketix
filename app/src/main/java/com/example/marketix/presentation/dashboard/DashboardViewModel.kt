package com.example.marketix.presentation.dashboard

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.MarketSignalPurchasedData
import com.example.marketix.domain.repository.MarketsRepository
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.util.Constants
import com.example.marketix.util.customLog
import com.example.marketix.util.customToast
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val marketRepository: MarketsRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<DashboardActivityListener>() {

    private val TAG = DashboardViewModel::class.java.name

    lateinit var listener: DashboardActivityListener
    private val compositeDisposable = CompositeDisposable()
    var marketSignalPurchasedData = MutableLiveData<MarketSignalPurchasedData>()


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

    fun backPressClick() {
        listener.backPressClick()
    }

    fun openProfileClick() {
        listener.openProfileActivity()
    }

    fun announcementClick() {
        listener.openAnnouncementActivity()
    }

    fun historyClick() {
        listener.openHistoryActivity()
    }

    fun learnTradingClick() {
        listener.openLearnTradingActivity()
    }

    fun startTradingClick() {
        listener.openStartTradingActivity()
    }


    override fun onCleared() {
        compositeDisposable.clear()
    }

}