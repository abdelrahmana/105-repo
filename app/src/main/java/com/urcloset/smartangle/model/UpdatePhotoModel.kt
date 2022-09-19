package com.urcloset.smartangle.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class UpdatePhotoModel(
    @SerializedName("item_media")
    var id: String?=null,
    var photo: Uri?=null,
    var imgUrl: String?=null,
    var img :Int?=0)