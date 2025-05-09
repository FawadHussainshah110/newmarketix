package com.example.marketix.domain.repository

import com.example.marketix.domain.model.CommonResponse
import com.example.marketix.domain.model.HistorysResponse
import io.reactivex.Single

interface HistoryRepository {

    fun historyListCall(
        deviceToken: String,
        accessToken: String,
        hashMap: HashMap<String, String>
    ): Single<HistorysResponse>


}