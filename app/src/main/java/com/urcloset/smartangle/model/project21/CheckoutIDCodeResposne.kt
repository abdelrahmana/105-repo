package com.urcloset.smartangle.model.project21


import com.google.gson.annotations.SerializedName

data class CheckoutIDCodeResposne(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("code")
        var code: String? = null
    )
}