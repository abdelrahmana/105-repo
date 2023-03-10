package com.urcloset.smartangle.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.window.SplashScreen
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools


import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.SplashActivity

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
          saveTokenRQ(referchToken,this)

        }


       /* if(firebaseUser!=null){
            updateToken(referchToken)
        }*/
    }






    override fun onMessageReceived(p0: RemoteMessage) {
        Log.d("hjh", "onMessageReceived: "+p0.data)
        super.onMessageReceived(p0)
        convertDataToNotification(p0,0)


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
  private fun convertDataToNotification(remoteMessage: RemoteMessage, notificationType: Int) {
      var intent: Intent? = null

          intent = Intent(this, SplashActivity::class.java)

      /*     if (notificationType == 1) {
          intent.putExtra("direction", "notifications");
      } else if (notificationType == 2) {
          intent.putExtra("direction", "messages");
      }*/
      //intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      val currentBadgeNumber = 1 //PrefUtils.getCurrentBadgeNumber(getApplicationContext());
      intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
      val pendingIntent = PendingIntent.getActivity(this, System.currentTimeMillis().toInt(), intent,
          if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_UPDATE_CURRENT)
      val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
      // if Android Version above 8 then we need to create a chanel in the system.
      makeChanel()
      val builder = NotificationCompat.Builder(this, default_notification_channel_id)
      // int badgeCount = 1;
    //  ShortcutBadger.applyCount(this, currentBadgeNumber + 1) //for 1.1.4+
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //    builder.setSmallIcon(R.drawable.test_nn);
          //   builder.setColor(getResources().getColor(R.color.app_color));
          builder.setSmallIcon(R.drawable.app_logo)
          builder.setContentTitle(remoteMessage.notification!!.title)
          builder.setContentText(remoteMessage.notification!!.body)
          builder.setSound(soundUri)
          builder.setAutoCancel(true)
          builder.setContentIntent(pendingIntent).priority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) NotificationManager.IMPORTANCE_HIGH else Notification.PRIORITY_MAX
          builder.setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.notification!!.body))
          builder.priority = NotificationCompat.PRIORITY_HIGH
          val notificationManagerCompat = NotificationManagerCompat.from(this)
          //            notificationManagerCompat.notify(/*Notification_base_id + 1*/(int) System.currentTimeMillis(), builder.build());
          notificationManagerCompat.notify( /*Notification_base_id + 1*/System.currentTimeMillis().toInt(), builder.build())
      } else { // notification.setSmallIcon(R.drawable.icon);
          builder.setSmallIcon(R.drawable.app_logo)
          builder.setContentTitle(remoteMessage.notification!!.title)
          builder.setContentText(remoteMessage.notification!!.body)
          builder.setSound(soundUri)
          builder.setAutoCancel(true)
          builder.setContentIntent(pendingIntent).priority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) NotificationManager.IMPORTANCE_HIGH else Notification.PRIORITY_MAX
          builder.setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.notification!!.body))
          builder.priority = NotificationCompat.PRIORITY_HIGH
          val notificationManagerCompat = NotificationManagerCompat.from(this)
          notificationManagerCompat.notify(System.currentTimeMillis().toInt(), builder.build())
      }
  }

    fun makeChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = channel_name
            val description = channel_description
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(default_notification_channel_id, name, importance)
            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.setShowBadge(true)
            channel.vibrationPattern = null
            channel.enableVibration(false)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


    companion object {
        val default_notification_channel_id = "100"
        val channel_name = "105"
        val channel_description = "description"
        fun saveTokenRQ(token: String?,context: Context) {

            val disposable= CompositeDisposable()
            if (BasicTools.isConnected(context)) {

                val shopApi = ApiClient.getClientJwt(
                    BasicTools.getToken(context),
                    BasicTools.getProtocol(context).toString())
                    ?.create(AppApi::class.java)




                val map=HashMap<String,String>()
                map.put("token","$token")

                val observable= shopApi!!.saveDevicesToken(map)
                disposable.clear()
                disposable.add(
                    observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : AppObservable<ResponseBody>(context) {
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

}