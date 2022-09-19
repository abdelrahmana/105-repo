package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("data")
    val `categories`: List<Category?>?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Category(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("media_path")
        val mediaPath: String?,
        @SerializedName("name_ar")
        val nameAr: String?,
        @SerializedName("name_en")
        val nameEn: String?,
        @SerializedName("number")
        val number: Int?
    )
}