package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class SignupResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)
//
//data class Data(
//	val any: Any? = null
//)
