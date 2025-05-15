package com.example.marketix.presentation.start_trading

import com.example.marketix.domain.model.MarketItem

interface StartTradingActivityListener {

    fun backPressActivity()
    fun openLoginActivity()
    fun openProfileActivity()
    fun announcementActivity()
    fun historyActivity()
    fun learnTradingActivity()
    fun dashboardActivity()
    fun displayMessageListener(message: String)

    fun openMarketSignals(market: MarketItem)
    fun openPaymentWebPage(url: String, marketId: String, price: String)

}