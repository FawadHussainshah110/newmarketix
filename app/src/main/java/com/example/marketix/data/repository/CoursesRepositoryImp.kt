package com.example.marketix.data.repository

import com.example.marketix.data.mapper.CoursesMapper
import com.example.marketix.data.source.remote.RetrofitService
import com.example.marketix.domain.model.CoursePaymentLinkResponse
import com.example.marketix.domain.model.CoursesLessonsResponse
import com.example.marketix.domain.model.CoursesListResponse
import com.example.marketix.domain.repository.CoursesRepository
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CoursesRepositoryImp(
    private val retrofitService: RetrofitService, val mapper: dagger.Lazy<CoursesMapper>
) : CoursesRepository {

    override fun coursesListCall(
        params: String,
        deviceToken: String,
        hashMap: HashMap<String, String>
    ): Single<CoursesListResponse> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("email", hashMap.get("email").toString())
        builder.addFormDataPart("password", hashMap.get("password").toString())

        val requestBody: RequestBody = builder.build()

        return retrofitService.loadCourses(params, deviceToken).map {
            mapper.get().getCoursesListMapping(it)
        }
    }

    override fun courseLearningListCall(
        deviceToken: String,
        accessToken: String,
        courseId: String,
        hashMap: HashMap<String, String>
    ): Single<CoursesLessonsResponse> {

        return retrofitService.loadCourseLessons(deviceToken, accessToken, courseId.toInt()).map {
            mapper.get().getCoursesLearningListMapping(it)
        }
    }

    override fun coursePaymentLink(
        deviceToken: String,
        accessToken: String,
        courseId: String
    ): Single<CoursePaymentLinkResponse> {

        return retrofitService.coursePaymentLink(deviceToken, accessToken, courseId).map {
            mapper.get().getCoursePaymentLinkMapping(it)
        }
    }


}