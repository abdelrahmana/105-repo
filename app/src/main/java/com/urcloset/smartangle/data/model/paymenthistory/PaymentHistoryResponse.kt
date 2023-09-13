package com.urcloset.smartangle.data.model.paymenthistory

data class PaymentHistoryResponse(
    val `data`: List<Data>,
    val messages: Any,
    val status: Boolean
)

data class Data(
    val amount: String,
    val count_items: Any,
    val created_at: String,
    val error_message: Any,
    val id: Int,
    val invoice_id: String,
    val month: String,
    val payment_id: String,
    val payment_status: String,
    val response_object: String,
    val updated_at: String,
    val user_id: String,
    val year: String
)