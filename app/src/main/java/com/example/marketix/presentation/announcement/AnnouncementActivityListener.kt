package com.example.marketix.presentation.announcement

interface AnnouncementActivityListener {

    fun backPressActivity()
    fun openLoginActivity()
    fun historyActivity()
    fun learnTradingActivity()
    fun startTradingActivity()
    fun dashboardActivity()
    fun displayMessageListener(message: String)

}