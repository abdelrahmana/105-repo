package com.urcloset.smartangle.model.project_105


import com.google.gson.annotations.SerializedName

data class CardResultModel(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("messages")
    var messages: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("full_phone")
        var fullPhone: String? = null,
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("updated_at")
        var updatedAt: String? = null
    )
}