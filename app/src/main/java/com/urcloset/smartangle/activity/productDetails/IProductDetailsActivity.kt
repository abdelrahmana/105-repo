package com.urcloset.smartangle.activity.productDetails

import com.urcloset.smartangle.model.ProductModel

interface IProductDetailsActivity {

    fun onProductChange(position: Int, state: Int,product: ProductModel.Product)
}