package com.urcloset.smartangle.listeners

import com.urcloset.smartangle.model.ProductModel

interface OnProductChange {
    fun onProductChange(position:Int,state:Int,product:ProductModel.Product)
}