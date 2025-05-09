package com.example.marketix.presentation.announcement

import com.example.marketix.domain.model.AnnouncementItem

interface OnAnnouncementListItemClickListener {

    fun clickAnnouncementListItem(model: AnnouncementItem, position: Int)

}