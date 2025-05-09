package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class MarketPaymentLinkResponse(

    @field:SerializedName("data")
    val data: MarketPaymentLinkData
) : CommonResponse()

data class MarketPaymentLinkData(

    @field:SerializedName("link")
    val link: String
)
