package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class CreateInvoiceRequest(
    @SerializedName("price_amount") val price_amount: Double,
    @SerializedName("price_currency") val price_currency: String,
    @SerializedName("order_id") val order_id: String,
    @SerializedName("order_description") val order_description: String,
    @SerializedName("ipn_callback_url") val ipn_callback_url: String,
    @SerializedName("pay_currency") val pay_currency: String? = null,
    @SerializedName("success_url") val success_url: String,
    @SerializedName("cancel_url") val cancel_url: String,
    @SerializedName("pay_address") val pay_address: String? = null
)

data class CreateInvoiceResponse(
    @SerializedName("id") val id: String,
    @SerializedName("invoice_url") val invoice_url: String,
    @SerializedName("token_id") val token_id: String,
    @SerializedName("order_id") val order_id: String?,
    @SerializedName("order_description") val order_description: String?,
    @SerializedName("price_amount") val price_amount: String,
    @SerializedName("price_currency") val price_currency: String,
    @SerializedName("pay_currency") val pay_currency: String?,
    @SerializedName("ipn_callback_url") val ipn_callback_url: String?,
    @SerializedName("success_url") val success_url: String?,
    @SerializedName("cancel_url") val cancel_url: String?,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("is_fixed_rate") val is_fixed_rate: Boolean,
    @SerializedName("is_fee_paid_by_user") val is_fee_paid_by_user: Boolean
)

data class PaymentStatusResponse(
    @SerializedName("payment_id") val payment_id: String,
    @SerializedName("payment_status") val payment_status: String,
    @SerializedName("pay_address") val pay_address: String,
    @SerializedName("price_amount") val price_amount: Double,
    @SerializedName("price_currency") val price_currency: String,
    @SerializedName("pay_amount") val pay_amount: Double,
    @SerializedName("pay_currency") val pay_currency: String,
    @SerializedName("order_id") val order_id: String?,
    @SerializedName("order_description") val order_description: String?,
    @SerializedName("ipn_callback_url") val ipn_callback_url: String?,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("purchase_id") val purchase_id: String
)

data class CurrencyListResponse(
    @SerializedName("currencies") val currencies: List<String>
)

data class CoursePaymentConfig(
    val price: Double,
    val priceCurrency: String = "USD",
    val paymentCurrency: String,
    val paymentAddress: String,
    val ipnCallbackUrl: String? = null,
    val successUrl: String? = null,
    val cancelUrl: String? = null
)

data class EstimateResponse(
    @SerializedName("estimated_amount") val estimatedAmount: Double
)