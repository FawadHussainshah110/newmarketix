package com.example.marketix.domain.model

import com.google.gson.annotations.SerializedName

data class CoursesLessonsResponse(

    @field:SerializedName("data")
    val data: CourseLessonData
) : CommonResponse()

data class CourseLessonData(

    @field:SerializedName("pagination")
    val pagination: Pagination,

    @field:SerializedName("lesson")
    val lesson: List<LessonItem>
)

data class LessonItem(

    @field:SerializedName("course_id")
    val courseId: String,

    @field:SerializedName("cdate")
    val cdate: String,

    @field:SerializedName("file_id")
    val fileId: Any,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("link")
    val link: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("udate")
    val udate: String,

    @field:SerializedName("thumbnail")
    val thumbnail: String
)
