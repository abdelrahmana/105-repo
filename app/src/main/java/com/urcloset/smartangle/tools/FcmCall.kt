package com.urcloset.smartangle.tools

import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import com.urcloset.smartangle.tools.Constants.api_url
import com.urcloset.smartangle.tools.Constants.api_url_image


object FcmCall {
    const val BASEURL = "base_url"
    const val BASEURLIMAGE = "base_url_image"
    fun setFirebaseData(referenceConnection: FirebaseDatabase) {
        referenceConnection.getReference("mobileurl").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val genericTypeIndicator: GenericTypeIndicator<Map<String?, Any?>?> =
                    object : GenericTypeIndicator<Map<String?, Any?>?>() {}
                val info: Map<String?, Any?>? = snapshot.getValue(genericTypeIndicator)
                //val info : Map<*,*>? = snapshot.getValue(Map::class.java) as Map<*, *>
              //  if (info?.get(MAINTENANCEMODE)?:false==true) // server in maintenacne
                api_url =   info?.get((BASEURL)?:"") as String
                api_url_image =   info.get((BASEURLIMAGE)?:"") as String

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}