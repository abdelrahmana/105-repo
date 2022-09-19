package com.urcloset.smartangle.globals
import com.urcloset.smartangle.model.ProductDetailsModel
import com.urcloset.smartangle.model.ProductModel


class Global {
    companion object {
        var productImages : ArrayList<ProductDetailsModel.Data.Item.ItemMedia>?=null
        var position = 0
    }
}