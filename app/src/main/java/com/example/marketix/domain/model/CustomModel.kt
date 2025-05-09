package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class CustomModel(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("image")
    val image: String = "",

    @field:SerializedName("deviceId")
    var deviceId: String = ""

)