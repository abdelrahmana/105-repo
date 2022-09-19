package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

 data class NearbyUsersModel(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("messages")
    val messages: Any?
) {
     data class Data(
        @SerializedName("nearby_users")
        val nearbyUsers: NearbyUsers,
    ) {
         data class NearbyUsers(
            @SerializedName("current_page")
            val currentPage: Int?,
            @SerializedName("data")
            var `data`: ArrayList<User>,
            @SerializedName("first_page_url")
            val firstPageUrl: String?,
            @SerializedName("from")
            val from: Int?,
            @SerializedName("last_page")
            var lastPage: Int?,
            @SerializedName("last_page_url")
            val lastPageUrl: String?,
            @SerializedName("links")
            val links: List<Link?>?,
            @SerializedName("next_page_url")
            val nextPageUrl: Any?,
            @SerializedName("path")
            val path: String?,
            @SerializedName("per_page")
            val perPage: Int?,
            @SerializedName("prev_page_url")
            val prevPageUrl: Any?,
            @SerializedName("to")
            val to: Int?,
            @SerializedName("total")
            var total: Int?
        ) {
             data class User(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("image")
                val image: String?,
                @SerializedName("email")
                val email: String?,
                @SerializedName("country_id")
                val countryId: String?,
                @SerializedName("city_id")
                val cityId: String?,
                @SerializedName("country_code")
                val countryCode: String?,
                @SerializedName("phone_number")
                val phoneNumber: String?,
                @SerializedName("account_status")
                val accountStatus: String?,
                @SerializedName("lat")
                val lat: String?,
                @SerializedName("long")
                val long: String?,
                @SerializedName("social_token")
                val socialToken: Any?,
                @SerializedName("social_type")
                val socialType: Any?,
                @SerializedName("rate")
                val rate: Double?,
                @SerializedName("distance")
                val distance: Double?,
                @SerializedName("full_path")
                val fullPath: String?
            )

             data class Link(
                @SerializedName("url")
                val url: Any?,
                @SerializedName("label")
                val label: String?,
                @SerializedName("active")
                val active: Boolean?
            )
        }
    }
}