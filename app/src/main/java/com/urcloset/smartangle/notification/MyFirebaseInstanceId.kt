package com.urcloset.smartangle.notification

import android.content.Intent
import android.util.Log

import com.google.firebase.messaging.FirebaseMessagingService


class MyFirebaseInstanceId: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        Log.i("ghgh","TOKEN_S= $p0")

        super.onNewToken(p0)

       // val firebaseUser= FirebaseAuth.getInstance().currentUser
      //  val referchToken=FirebaseInstanceId.getInstance().token
        val referchToken=p0





  /*      if(firebaseUser!=null){
            //updateToken(referchToken)
        }*/


    }

    private fun updateToken(referchToken: String?) {
      //  val firebaseUser= FirebaseAuth.getInstance().currentUser
      //  var ref=FirebaseDatabase.getInstance().reference.child("Tokens")
      //  val token=TokenModel(referchToken!!)
     //   ref.child(firebaseUser!!.uid).setValue(token)

    }
}