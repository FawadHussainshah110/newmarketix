package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class Pagination(

    @field:SerializedName("totalpages")
    val totalpages: Int,

    @field:SerializedName("recordsperpage")
    val recordsperpage: Int,

    @field:SerializedName("totalrecords")
    val totalrecords: Int
)
