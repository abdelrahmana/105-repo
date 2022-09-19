package com.urcloset.smartangle.notification

import android.util.Log
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools


import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class MyFirebaseMessaging : FirebaseMessagingService() {


    override fun onNewToken(p0: String) {
        Log.i("dfdfd","xcxc= $p0")

        super.onNewToken(p0)



        val firebaseUser= FirebaseAuth.getInstance().currentUser
        //  val referchToken=FirebaseInstanceId.getInstance().token
        val referchToken=p0

        Log.i("TEST_TEST","TOKEN S= $referchToken")


        if(!BasicTools.getToken(this).isEmpty()){
          saveTokenRQ(referchToken)

        }


       /* if(firebaseUser!=null){
            updateToken(referchToken)
        }*/
    }






    override fun onMessageReceived(p0: RemoteMessage) {
        Log.d("hjh", "onMessageReceived: "+p0.data)
        super.onMessageReceived(p0)


/*
        val sented=p0.data["sented"]

        val user=p0.data["user"]

        val sharePref=getSharedPreferences("PRFES", Context.MODE_PRIVATE)

        val currentUserOnline=sharePref.getString("currentUser","none")

        val firebaseUser=FirebaseAuth.getInstance().currentUser

        if(firebaseUser!=null)
        Log.i("TEST_TEST","1")
        else   Log.i("TEST_TEST","2")

        if(sented.equals(firebaseUser!!.uid))
            Log.i("TEST_TEST","3")
        else   Log.i("TEST_TEST","4$sented ${firebaseUser.uid}")
        if(firebaseUser!=null&&sented.equals(firebaseUser!!.uid)){

            if(currentUserOnline!=user){

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

                    sendOreoNotification(p0)
                }else{

                    sendNotification(p0)
                }
            }
        }*/
    }

  /*  private fun sendNotification(remoteMessage: RemoteMessage) {
        val user=remoteMessage.data["user"]
        val icon=remoteMessage.data["icon"]
        val title=remoteMessage.data["title"]
        val body=remoteMessage.data["body"]
        val receiverImage=remoteMessage.data["receiverImage"]
        val receiverID=remoteMessage.data["sentedID"]
        val receiverName=remoteMessage.data["receiverName"]




        val notification=remoteMessage.notification

        val j=user!!.replace("[\\D]".toRegex(),"").toInt()

        val intent= Intent(this,ChatPageActivity::class.java)

        BasicTools.setChatName(this,receiverName!!)
        BasicTools.setChatImage(this,receiverImage!!)
        BasicTools.setChatID(this,receiverID!!)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT)

        val defaultSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


        var builder:NotificationCompat.Builder=NotificationCompat.Builder(this)
            .setSmallIcon(icon!!.toInt())
            . setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)

        val noti=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        var i=0
        if(j>0){
            i=j
        }
       noti.notify(i,builder.build())


    }

    private fun sendOreoNotification(remoteMessage:RemoteMessage) {

        val user=remoteMessage.data["user"]
        val icon=remoteMessage.data["icon"]
        val title=remoteMessage.data["title"]
        val body=remoteMessage.data["body"]


        Log.i("TEST_TEST","$user")
        Log.i("TEST_TEST","$icon")
        Log.i("TEST_TEST","$title")
        Log.i("TEST_TEST","$body")
        val notification=remoteMessage.notification

        val j=user!!.replace("[\\D]".toRegex(),"").toInt()

        val intent= Intent(this,ChatPageActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT)

        val defaultSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val oreoNotification= OreoNotification(this)

        val builder:Notification.Builder=oreoNotification.getOreoNotification(title,body,pendingIntent,defaultSound,icon)

        var i=0
        if(j>0){
            i=j
        }
        oreoNotification.getManager!!.notify(i,builder.build())


    }*/

    fun saveTokenRQ(token: String?) {

        val disposable= CompositeDisposable()
        if (BasicTools.isConnected(this)) {

            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(this),
                BasicTools.getProtocol(this).toString())
                ?.create(AppApi::class.java)




            var map=HashMap<String,String>()
            map.put("token","$token")

            val observable= shopApi!!.saveDevicesToken(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(this) {
                        override fun onSuccess(result: ResponseBody) {

                        }
                        override fun onFailed(status: Int) {

                        }
                    }))

        }
        else {


        }
    }
}