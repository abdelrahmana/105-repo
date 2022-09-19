package com.urcloset.smartangle.model.project_105


import com.google.gson.annotations.SerializedName

data class CountryModel(
    @SerializedName("data")
    var `data`: ArrayList<Data>? = null,
    @SerializedName("messages")
    var messages: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("name_ar")
        var nameAr: String? = null
    )
}