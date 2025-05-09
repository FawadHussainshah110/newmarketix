package com.example.marketix.data.repository

import com.example.marketix.data.mapper.SettingsMapper
import com.example.marketix.data.source.remote.RetrofitService
import com.example.marketix.domain.model.AppSettingsResponse
import com.example.marketix.domain.repository.SettingsRepository
import io.reactivex.Single

class SettingsRepositoryImp(
    private val retrofitService: RetrofitService, val mapper: dagger.Lazy<SettingsMapper>
) : SettingsRepository {

    override fun loadAllSettings(
        params: String,
        deviceToken: String
    ): Single<AppSettingsResponse> {
        return retrofitService.loadSettings(params, deviceToken).map {
            mapper.get().getSettingsMapping(it)
        }
    }


}