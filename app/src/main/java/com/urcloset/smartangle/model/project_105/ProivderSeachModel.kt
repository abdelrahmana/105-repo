package com.urcloset.smartangle.model.project_105


import com.google.gson.annotations.SerializedName

data class ProivderSeachModel(
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
            @SerializedName("city")
            var city: Any? = null,
            @SerializedName("city_id")
            var cityId: String? = null,
            @SerializedName("country")
            var country: Any? = null,
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
            var emailVerifiedAt: String? = null,
            @SerializedName("enable_notification")
            var enableNotification: Int? = null,
            @SerializedName("id")
            var id: Int? = null,
            @SerializedName("image")
            var image: String? = null,
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
            var phoneVerifiedAt: String? = null,
            @SerializedName("rate")
            var rate: Double? = null,
            @SerializedName("social_links")
            var socialLinks: List<SocialLink?>? = null,
            @SerializedName("social_token")
            var socialToken: String? = null,
            @SerializedName("social_type")
            var socialType: String? = null,
            @SerializedName("updated_at")
            var updatedAt: String? = null,
            @SerializedName("voic_identity")
            var voicIdentity: Any? = null
        ) {
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