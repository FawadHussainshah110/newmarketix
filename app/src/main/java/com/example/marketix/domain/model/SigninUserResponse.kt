package com.example.marketix.domain.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class SigninUserResponse(

    @field:SerializedName("data")
    val data: DataOrEmptyArray

) : CommonResponse()

open class DataOrEmptyArray {
    data class Data(
        @field:SerializedName("user")
        val user: User
    ) : DataOrEmptyArray()

    object EmptyArray : DataOrEmptyArray()
}

//data class Data(
//    @field:SerializedName("user")
//    val user: User
//) : DataOrEmptyArray()

class DataOrEmptyArrayDeserializer : JsonDeserializer<DataOrEmptyArray> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): DataOrEmptyArray {
        return if (json.isJsonArray && json.asJsonArray.size() == 0) {
            DataOrEmptyArray.EmptyArray
        } else {
            context.deserialize(json, DataOrEmptyArray.Data::class.java)
        }
    }
}

data class User(

    @field:SerializedName("image")
    var image: String,

    @field:SerializedName("address")
    var address: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("devicetoken")
    val devicetoken: String,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("cdate")
    val cdate: String,

    @field:SerializedName("filename")
    val filename: String = "",

    @field:SerializedName("phone")
    var phone: String,

    @field:SerializedName("file_id")
    val fileId: String = "",

    @field:SerializedName("name")
    var name: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("udate")
    val udate: String,

    @field:SerializedName("email")
    var email: String,

    @field:SerializedName("emailverified")
    val emailverified: Boolean,

    @field:SerializedName("status")
    val status: String
)
