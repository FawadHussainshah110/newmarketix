package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class CommonResponse(

    @field:SerializedName("message")
    val message: String = "",

    @field:SerializedName("status")
    val status: String = ""

) : Serializable
