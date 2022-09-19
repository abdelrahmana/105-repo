package com.urcloset.smartangle.model.project21

import com.google.gson.annotations.SerializedName

data class MessageModel(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
}