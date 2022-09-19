package com.urcloset.smartangle.model.project21


import com.google.gson.annotations.SerializedName

data class CheckOutResponse(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("buildNumber")
        var buildNumber: String? = null,
        @SerializedName("ndc")
        var ndc: String? = null,
        @SerializedName("result")
        var result: Result? = null,
        @SerializedName("timestamp")
        var timestamp: String? = null
    ) {
        data class Result(
            @SerializedName("code")
            var code: String? = null,
            @SerializedName("description")
            var description: String? = null,
            @SerializedName("parameterErrors")
            var parameterErrors: List<ParameterError?>? = null
        ) {
            data class ParameterError(
                @SerializedName("message")
                var message: String? = null,
                @SerializedName("name")
                var name: String? = null,
                @SerializedName("value")
                var value: String? = null
            )
        }
    }
}