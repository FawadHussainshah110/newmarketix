package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class VerifyForgetPasswordResponse(

    @field:SerializedName("data")
    val data: Data

) : CommonResponse()

data class Data(

    @field:SerializedName("token")
    val token: Int
)
