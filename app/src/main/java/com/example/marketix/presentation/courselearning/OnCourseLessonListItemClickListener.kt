package com.example.marketix.presentation.courselearning

import com.example.marketix.domain.model.LessonItem

interface OnCourseLessonListItemClickListener {

    fun clickCourseLessonListItem(model: LessonItem, position: Int)

}