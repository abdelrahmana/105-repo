package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class CreateProductModel(
    @SerializedName("data")
    val `data`: Product?,
    @SerializedName("messages")
    val messages: String?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Product(
        @SerializedName("box_available")
        val boxAvailable: Int?,
        @SerializedName("category")
        val category: Category?,
        @SerializedName("category_id")
        val categoryId: String?,
        @SerializedName("condition")
        val condition: Condition?,
        @SerializedName("condition_id")
        val conditionId: String?,
        @SerializedName("country_code")
        val countryCode: String?,
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("current_item_status")
        val currentItemStatus: CurrentItemStatus?,
        @SerializedName("current_payment_status")
        val currentPaymentStatus: CurrentPaymentStatus?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("invoice_available")
        val invoiceAvailable: Int?,
        @SerializedName("is_negotiable")
        val isNegotiable: String?,
        @SerializedName("item_media")
        val itemMedia: List<Any?>?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("negotiable_sentence")
        val negotiableSentence: NegotiableSentence?,
        @SerializedName("negotiable_sentence_id")
        val negotiableSentenceId: String?,
        @SerializedName("owner")
        val owner: Owner?,
        @SerializedName("phone_number")
        val phoneNumber: String?,
        @SerializedName("price")
        val price: Int?,
        @SerializedName("saved_before")
        val savedBefore: Boolean?,
        @SerializedName("selected_colors")
        val selectedColors: List<SelectedColor?>?,
        @SerializedName("selected_sizes")
        val selectedSizes: List<SelectedSize?>?,
        @SerializedName("status_list")
        val statusList: List<Status?>?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        @SerializedName("used_sentence")
        val usedSentence: UsedSentence?,
        @SerializedName("used_sentence_id")
        val usedSentenceId: String?,
        @SerializedName("user_id")
        val userId: Int?
    ) {
        data class Category(
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

        data class NegotiableSentence(
            @SerializedName("arabic")
            val arabic: String?,
            @SerializedName("english")
            val english: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("key")
            val key: String?
        )

        data class Owner(
            @SerializedName("email")
            val email: String?,
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