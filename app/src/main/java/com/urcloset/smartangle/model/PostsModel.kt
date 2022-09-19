package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class PostsModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("current_page")
        val currentPage: Int?,
        @SerializedName("data")
        val `data`: ArrayList<Post>?,
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
        val nextPageUrl: String?,
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
        data class Post(
            @SerializedName("box_available")
            val boxAvailable: Int?,
            @SerializedName("category_id")
            val categoryId: String?,
            @SerializedName("condition_id")
            val conditionId: String?,
            @SerializedName("connect_whatsapp")
            val connectWhatsapp: Int?,
            @SerializedName("country_code")
            val countryCode: String?,
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("current_item_status")
            val currentItemStatus: CurrentItemStatus?,
            @SerializedName("current_payment_status")
            val currentPaymentStatus: CurrentPaymentStatus?,
            @SerializedName("current_publish_status")
            val currentPublishStatus: CurrentPublishStatus?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("invoice_available")
            val invoiceAvailable: Int?,
            @SerializedName("is_negotiable")
            val isNegotiable: Int?,
            @SerializedName("item_media")
            val itemMedia: List<ItemMedia?>?,
            @SerializedName("item_status")
            val itemStatus: Int?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("negotiable_sentence_id")
            val negotiableSentenceId: Any?,
            @SerializedName("owner")
            val owner: Owner?,
            @SerializedName("payment_status")
            val paymentStatus: Int?,
            @SerializedName("phone_number")
            val phoneNumber: String?,
            @SerializedName("price")
            val price: Int?,
            @SerializedName("publish_message_ar")
            val publishMessageAr: Any?,
            @SerializedName("publish_message_en")
            val publishMessageEn: Any?,
            @SerializedName("publish_status")
            val publishStatus: Int?,
            @SerializedName("saved_before")
            val savedBefore: Boolean?,
            @SerializedName("updated_at")
            val updatedAt: String?,
            @SerializedName("used_sentence_id")
            val usedSentenceId: Any?,
            @SerializedName("user_id")
            val userId: String?,
            @SerializedName("views")
            val views: Int?
        ) {
            data class CurrentItemStatus(
                @SerializedName("name_ar")
                val nameAr: String?,
                @SerializedName("name_en")
                val nameEn: String?,
                @SerializedName("status_value")
                val statusValue: Int?
            )

            data class CurrentPaymentStatus(
                @SerializedName("name_ar")
                val nameAr: String?,
                @SerializedName("name_en")
                val nameEn: String?,
                @SerializedName("status_value")
                val statusValue: Int?
            )

            data class CurrentPublishStatus(
                @SerializedName("name_ar")
                val nameAr: String?,
                @SerializedName("name_en")
                val nameEn: String?,
                @SerializedName("status_value")
                val statusValue: Int?
            )

            data class ItemMedia(
                @SerializedName("full_path")
                val fullPath: String?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("item_id")
                val itemId: String?,
                @SerializedName("media_path")
                val mediaPath: String?,
                @SerializedName("media_type")
                val mediaType: String?
            )

            data class Owner(
                @SerializedName("account_status")
                val accountStatus: String?,
                @SerializedName("city_id")
                val cityId: String?,
                @SerializedName("count_aviable_products")
                val countAviableProducts: Int?,
                @SerializedName("count_raters")
                val countRaters: Int?,
                @SerializedName("country_code")
                val countryCode: String?,
                @SerializedName("country_id")
                val countryId: String?,
                @SerializedName("created_at")
                val createdAt: String?,
                @SerializedName("current_lang")
                val currentLang: String?,
                @SerializedName("email")
                val email: String?,
                @SerializedName("email_verified_at")
                val emailVerifiedAt: Any?,
                @SerializedName("enable_notification")
                val enableNotification: Int?,
                @SerializedName("full_path")
                val fullPath: String?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("image")
                val image: String?,
                @SerializedName("is_deleted")
                val isDeleted: Int?,
                @SerializedName("lat")
                val lat: Any?,
                @SerializedName("long")
                val long: Any?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("phone_number")
                val phoneNumber: String?,
                @SerializedName("phone_verified_at")
                val phoneVerifiedAt: Any?,
                @SerializedName("rate")
                val rate: Double?,
                @SerializedName("social_token")
                val socialToken: Any?,
                @SerializedName("social_type")
                val socialType: Any?,
                @SerializedName("updated_at")
                val updatedAt: String?
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