package com.example.marketix.domain.repository

import com.example.marketix.domain.model.CoursePaymentLinkResponse
import com.example.marketix.domain.model.CoursesLessonsResponse
import com.example.marketix.domain.model.CoursesListResponse
import io.reactivex.Single

interface CoursesRepository {

    fun coursesListCall(
        params: String, deviceToken: String, hashMap: HashMap<String, String>
    ): Single<CoursesListResponse>

    fun courseLearningListCall(
        params: String, deviceToken: String, courseId: String, hashMap: HashMap<String, String>
    ): Single<CoursesLessonsResponse>

    fun coursePaymentLink(
        params: String, deviceToken: String, courseId: String
    ): Single<CoursePaymentLinkResponse>


}