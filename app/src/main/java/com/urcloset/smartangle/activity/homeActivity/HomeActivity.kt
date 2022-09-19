package com.urcloset.smartangle.activity.homeActivity


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.demo105.AddProductActivity
import com.urcloset.smartangle.activity.productDetails.ProductDetails
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.fragment.HomeFragment.HomeFragment
import com.urcloset.smartangle.fragment.UserInfoFragment
import com.urcloset.smartangle.fragment.bookmark_fragment.BookMarkFragment
import com.urcloset.smartangle.fragment.myselleraccount.MySellerAccount
import com.urcloset.smartangle.fragment.postsFragment.PostsFragment
import com.urcloset.smartangle.fragment.setting_fragment.SettingFragment
import com.urcloset.smartangle.model.UserProfileModel


import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import droidninja.filepicker.FilePickerConst

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.ResponseBody
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.HashMap


class HomeActivity : TemplateActivity() {

    var disposable= CompositeDisposable()
    companion object{
        var bottomNavigation: AHBottomNavigation?=null
        var doNothing: Boolean=false


    }
    override fun set_layout() {
        setContentView(R.layout.activity_home)
    }

    override fun init_activity(savedInstanceState: Bundle?) {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    val data = deepLink?.getQueryParameter("id")
                    val intent = Intent(this, ProductDetails::class.java)
                    Log.d("Share product", data.toString())
                    intent.putExtra("id", data)
                    startActivity(intent)

                }



            }
            .addOnFailureListener(this) { e -> Log.w("", "getDynamicLink:onFailure", e) }

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener<String> { task ->
                if(task.isSuccessful) {
                    if (!BasicTools.getToken(this).isEmpty()) {
                        Log.d("fcm-save", "init_activity: ")

                        saveToken(task.result)
                    }
                }
                else {
                    Log.d("fcm-fail", "init_activity: ")
                }

                // send it to server
            })
    }
    fun saveToken(token: String?) {

        val disposable= CompositeDisposable()
        if (BasicTools.isConnected(this)) {

            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(this),
                BasicTools.getProtocol(this).toString())
                ?.create(AppApi::class.java)




            val map=HashMap<String,String>()
            map.put("token","$token")

            val observable= shopApi!!.saveDevicesToken(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(this) {
                        override fun onSuccess(result: ResponseBody) {
                            Log.d("FCM Update", "onSuccess: ")

                        }
                        override fun onFailed(status: Int) {
                            Log.d("FCM Update failure", "onSuccess: ")

                        }
                    }))

        }

    }

    override fun init_views() {

    }

    override fun init_events() {

        initBottomNavigation()
        val postsFragment = PostsFragment()

        show_fragment2(postsFragment, false, false)
        bottomNavigation!!.setOnTabSelectedListener { position, wasSelected ->
            // Do something cool here...
            when(position.toInt()){
                0 -> if (!wasSelected) {

                    if(!doNothing) {

                        val postsFragment = PostsFragment()

                        show_fragment2(postsFragment, false, false)
                    }



                }
                1->if(!wasSelected){
                    if(!doNothing) {
                        val f = HomeFragment()

                        show_fragment2(f, false, false)
                    }
                }


                2 -> if (!wasSelected) {
                    if (TemplateActivity.loginResponse?.data?.accessToken != null) {


                        BasicTools.openActivity(
                            this@HomeActivity,
                            com.urcloset.smartangle.activity.addProductActivity.AddProductActivity::class.java,
                            false
                        )
                    } else {
                        Toast.makeText(applicationContext, getString(R.string.you_must_log_in), Toast.LENGTH_SHORT).show()
                    }

                }
                3->if(!wasSelected) {
                    if (TemplateActivity.loginResponse?.data?.accessToken != null) {

                        if(!doNothing) {

                            val f = BookMarkFragment()
                            show_fragment2(f, false, false)
                        }
                    }
                    else {
                        Toast.makeText(applicationContext, getString(R.string.you_must_log_in), Toast.LENGTH_SHORT).show()


                    }

                }


               4->if(!wasSelected) {



                   // var f= UserInfoFragment()
                    val f= MySellerAccount()
                if (TemplateActivity.loginResponse?.data?.accessToken != null) {
                    if(!doNothing) {


                        show_fragment2(f, false, false)
                    }
                }
                else {
                    if(!doNothing) {
                        val settingFragment = SettingFragment()
                        show_fragment2(settingFragment, false, false)
                    }
                }

                }

            }

            true
        }

    }

    override fun set_fragment_place() {

        this.fragment_place=root_fragment_home

    }




    fun initBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation) as AHBottomNavigation
        val pixels = 5 * resources.displayMetrics.density
        if (Build.VERSION.SDK_INT >= 21)
            bottomNavigation!!.setElevation(pixels)


        val item1 =AHBottomNavigationItem(resources.getString(R.string.home), R.drawable.ic_discover)
        val item2 = AHBottomNavigationItem(getString(R.string.account), R.drawable.ic_nearby_users)
        val item3 = AHBottomNavigationItem(resources.getString(R.string.add_product), R.drawable.ic_add__item_icon)
        val item4 = AHBottomNavigationItem(resources.getString(R.string.message), R.drawable.bookmark_ic_bn)
        val item5 = AHBottomNavigationItem(resources.getString(R.string.account), R.drawable.person_ic_bn)



        bottomNavigation!!.addItem(item1)
        bottomNavigation!!.addItem(item2)
        bottomNavigation!!.addItem(item3)
        bottomNavigation!!.addItem(item4)
        bottomNavigation!!.addItem(item5)



        /*if(BasicTools.getToken(this@HomeActivity).isEmpty())
            bottomNavigation!!.disableItemAtPosition(1)
        else  bottomNavigation!!.enableItemAtPosition(1)*/

        //bottomNavigation.disableItemAtPosition(4);

        bottomNavigation!!.setCurrentItem(0);
        bottomNavigation!!.setBehaviorTranslationEnabled(false)

        // Manage titles
        bottomNavigation!!.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE)
        // bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        //bottomNavigation!!.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);


        bottomNavigation!!.setDefaultBackgroundColor(fetchColor(R.color.white))
        bottomNavigation!!.setAccentColor(fetchColor(R.color.login_btn))
        bottomNavigation!!.setInactiveColor(fetchColor(R.color.black))


        bottomNavigation!!.currentItem = 0



        //bottomNavigation!!.isColored = true



        /* notification = AHNotification.Builder()
             .setText("")
             .setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.main_orange))
             .setTextColor(ContextCompat.getColor(this@MainActivity, R.color.tabs_white))
             .build()
         bottomNavigation.setNotification(notification, 3)*/

    }


    fun saveTokenRQ(token: String?) {
        if (BasicTools.isConnected(this@HomeActivity)) {
         
            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(this@HomeActivity),
                BasicTools.getProtocol(this@HomeActivity).toString())
                ?.create(AppApi::class.java)

            val id: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)



            var map=HashMap<String,String>()
            map.put("device_token","$token")
            map.put("device_id", id.toString())
            val observable= shopApi!!.saveDevicesToken(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(this@HomeActivity) {
                        override fun onSuccess(result: ResponseBody) {

                        }
                        override fun onFailed(status: Int) {
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {


        }
    }





}
