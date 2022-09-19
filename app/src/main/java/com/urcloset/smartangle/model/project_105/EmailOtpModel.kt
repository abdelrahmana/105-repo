package com.urcloset.smartangle.model.project_105


import com.google.gson.annotations.SerializedName

data class EmailOtpModel(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("messages")
    var messages: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("status_send")
        var statusSend: Boolean? = null
    )
}