package com.example.marketix.data.source.remote

import com.example.marketix.domain.model.AnnouncementsResponse
import com.example.marketix.domain.model.AppSettingsResponse
import com.example.marketix.domain.model.CommonResponse
import com.example.marketix.domain.model.CoursePaymentLinkResponse
import com.example.marketix.domain.model.CoursesLessonsResponse
import com.example.marketix.domain.model.CoursesListResponse
import com.example.marketix.domain.model.HistorysResponse
import com.example.marketix.domain.model.MarketPaymentLinkResponse
import com.example.marketix.domain.model.MarketSignalPurchasedResponse
import com.example.marketix.domain.model.MarketSignalsResponse
import com.example.marketix.domain.model.MarketsListResponse
import com.example.marketix.domain.model.SigninUserResponse
import com.example.marketix.domain.model.VerifyForgetPasswordResponse
import com.example.marketix.util.Constants
import dagger.Provides
import io.reactivex.Single
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {

    @Headers("Accept: application/json")
    @POST("Profile/signinrequest")
    fun signinUser(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Body body: RequestBody
    ): Single<SigninUserResponse>

    @Headers("Accept: application/json")
    @POST("Users/submit")
    fun signupUser(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Body body: RequestBody
    ): Single<CommonResponse>

    @Headers("Accept: application/json")
    @POST("Profile/sendrecoveryemail")
    fun sendRecoveryEmail(
        @Body body: RequestBody
    ): Single<CommonResponse>

    @Headers("Accept: application/json")
    @POST("Profile/verifyforgetpassword")
    fun sendVerifyForgetPassword(
        @Body body: RequestBody
    ): Single<VerifyForgetPasswordResponse>

    @Headers("Accept: application/json")
    @POST("Profile/resetpassword")
    fun sendResetPassword(
        @Body body: RequestBody
    ): Single<CommonResponse>

    @Headers("Accept: application/json")
    @POST("Profile/sendverificationemail")
    fun sendVerificationEmail(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String
    ): Single<CommonResponse>

    @Headers("Accept: application/json")
    @POST("Profile/verifyemail")
    fun sendVerifyEmailOTP(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String,
        @Body body: RequestBody
    ): Single<CommonResponse>

    @Headers("Accept: application/json")
    @POST("Profile/changepassword")
    fun updatePassword(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String,
        @Body body: RequestBody
    ): Single<CommonResponse>

    @Headers("Accept: application/json")
    @POST("Profile/edit")
    fun editProfile(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String,
        @Body body: RequestBody
    ): Single<CommonResponse>

    @Headers("Accept: application/json")
    @POST("Profile")
    fun loadUserProfile(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String
    ): Single<CommonResponse>

    @Headers("Accept: application/json")
    @POST("Announcements")
    fun loadAnnouncements(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String
    ): Single<AnnouncementsResponse>

    @Headers("Accept: application/json")
    @POST("Settings")
    fun loadSettings(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String
    ): Single<AppSettingsResponse>

    @Headers("Accept: application/json")
    @POST("History")
    fun loadHistory(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String
    ): Single<HistorysResponse>

    @Headers("Accept: application/json")
    @POST("Courses")
    fun loadCourses(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String
    ): Single<CoursesListResponse>

    @Headers("Accept: application/json")
    @POST("Lessons/index/{courseId}")
    fun loadCourseLessons(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String,
        @Path("courseId") customerId: Int
    ): Single<CoursesLessonsResponse>

    @Headers("Accept: application/json")
    @POST("Courses/generatepaymentlink")
    fun coursePaymentLink(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String,
        @Query("course_id") courseId: String
    ): Single<CoursePaymentLinkResponse>

    @Headers("Accept: application/json")
    @POST("Markets")
    fun loadMarkets(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String
    ): Single<MarketsListResponse>

    @Headers("Accept: application/json")
    @POST("Signals/index/{marketId}")
    fun loadMarketSignal(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String,
        @Path("marketId") customerId: Int,
        @Query("type") type: String
    ): Single<MarketSignalsResponse>

    @Headers("Accept: application/json")
    @POST("Markets/ispurchased")
    fun isMarketSignalPurchased(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String
    ): Single<MarketSignalPurchasedResponse>

    @Headers("Accept: application/json")
    @POST("Markets/generatepaymentlink")
    fun marketSignalPaymentLink(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String
    ): Single<MarketPaymentLinkResponse>

    @Headers("Accept: application/json")
    @POST("Profile/logout")
    fun logoutUser(
        @Header(Constants.DEVICE_TOKEN) deviceToken: String,
        @Header(Constants.ACCESS_AUTHORIZATION_TOKEN) accessToken: String
    ): Single<CommonResponse>


}