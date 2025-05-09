package com.example.marketix.data.repository

import com.example.marketix.data.source.local.prefs.PreferencesHelper
import com.example.marketix.domain.repository.PreferencesRepository
import javax.inject.Inject

class PreferencesRepositoryImp @Inject constructor(
    private val preferencesHelper: PreferencesHelper
) : PreferencesRepository {

    override fun getAccessToken(): String {
        return preferencesHelper.getAccessToken()
    }

    override fun setAccessToken(token: String) {
        preferencesHelper.setAccessToken(token)
    }

    override fun getDeviceToken(): String {
        return preferencesHelper.getDeviceToken()
    }

    override fun setDeviceToken(token: String) {
        preferencesHelper.setDeviceToken(token)
    }

    override fun getUserProfile(): String {
        return preferencesHelper.getUserProfile()
    }

    override fun setUserProfile(userProfile: String) {
        preferencesHelper.setUserProfile(userProfile)
    }

    override fun getAppSettings(): String {
        return preferencesHelper.getAppSettings()
    }

    override fun setAppSettings(appSettings: String) {
        preferencesHelper.setAppSettings(appSettings)
    }

    override fun getCurrentUserName(): String {
        return preferencesHelper.getCurrentUserName()
    }

    override fun setCurrentUserName(name: String) {
        preferencesHelper.setCurrentUserName(name)
    }

    override fun getCurrentUserId(): String {
        return preferencesHelper.getCurrentUserId()
    }

    override fun setCurrentUserId(id: String) {
        preferencesHelper.setCurrentUserId(id)
    }

    override fun getSplashCheck(): Int {
        return preferencesHelper.getSplashCheck()
    }

    override fun setSplashCheck(check: Int) {
        preferencesHelper.setSplashCheck(check)
    }

    override fun getAppConfiguration(): String {
        return preferencesHelper.getAppConfiguration()
    }

    override fun setAppConfiguration(config: String) {
        preferencesHelper.setAppConfiguration(config)
    }

}

