package com.urcloset.smartangle.model.project_105


import com.google.gson.annotations.SerializedName



data class ContactModelList(
    @SerializedName("list")
    var list: ArrayList<ContactModel>? = null


) {

}

data class ContactModel(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("full_phone")
    var phone: String? = null

) {

}