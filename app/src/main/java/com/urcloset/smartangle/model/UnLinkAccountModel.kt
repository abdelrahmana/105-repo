package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class UnLinkAccountModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("access_token")
        val accessToken: String?,
        @SerializedName("expires_at")
        val expiresAt: String?,
        @SerializedName("token_type")
        val tokenType: String?,
        @SerializedName("user")
        val user: User?
    ) {
        data class User(
            @SerializedName("city")
            val city: Any?,
            @SerializedName("city_id")
            val cityId: Any?,
            @SerializedName("country")
            val country: Any?,
            @SerializedName("country_code")
            val countryCode: String?,
            @SerializedName("country_id")
            val countryId: Any?,
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("email")
            val email: String?,
            @SerializedName("email_verified_at")
            val emailVerifiedAt: Any?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("image")
            val image: Any?,
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