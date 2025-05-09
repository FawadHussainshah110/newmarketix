package com.example.marketix.presentation.start_trading

interface StartTradingActivityListener {

    fun backPressActivity()
    fun openLoginActivity()
    fun openProfileActivity()
    fun announcementActivity()
    fun historyActivity()
    fun learnTradingActivity()
    fun dashboardActivity()
    fun displayMessageListener(message: String)

}