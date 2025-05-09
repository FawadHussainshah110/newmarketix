package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class MarketSignalsResponse(

    @field:SerializedName("data")
    val data: MarketSignalData
) : CommonResponse()

data class DataItem(

    val title: String,

    val value: String,
)

data class SignalItem(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("data")
    val data: String,

    @field:SerializedName("expirytime")
    val expirytime: String,

    @field:SerializedName("market_id")
    val marketId: String,

    @field:SerializedName("filetitle")
    val filetitle: String,

    @field:SerializedName("cdate")
    val cdate: String,

    @field:SerializedName("filename")
    val filename: String,

    @field:SerializedName("filedescription")
    val filedescription: String,

    @field:SerializedName("file_id")
    val fileId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("udate")
    val udate: String,

    @field:SerializedName("status")
    val status: String
)

data class MarketSignalData(

    @field:SerializedName("pagination")
    val pagination: Pagination,

    @field:SerializedName("signal")
    val signal: List<SignalItem>
)
