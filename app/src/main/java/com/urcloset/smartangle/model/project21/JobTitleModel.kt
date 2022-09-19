package com.urcloset.smartangle.model.project21


import com.google.gson.annotations.SerializedName

data class JobTitleModel(
    @SerializedName("data")
    var `data`: ArrayList<Data>? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Boolean
) {
    data class Data(
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("image")
        var image: String? = null,
        @SerializedName("name")
        var name: String? = null
    )
}