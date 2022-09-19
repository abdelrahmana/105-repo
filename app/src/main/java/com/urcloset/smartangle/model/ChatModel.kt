package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class ChatModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("messages")
    val messages: String?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("chat")
        val chat: Chat?,
        @SerializedName("count_unseen")
        val countUnseen: Int?
    ) {
        data class Chat(
            @SerializedName("current_page")
            val currentPage: Int?,
            @SerializedName("data")
            val `data`: List<ChatItems?>?,
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
            data class ChatItems(
                @SerializedName("email")
                val email: String?,
                @SerializedName("file")
                val `file`: Any?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("message")
                val message: String?,
                @SerializedName("sender_client_id")
                val senderClientId: Any?,
                @SerializedName("sender_replyer_id")
                val senderReplyerId: String?,
                @SerializedName("subject")
                val subject: String?,
                @SerializedName("updated_at")
                val updatedAt: String?
            )

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