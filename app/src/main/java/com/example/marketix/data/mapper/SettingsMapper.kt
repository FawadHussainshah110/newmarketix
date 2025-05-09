package com.example.marketix.data.mapper

import com.example.marketix.domain.model.AppSettingsResponse
import javax.inject.Inject

class SettingsMapper @Inject constructor() {

    fun getSettingsMapping(response: AppSettingsResponse): AppSettingsResponse {
        return response
    }


}
