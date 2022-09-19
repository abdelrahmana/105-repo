package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class BasicModel(
    @SerializedName("data")
    val `data`: Any?,
    @SerializedName("messages")
    val messages: String?,
    @SerializedName("status")
    val status: Boolean?
)