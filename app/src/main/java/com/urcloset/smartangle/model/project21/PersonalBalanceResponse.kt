package com.urcloset.smartangle.model.project21


import com.google.gson.annotations.SerializedName

data class PersonalBalanceResponse(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("status")
    var status: Boolean? = null
) {
    data class Data(
        @SerializedName("remain")
        var remain: Int? = null,
        @SerializedName("settled")
        var settled: Int? = null,
        @SerializedName("total")
        var total: Int? = null
    )
}