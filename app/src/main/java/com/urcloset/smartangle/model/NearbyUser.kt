package com.urcloset.smartangle.model

data class NearbyUser(
    val city_id: String,
    val country_code: String,
    val country_id: String,
    val created_at: String,
    val email: String,
    val email_verified_at: Any,
    val id: Int,
    val image: String,
    val is_deleted: Int,
    val lat: String,
    val long: String,
    val name: String,
    val phone_number: String,
    val phone_verified_at: Any,
    val rate: Int,
    val social_token: String,
    val social_type: String,
    val updated_at: String
)