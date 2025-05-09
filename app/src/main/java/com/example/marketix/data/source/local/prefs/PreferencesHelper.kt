package com.example.marketix.data.source.local.prefs

interface PreferencesHelper {

    fun getSplashCheck(): Int
    fun setSplashCheck(check: Int)

    fun setUserProfile(user: String)
    fun getUserProfile(): String
    fun setAppSettings(settings: String)
    fun getAppSettings(): String
    fun setAppConfiguration(configuration: String)
    fun getAppConfiguration(): String

    fun getDeviceToken(): String
    fun setDeviceToken(deviceToken: String)

    fun getAccessToken(): String
    fun setAccessToken(accessToken: String)
    fun getCurrentUserEmail(): String
    fun setCurrentUserEmail(email: String)
    fun getCurrentUserId(): String
    fun setCurrentUserId(userId: String)
    fun getCurrentUserName(): String
    fun setCurrentUserName(userName: String)

    companion object {
        const val PREFERENCEs_NAME = "_saeepreferences"

    }
}