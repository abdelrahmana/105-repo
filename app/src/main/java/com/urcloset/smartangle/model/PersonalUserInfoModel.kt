package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class PersonalUserInfoModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("user")
        val user: User?
    ) {
        data class User(
            @SerializedName("city")
            val city: Any?,
            @SerializedName("city_id")
            val cityId: Any?,
            @SerializedName("count_aviable_products")
            val countAviableProducts: Int?,
      /*      @SerializedName("count_raters")
            val countRaters: CountRaters?,*/
            @SerializedName("country")
            val country: Any?,
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
            @SerializedName("products")
            val products: List<Any?>?,
            @SerializedName("rate")
            val rate: Float?,
            @SerializedName("social_links")
            val socialLinks: List<SocialLink?>?,
            @SerializedName("social_token")
            val socialToken: String?,
            @SerializedName("social_type")
            val socialType: String?,
            @SerializedName("updated_at")
            val updatedAt: String?,
            @SerializedName("voic_identity")
            val voicIdentity: VoicIdentity?
        ) {
            data class CountRaters(
                @SerializedName("exception")
                val exception: Any?,
                @SerializedName("headers")
                val headers: Headers?,
                @SerializedName("original")
                val original: Original?
            ) {
                class Headers

                data class Original(
                    @SerializedName("data")
                    val `data`: Int?,
                    @SerializedName("messages")
                    val messages: Any?,
                    @SerializedName("status")
                    val status: Boolean?
                )
            }

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
                val mediaPath: Any?,
                @SerializedName("user_id")
                val userId: String?
            )
        }
    }
}