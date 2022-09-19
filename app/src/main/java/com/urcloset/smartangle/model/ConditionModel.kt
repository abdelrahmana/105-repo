package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class ConditionModel(
    @SerializedName("data")
    val `data`: ArrayList<Condition>?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Condition(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("is_selected")
        val isSelected: Int?,
        @SerializedName("name_ar")
        val nameAr: String?,
        @SerializedName("name_en")
        val nameEn: String?
    )
}