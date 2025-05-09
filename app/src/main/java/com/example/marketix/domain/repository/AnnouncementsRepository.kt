package com.example.marketix.domain.repository

import com.example.marketix.domain.model.AnnouncementsResponse
import io.reactivex.Single

interface AnnouncementsRepository {

    fun announcementsListCall(
        deviceToken: String,
        accessToken: String,
        hashMap: HashMap<String, String>
    ): Single<AnnouncementsResponse>

}