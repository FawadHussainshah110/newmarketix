package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class CoursePaymentLinkResponse(

    @field:SerializedName("data")
    val data: CoursePaymentLinkData
) : CommonResponse()

data class CoursePaymentLinkData(

    @field:SerializedName("link")
    val link: String
)
