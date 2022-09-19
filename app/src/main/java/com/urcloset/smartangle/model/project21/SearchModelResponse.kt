package com.urcloset.smartangle.model.project21


import com.google.gson.annotations.SerializedName

data class SearchModelResponse(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("profiles")
        var profiles: ArrayList<Profile>? = null,
        @SerializedName("titles")
        var titles: List<Title?>? = null
    ) {
        data class Profile(
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

        data class Title(
            @SerializedName("id")
            var id: Int? = null,
            @SerializedName("image")
            var image: String? = null,
            @SerializedName("name")
            var name: String? = null
        )
    }
}