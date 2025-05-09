package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class MarketSignalPurchasedResponse(

    @field:SerializedName("data")
    val data: MarketSignalPurchasedData
) : CommonResponse()

data class MarketSignalPurchasedData(

    @field:SerializedName("purchased")
    val purchased: Boolean
)
