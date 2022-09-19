package com.urcloset.smartangle.model.project21


import com.google.gson.annotations.SerializedName

data class PaymentStatusModel(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("amount")
        var amount: String? = null,
        @SerializedName("buildNumber")
        var buildNumber: String? = null,
        @SerializedName("card")
        var card: Card? = null,
        @SerializedName("currency")
        var currency: String? = null,
        @SerializedName("customParameters")
        var customParameters: CustomParameters? = null,
        @SerializedName("customer")
        var customer: Customer? = null,
        @SerializedName("descriptor")
        var descriptor: String? = null,
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("ndc")
        var ndc: String? = null,
        @SerializedName("paymentBrand")
        var paymentBrand: String? = null,
        @SerializedName("paymentType")
        var paymentType: String? = null,
        @SerializedName("result")
        var result: Result? = null,
        @SerializedName("resultDetails")
        var resultDetails: ResultDetails? = null,
        @SerializedName("risk")
        var risk: Risk? = null,
        @SerializedName("timestamp")
        var timestamp: String? = null
    ) {
        data class Card(
            @SerializedName("bin")
            var bin: String? = null,
            @SerializedName("binCountry")
            var binCountry: String? = null,
            @SerializedName("expiryMonth")
            var expiryMonth: String? = null,
            @SerializedName("expiryYear")
            var expiryYear: String? = null,
            @SerializedName("holder")
            var holder: String? = null,
            @SerializedName("last4Digits")
            var last4Digits: String? = null
        )

        data class CustomParameters(
            @SerializedName("CTPE_DESCRIPTOR_TEMPLATE")
            var cTPEDESCRIPTORTEMPLATE: String? = null,
            @SerializedName("SHOPPER_device")
            var sHOPPERDevice: String? = null,
            @SerializedName("SHOPPER_MSDKIntegrationType")
            var sHOPPERMSDKIntegrationType: String? = null,
            @SerializedName("SHOPPER_MSDKVersion")
            var sHOPPERMSDKVersion: String? = null,
            @SerializedName("SHOPPER_OS")
            var sHOPPEROS: String? = null
        )

        data class Customer(
            @SerializedName("ip")
            var ip: String? = null,
            @SerializedName("ipCountry")
            var ipCountry: String? = null
        )

        data class Result(
            @SerializedName("code")
            var code: String? = null,
            @SerializedName("description")
            var description: String? = null
        )

        data class ResultDetails(
            @SerializedName("ConnectorTxID1")
            var connectorTxID1: String? = null
        )

        data class Risk(
            @SerializedName("score")
            var score: String? = null
        )
    }
}