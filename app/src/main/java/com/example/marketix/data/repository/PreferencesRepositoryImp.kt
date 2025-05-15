package com.example.marketix.data.repository

import com.example.marketix.data.source.local.prefs.PreferencesHelper
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.util.Constants.MARKET_ACCESS_PREFIX
import javax.inject.Inject

class PreferencesRepositoryImp @Inject constructor(
    private val preferencesHelper: PreferencesHelper
) : PreferencesRepository {

    companion object {
        private const val COURSE_ACCESS_PREFIX = "course_access_"
    }

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

    override fun setCourseAccess(courseId: String, expiryTime: String) {
        preferencesHelper.setString("${COURSE_ACCESS_PREFIX}$courseId", expiryTime)
    }

    override fun getCourseAccess(courseId: String): String {
        return preferencesHelper.getString("${COURSE_ACCESS_PREFIX}$courseId", "")
    }

    override fun setMarketAccess(marketId: String, expiryTime: String) {
        preferencesHelper.setString("${MARKET_ACCESS_PREFIX}$marketId", expiryTime)
    }

    override fun getMarketAccess(marketId: String): String {
        return preferencesHelper.getString("${MARKET_ACCESS_PREFIX}$marketId", "")
    }
}