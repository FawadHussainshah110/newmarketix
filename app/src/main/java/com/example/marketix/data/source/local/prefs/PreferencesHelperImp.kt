package com.example.marketix.data.source.local.prefs

import android.content.SharedPreferences

class PreferencesHelperImp(private val sharedPreferences: SharedPreferences) : PreferencesHelper {

    /*
    * 0 -> default means LanguageActivity
    * 1 -> means Login Activity
    * 2 -> means Main Activity
    * */
    override fun getSplashCheck(): Int {
        return sharedPreferences.getInt(PREF_KEY_SPLASH_CHECK, 0)
    }

    override fun setSplashCheck(check: Int) {
        sharedPreferences.edit().putInt(PREF_KEY_SPLASH_CHECK, check).apply()
    }

    override fun setUserProfile(user: String) {
        sharedPreferences.edit().putString(PREF_KEY_USER_PROFILE, user).apply()
    }

    override fun getUserProfile(): String {
        return sharedPreferences.getString(PREF_KEY_USER_PROFILE, "").toString()
    }

    override fun setAppSettings(settings: String) {
        sharedPreferences.edit().putString(PREF_KEY_APP_SETTINGS, settings).apply()
    }

    override fun getAppSettings(): String {
        return sharedPreferences.getString(PREF_KEY_APP_SETTINGS, "").toString()
    }

    override fun setAppConfiguration(configuration: String) {
        sharedPreferences.edit().putString(PREF_KEY_APP_CONFIGURATION, configuration).apply()
    }

    override fun getAppConfiguration(): String {
        return sharedPreferences.getString(PREF_KEY_APP_CONFIGURATION, "").toString()
    }

    override fun getAccessToken(): String {
        return sharedPreferences.getString(PREF_KEY_ACCESS_TOKEN, "")!!
    }

    override fun setAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply()
    }

    override fun getDeviceToken(): String {
        return sharedPreferences.getString(PREF_KEY_DEVICE_TOKEN, "")!!
    }

    override fun setDeviceToken(deviceToken: String) {
        sharedPreferences.edit().putString(PREF_KEY_DEVICE_TOKEN, deviceToken).apply()
    }

    override fun getCurrentUserEmail(): String {
        return sharedPreferences.getString(PREF_KEY_CURRENT_USER_EMAIL, "")!!
    }

    override fun setCurrentUserEmail(email: String) {
        sharedPreferences.edit().putString(PREF_KEY_CURRENT_USER_EMAIL, email).apply()
    }

    override fun getCurrentUserId(): String {
        return sharedPreferences.getString(PREF_KEY_CURRENT_USER_ID, "")!!
    }

    override fun setCurrentUserId(userId: String) {
        sharedPreferences.edit().putString(PREF_KEY_CURRENT_USER_ID, userId).apply()
    }

    override fun getCurrentUserName(): String {
        return sharedPreferences.getString(PREF_KEY_CURRENT_USER_NAME, "")!!
    }

    override fun setCurrentUserName(userName: String) {
        sharedPreferences.edit().putString(PREF_KEY_CURRENT_USER_NAME, userName).apply()
    }


    private companion object key {
        const val PREF_KEY_SPLASH_CHECK = "MARKETIX_KEY_SPLASH_CHECK"
        const val PREF_KEY_USER_PROFILE = "MARKETIX_KEY_USER_PROFILE"
        const val PREF_KEY_APP_SETTINGS = "MARKETIX_KEY_APP_SETTINGS"
        const val PREF_KEY_APP_CONFIGURATION = "MARKETIX_KEY_APP_CONFIGURATION"
        const val PREF_KEY_ACCESS_TOKEN = "MARKETIX_KEY_ACCESS_TOKEN"
        const val PREF_KEY_DEVICE_TOKEN = "MARKETIX_KEY_DEVICE_TOKEN"
        const val PREF_KEY_CURRENT_USER_EMAIL = "MARKETIX_KEY_CURRENT_USER_EMAIL"
        const val PREF_KEY_CURRENT_USER_ID = "MARKETIX_KEY_CURRENT_USER_ID"
        const val PREF_KEY_CURRENT_USER_NAME = "MARKETIX_KEY_CURRENT_USER_NAME"
    }
}