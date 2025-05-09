package com.example.marketix.data.mapper

import com.example.marketix.domain.model.MarketPaymentLinkResponse
import com.example.marketix.domain.model.MarketSignalPurchasedResponse
import com.example.marketix.domain.model.MarketSignalsResponse
import com.example.marketix.domain.model.MarketsListResponse
import okhttp3.Response
import okhttp3.ResponseBody
import javax.inject.Inject

class MarketsMapper @Inject constructor() {

    fun getMarketsListMapping(response: MarketsListResponse): MarketsListResponse {
        return response
    }

    fun getMarketsSignalListMapping(response: MarketSignalsResponse): MarketSignalsResponse {
        return response
    }

    fun getMarketSignalPurchasedMapping(response: MarketSignalPurchasedResponse): MarketSignalPurchasedResponse {
        return response
    }


    fun getmarketSignalPaymentLinkMapping(response: MarketPaymentLinkResponse): MarketPaymentLinkResponse {
        return response
    }

}
