package com.example.marketix.domain.repository

import com.example.marketix.domain.model.CreateInvoiceRequest
import com.example.marketix.domain.model.CreateInvoiceResponse
import com.example.marketix.domain.model.CurrencyListResponse
import com.example.marketix.domain.model.PaymentStatusResponse
import com.example.marketix.domain.model.Result
import retrofit2.Response

interface PaymentRepository {
    suspend fun createCoursePayment(amount: Double, courseId: String): Result<CreateInvoiceResponse>
    suspend fun checkPaymentStatus(paymentId: String): Result<PaymentStatusResponse>
    suspend fun createInvoice(request: CreateInvoiceRequest): Result<CreateInvoiceResponse>
    suspend fun getAvailableCurrencies(): Response<CurrencyListResponse>

    fun setCourseAccess(courseId: String, days: Int)
    fun hasCourseAccess(courseId: String): Boolean

    suspend fun createMarketPayment(amount: Double, marketId: String): Result<CreateInvoiceResponse>
    fun setMarketAccess(marketId: String, days: Int)
    fun hasMarketAccess(marketId: String): Boolean
}