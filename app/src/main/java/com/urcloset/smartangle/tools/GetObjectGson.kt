package com.urcloset.smartangle.tools

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GetObjectGson {
    fun getGenericMap(objectData : String): HashMap<String,Any>? { // this should return the registeration model
        val jso = objectData
        val gson = Gson()
        val typeToken = object : TypeToken<HashMap<String,Any>?>() {}.type
        val obj = gson.fromJson<HashMap<String,Any>?>(jso, typeToken) ?: null
        return obj

    }



}