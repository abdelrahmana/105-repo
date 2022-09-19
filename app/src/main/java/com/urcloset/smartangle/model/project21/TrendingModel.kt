package com.urcloset.smartangle.model.project21


import com.google.gson.annotations.SerializedName

data class TrendingModel(
    @SerializedName("data")
    var `data`: ArrayList<Data>? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("image")
        var image: String? = null,
        @SerializedName("isFavorited")
        var isFavorited: Boolean? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("title")
        var title: String? = null
    )
}