package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class DF(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("box_available")
        val boxAvailable: Int?,
        @SerializedName("category")
        val category: Category?,
        @SerializedName("category_id")
        val categoryId: String?,
        @SerializedName("colors_list")
        val colorsList: List<Colors?>?,
        @SerializedName("condition")
        val condition: Condition?,
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
        @SerializedName("negotiable_sentence")
        val negotiableSentence: Any?,
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
        @SerializedName("selected_colors")
        val selectedColors: List<SelectedColor?>?,
        @SerializedName("selected_sizes")
        val selectedSizes: List<SelectedSize?>?,
        @SerializedName("sizes_list")
        val sizesList: List<Sizes?>?,
        @SerializedName("status_list")
        val statusList: List<Status?>?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        @SerializedName("used_sentence")
        val usedSentence: UsedSentence?,
        @SerializedName("used_sentence_id")
        val usedSentenceId: String?,
        @SerializedName("user_id")
        val userId: String?,
        @SerializedName("views")
        val views: Int?
    ) {
        data class Category(
            @SerializedName("full_path")
            val fullPath: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("media_path")
            val mediaPath: String?,
            @SerializedName("name_ar")
            val nameAr: String?,
            @SerializedName("name_en")
            val nameEn: String?,
            @SerializedName("number")
            val number: Int?
        )

        data class Colors(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("is_selected")
            val isSelected: Int?,
            @SerializedName("value")
            val value: String?
        )

        data class Condition(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("name_ar")
            val nameAr: String?,
            @SerializedName("name_en")
            val nameEn: String?
        )

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
            @SerializedName("email")
            val email: String?,
            @SerializedName("full_path")
            val fullPath: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("image")
            val image: String?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("voic_identity")
            val voicIdentity: VoicIdentity?
        ) {
            data class VoicIdentity(
                @SerializedName("full_path")
                val fullPath: String?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("media_path")
                val mediaPath: Any?,
                @SerializedName("user_id")
                val userId: String?
            )
        }

        data class SelectedColor(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("is_selected")
            val isSelected: Int?,
            @SerializedName("value")
            val value: String?
        )

        data class SelectedSize(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("is_selected")
            val isSelected: Int?,
            @SerializedName("name_ar")
            val nameAr: String?,
            @SerializedName("name_en")
            val nameEn: String?
        )

        data class Sizes(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("is_selected")
            val isSelected: Int?,
            @SerializedName("name_ar")
            val nameAr: String?,
            @SerializedName("name_en")
            val nameEn: String?
        )

        data class Status(
            @SerializedName("name_ar")
            val nameAr: String?,
            @SerializedName("name_en")
            val nameEn: String?,
            @SerializedName("status_value")
            val statusValue: Int?
        )

        data class UsedSentence(
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
}