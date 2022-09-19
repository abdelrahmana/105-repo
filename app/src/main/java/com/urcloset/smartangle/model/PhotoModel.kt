package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class PhotoModel(
    @SerializedName("dataArray")
    var dataArray: ArrayList<DataArray>? = null
) {
    data class DataArray(
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("photo")
        var photo: String? = null,
        @SerializedName("product_id")
        var productId: String? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("updated_at")
        var updatedAt: String? = null,
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
        var x5: String? = null
    )
}