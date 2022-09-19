package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class PublishStateModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("in_review")
        val inReview: List<ProductModel.Product?>?,
        @SerializedName("published")
        val published: List<ProductModel.Product?>?,
        @SerializedName("rejected")
        val rejected: List<ProductModel.Product?>?
    ) {
        data class InReview(
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
            val negotiableSentenceId: String?,
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
            val usedSentenceId: String?,
            @SerializedName("user_id")
            val userId: Any?,
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
                @SerializedName("id")
                val id: Int?,
                @SerializedName("item_id")
                val itemId: String?,
                @SerializedName("media_path")
                val mediaPath: String?,
                @SerializedName("media_type")
                val mediaType: String?
            )
        }
    }
}