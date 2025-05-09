package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class AnnouncementsResponse(

    @field:SerializedName("data")
    val data: AnnouncementData

) : CommonResponse()

data class AnnouncementData(

    @field:SerializedName("pagination")
    val pagination: Pagination,

    @field:SerializedName("announcement")
    val announcement: List<AnnouncementItem>
)

data class AnnouncementItem(

    @field:SerializedName("filetype")
    val filetype: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("cdate")
    val cdate: String,

    @field:SerializedName("filename")
    val filename: String,

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

    @field:SerializedName("thumbnail")
    val thumbnail: String,

    @field:SerializedName("thumbnailname")
    val thumbnailname: String,

    @field:SerializedName("thumbnail_id")
    val thumbnailid: String,
)

