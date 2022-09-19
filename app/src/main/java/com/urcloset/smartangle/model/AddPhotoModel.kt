package com.urcloset.smartangle.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class AddPhotoModel(
    @SerializedName("item_media")
    var photo: Uri?=null
    ,var img :Int?=0)




