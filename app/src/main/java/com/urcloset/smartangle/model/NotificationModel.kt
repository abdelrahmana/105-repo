package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class NotificationModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("count_unseen")
        val countUnseen: Int?,
        @SerializedName("list")
        val list: NotificationList?
    ) {
        data class NotificationList(
            @SerializedName("current_page")
            val currentPage: Int?,
            @SerializedName("data")
            val `notifications`: List<NotificationItem?>?,
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
            data class NotificationItem(
                @SerializedName("body_ar")
                val bodyAr: String?,
                @SerializedName("body_en")
                val bodyEn: String?,
                @SerializedName("category")
                val category: Category?,
                @SerializedName("category_id")
                val categoryId: String?,
                @SerializedName("created_at")
                val createdAt: String?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("is_deleted")
                val isDeleted: Int?,
                @SerializedName("item")
                val item: ProductModel.Product?,
                @SerializedName("item_id")
                val itemId: String?,
                @SerializedName("object_action")
                val objectAction: Any?,
                @SerializedName("owner_item_id")
                val ownerItemId: String?,
                @SerializedName("receiver")
                val `receiver`: Receiver?,
                @SerializedName("receiver_id")
                val receiverId: Int?,
                @SerializedName("sender")
                val sender: Sender?,
                @SerializedName("sender_id")
                val senderId: Int?,
                @SerializedName("status")
                val status: Int?,
                @SerializedName("title_ar")
                val titleAr: String?,
                @SerializedName("title_en")
                val titleEn: String?,
                @SerializedName("type")
                val type: String?,
                @SerializedName("updated_at")
                val updatedAt: String?
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

                data class Item(
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
                    @SerializedName("description")
                    val description: String?,
                    @SerializedName("id")
                    val id: Int?,
                    @SerializedName("invoice_available")
                    val invoiceAvailable: Int?,
                    @SerializedName("item_media")
                    val itemMedia: List<String?>?,
                    @SerializedName("item_status")
                    val itemStatus: Int?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("payment_status")
                    val paymentStatus: Int?,
                    @SerializedName("phone_number")
                    val phoneNumber: String?,
                    @SerializedName("price")
                    val price: Int?,
                    @SerializedName("updated_at")
                    val updatedAt: String?,
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
                }

                data class Receiver(
                    @SerializedName("city_id")
                    val cityId: Any?,
                    @SerializedName("country_code")
                    val countryCode: String?,
                    @SerializedName("country_id")
                    val countryId: Any?,
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
                    val phoneNumber: Any?,
                    @SerializedName("phone_verified_at")
                    val phoneVerifiedAt: Any?,
                    @SerializedName("rate")
                    val rate: Double?,
                    @SerializedName("social_token")
                    val socialToken: String?,
                    @SerializedName("social_type")
                    val socialType: String?,
                    @SerializedName("updated_at")
                    val updatedAt: String?
                )

                data class Sender(
                    @SerializedName("city_id")
                    val cityId: Any?,
                    @SerializedName("country_code")
                    val countryCode: String?,
                    @SerializedName("country_id")
                    val countryId: Any?,
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
                    val phoneNumber: Any?,
                    @SerializedName("phone_verified_at")
                    val phoneVerifiedAt: Any?,
                    @SerializedName("rate")
                    val rate: Double?,
                    @SerializedName("social_token")
                    val socialToken: String?,
                    @SerializedName("social_type")
                    val socialType: String?,
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
}