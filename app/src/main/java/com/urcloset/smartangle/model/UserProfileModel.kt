package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class UserProfileModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("messages")
    val messages: String?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("addtion_info")
        val addtionInfo: AddtionInfo?,
        @SerializedName("categoires")
        val categoires: List<CategoryModel.Category?>?,
        @SerializedName("user")
        val user: User?
    ) {
        data class AddtionInfo(
            @SerializedName("enable_notification")
            val enableNotification: Boolean?,
            @SerializedName("pre_favorite")
            val preFavorite: Boolean?,
            @SerializedName("previously_rated")
            val previouslyRated: Boolean?
        )

//        data class Categoire(
//            @SerializedName("id")
//            val id: Int?,
//            @SerializedName("media_path")
//            val mediaPath: String?,
//            @SerializedName("name_ar")
//            val nameAr: String?,
//            @SerializedName("name_en")
//            val nameEn: String?,
//            @SerializedName("number")
//            val number: Int?
//        )

        data class User(
            @SerializedName("city")
            val city: City?,
            @SerializedName("city_id")
            val cityId: String?,
            @SerializedName("count_aviable_products")
            val countAviableProducts: Int?,
            @SerializedName("count_raters")
            val countRaters: Double?,
            @SerializedName("country")
            val country: Country?,
            @SerializedName("country_code")
            val countryCode: String?,
            @SerializedName("country_id")
            val countryId: String?,
            @SerializedName("created_at")
            val createdAt: String?,
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
            val lat: String?,
            @SerializedName("long")
            val long: String?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("phone_number")
            val phoneNumber: String?,
            @SerializedName("phone_verified_at")
            val phoneVerifiedAt: Any?,
            @SerializedName("products")
            val products: List<Any?>?,
            @SerializedName("rate")
            val rate: Double?,
            @SerializedName("social_links")
            val socialLinks: List<SocialLink?>?,
            @SerializedName("social_token")
            val socialToken: Any?,
            @SerializedName("social_type")
            val socialType: Any?,
            @SerializedName("updated_at")
            val updatedAt: String?,
            @SerializedName("voic_identity")
            val voicIdentity: VoicIdentity?
        ) {
            data class City(
                @SerializedName("country_id")
                val countryId: Int?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("name_ar")
                val nameAr: String?
            )

            data class Country(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("name_ar")
                val nameAr: String?
            )

            data class SocialLink(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("link_account")
                val linkAccount: String?,
                @SerializedName("socail_name")
                val socailName: String?,
                @SerializedName("user_id")
                val userId: String?
            )

            data class VoicIdentity(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("media_path")
                val mediaPath: String?,
                @SerializedName("user_id")
                val userId: String?
            )
        }
    }
}