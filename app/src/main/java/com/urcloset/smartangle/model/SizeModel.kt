package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class SizeModel(
    @SerializedName("data")
    val `data`: List<Size?>?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Size(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("is_selected")
        var isSelected: Int?,
        @SerializedName("name_ar")
        val nameAr: String?,
        @SerializedName("name_en")
        val nameEn: String?
    )
}