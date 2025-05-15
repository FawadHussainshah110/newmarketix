package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class MarketsListResponse(

    @field:SerializedName("data")
    val data: MarketsData
) : CommonResponse()

data class MarketItem(
    @field:SerializedName("defaultdata")
    val defaultdata: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("cdate")
    val cdate: String,

    @field:SerializedName("filename")
    val filename: Any,

    @field:SerializedName("purchased")
    val purchased: Boolean,

    @field:SerializedName("file_id")
    val fileId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("udate")
    val udate: String,

    @SerializedName("price")
    private val _price: Double? = null,

    @SerializedName("price_amount")
    private val priceAmount: Double? = null
) {
    val price: Double
        get() = _price ?: priceAmount ?: 0.0
}

data class MarketsData(

    @field:SerializedName("market")
    val market: List<MarketItem>,

    @field:SerializedName("pagination")
    val pagination: Pagination


)
