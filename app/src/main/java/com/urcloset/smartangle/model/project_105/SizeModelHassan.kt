package com.urcloset.smartangle.model.project_105


import com.google.gson.annotations.SerializedName

data class SizeModelHassan(
    @SerializedName("data")
    val `data`: ArrayList<Size>?=null,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Size(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("isselected")
        var isSelected: Boolean?=false,
        @SerializedName("name_ar")
        val nameAr: String?,
        @SerializedName("name_en")
        val nameEn: String?
    )
}