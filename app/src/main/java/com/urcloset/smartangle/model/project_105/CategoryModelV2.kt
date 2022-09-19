package com.urcloset.smartangle.model.project_105


import com.google.gson.annotations.SerializedName

data class CategoryModelV2(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("messages")
    var messages: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("current_page")
        var currentPage: Int? = null,
        @SerializedName("data")
        var `data`: ArrayList<Data>? = null,
        @SerializedName("first_page_url")
        var firstPageUrl: String? = null,
        @SerializedName("from")
        var from: Int? = null,
        @SerializedName("last_page")
        var lastPage: Int? = null,
        @SerializedName("last_page_url")
        var lastPageUrl: String? = null,
        @SerializedName("links")
        var links: List<Link?>? = null,
        @SerializedName("next_page_url")
        var nextPageUrl: String? = null,
        @SerializedName("path")
        var path: String? = null,
        @SerializedName("per_page")
        var perPage: Int? = null,
        @SerializedName("prev_page_url")
        var prevPageUrl: String? = null,
        @SerializedName("to")
        var to: Int? = null,
        @SerializedName("total")
        var total: Int? = null
    ) {
        data class Data(
            @SerializedName("id")
            var id: Int? = null,
            @SerializedName("media_path")
            var mediaPath: String? = null,
            @SerializedName("name_ar")
            var nameAr: String? = null,
            @SerializedName("name_en")
            var nameEn: String? = null,
            @SerializedName("number")
            var number: Int? = null
        )

        data class Link(
            @SerializedName("active")
            var active: Boolean? = null,
            @SerializedName("label")
            var label: String? = null,
            @SerializedName("url")
            var url: String? = null
        )
    }
}