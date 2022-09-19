package com.urcloset.smartangle.model.project21


import com.google.gson.annotations.SerializedName

data class MessageInboxSendModel(
    @SerializedName("data")
    var `data`: ArrayList<Data>? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("refund_date")
        var refundDate: String? = null,
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("message_seen")
        var messageSeen: Int? = null,
        @SerializedName("price")
        var price: String? = null,
        @SerializedName("receiver_image")
        var receiverImage: String? = null,
        @SerializedName("receiver_name")
        var receiverName: String? = null,
        @SerializedName("reply")
        var reply: String? = null,
        @SerializedName("sender_image")
        var senderImage: String? = null,
        @SerializedName("sender_name")
        var senderName: String? = null,
        @SerializedName("type")
        var type: String? = null,
        @SerializedName("pay_status")
        var payStatus: String? = null
    )
}