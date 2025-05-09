package com.example.marketix.data.mapper

import com.example.marketix.domain.model.HistorysResponse
import javax.inject.Inject

class HistoryMapper @Inject constructor() {

    fun getHistoryListMapping(response: HistorysResponse): HistorysResponse {
        return response
    }


}
