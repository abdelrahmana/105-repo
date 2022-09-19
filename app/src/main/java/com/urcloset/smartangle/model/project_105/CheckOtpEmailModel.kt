package com.urcloset.smartangle.model.project_105


import com.google.gson.annotations.SerializedName

data class CheckOtpEmailModel(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("messages")
    var messages: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("status_check")
        var statusCheck: Boolean? = null
    )
}