package com.example.marketix.presentation.marketpayment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.AppSettingsResponse
import com.example.marketix.domain.model.MarketPaymentLinkData
import com.example.marketix.domain.repository.MarketsRepository
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.util.Constants
import com.example.marketix.util.customLog
import com.example.marketix.util.customToast
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MarketPaymentViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val marketRepository: MarketsRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<MarketPaymentActivityListener>() {

    private val TAG = MarketPaymentViewModel::class.java.name

    lateinit var listener: MarketPaymentActivityListener
    private val compositeDisposable = CompositeDisposable()

    var marketName = MutableLiveData<String>()
    var marketurl = MutableLiveData<String>()
    var payment = MutableLiveData<String>()
    var paymentdetail = MutableLiveData<String>()
    var isVideoDataFound = MutableLiveData<Boolean>()

    var marketPaymentLinkData = MutableLiveData<MarketPaymentLinkData>()

    fun loadMarketData() {
        val marketModel =
            Gson().fromJson(preferencesRepository.getAppSettings(), AppSettingsResponse::class.java)
        for (item in marketModel.data.setting) {
            if (item.name == Constants.ALL_MARKET_MONTHLY_FEE_SCREEN_CONTENT) {
                payment.postValue("${item.title}/month")
            }
            if (item.name == Constants.ALL_MARKET_MONTHLY_FEE_DETAIL_SCREEN_CONTENT) {
                marketName.postValue(item.title)
                paymentdetail.postValue(item.description)
                marketurl.postValue(item.image)
                if (item.filetype.contains("video"))
                    isVideoDataFound.postValue(true)
                else
                    isVideoDataFound.postValue(false)
            }
        }
    }

    fun getCoursePaymentLink() {
        progressVisibility.postValue(true)

        marketRepository.marketSignalPaymentLink(
            preferencesRepository.getDeviceToken(),
            preferencesRepository.getAccessToken()
        ).subscribeOn(schedulersProvider.io()).subscribe({
            it?.let {
                context.customLog(it.toString())
                if (it.status == Constants.STATUS_OK) {
                    marketPaymentLinkData.postValue(it.data)
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

    fun playVideoClick() {
        listener.playVideoActivity()
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