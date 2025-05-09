package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class CoursesListResponse(

	@field:SerializedName("data")
	val data: CourseData,

): CommonResponse()

data class CourseItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("cdate")
	val cdate: String,

	@field:SerializedName("filename")
	val filename: String,

	@field:SerializedName("purchased")
	val purchased: Boolean,

	@field:SerializedName("price")
	val price: String,

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

	@field:SerializedName("usercourseamount")
	val usercourseamount: String,

	@field:SerializedName("version")
	val version: String
)

data class CourseData(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("course")
	val course: List<CourseItem>
)
