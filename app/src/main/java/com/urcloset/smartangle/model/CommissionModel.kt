package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

 data class CommissionModel(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("messages")
    var messages: Any?,
    @SerializedName("status")
    var status: Boolean?
) {
     data class Data(
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("end")
        var end: String?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("is_free")
        var isFree: Int?,
        @SerializedName("start")
        var start: String?,
        @SerializedName("type")
        var type: String?,
        @SerializedName("updated_at")
        var updatedAt: Any?,
        @SerializedName("value")
        var value: String?
    )
}