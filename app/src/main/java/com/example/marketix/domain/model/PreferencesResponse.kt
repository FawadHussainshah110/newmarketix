package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class PreferencesResponse(

    @field:SerializedName("Preferences")
    val preferences: List<PreferencesItem>

) : CommonResponse()

data class TypesItem(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("is_show")
    val isShow: Int
)

data class PreferencesItem(

    @field:SerializedName("types")
    val types: List<TypesItem>,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int
)
