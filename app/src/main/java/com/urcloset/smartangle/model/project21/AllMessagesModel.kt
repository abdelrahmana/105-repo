package com.urcloset.smartangle.model.project21


import com.google.gson.annotations.SerializedName

data class AllMessagesModel(
    @SerializedName("data")
    var `data`: ArrayList<Data>? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("active")
        var active: String? = null,
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("price")
        var price: String? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("type")
        var type: String? = null,
        @SerializedName("tax_percent")
        var taxPercent: String? = null,
        @SerializedName("tax")
        var tax: String? = null,

        var checked:Boolean=false
    )
}