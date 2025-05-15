package com.example.marketix.data.repository

import android.util.Log
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

    companion object {
        private const val TAG = "MarketsRepository"
    }

    override fun marketsListCall(
        deviceToken: String,
        accessToken: String,
        hashMap: HashMap<String, String>
    ): Single<MarketsListResponse> {
        Log.d(TAG, "Fetching markets list with params: $hashMap")

        return retrofitService.loadMarkets(deviceToken, accessToken)
            .doOnSuccess { response ->
                Log.d(TAG, "Raw API response: ${response.toString()}")
                Log.d(TAG, "Response status: ${response.status}")
                Log.d(TAG, "Response message: ${response.message}")

                response.data?.market?.forEach { market ->
                    Log.d(TAG, "Market item - ID: ${market.id}, Price: ${market.price}, " +
                            "Name: ${market.name}")
                }
            }
            .map { response ->
                val mappedResponse = mapper.get().getMarketsListMapping(response)
                Log.d(TAG, "After mapping - Market count: ${mappedResponse.data.market.size}")

                mappedResponse.data.market.forEach { market ->
                    Log.d(TAG, "Mapped Market - ID: ${market.id}, Price: ${market.price}")
                }

                mappedResponse
            }
            .doOnError { error ->
                Log.e(TAG, "Error fetching markets list", error)
            }
    }

    override fun marketSignalListCall(
        deviceToken: String,
        accessToken: String,
        courseId: String,
        type: String
    ): Single<MarketSignalsResponse> {
        Log.d(TAG, "Fetching market signals for course: $courseId, type: $type")

        return retrofitService.loadMarketSignal(deviceToken, accessToken, courseId.toInt(), type)
            .doOnSuccess { response ->
                Log.d(TAG, "Raw market signals response: ${response.toString()}")
            }
            .map {
                val mappedResponse = mapper.get().getMarketsSignalListMapping(it)
                Log.d(TAG, "Mapped market signals count: ${mappedResponse.data.signal.size}")
                mappedResponse
            }
            .doOnError { error ->
                Log.e(TAG, "Error fetching market signals", error)
            }
    }

    override fun isMarketSignalPurchased(
        deviceToken: String,
        accessToken: String
    ): Single<MarketSignalPurchasedResponse> {
        Log.d(TAG, "Checking if market signal is purchased")

        return retrofitService.isMarketSignalPurchased(deviceToken, accessToken)
            .doOnSuccess { response ->
                Log.d(TAG, "Purchase check response: purchased=${response.data.purchased}")
            }
            .map {
                mapper.get().getMarketSignalPurchasedMapping(it)
            }
            .doOnError { error ->
                Log.e(TAG, "Error checking purchase status", error)
            }
    }

    override fun marketSignalPaymentLink(
        deviceToken: String,
        accessToken: String
    ): Single<MarketPaymentLinkResponse> {
        Log.d(TAG, "Fetching market signal payment link")

        return retrofitService.marketSignalPaymentLink(deviceToken, accessToken)
            .doOnSuccess { response ->
                Log.d(TAG, "Payment link response: ${response.data.link}")
            }
            .map {
                mapper.get().getmarketSignalPaymentLinkMapping(it)
            }
            .doOnError { error ->
                Log.e(TAG, "Error fetching payment link", error)
            }
    }
}