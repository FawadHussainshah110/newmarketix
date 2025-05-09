package com.example.marketix.util

import com.example.marketix.BuildConfig

/**
 * Contains fixed values that remains during execution once it is initialized
 */
object Constants {

    fun configCheck(): String {
        val baseUrl: String = if (BuildConfig.DEBUG)
            "https://test.marketix777.com/"
        else
            "https://marketix.kharnaat.com/"
        return baseUrl
    }

    object Constants {
        const val NOW_PAYMENTS_API_KEY = "TQJ0SSN-N1RMNRZ-KV5F247-Y4GRF0H"
    }

    const val REACHABILITY_SERVER = "https://www.google.com"

    const val DEVICE_TYPE = "android"
    const val DEVICE_TOKEN = "Device-Token"
    const val ACCESS_AUTHORIZATION_TOKEN = "Authorization"
    const val WELCOME_SCREEN_CONTENT = "welcome_screen_content"
    const val ABOUT_US_SCREEN_CONTENT = "about_us_screen_content"
    const val ALL_MARKET_MONTHLY_FEE_SCREEN_CONTENT = "all_market_monthly_fee_screen_content"
    const val ALL_MARKET_MONTHLY_FEE_DETAIL_SCREEN_CONTENT =
        "all_market_monthly_fee_detail_screen_content"
    const val NO_SIGNAL_CONTENT = "no_signal_content_"

    const val STATUS_ERROR = "ERROR"
    const val STATUS_OK = "OK"

    const val PARAM_EMAIL = "email"
    const val PARAM_PASSWORD = "password"
    const val PARAM_NAME = "name"
    const val PARAM_PHONE = "phone"
    const val PARAM_CONFIRM_PASSWORD = "confirmpassword"
    const val PARAM_DEVICE_TOKEN = "devicetoken"
    const val PARAM_ADDRESS = "address"
    const val PARAM_TOKEN = "token"
    const val PARAM_OLD_PASSWORD = "oldpassword"
    const val PARAM_NEW_PASSWORD = "newpassword"
    const val PARAM_CONFIRM_NEW_PASSWORD = "confirmnewpassword"
    const val PARAM_COURSE_ID = "courseid"
    const val PARAM_IMAGE = "image"


    const val LANGUAGE_ENGLISH = "en"
    const val LANGUAGE_ARABIC = "ur"

    const val DEFAULT_TAG = "tag_exception"
    const val PROXIMITY_RADIUS = "1000"

    const val COVER_IMAGE = 0
    const val PROFILE_IMAGE = 1

    const val COURSES_PAYMENT = 2000
    const val MARKETS_PAYMENT = 2001
    const val EDIT_PROFILE_DATA = 2002
    const val MARKETS_ACTIVE = "active"
    const val MARKETS_FINISHED = "finished"

    const val PERMISSION_CHECK = 1003
    const val STORAGE_PERMISSION_CHECK = 3004


    const val UNAUTHENTICATED_USER = 7002
    const val UNAUTHENTICATED_ACCESS = "Unauthorized Access"

    const val CASH_ON_DELIVERY = "Cash on delivery"
    const val CREDIT = "Credit"
    const val GOOGLE_MAPS = "googleMaps"
    const val SHARE_ADDRESS = "shareAddress"

    const val isFromHomeFragment = "isFromHomeFragment"
    const val FROM_PROFILE_CHANGE = "from_profile_change"
    const val FROM_NOTIFICATION = "from_notification"

    const val ALERT_NOTIFICATION_SCREEN = 1000

    const val VIEW_TYPE_ITEM = 0
    const val VIEW_TYPE_LOADING = 1


    const val ERROR_410 = 410


}