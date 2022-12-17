package com.urcloset.smartangle.data.model.agreement_terms

data class AgreementResponseTerms(
    val `data`: Data?,
    val messages: Any?,
    val status: Boolean?
)

data class Data(
    val content: String?,
    val content_ar: String?,
    val created_at: Any?,
    val email: Any?,
    val full_path: String?,
    val id: Int?,
    val key: String?,
    val media_path: Any?,
    val title: String?,
    val title_ar: Any?,
    val updated_at: String?
)