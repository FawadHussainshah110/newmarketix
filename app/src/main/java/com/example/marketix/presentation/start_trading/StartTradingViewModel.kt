package com.example.marketix.presentation.start_trading

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.CreateInvoiceResponse
import com.example.marketix.domain.model.MarketItem
import com.example.marketix.domain.model.MarketsData
import com.example.marketix.domain.repository.MarketsRepository
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.util.Constants
import com.example.marketix.util.customLog
import com.example.marketix.util.customToast
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.marketix.domain.model.Result
import com.example.marketix.domain.repository.PaymentRepository


class StartTradingViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val marketsRepository: MarketsRepository,
    private val schedulersProvider: SchedulersProvider,
    private val paymentRepository: PaymentRepository
) : BaseViewModel<StartTradingActivityListener>() {

    private val TAG = StartTradingViewModel::class.java.name

    lateinit var listener: StartTradingActivityListener
    private val compositeDisposable = CompositeDisposable()

    var marketsResponse = MutableLiveData<MarketsData>()
    var marketsDetail = MutableLiveData<String>()
    val paymentStatus = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String>()
    val invoiceData = MutableLiveData<CreateInvoiceResponse>()
    var selectedMarketId = ""
    var selectedMarketPrice = ""

    // for shared preference
//        selectedMarketId = market.id
//        selectedMarketPrice = market.price.toString() ?: "0.0"
//
//        Log.d(TAG, "Processing payment for market ${market.id} with price ${market.price}")
//
//        if (paymentRepository.hasMarketAccess(market.id)) {
//            listener.openMarketSignals(market)
//        } else {
//            initiatePayment(market.price)
//        }
//    }

    fun handleMarketSelection(market: MarketItem) {
        viewModelScope.launch {
            try {
                if (checkMarketAccess(market.id)) {
                    listener?.openMarketSignals(market)
                } else {
                    initiatePayment(market.price)
                }
            } catch (e: Exception) {
                errorMessage.postValue("Error checking market access")
                Log.e(TAG, "Error checking market access", e)
            }
        }
    }

    // for test payment
//    fun handleMarketSelection(market: MarketItem) {
//        selectedMarketId = market.id
//        val testPrice = 10.0
//        selectedMarketPrice = testPrice.toString()
//
//        Log.d(TAG, "Using test price: $testPrice for market: $selectedMarketId")
//
//        initiatePayment(testPrice)
//    }

    private fun initiatePayment(amount: Double) {
        viewModelScope.launch {
            when (val result = paymentRepository.createMarketPayment(amount, selectedMarketId)) {
                is Result.Success -> {
                    Log.d(TAG, "Payment initiated successfully")
                    invoiceData.postValue(result.data)
                }
                is Result.Error -> {
                    Log.e(TAG, "Payment initiation failed", result.exception)
                    errorMessage.postValue("Payment error: ${result.exception.message}")
                }
            }
        }
    }

    fun checkPaymentStatus(paymentId: String) {
        viewModelScope.launch {
            when (val result = paymentRepository.checkPaymentStatus(paymentId)) {
                is Result.Success -> {
                    if (result.data.payment_status.equals("finished", ignoreCase = true) ||
                        result.data.payment_status.equals("completed", ignoreCase = true)) {
                        paymentRepository.setMarketAccess(selectedMarketId, 30)
                        paymentStatus.postValue("success")
                    } else {
                        paymentStatus.postValue(result.data.payment_status)
                    }
                }
                is Result.Error -> {
                    errorMessage.postValue("Payment check failed: ${result.exception.message}")
                }
            }
        }
    }


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

    suspend fun checkMarketAccess(marketId: String): Boolean {
        return paymentRepository.hasMarketAccess(marketId)
    }



}