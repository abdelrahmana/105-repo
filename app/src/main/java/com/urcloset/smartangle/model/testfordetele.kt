package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class testfordetele(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("messages")
    var messages: Any? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("item")
        var item: Item? = null,
        @SerializedName("similar_products")
        var similarProducts: List<SimilarProduct?>? = null
    ) {
        data class Item(
            @SerializedName("box_available")
            var boxAvailable: Int? = null,
            @SerializedName("category")
            var category: Category? = null,
            @SerializedName("category_id")
            var categoryId: String? = null,
            @SerializedName("colors_list")
            var colorsList: List<Colors?>? = null,
            @SerializedName("condition")
            var condition: Condition? = null,
            @SerializedName("condition_id")
            var conditionId: String? = null,
            @SerializedName("connect_whatsapp")
            var connectWhatsapp: Int? = null,
            @SerializedName("country_code")
            var countryCode: String? = null,
            @SerializedName("created_at")
            var createdAt: String? = null,
            @SerializedName("current_item_status")
            var currentItemStatus: CurrentItemStatus? = null,
            @SerializedName("current_payment_status")
            var currentPaymentStatus: CurrentPaymentStatus? = null,
            @SerializedName("current_publish_status")
            var currentPublishStatus: CurrentPublishStatus? = null,
            @SerializedName("description")
            var description: String? = null,
            @SerializedName("id")
            var id: Int? = null,
            @SerializedName("invoice_available")
            var invoiceAvailable: Int? = null,
            @SerializedName("is_negotiable")
            var isNegotiable: Int? = null,
            @SerializedName("item_media")
            var itemMedia: List<ItemMedia?>? = null,
            @SerializedName("item_status")
            var itemStatus: Int? = null,
            @SerializedName("name")
            var name: String? = null,
            @SerializedName("negotiable_sentence")
            var negotiableSentence: Any? = null,
            @SerializedName("negotiable_sentence_id")
            var negotiableSentenceId: Any? = null,
            @SerializedName("owner")
            var owner: Owner? = null,
            @SerializedName("payment_status")
            var paymentStatus: Int? = null,
            @SerializedName("phone_number")
            var phoneNumber: String? = null,
            @SerializedName("price")
            var price: Int? = null,
            @SerializedName("publish_date")
            var publishDate: Any? = null,
            @SerializedName("publish_message_ar")
            var publishMessageAr: Any? = null,
            @SerializedName("publish_message_en")
            var publishMessageEn: Any? = null,
            @SerializedName("publish_status")
            var publishStatus: Int? = null,
            @SerializedName("saved_before")
            var savedBefore: Boolean? = null,
            @SerializedName("selected_colors")
            val selectedColors: List<ColorModel.Color?>?,
            @SerializedName("selected_sizes")
            var selectedSizes: List<SelectedSize?>? ,
            @SerializedName("sizes_list")
            var sizesList: List<Sizes?>? = null,
            @SerializedName("status_list")
            var statusList: List<Status?>? = null,
            @SerializedName("updated_at")
            var updatedAt: String? = null,
            @SerializedName("used_sentence")
            var usedSentence: Any? = null,
            @SerializedName("used_sentence_id")
            var usedSentenceId: Any? = null,
            @SerializedName("user_id")
            var userId: String? = null,
            @SerializedName("views")
            var views: Int? = null
        ) {
            data class Category(
                @SerializedName("full_path")
                var fullPath: String? = null,
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

            data class Colors(
                @SerializedName("id")
                var id: Int? = null,
                @SerializedName("is_selected")
                var isSelected: Int? = null,
                @SerializedName("value")
                var value: String? = null
            )

            data class Condition(
                @SerializedName("id")
                var id: Int? = null,
                @SerializedName("name_ar")
                var nameAr: String? = null,
                @SerializedName("name_en")
                var nameEn: String? = null
            )

            data class CurrentItemStatus(
                @SerializedName("name_ar")
                var nameAr: String? = null,
                @SerializedName("name_en")
                var nameEn: String? = null,
                @SerializedName("status_value")
                var statusValue: Int? = null
            )

            data class CurrentPaymentStatus(
                @SerializedName("name_ar")
                var nameAr: String? = null,
                @SerializedName("name_en")
                var nameEn: String? = null,
                @SerializedName("status_value")
                var statusValue: Int? = null
            )

            data class CurrentPublishStatus(
                @SerializedName("name_ar")
                var nameAr: String? = null,
                @SerializedName("name_en")
                var nameEn: String? = null,
                @SerializedName("status_value")
                var statusValue: Int? = null
            )

            data class ItemMedia(
                @SerializedName("full_path")
                var fullPath: String? = null,
                @SerializedName("id")
                var id: Int? = null,
                @SerializedName("item_id")
                var itemId: String? = null,
                @SerializedName("media_path")
                var mediaPath: String? = null,
                @SerializedName("media_type")
                var mediaType: String? = null
            )

            data class Owner(
                @SerializedName("city")
                var city: Any? = null,
                @SerializedName("count_aviable_products")
                var countAviableProducts: Int? = null,
                @SerializedName("count_raters")
                var countRaters: Int? = null,
                @SerializedName("country")
                var country: Any? = null,
                @SerializedName("email")
                var email: String? = null,
                @SerializedName("full_path")
                var fullPath: String? = null,
                @SerializedName("id")
                var id: Int? = null,
                @SerializedName("image")
                var image: String? = null,
                @SerializedName("name")
                var name: String? = null,
                @SerializedName("rate")
                var rate: Double? = null,
                @SerializedName("voic_identity")
                var voicIdentity: VoicIdentity? = null
            ) {
                data class VoicIdentity(
                    @SerializedName("full_path")
                    var fullPath: String? = null,
                    @SerializedName("id")
                    var id: Int? = null,
                    @SerializedName("media_path")
                    var mediaPath: String? = null,
                    @SerializedName("user_id")
                    var userId: String? = null
                )
            }

            data class SelectedColor(
                @SerializedName("id")
                var id: Int? = null,
                @SerializedName("is_selected")
                var isSelected: Int? = null,
                @SerializedName("value")
                var value: String? = null
            )

            data class SelectedSize(
                @SerializedName("id")
                var id: Int? = null,
                @SerializedName("is_selected")
                var isSelected: Int? = null,
                @SerializedName("name_ar")
                var nameAr: String? = null,
                @SerializedName("name_en")
                var nameEn: String? = null
            )

            data class Sizes(
                @SerializedName("id")
                var id: Int? = null,
                @SerializedName("is_selected")
                var isSelected: Int? = null,
                @SerializedName("name_ar")
                var nameAr: String? = null,
                @SerializedName("name_en")
                var nameEn: String? = null
            )

            data class Status(
                @SerializedName("name_ar")
                var nameAr: String? = null,
                @SerializedName("name_en")
                var nameEn: String? = null,
                @SerializedName("status_value")
                var statusValue: Int? = null
            )
        }

        data class SimilarProduct(
            @SerializedName("box_available")
            var boxAvailable: Int? = null,
            @SerializedName("category_id")
            var categoryId: String? = null,
            @SerializedName("condition_id")
            var conditionId: String? = null,
            @SerializedName("connect_whatsapp")
            var connectWhatsapp: Int? = null,
            @SerializedName("country_code")
            var countryCode: String? = null,
            @SerializedName("created_at")
            var createdAt: String? = null,
            @SerializedName("current_item_status")
            var currentItemStatus: CurrentItemStatus? = null,
            @SerializedName("current_payment_status")
            var currentPaymentStatus: CurrentPaymentStatus? = null,
            @SerializedName("current_publish_status")
            var currentPublishStatus: CurrentPublishStatus? = null,
            @SerializedName("description")
            var description: String? = null,
            @SerializedName("id")
            var id: Int? = null,
            @SerializedName("invoice_available")
            var invoiceAvailable: Int? = null,
            @SerializedName("is_negotiable")
            var isNegotiable: Int? = null,
            @SerializedName("item_status")
            var itemStatus: Int? = null,
            @SerializedName("name")
            var name: String? = null,
            @SerializedName("negotiable_sentence_id")
            var negotiableSentenceId: String? = null,
            @SerializedName("payment_status")
            var paymentStatus: Int? = null,
            @SerializedName("phone_number")
            var phoneNumber: String? = null,
            @SerializedName("price")
            var price: Int? = null,
            @SerializedName("publish_date")
            var publishDate: Any? = null,
            @SerializedName("publish_message_ar")
            var publishMessageAr: Any? = null,
            @SerializedName("publish_message_en")
            var publishMessageEn: Any? = null,
            @SerializedName("publish_status")
            var publishStatus: Int? = null,
            @SerializedName("saved_before")
            var savedBefore: Boolean? = null,
            @SerializedName("updated_at")
            var updatedAt: String? = null,
            @SerializedName("used_sentence_id")
            var usedSentenceId: String? = null,
            @SerializedName("user_id")
            var userId: String? = null,
            @SerializedName("views")
            var views: Int? = null
        ) {
            data class CurrentItemStatus(
                @SerializedName("name_ar")
                var nameAr: String? = null,
                @SerializedName("name_en")
                var nameEn: String? = null,
                @SerializedName("status_value")
                var statusValue: Int? = null
            )

            data class CurrentPaymentStatus(
                @SerializedName("name_ar")
                var nameAr: String? = null,
                @SerializedName("name_en")
                var nameEn: String? = null,
                @SerializedName("status_value")
                var statusValue: Int? = null
            )

            data class CurrentPublishStatus(
                @SerializedName("name_ar")
                var nameAr: String? = null,
                @SerializedName("name_en")
                var nameEn: String? = null,
                @SerializedName("status_value")
                var statusValue: Int? = null
            )
        }
    }
}