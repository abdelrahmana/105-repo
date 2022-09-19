package com.urcloset.smartangle.model.project_105


import com.google.gson.annotations.SerializedName

data class CountryWithCity(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("messages")
    var messages: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("citties")
        var citties: ArrayList<Citty>? = null,
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("name_ar")
        var nameAr: String? = null
    ) {
        data class Citty(
            @SerializedName("country_id")
            var countryId: Int? = null,
            @SerializedName("id")
            var id: Int? = null,
            @SerializedName("name")
            var name: String? = null,
            @SerializedName("name_ar")
            var nameAr: String? = null
        )
    }
}