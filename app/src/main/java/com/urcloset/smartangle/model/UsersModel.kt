package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class UsersModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("nearby_users")
        val nearbyUsers: ArrayList<SellerUser>?,

    ) {
        data class SellerUser(
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
            @SerializedName("pre_favorite")
            val preFavorite: Boolean?,
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
            val socialToken: Any?,
            @SerializedName("social_type")
            val socialType: String?,
            @SerializedName("updated_at")
            val updatedAt: String?
        )

    }
}