package com.example.marketix.data.mapper

import com.example.marketix.domain.model.CoursePaymentLinkResponse
import com.example.marketix.domain.model.CoursesLessonsResponse
import com.example.marketix.domain.model.CoursesListResponse
import javax.inject.Inject

class CoursesMapper @Inject constructor() {

    fun getCoursesListMapping(response: CoursesListResponse): CoursesListResponse {
        return response
    }

    fun getCoursesLearningListMapping(response: CoursesLessonsResponse): CoursesLessonsResponse {
        return response
    }

    fun getCoursePaymentLinkMapping(response: CoursePaymentLinkResponse): CoursePaymentLinkResponse {
        return response
    }

}
