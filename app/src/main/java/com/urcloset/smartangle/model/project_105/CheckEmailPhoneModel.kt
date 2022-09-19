package com.urcloset.smartangle.model.project_105


import com.google.gson.annotations.SerializedName

data class CheckEmailPhoneModel(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("messages")
    var messages: List<String>? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("email")
        var email: Boolean? = null,
        @SerializedName("messages")
        var messages: List<String>? = null,
        @SerializedName("phone_number")
        var phoneNumber: Boolean? = null
    )
}