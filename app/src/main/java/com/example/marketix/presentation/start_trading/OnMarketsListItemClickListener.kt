package com.example.marketix.presentation.start_trading

import com.example.marketix.domain.model.MarketItem

interface OnMarketsListItemClickListener {

    fun clickMarketsListItem(model: MarketItem, position: Int)
    fun displayMessageListener(message: String)

}