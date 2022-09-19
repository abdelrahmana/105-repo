package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class RateModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("messages")
    val messages: String?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("new_rate")
        val newRate: Double?
    )
}