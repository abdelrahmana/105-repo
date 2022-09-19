package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class ColorModel(
    @SerializedName("data")
    val `data`: List<Color?>?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Color(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("is_selected")
        var isSelected: Int?,
        @SerializedName("value")
        val value: String?
    )
}