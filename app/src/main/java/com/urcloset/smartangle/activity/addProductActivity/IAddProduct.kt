package com.urcloset.smartangle.activity.addProductActivity

import okhttp3.MultipartBody

interface IAddProduct {

    fun addProductRQDialog(images:ArrayList<MultipartBody.Part>,
                     sizes:ArrayList<Int>,
                     map:Map<String,String>,
                     colors:ArrayList<Int>)
}