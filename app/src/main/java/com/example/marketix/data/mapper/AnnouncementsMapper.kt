package com.example.marketix.data.mapper

import com.example.marketix.domain.model.AnnouncementsResponse
import com.example.marketix.domain.model.CommonResponse
import javax.inject.Inject

class AnnouncementsMapper @Inject constructor() {

    fun getAnnouncementsListMapping(response: AnnouncementsResponse): AnnouncementsResponse {
        return response
    }


}
