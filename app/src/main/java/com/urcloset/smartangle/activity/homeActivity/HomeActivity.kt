package com.urcloset.smartangle.activity.homeActivity


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.productDetails.ProductDetails
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.databinding.ActivityHomeBinding
import com.urcloset.smartangle.fragment.HomeFragment.HomeFragment
import com.urcloset.smartangle.fragment.bookmark_fragment.BookMarkFragment
import com.urcloset.smartangle.fragment.myselleraccount.MySellerAccount
import com.urcloset.smartangle.fragment.postsFragment.PostsFragment
import com.urcloset.smartangle.fragment.setting_fragment.SettingFragment


import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.BasicTools.openReviewGoogle
import com.urcloset.smartangle.tools.TemplateActivity
import dagger.hilt.android.AndroidEntryPoint

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import kotlin.collections.HashMap

@AndroidEntryPoint
class HomeActivity : TemplateActivity() {

    var disposable= CompositeDisposable()
    companion object{
        var bottomNavigation: BottomNavigationView?=null
        var doNothing: Boolean=false


    }
    var binding : ActivityHomeBinding? =null
    override fun set_layout() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

    }
    val viewModel : HomeViewModel by viewModels()

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



    val postsFragment = PostsFragment()
    val homeFragment  = HomeFragment()
    val bookMark = BookMarkFragment()
    val sellerAccount = MySellerAccount()
    override fun init_views() {

    }

    override fun init_events() {

        initBottomNavigation()
       // val postsFragment = postsFragment//PostsFragment()
        val bundle  = bundleOf("show_review" to intent.getBooleanExtra("show_review",false))
        postsFragment.arguments = bundle
        show_fragment2(postsFragment, false, false, R.id.root_fragment_home)

      /*  bottomNavigation!!.setOnTabSelectedListener { position, wasSelected ->
            // Do something cool here...
            when(position){
                0 -> if (!wasSelected) {

                    if(!doNothing) {
                        show_fragment2(postsFragment, false, false,R.id.root_fragment_home)
                    }



                }
                1->if(!wasSelected){
                    if(!doNothing) {
                       // val f = HomeFragment()

                        show_fragment2(homeFragment, false, false, R.id.root_fragment_home)
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
                            //bottomNavigation!!.currentItem = 3
                            val f =bookMark
                            show_fragment2(f, false, false, R.id.root_fragment_home)
                        }
                    }
                    else {
                        Toast.makeText(applicationContext, getString(R.string.you_must_log_in), Toast.LENGTH_SHORT).show()


                    }

                }


               4->if(!wasSelected) {

                   // var f= UserInfoFragment()
                    val f= sellerAccount//MySellerAccount()
                if (TemplateActivity.loginResponse?.data?.accessToken != null) {
                    if(!doNothing) {

                        show_fragment2(f, false, false, R.id.root_fragment_home)
                    }
                }
                else {
                    if(!doNothing) {
                        val settingFragment = SettingFragment()
                        show_fragment2(settingFragment, false, false, R.id.root_fragment_home)
                    }
                }

                }

            }

            true
        }
        bottomNavigation!!.currentItem = 0*/
        val navListener =
            bottomNavigation!!.setOnNavigationItemSelectedListener { menuItem ->
                //           previousFragment = selectedFragment // previous to be equal to selected
                // if (isDoubleSelection(menuItem.itemId)){
                /*    val f = supportFragmentManager.findFragmentById(R.id.fragment_container_navigation)
                        if (f == selectedFragment) // do something with f
                            return@OnNavigationItemSelectedListener true*/

                // }
                bottomNavigation?.menu?.setGroupCheckable(0, true, true)

                when (menuItem.itemId) {
                    R.id.posts -> {

                            if (!doNothing) {
                                show_fragment2(postsFragment, false, false, R.id.root_fragment_home)
                            }

                    }
                    R.id.users -> {
                        if(!doNothing) {
                            // val f = HomeFragment()

                            show_fragment2(homeFragment, false, false, R.id.root_fragment_home)
                        }

                    }
                    R.id.adding -> {
                        if (TemplateActivity.loginResponse?.data?.accessToken != null) {
                            // will check if the user agree or not
                            BasicTools.openActivity(
                                this@HomeActivity,
                                com.urcloset.smartangle.activity.addProductActivity.AddProductActivity::class.java,
                                false
                            )
                        } else {
                            Toast.makeText(applicationContext, getString(R.string.you_must_log_in), Toast.LENGTH_SHORT).show()
                        }

                    }
                    R.id.bookmark -> {
                        if (TemplateActivity.loginResponse?.data?.accessToken != null) {
                            if(!doNothing) {
                                //bottomNavigation!!.currentItem = 3
                                val f =bookMark
                                show_fragment2(f, false, false, R.id.root_fragment_home)
                            }
                        }
                        else {
                            Toast.makeText(applicationContext, getString(R.string.you_must_log_in), Toast.LENGTH_SHORT).show()


                        }

                    }
                    R.id.setting -> {
                        if (TemplateActivity.loginResponse?.data?.accessToken != null) {
                            if(!doNothing) {

                                show_fragment2(MySellerAccount(), false, false, R.id.root_fragment_home)
                            }
                        }
                        else {
                            if(!doNothing) {
                                val settingFragment = SettingFragment()
                                show_fragment2(settingFragment, false, false, R.id.root_fragment_home)
                            }
                        }

                    }
                }


                true
            }

        viewModel.loadPreviousNavBottom.observe(this, Observer<Int> { updatedId ->
            if (updatedId != null) {
                // this is the id
                //bottomNavigationView.
             /*   if (updatedId == R.id.users)
                    bottomNavigation!!.menu?.findItem(R.id.users)?.setChecked(true)
                else  if (updatedId == R.id.home)
                    bottomNavigation!!.menu?.findItem(R.id.home)?.setChecked(true)*/
                //bottomNavigation!!.selectedItemId = updatedId
               bottomNavigation!!.menu.findItem(updatedId)?.setChecked(true)
                //  setCurrentCheckedItemIfAvaliable(updatedId,true) // when observe so we only need the selectdd not all action so send filter
                viewModel.setPreviousNavBottom(null)
            }

        })

    }

    override fun set_fragment_place() {

        this.fragment_place=binding!!.rootFragmentHome

    }




    fun initBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigation?.setItemIconTintList(null)
        val pixels = 5 * resources.displayMetrics.density
        if (Build.VERSION.SDK_INT >= 21)
            bottomNavigation!!.setElevation(pixels)

/*
            val item1 =AHBottomNavigationItem(resources.getString(R.string.home), R.drawable.ic_discover)
        val item2 = AHBottomNavigationItem(getString(R.string.account), R.drawable.ic_nearby_users)
        val item3 = AHBottomNavigationItem(resources.getString(R.string.add_product),
            R.drawable.ic_add__item_icon)
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

     //   bottomNavigation!!.currentItem = 1
       // bottomNavigation!!.setBehaviorTranslationEnabled(false)
        bottomNavigation!!.setColored(false)


        // Manage titles
        bottomNavigation!!.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE)
        // bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        //bottomNavigation!!.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);


        bottomNavigation!!.setDefaultBackgroundColor(fetchColor(R.color.white))
        bottomNavigation!!.setAccentColor(fetchColor(R.color.login_btn))
        bottomNavigation!!.setInactiveColor(fetchColor(R.color.black))




        //bottomNavigation!!.isColored = true
*/


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
