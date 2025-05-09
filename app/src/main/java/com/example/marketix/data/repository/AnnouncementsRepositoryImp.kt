package com.example.marketix.data.repository

import com.example.marketix.data.mapper.AnnouncementsMapper
import com.example.marketix.data.source.remote.RetrofitService
import com.example.marketix.domain.model.AnnouncementsResponse
import com.example.marketix.domain.repository.AnnouncementsRepository
import io.reactivex.Single

class AnnouncementsRepositoryImp(
    private val retrofitService: RetrofitService, val mapper: dagger.Lazy<AnnouncementsMapper>
) : AnnouncementsRepository {

    override fun announcementsListCall(
        deviceToken: String,
        accessToken: String,
        hashMap: HashMap<String, String>
    ): Single<AnnouncementsResponse> {
//        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
//        builder.addFormDataPart("email", hashMap.get("email").toString())
//        builder.addFormDataPart("password", hashMap.get("password").toString())
//        val requestBody: RequestBody = builder.build()

        return retrofitService.loadAnnouncements(deviceToken, accessToken).map {
            mapper.get().getAnnouncementsListMapping(it)
        }
    }


}