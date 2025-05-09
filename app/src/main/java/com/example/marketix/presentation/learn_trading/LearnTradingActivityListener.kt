package com.example.marketix.presentation.learn_trading

interface LearnTradingActivityListener {

    fun backPressActivity()
    fun openLoginActivity()
    fun openProfileActivity()
    fun announcementActivity()
    fun historyActivity()
    fun startTradingActivity()
    fun opendashboardActivity()
    fun displayMessageListener(message: String)

}