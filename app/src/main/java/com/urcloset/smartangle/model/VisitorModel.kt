package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class VisitorModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("users")
        val users: Users?
    ) {
        data class Users(
            @SerializedName("current_page")
            val currentPage: Int?,
            @SerializedName("data")
            val `data`: List<Data?>?,
            @SerializedName("first_page_url")
            val firstPageUrl: String?,
            @SerializedName("from")
            val from: Int?,
            @SerializedName("last_page")
            val lastPage: Int?,
            @SerializedName("last_page_url")
            val lastPageUrl: String?,
            @SerializedName("links")
            val links: List<Link?>?,
            @SerializedName("next_page_url")
            val nextPageUrl: Any?,
            @SerializedName("path")
            val path: String?,
            @SerializedName("per_page")
            val perPage: Int?,
            @SerializedName("prev_page_url")
            val prevPageUrl: Any?,
            @SerializedName("to")
            val to: Int?,
            @SerializedName("total")
            val total: Int?
        ) {
            data class Data(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("visited_at")
                val visitedAt: String?,
                @SerializedName("visitor")
                val visitor: Visitor?
            ) {
                data class Visitor(
                    @SerializedName("id")
                    val id: Int?,
                    @SerializedName("image")
                    val image: String?,
                    @SerializedName("name")
                    val name: String?
                )
            }

            data class Link(
                @SerializedName("active")
                val active: Boolean?,
                @SerializedName("label")
                val label: String?,
                @SerializedName("url")
                val url: Any?
            )
        }
    }
}