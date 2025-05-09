package com.example.marketix.presentation.history

interface HistoryActivityListener {

    fun backPressActivity()
    fun openLoginActivity()
    fun announcementActivity()
    fun learnTradingActivity()
    fun startTradingActivity()
    fun dashboardclick()
    fun displayMessageListener(message: String)

}