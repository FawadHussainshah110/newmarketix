package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class HistorysResponse(

    @field:SerializedName("data")
    val data: HistoryData
) : CommonResponse()

data class HistoryItem(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String
)

data class HistoryData(

    @field:SerializedName("pagination")
    val pagination: Pagination,

    @field:SerializedName("history")
    val history: List<HistoryItem>
)
