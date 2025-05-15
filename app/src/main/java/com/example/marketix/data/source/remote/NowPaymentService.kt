package com.example.marketix.data.source.remote

import com.example.marketix.domain.model.CoursePaymentConfig
import com.example.marketix.domain.model.CreateInvoiceRequest
import com.example.marketix.domain.model.CreateInvoiceResponse
import com.example.marketix.domain.model.CurrencyListResponse
import com.example.marketix.domain.model.EstimateResponse
import com.example.marketix.domain.model.PaymentStatusResponse
import com.example.marketix.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface NowPaymentService {
    @POST("v1/invoice")
    suspend fun createInvoice(
        @Body request: CreateInvoiceRequest
    ): Response<CreateInvoiceResponse>

    @Headers("x-api-key: ${Constants.NOWPAYMENT_API_KEY}")
    @GET("v1/payment/{paymentId}")
    suspend fun checkPaymentStatus(
        @Path("paymentId") paymentId: String
    ): Response<PaymentStatusResponse>

    @Headers("x-api-key: ${Constants.NOWPAYMENT_API_KEY}")
    @GET("v1/currencies")
    suspend fun getAvailableCurrencies(): Response<CurrencyListResponse>

    @Headers("x-api-key: ${Constants.NOWPAYMENT_API_KEY}")
    @GET("courses/{courseId}/payment-config")
    suspend fun getCoursePaymentConfig(
        @Path("courseId") courseId: String
    ): Response<CoursePaymentConfig>

    @Headers("x-api-key: ${Constants.NOWPAYMENT_API_KEY}")
    @GET("v1/payments/user/{userId}")
    suspend fun getUserPayments(
        @Path("userId") userId: String
    ): Response<List<PaymentStatusResponse>>
}