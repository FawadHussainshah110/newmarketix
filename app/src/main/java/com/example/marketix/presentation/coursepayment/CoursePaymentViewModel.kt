package com.example.marketix.presentation.coursepayment

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.CreateInvoiceRequest
import com.example.marketix.domain.model.CreateInvoiceResponse
import com.example.marketix.domain.model.PaymentStatusResponse
import com.example.marketix.domain.model.Result
import com.example.marketix.domain.repository.PaymentRepository
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.util.Constants
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class CoursePaymentViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val paymentRepository: PaymentRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<CoursePaymentActivityListener>() {

    var listener: CoursePaymentActivityListener? = null

    val courseName = MutableLiveData<String>()
    val price = MutableLiveData<String>()
    val numericPrice = MutableLiveData<Double>()
    val paymentDetail = MutableLiveData<String>()
    val invoiceData = MutableLiveData<CreateInvoiceResponse>()
    val paymentStatus = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String>()

    var courseID: String = ""

    init {
        price.observeForever { priceStr ->
            parsePrice(priceStr)
        }
    }

    private fun parsePrice(priceString: String?) {
        try {
            priceString?.let {
                val numericValue = it.replace("[^\\d.]".toRegex(), "")
                    .trim()
                    .toDoubleOrNull()

                numericPrice.value = numericValue ?: 0.0
                if (numericValue == 0.0) {
                    errorMessage.postValue("This market item is free")
                }
                Log.d("PriceParsing", "Parsed $it to $numericValue")
            } ?: run {
                numericPrice.value = 0.0
                Log.e("PriceParsing", "Price string is null")
            }
        } catch (e: Exception) {
            numericPrice.value = 0.0
            Log.e("PriceParsing", "Failed to parse price: $priceString", e)
            errorMessage.postValue("Invalid price format")
        }
    }

    fun createPaymentLink() {
        if (courseID.isEmpty()) {
            errorMessage.postValue("Course ID is missing")
            return
        }

        val amount = numericPrice.value ?: 0.0
        if (amount <= 0) {
            errorMessage.postValue("Invalid price amount")
            return
        }

        viewModelScope.launch {
            try {
                val request = CreateInvoiceRequest(
                    price_amount = amount,
                    price_currency = "USD",
                    order_id = "course_$courseID",
                    order_description = "Payment for ${courseName.value}",
                    ipn_callback_url = "https://yourdomain.com/ipn-callback",
                    pay_currency = null,
                    success_url = "https://yourdomain.com/success?courseId=$courseID",
                    cancel_url = "https://yourdomain.com/cancel?courseId=$courseID"
                )

                when (val result = paymentRepository.createInvoice(request)) {
                    is Result.Success -> {
                        invoiceData.postValue(result.data)
                    }

                    is Result.Error -> {
                        errorMessage.postValue("Payment error: ${result.exception.message}")
                    }
                }
            } catch (e: Exception) {
                errorMessage.postValue("Payment failed: ${e.message}")
            }
        }
    }


    fun checkPaymentStatus(paymentId: String) {
        showProgress(true)
        viewModelScope.launch {
            when (val result = paymentRepository.checkPaymentStatus(paymentId)) {
                is Result.Success -> {
                    if (result.data.payment_status.equals("finished", ignoreCase = true) ||
                        result.data.payment_status.equals("completed", ignoreCase = true)
                    ) {
                        paymentRepository.setCourseAccess(courseID, 30)
                    }
                    paymentStatus.postValue(result.data.payment_status)
                    Log.d("PaymentFlow", "Payment status: ${result.data.payment_status}")
                }

                is Result.Error -> {
                    // ... existing error handling ...
                }
            }
            showProgress(false)
        }
    }

    // for testing connection
//    fun testConnection() {
//        viewModelScope.launch {
//            showProgress(true)
//            val isConnected = paymentRepository.testNowPaymentsIntegration()
//            showProgress(false)
//
//            if (isConnected) {
//                errorMessage.postValue("Connection successful!")
//            } else {
//                errorMessage.postValue("Connection failed. Check logs.")
//            }
//        }
//    }

    fun backPressClick() {
        listener?.backPressActivity()
    }

    fun openProfileClick() {
        listener?.openProfileActivity()
    }

    private fun showProgress(show: Boolean) {
        progressVisibility.postValue(show)
    }

    private fun handleUnauthenticatedAccess() {
        preferencesRepository.setAccessToken("")
        preferencesRepository.setSplashCheck(1)
        listener?.openLoginActivity()
    }


    fun initiatePayment() {
        val amount = numericPrice.value ?: 0.0
        if (amount <= 0) {
            errorMessage.postValue("Please enter a valid amount")
            return
        }
        createPaymentLink()
    }

    override fun onCleared() {
        price.removeObserver { parsePrice(it) }
        super.onCleared()
    }
}