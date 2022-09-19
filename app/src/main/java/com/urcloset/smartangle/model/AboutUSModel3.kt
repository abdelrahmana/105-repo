package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class AboutUSModel3(
    @SerializedName("dataArray")
    var dataArray: List<DataArray?>? = null
) {
    data class DataArray(
        @SerializedName("added_date")
        var addedDate: String? = null,
        @SerializedName("added_user")
        var addedUser: String? = null,
        @SerializedName("banner")
        var banner: String? = null,
        @SerializedName("block_id")
        var blockId: String? = null,
        @SerializedName("brief")
        var brief: String? = null,
        @SerializedName("built_in")
        var builtIn: String? = null,
        @SerializedName("col_md")
        var colMd: String? = null,
        @SerializedName("col_sm")
        var colSm: String? = null,
        @SerializedName("col_xs")
        var colXs: String? = null,
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("doc_order")
        var docOrder: String? = null,
        @SerializedName("docs_key")
        var docsKey: String? = null,
        @SerializedName("download_file")
        var downloadFile: String? = null,
        @SerializedName("file")
        var `file`: String? = null,
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("image_file")
        var imageFile: String? = null,
        @SerializedName("inner_page_class")
        var innerPageClass: String? = null,
        @SerializedName("inner_page_image_block_class")
        var innerPageImageBlockClass: String? = null,
        @SerializedName("inner_page_image_block_style")
        var innerPageImageBlockStyle: String? = null,
        @SerializedName("inner_page_image_class")
        var innerPageImageClass: String? = null,
        @SerializedName("inner_page_image_style_docs")
        var innerPageImageStyleDocs: String? = null,
        @SerializedName("inner_page_style_docs")
        var innerPageStyleDocs: String? = null,
        @SerializedName("inner_page_text_class")
        var innerPageTextClass: String? = null,
        @SerializedName("inner_page_text_style_docs")
        var innerPageTextStyleDocs: String? = null,
        @SerializedName("inner_page_title_class")
        var innerPageTitleClass: String? = null,
        @SerializedName("inner_page_title_style_docs")
        var innerPageTitleStyleDocs: String? = null,
        @SerializedName("is_archive")
        var isArchive: String? = null,
        @SerializedName("is_download")
        var isDownload: String? = null,
        @SerializedName("is_image")
        var isImage: String? = null,
        @SerializedName("is_link")
        var isLink: String? = null,
        @SerializedName("is_visible")
        var isVisible: String? = null,
        @SerializedName("lang")
        var lang: String? = null,
        @SerializedName("main_image_class")
        var mainImageClass: String? = null,
        @SerializedName("main_image_style")
        var mainImageStyle: String? = null,
        @SerializedName("menu_id")
        var menuId: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("outer_class")
        var outerClass: String? = null,
        @SerializedName("parent")
        var parent: String? = null,
        @SerializedName("photo")
        var photo: String? = null,
        @SerializedName("prints")
        var prints: String? = null,
        @SerializedName("sends")
        var sends: String? = null,
        @SerializedName("service")
        var service: String? = null,
        @SerializedName("show_block_content")
        var showBlockContent: String? = null,
        @SerializedName("style")
        var style: String? = null,
        @SerializedName("style_title")
        var styleTitle: String? = null,
        @SerializedName("sub_links")
        var subLinks: String? = null,
        @SerializedName("target")
        var target: String? = null,
        @SerializedName("title_class")
        var titleClass: String? = null,
        @SerializedName("updated_at")
        var updatedAt: String? = null,
        @SerializedName("updated_date")
        var updatedDate: String? = null,
        @SerializedName("updated_user")
        var updatedUser: String? = null,
        @SerializedName("url")
        var url: String? = null,
        @SerializedName("url_type")
        var urlType: String? = null,
        @SerializedName("visits")
        var visits: String? = null,
        @SerializedName("with_block")
        var withBlock: String? = null,
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
        @SerializedName("51")
        var x51: String? = null,
        @SerializedName("52")
        var x52: String? = null,
        @SerializedName("53")
        var x53: String? = null,
        @SerializedName("54")
        var x54: String? = null,
        @SerializedName("55")
        var x55: String? = null,
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