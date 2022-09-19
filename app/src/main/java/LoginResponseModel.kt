
import com.google.gson.annotations.SerializedName

data class LoginResponseModel(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("messages")
    var messages: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("access_token")
        var accessToken: String? = null,
        @SerializedName("expires_at")
        var expiresAt: String? = null,
        @SerializedName("token_type")
        var tokenType: String? = null,
        @SerializedName("user")
        var user: User? = null
    ) {
        data class User(
            @SerializedName("city")
            var city: City? = null,
            @SerializedName("city_id")
            var cityId: String? = null,
            @SerializedName("count_aviable_products")
            var countAviableProducts: Int? = null,
            @SerializedName("country")
            var country: Country? = null,
            @SerializedName("country_code")
            var countryCode: String? = null,
            @SerializedName("country_id")
            var countryId: String? = null,
            @SerializedName("created_at")
            var createdAt: String? = null,
            @SerializedName("current_lang")
            var currentLang: String? = null,
            @SerializedName("email")
            var email: String? = null,
            @SerializedName("email_verified_at")
            var emailVerifiedAt: Any? = null,
            @SerializedName("enable_notification")
            var enableNotification: Int? = null,
            @SerializedName("id")
            var id: Int? = null,
            @SerializedName("image")
            var image: Any? = null,
            @SerializedName("is_deleted")
            var isDeleted: Int? = null,
            @SerializedName("lat")
            var lat: String? = null,
            @SerializedName("long")
            var long: String? = null,
            @SerializedName("name")
            var name: String? = null,
            @SerializedName("phone_number")
            var phoneNumber: String? = null,
            @SerializedName("phone_verified_at")
            var phoneVerifiedAt: Any? = null,
            @SerializedName("rate")
            var rate: Int? = null,
            @SerializedName("social_links")
            var socialLinks: List<SocialLink?>? = null,
            @SerializedName("social_token")
            var socialToken: Any? = null,
            @SerializedName("social_type")
            var socialType: Any? = null,
            @SerializedName("updated_at")
            var updatedAt: String? = null,
            @SerializedName("voic_identity")
            var voicIdentity: VoicIdentity? = null
        ) {
            data class City(
                @SerializedName("country_id")
                var countryId: Int? = null,
                @SerializedName("id")
                var id: Int? = null,
                @SerializedName("name")
                var name: String? = null,
                @SerializedName("name_ar")
                var nameAr: String? = null
            )

            data class Country(
                @SerializedName("id")
                var id: Int? = null,
                @SerializedName("name")
                var name: String? = null,
                @SerializedName("name_ar")
                var nameAr: String? = null
            )

            data class SocialLink(
                @SerializedName("id")
                var id: Int? = null,
                @SerializedName("link_account")
                var linkAccount: String? = null,
                @SerializedName("socail_name")
                var socailName: String? = null,
                @SerializedName("user_id")
                var userId: String? = null
            )

            data class VoicIdentity(
                @SerializedName("id")
                var id: Int? = null,
                @SerializedName("media_path")
                var mediaPath: Any? = null,
                @SerializedName("user_id")
                var userId: String? = null
            )
        }
    }
}