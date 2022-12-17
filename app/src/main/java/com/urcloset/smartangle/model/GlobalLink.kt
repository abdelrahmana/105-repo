package com.urcloset.smartangle.model

data class GlobalLink(
    val `data`: List<Datax>?,
    val messages: Any?,
    val status: Boolean?
)

data class Datax(
    val full_path: String?,
    val id: Int?,
    val value: String?
)