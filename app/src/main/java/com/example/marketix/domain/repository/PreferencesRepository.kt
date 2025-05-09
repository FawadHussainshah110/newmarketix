package com.example.marketix.domain.repository

interface PreferencesRepository {

    fun getAccessToken(): String
    fun setAccessToken(token: String)

    fun getDeviceToken(): String
    fun setDeviceToken(token: String)

    fun getUserProfile(): String
    fun setUserProfile(userProfile: String)

    fun getAppSettings(): String
    fun setAppSettings(userProfile: String)

    fun getCurrentUserName(): String
    fun setCurrentUserName(name: String)

    fun getCurrentUserId(): String
    fun setCurrentUserId(id: String)

    fun getSplashCheck(): Int
    fun setSplashCheck(check: Int)

    fun getAppConfiguration(): String
    fun setAppConfiguration(config: String)

}