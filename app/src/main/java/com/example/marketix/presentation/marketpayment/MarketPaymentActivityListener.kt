package com.example.marketix.presentation.marketpayment

interface MarketPaymentActivityListener {

    fun backPressActivity()
    fun playVideoActivity()
    fun openProfileActivity()
    fun openLoginActivity()
    fun displayMessageListener(message: String)

    fun openWebViewWithUrl(url: String)

}