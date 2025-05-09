package com.example.marketix.data.repository

import com.example.marketix.data.mapper.MarketsMapper
import com.example.marketix.data.source.remote.RetrofitService
import com.example.marketix.domain.model.MarketPaymentLinkResponse
import com.example.marketix.domain.model.MarketSignalPurchasedResponse
import com.example.marketix.domain.model.MarketSignalsResponse
import com.example.marketix.domain.model.MarketsListResponse
import com.example.marketix.domain.repository.MarketsRepository
import io.reactivex.Single
import okhttp3.ResponseBody
import javax.inject.Inject

class MarketsRepositoryImp @Inject constructor(
    private val retrofitService: RetrofitService,
    private val mapper: dagger.Lazy<MarketsMapper>,
) : MarketsRepository {

    // Existing methods for market list and signals...

    override fun marketsListCall(
        deviceToken: String, accessToken: String, hashMap: HashMap<String, String>
    ): Single<MarketsListResponse> {
        return retrofitService.loadMarkets(deviceToken, accessToken).map {
            mapper.get().getMarketsListMapping(it)
        }
    }

    override fun marketSignalListCall(
        deviceToken: String, accessToken: String, courseId: String, type: String
    ): Single<MarketSignalsResponse> {
        return retrofitService.loadMarketSignal(deviceToken, accessToken, courseId.toInt(), type)
            .map {
                mapper.get().getMarketsSignalListMapping(it)
            }
    }

    override fun isMarketSignalPurchased(
        deviceToken: String, accessToken: String
    ): Single<MarketSignalPurchasedResponse> {
        return retrofitService.isMarketSignalPurchased(deviceToken, accessToken)
            .map {
                mapper.get().getMarketSignalPurchasedMapping(it)
            }
    }

    override fun marketSignalPaymentLink(
        deviceToken: String,
        accessToken: String
    ): Single<MarketPaymentLinkResponse> {
        return retrofitService.marketSignalPaymentLink(deviceToken, accessToken)
            .map {
                mapper.get().getmarketSignalPaymentLinkMapping(it)
            }
    }


}
