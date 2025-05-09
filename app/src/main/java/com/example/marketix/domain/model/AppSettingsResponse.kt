package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class AppSettingsResponse(

    @field:SerializedName("data")
    val data: SettingsData
) : CommonResponse()

data class SettingItem(

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

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("udate")
    val udate: String
)

data class SettingsData(

    @field:SerializedName("pagination")
    val pagination: Pagination,

    @field:SerializedName("setting")
    val setting: List<SettingItem>
)
