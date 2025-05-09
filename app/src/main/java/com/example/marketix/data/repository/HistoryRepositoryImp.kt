package com.example.marketix.data.repository

import com.example.marketix.data.mapper.HistoryMapper
import com.example.marketix.data.source.remote.RetrofitService
import com.example.marketix.domain.model.HistorysResponse
import com.example.marketix.domain.repository.HistoryRepository
import io.reactivex.Single

class HistoryRepositoryImp(
    private val retrofitService: RetrofitService, val mapper: dagger.Lazy<HistoryMapper>
) : HistoryRepository {

    override fun historyListCall(
        deviceToken: String,
        accessToken: String,
        hashMap: HashMap<String, String>
    ): Single<HistorysResponse> {
//        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
//        builder.addFormDataPart("email", hashMap.get("email").toString())
//        builder.addFormDataPart("password", hashMap.get("password").toString())
//        val requestBody: RequestBody = builder.build()

        return retrofitService.loadHistory(deviceToken, accessToken).map {
            mapper.get().getHistoryListMapping(it)
        }
    }


}