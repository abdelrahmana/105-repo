package com.urcloset.smartangle.model

data class Data(
    val nearby_users: List<UsersModel.Data.SellerUser>,
    val top_users: List<UsersModel.Data.SellerUser>
)