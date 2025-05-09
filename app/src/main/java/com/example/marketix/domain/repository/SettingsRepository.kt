package com.example.marketix.domain.repository

import com.example.marketix.domain.model.AppSettingsResponse
import io.reactivex.Single

interface SettingsRepository {

    fun loadAllSettings(
        deviceToken: String,
        accessToken: String
    ): Single<AppSettingsResponse>


}