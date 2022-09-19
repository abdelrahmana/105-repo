package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class NewArrivalsModel(
    @SerializedName("dataArray")
    var dataArray: ArrayList<DataArray>? = null
) {
    data class DataArray(
        @SerializedName("accepted_by")
        var acceptedBy: String? = null,
        @SerializedName("accepted_date")
        var acceptedDate: String? = null,
        @SerializedName("ar_brief")
        var arBrief: String? = null,
        @SerializedName("ar_description")
        var arDescription: String? = null,
        @SerializedName("ar_fullname")
        var arFullname: String? = null,
        @SerializedName("ar_name")
        var arName: String? = null,
        @SerializedName("arbrand")
        var arbrand: String? = null,
        @SerializedName("arcategory")
        var arcategory: String? = null,
        @SerializedName("arsub")
        var arsub: String? = null,
        @SerializedName("category_id")
        var categoryId: String? = null,
        @SerializedName("code")
        var code: String? = null,
        @SerializedName("company_id")
        var companyId: String? = null,
        @SerializedName("country")
        var country: String? = null,
        @SerializedName("en_brief")
        var enBrief: String? = null,
        @SerializedName("en_description")
        var enDescription: String? = null,
        @SerializedName("en_fullname")
        var enFullname: String? = null,
        @SerializedName("en_name")
        var enName: String? = null,
        @SerializedName("enbrand")
        var enbrand: String? = null,
        @SerializedName("encategory")
        var encategory: String? = null,
        @SerializedName("ensub")
        var ensub: String? = null,
        @SerializedName("free_shipping")
        var freeShipping: String? = null,
        @SerializedName("hallprice")
        var hallprice: String? = null,
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("is_accepted")
        var isAccepted: String? = null,
        @SerializedName("is_featured")
        var isFeatured: String? = null,
        @SerializedName("is_new")
        var isNew: String? = null,
        @SerializedName("is_offer")
        var isOffer: String? = null,
        @SerializedName("is_visible")
        var isVisible: String? = null,
        @SerializedName("item_type")
        var itemType: String? = null,
        @SerializedName("marchio_id")
        var marchioId: String? = null,
        @SerializedName("notes")
        var notes: String? = null,
        @SerializedName("offerprice")
        var offerprice: String? = null,
        @SerializedName("on_home")
        var onHome: String? = null,
        @SerializedName("originprice")
        var originprice: String? = null,
        @SerializedName("parent")
        var parent: String? = null,
        @SerializedName("partitionprice")
        var partitionprice: String? = null,
        @SerializedName("pdf")
        var pdf: String? = null,
        @SerializedName("photo")
        var photo: String? = null,
        @SerializedName("post_date")
        var postDate: String? = null,
        @SerializedName("qty")
        var qty: String? = null,
        @SerializedName("rating")
        var rating: String? = null,
        @SerializedName("sellprice")
        var sellprice: String? = null,
        @SerializedName("shipping_price")
        var shippingPrice: String? = null,
        @SerializedName("subcategory_id")
        var subcategoryId: String? = null,
        @SerializedName("tempcount")
        var tempcount: String? = null,
        @SerializedName("unit")
        var unit: String? = null,
        @SerializedName("user_id")
        var userId: String? = null,
        @SerializedName("video_file")
        var videoFile: String? = null,
        @SerializedName("visits")
        var visits: String? = null,
        @SerializedName("whieght")
        var whieght: String? = null,
        @SerializedName("wholprice")
        var wholprice: String? = null,
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
        @SerializedName("19")
        var x19: String? = null,
        @SerializedName("2")
        var x2: String? = null,
        @SerializedName("20")
        var x20: String? = null,
        @SerializedName("21")
        var x21: String? = null,
        @SerializedName("22")
        var x22: String? = null,
        @SerializedName("23")
        var x23: String? = null,
        @SerializedName("24")
        var x24: String? = null,
        @SerializedName("25")
        var x25: String? = null,
        @SerializedName("26")
        var x26: String? = null,
        @SerializedName("27")
        var x27: String? = null,
        @SerializedName("28")
        var x28: String? = null,
        @SerializedName("29")
        var x29: String? = null,
        @SerializedName("3")
        var x3: String? = null,
        @SerializedName("30")
        var x30: String? = null,
        @SerializedName("31")
        var x31: String? = null,
        @SerializedName("32")
        var x32: String? = null,
        @SerializedName("33")
        var x33: String? = null,
        @SerializedName("34")
        var x34: String? = null,
        @SerializedName("35")
        var x35: String? = null,
        @SerializedName("36")
        var x36: String? = null,
        @SerializedName("37")
        var x37: String? = null,
        @SerializedName("38")
        var x38: String? = null,
        @SerializedName("39")
        var x39: String? = null,
        @SerializedName("4")
        var x4: String? = null,
        @SerializedName("40")
        var x40: String? = null,
        @SerializedName("41")
        var x41: String? = null,
        @SerializedName("42")
        var x42: String? = null,
        @SerializedName("43")
        var x43: String? = null,
        @SerializedName("44")
        var x44: String? = null,
        @SerializedName("45")
        var x45: String? = null,
        @SerializedName("46")
        var x46: String? = null,
        @SerializedName("47")
        var x47: String? = null,
        @SerializedName("48")
        var x48: String? = null,
        @SerializedName("49")
        var x49: String? = null,
        @SerializedName("5")
        var x5: String? = null,
        @SerializedName("50")
        var x50: String? = null,
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