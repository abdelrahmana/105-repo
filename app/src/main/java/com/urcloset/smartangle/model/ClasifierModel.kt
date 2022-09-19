package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class ClasifierModel(
    @SerializedName("dataArray")
    var dataArray: ArrayList<DataArray>? = null
) {
    data class DataArray(
        @SerializedName("ar_brief")
        var arBrief: String? = null,
        @SerializedName("ar_description")
        var arDescription: String? = null,
        @SerializedName("ar_name")
        var arName: String? = null,
        @SerializedName("ar_title_image")
        var arTitleImage: String? = null,
        @SerializedName("category_order")
        var categoryOrder: String? = null,
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("doc_id")
        var docId: String? = null,
        @SerializedName("en_brief")
        var enBrief: String? = null,
        @SerializedName("en_description")
        var enDescription: String? = null,
        @SerializedName("en_name")
        var enName: String? = null,
        @SerializedName("en_title_image")
        var enTitleImage: String? = null,
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("is_visible")
        var isVisible: String? = null,
        @SerializedName("items_count")
        var itemsCount: String? = null,
        @SerializedName("main_parent")
        var mainParent: String? = null,
        @SerializedName("parent")
        var parent: String? = null,
        @SerializedName("photo")
        var photo: String? = null,
        @SerializedName("updated_at")
        var updatedAt: String? = null,
        @SerializedName("visits")
        var visits: String? = null,
        @SerializedName("0")
        var x0: String? = null,
        @SerializedName("1")
        var x1: String? = null,
        @SerializedName("10")
        var x10: String? = null,
        @SerializedName("11")
        var x11: String? = null,
        @SerializedName("12")
        var x12: String? = null,
        @SerializedName("13")
        var x13: String? = null,
        @SerializedName("14")
        var x14: String? = null,
        @SerializedName("15")
        var x15: String? = null,
        @SerializedName("16")
        var x16: String? = null,
        @SerializedName("17")
        var x17: String? = null,
        @SerializedName("18")
        var x18: String? = null,
        @SerializedName("2")
        var x2: String? = null,
        @SerializedName("3")
        var x3: String? = null,
        @SerializedName("4")
        var x4: String? = null,
        @SerializedName("5")
        var x5: String? = null,
        @SerializedName("6")
        var x6: String? = null,
        @SerializedName("7")
        var x7: String? = null,
        @SerializedName("8")
        var x8: String? = null,
        @SerializedName("9")
        var x9: String? = null
    )
}