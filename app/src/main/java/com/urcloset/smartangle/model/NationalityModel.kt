package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class NationalityModel(
    @SerializedName("dataArray")
    var dataArray: ArrayList<DataArray>? = null
) {
    data class DataArray(
        @SerializedName("action")
        var action: String? = null,
        @SerializedName("ar_title")
        var arTitle: String? = null,
        @SerializedName("Code")
        var code: String? = null,
        @SerializedName("Description")
        var description: String? = null,
        @SerializedName("en_title")
        var enTitle: String? = null,
        @SerializedName("ID")
        var iD: String? = null,
        @SerializedName("insertDate")
        var insertDate: String? = null,
        @SerializedName("PhoneCode")
        var phoneCode: String? = null,
        @SerializedName("Shipping_Price")
        var shippingPrice: String? = null,
        @SerializedName("UpdateDate")
        var updateDate: String? = null,
        @SerializedName("0")
        var x0: String? = null,
        @SerializedName("1")
        var x1: String? = null,
        @SerializedName("2")
        var x2: String? = null,
        @SerializedName("3")
        var x3: String? = null,
        @SerializedName("4")
        var x4: String? = null,
        @SerializedName("5")
        var x5: String? = null,
        @SerializedName("6")
        var x6: String? = null,
        @SerializedName("7")
        var x7: String? = null,
        @SerializedName("8")
        var x8: String? = null,
        @SerializedName("9")
        var x9: String? = null
    )
}