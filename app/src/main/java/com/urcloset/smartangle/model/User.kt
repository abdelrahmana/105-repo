package com.urcloset.smartangle.model

/*import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey*/
import com.google.gson.annotations.SerializedName

const val CURRENT_USER_ID = 0

/*@Entity(tableName = "User")*/
data class User(

    var birthday: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("name")
    var name: String? = null,

    /*  @Ignore*/
    var password: String? = null,
    @SerializedName("number_phone")
    var numberPhone: String? = null,

    var token:String?=null
){

/*    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID*/
}

