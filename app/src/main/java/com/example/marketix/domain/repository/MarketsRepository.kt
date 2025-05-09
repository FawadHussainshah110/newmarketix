package com.example.marketix.domain.repository

import com.example.marketix.domain.model.MarketPaymentLinkResponse
import com.example.marketix.domain.model.MarketSignalPurchasedResponse
import com.example.marketix.domain.model.MarketSignalsResponse
import com.example.marketix.domain.model.MarketsListResponse
import io.reactivex.Single
import okhttp3.Response
import okhttp3.ResponseBody

interface MarketsRepository {

    fun marketsListCall(
        params: String,
        deviceToken: String,
        hashMap: HashMap<String, String>
    ): Single<MarketsListResponse>

    fun marketSignalListCall(
        params: String,
        deviceToken: String,
        courseId: String,
        type: String
    ): Single<MarketSignalsResponse>

    fun isMarketSignalPurchased(
        deviceToken: String,
        accessToken: String
    ): Single<MarketSignalPurchasedResponse>

    fun marketSignalPaymentLink(
        deviceToken: String,
        accessToken: String
    ): Single<MarketPaymentLinkResponse>

}