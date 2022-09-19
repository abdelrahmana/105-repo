package com.urcloset.smartangle.model.project21


import com.google.gson.annotations.SerializedName

data class HomeModel(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("categories")
        var categories: ArrayList<Category>? = null,
        @SerializedName("featured")
        var featured: ArrayList<Featured>? = null,
        @SerializedName("new")
        var new: ArrayList<New>? = null,
        @SerializedName("trending")
        var trending: ArrayList<Trending>? = null
    ) {
        data class Category(
            @SerializedName("id")
            var id: Int? = null,
            @SerializedName("image")
            var image: String? = null,
            @SerializedName("name")
            var name: String? = null
        )

        data class Featured(
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

        data class New(
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

        data class Trending(
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
}