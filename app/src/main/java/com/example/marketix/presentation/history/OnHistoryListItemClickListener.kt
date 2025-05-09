package com.example.marketix.presentation.history

import com.example.marketix.domain.model.HistoryItem

interface OnHistoryListItemClickListener {

    fun clickHistoryListItem(model: HistoryItem, position: Int)

}