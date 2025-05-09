package com.example.marketix.presentation.signals

interface MarketSignalsActivityListener {

    fun loadFinishList()
    fun loadActiveList()
    fun backPressActivity()
    fun openProfileActivity()
    fun openLoginActivity()
    fun announcementActivity()
    fun historyActivity()
    fun learnTradingActivity()
    fun dashboardActivity()
    fun displayMessageListener(message: String)

}