package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName
import com.urcloset.smartangle.model.project_105.CountryWithCity

data class ProductDetailsModel(
    @SerializedName("data")
    val productDetails: Data,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("item")
        var item: Item? = null,
        @SerializedName("similar_products")
        var similarProducts: ArrayList<SimilarProduct>? = ArrayList()
    ) {
        data class Item(
            @SerializedName("box_available")
            var boxAvailable: Int? = null,
            @SerializedName("category")
            var category: Category? = null,
            @SerializedName("category_id")
            var categoryId: String? = null,
            @SerializedName("colors_list")
            var colorsList: List<ColorModel.Color?>? = null,
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
            var currentItemStatus: CurrentItemStatus?,
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
            var itemMedia: ArrayList<ItemMedia>,
            @SerializedName("item_status")
            var itemStatus: Int? = null,
            @SerializedName("name")
            var name: String? = null,
            @SerializedName("negotiable_sentence")
            var negotiableSentence: Any? = null,
            @SerializedName("negotiable_sentence_id")
            var negotiableSentenceId: String? = null,
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
            var savedBefore: Boolean? = false,
            @SerializedName("selected_colors")
            var selectedColors: List<ColorModel.Color?>?,
            @SerializedName("selected_sizes")
            var selectedSizes: List<SizeModel.Size?>?,
            @SerializedName("sizes_list")
            var sizesList: List<SizeModel.Size?>?,
            @SerializedName("status_list")
            var statusList: List<Status?>? = null,
            @SerializedName("updated_at")
            var updatedAt: String? = null,
            @SerializedName("used_sentence")
            var usedSentence: UsedSentence? = null,
            @SerializedName("used_sentence_id")
            var usedSentenceId: String? = null,
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
                var city: Citty? = null,
                @SerializedName("count_aviable_products")
                var countAviableProducts: Int? = null,
                @SerializedName("count_raters")
                var countRaters: Int? = null,
                @SerializedName("country")
                var country: Citty? = null,
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

                data class Citty(
                    @SerializedName("country_id")
                    var countryId: Int? = null,
                    @SerializedName("id")
                    var id: Int? = null,
                    @SerializedName("name")
                    var name: String? = null,
                    @SerializedName("name_ar")
                    var nameAr: String? = null
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
            @SerializedName("item_media")
            val itemMedia: List<ProductModel.Product.ItemMedia?>?,
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