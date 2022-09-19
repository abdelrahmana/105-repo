package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class PrivacyModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("content")
        val content: String?,
        @SerializedName("content_ar")
        val contentAr: String?,
        @SerializedName("created_at")
        val createdAt: Any?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("key")
        val key: String?,
        @SerializedName("media_path")
        val mediaPath: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("title_ar")
        val titleAr: String?,
        @SerializedName("updated_at")
        val updatedAt: Any?,
        @SerializedName("full_path")
        val  fullPath:String
    )
}