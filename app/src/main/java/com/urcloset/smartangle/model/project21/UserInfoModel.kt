package com.urcloset.smartangle.model.project21


import com.google.gson.annotations.SerializedName

data class UserInfoModel(
    @SerializedName("data")
    var `data`: Any? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("bio")
        var bio: String? = null,
        @SerializedName("birth_date")
        var birthDate: Int? = null,
        @SerializedName("created_at")
        var createdAt: Int? = null,
        @SerializedName("email")
        var email: String? = null,
        @SerializedName("iban")
        var iban: String? = null,
        @SerializedName("image")
        var image: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("phone")
        var phone: String? = null,
        @SerializedName("specialty")
        var specialty: String? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("token")
        var token: String? = null,
        @SerializedName("type")
        var type: String? = null,
        @SerializedName("updated_at")
        var updatedAt: Int? = null
    )
}