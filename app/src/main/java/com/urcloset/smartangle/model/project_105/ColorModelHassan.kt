package com.urcloset.smartangle.model.project_105


import com.google.gson.annotations.SerializedName

data class ColorModelHassan(
    @SerializedName("data")
    val `data`: ArrayList<Color>?=null,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Color(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("isselected")
        var isSelected: Boolean?=false,
        @SerializedName("value")
        val value: String?
    )
}