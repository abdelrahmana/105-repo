package com.urcloset.smartangle.model

data class ProductItemResponse(
    val `data`: List<ProductModel.Product>?,
    val messages: Any?,
    val status: Boolean?
)