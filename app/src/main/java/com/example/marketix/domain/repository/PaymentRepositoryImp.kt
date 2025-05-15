package com.example.marketix.data.repository

import android.util.Log
import com.example.marketix.data.source.remote.NowPaymentService
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.domain.model.*
import com.example.marketix.domain.repository.PaymentRepository
import com.example.marketix.domain.repository.PreferencesRepository
import retrofit2.Response
import javax.inject.Inject

class PaymentRepositoryImp @Inject constructor(
    private val nowPaymentService: NowPaymentService,
    private val schedulersProvider: SchedulersProvider,
    private val preferencesRepository: PreferencesRepository
) : PaymentRepository {

    companion object {
        private const val TAG = "PaymentRepository"
        private const val COURSE_ACCESS_PREFIX = "course_access_"
        private const val MARKET_ACCESS_PREFIX = "market_access_"
    }

    override fun setCourseAccess(courseId: String, days: Int) {
        val expiryTime = System.currentTimeMillis() + (days * 24 * 60 * 60 * 1000L)
        preferencesRepository.setCourseAccess(courseId, expiryTime.toString())
    }

    override fun hasCourseAccess(courseId: String): Boolean {
        val expiryString = preferencesRepository.getCourseAccess(courseId)
        if (expiryString.isBlank()) return false

        return try {
            val expiryTime = expiryString.toLong()
            System.currentTimeMillis() < expiryTime
        } catch (e: NumberFormatException) {
            false
        }
    }

    override fun setMarketAccess(marketId: String, days: Int) {
        val expiryTime = System.currentTimeMillis() + (days * 24 * 60 * 60 * 1000L)
        preferencesRepository.setCourseAccess(
            "${MARKET_ACCESS_PREFIX}$marketId",
            expiryTime.toString()
        )
    }

    override fun hasMarketAccess(marketId: String): Boolean {
        val expiryString = preferencesRepository.getCourseAccess("${MARKET_ACCESS_PREFIX}$marketId")
        if (expiryString.isBlank()) return false

        return try {
            val expiryTime = expiryString.toLong()
            System.currentTimeMillis() < expiryTime
        } catch (e: NumberFormatException) {
            false
        }
    }

    override suspend fun createMarketPayment(
        amount: Double,
        marketId: String
    ): Result<CreateInvoiceResponse> {
        return try {
            val request = CreateInvoiceRequest(
                price_amount = amount,
                price_currency = "USD",
                order_id = "market_$marketId",
                order_description = "Payment for market signals: $marketId",
                ipn_callback_url = "https://yourdomain.com/ipn-callback",
                pay_currency = null,
                success_url = "https://yourdomain.com/success?marketId=$marketId",
                cancel_url = "https://yourdomain.com/cancel?marketId=$marketId"
            )

            Log.d(TAG, "Creating market payment invoice: ${request.toString()}")

            val response = nowPaymentService.createInvoice(request)
            if (!response.isSuccessful) {
                val error = response.errorBody()?.string() ?: "Unknown error"
                Log.e(TAG, "Market payment failed: $error")
                return Result.Error(Exception(error))
            }

            response.body()?.let {
                Log.d(TAG, "Market payment invoice created: ${it.invoice_url}")
                Result.Success(it)
            } ?: run {
                Log.e(TAG, "Empty market payment response")
                Result.Error(Exception("Empty response body"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception in createMarketPayment", e)
            Result.Error(e)
        }
    }

    override suspend fun createCoursePayment(
        amount: Double,
        courseId: String
    ): Result<CreateInvoiceResponse> {
        return try {
            // Step 1: Get payment configuration for this course
            val configResponse = nowPaymentService.getCoursePaymentConfig(courseId)
            if (!configResponse.isSuccessful) {
                Log.e(TAG, "Failed to get payment config: ${configResponse.errorBody()?.string()}")
                return Result.Error(Exception("Failed to get payment configuration"))
            }

            val config = configResponse.body() ?: run {
                Log.e(TAG, "Empty payment configuration")
                return Result.Error(Exception("Empty payment configuration"))
            }

            // Step 2: Create the invoice
            val request = CreateInvoiceRequest(
                price_amount = amount,
                price_currency = config.priceCurrency,
                order_id = "course_$courseId",
                order_description = "Payment for course: $courseId",
                ipn_callback_url = config.ipnCallbackUrl ?: "https://yourdomain.com/ipn-callback",
                pay_currency = config.paymentCurrency,
                success_url = config.successUrl
                    ?: "https://yourdomain.com/success?courseId=$courseId",
                cancel_url = config.cancelUrl ?: "https://yourdomain.com/cancel?courseId=$courseId"
            )

            Log.d(TAG, "Creating invoice with: ${request.toString()}")

            val response = nowPaymentService.createInvoice(request)
            if (!response.isSuccessful) {
                val error = response.errorBody()?.string() ?: "Unknown error"
                Log.e(TAG, "Invoice creation failed: $error")
                return Result.Error(Exception(error))
            }

            response.body()?.let { invoiceResponse ->
                Log.d(TAG, "Invoice created successfully: ${invoiceResponse.invoice_url}")
                Result.Success(invoiceResponse)
            } ?: run {
                Log.e(TAG, "Empty invoice response")
                Result.Error(Exception("Empty response body"))
            }

        } catch (e: Exception) {
            Log.e(TAG, "Exception in createCoursePayment", e)
            Result.Error(e)
        }
    }

    override suspend fun createInvoice(request: CreateInvoiceRequest): Result<CreateInvoiceResponse> {
        return try {
            val response = nowPaymentService.createInvoice(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Error(Exception("Empty response body"))
            } else {
                Result.Error(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getAvailableCurrencies(): Response<CurrencyListResponse> {
        return nowPaymentService.getAvailableCurrencies()
    }


    override suspend fun checkPaymentStatus(paymentId: String): Result<PaymentStatusResponse> {
        return try {
            Log.d(TAG, "Checking payment status for: $paymentId")
            val response = nowPaymentService.checkPaymentStatus(paymentId)

            if (!response.isSuccessful) {
                val error = response.errorBody()?.string() ?: "Unknown error"
                Log.e(TAG, "Payment status check failed: $error")
                return Result.Error(Exception(error))
            }

            response.body()?.let { statusResponse ->
                Log.d(TAG, "Payment status: ${statusResponse.payment_status}")
                Result.Success(statusResponse)
            } ?: run {
                Log.e(TAG, "Empty payment status response")
                Result.Error(Exception("Empty response body"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception in checkPaymentStatus", e)
            Result.Error(e)
        }
    }
}
