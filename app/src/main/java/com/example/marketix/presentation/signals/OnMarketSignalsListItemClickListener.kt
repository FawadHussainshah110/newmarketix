package com.example.marketix.presentation.signals

import com.example.marketix.domain.model.SignalItem

interface OnMarketSignalsListItemClickListener {

    fun clickMarketSignalsListItem(model: SignalItem, position: Int)

}