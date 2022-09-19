package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class SentenceModel(
    @SerializedName("data")
    val `data`: List<Sentence?>?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Sentence(
        @SerializedName("arabic")
        val arabic: String?,
        @SerializedName("english")
        val english: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("key")
        val key: String?
    )
}