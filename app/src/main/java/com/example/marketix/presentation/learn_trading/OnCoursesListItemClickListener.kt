package com.example.marketix.presentation.learn_trading

import com.example.marketix.domain.model.CourseItem

interface OnCoursesListItemClickListener {

    fun clickCoursesListItem(model: CourseItem, position: Int)

}