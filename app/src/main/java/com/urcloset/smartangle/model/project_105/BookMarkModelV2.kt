package com.urcloset.smartangle.model.project_105
import com.google.gson.annotations.SerializedName
data class BookMarkModelV2(
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
            var descriptio: String? = null,
            @SerializedName("description")
            var description: String? = null,
            @SerializedName("id")
            var id: Int? = null,
            @SerializedName("invoice_available")
            var invoiceAvailable: Int? = null,
            @SerializedName("is_negotiable")
            var isNegotiable: Int? = null,
            @SerializedName("item_media")
            var itemMedia: ArrayList<ItemMedia>? = null,
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
            @SerializedName("publish_message_ar")
            var publishMessageAr: String? = null,
            @SerializedName("publish_message_en")
            var publishMessageEn: String? = null,
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

            data class ItemMedia(
                @SerializedName("id")
                var id: Int? = null,
                @SerializedName("item_id")
                var itemId: String? = null,
                @SerializedName("media_path")
                var mediaPath: String? = null,
                @SerializedName("media_type")
                var mediaType: String? = null
            )
        }

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