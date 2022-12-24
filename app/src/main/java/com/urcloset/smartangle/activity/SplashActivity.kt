package com.urcloset.smartangle.activity


import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.activity.loginActivity.LoginAcitivty

import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.project_105.LoginResponseModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.Constants
import com.urcloset.smartangle.tools.TemplateActivity
import com.urcloset.smartangle.tools.Constants.LOGIN_TYPE_EMAIL
import com.urcloset.smartangle.tools.Constants.LOGIN_TYPE_PHONE
import com.urcloset.smartangle.tools.FcmCall.setFirebaseData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : TemplateActivity() {
    var disposable: CompositeDisposable = CompositeDisposable()
    private var anim: Animation? = null
    var counterDown:CountDownTimer?=null
    override fun set_layout() {
        setContentView(R.layout.activity_splash)
    }

    override fun init_activity(savedInstanceState: Bundle?) {


    }


    override fun onStop() {
        counterDown?.cancel()
        super.onStop()
    }

    override fun init_views() {
        setFirebaseData(FirebaseDatabase.getInstance())
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        BasicTools.initialize_image_loader(this@SplashActivity)
        iv_splash.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo1))
        anim = AnimationUtils.loadAnimation(this@SplashActivity, android.R.anim.fade_in)

        anim!!.setDuration(2000)
        iv_splash.animation= anim


        counterDown=object : CountDownTimer(3000,1000){
            override fun onFinish() {

                var i=Intent(this@SplashActivity,LoginAcitivty::class.java)
                startActivity(i)
                finish()

            }

            override fun onTick(p0: Long) {



            }

        }

        if(BasicTools.getToken(this@SplashActivity).isEmpty())
            counterDown!!.start()
        else {
            //TODO ADD FACEBOOK AND GOOGLE

            if(BasicTools.getLoginType(this@SplashActivity).equals(Constants.LOGIN_TYPE_GMAIL)){

                loginRQ(Constants.LOGIN_TYPE_GMAIL,BasicTools.getUserName(this@SplashActivity),
                    BasicTools.getSocialProviderID(this@SplashActivity))
            }else if(BasicTools.getLoginType(this@SplashActivity).equals(Constants.LOGIN_TYPE_FACEBOOK)){
                loginRQ(Constants.LOGIN_TYPE_FACEBOOK,BasicTools.getUserName(this@SplashActivity),
                    BasicTools.getSocialProviderID(this@SplashActivity))
            }

            else{
                loginRQ()
            }

        }

    }

    override fun init_events() {




    }

    override fun set_fragment_place() {

    }

    fun loginRQ(){
        if (BasicTools.isConnected(this@SplashActivity)) {






            var map = HashMap<String, String>()

            if(BasicTools.getLoginType(this@SplashActivity).equals(LOGIN_TYPE_EMAIL)){
                map.put("email",BasicTools.getUserName(this@SplashActivity))
                map.put("type_login","1")
            }else if(BasicTools.getLoginType(this@SplashActivity).equals(LOGIN_TYPE_PHONE)){
                map.put("phone_number",BasicTools.getPhoneUser(this@SplashActivity))
                map.put("country_code",BasicTools.getUserCountryCode(this@SplashActivity))
                map.put("type_login","2")
            }
            map.put("password",BasicTools.getPassword(this@SplashActivity))



            val shopApi = ApiClient.getClient(BasicTools.getProtocol(this@SplashActivity).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.loginNormal(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<LoginResponseModel>(this@SplashActivity) {
                        override fun onSuccess(result: LoginResponseModel) {



                            if(result.status!!) {
                                TemplateActivity.loginResponse = result
                                BasicTools.setIsSocial(this@SplashActivity, false)
                                /*   BasicTools.setLoginType(
                                       this@SplashActivity,
                                       Constants.LOGIN_TYPE_NORMAL
                                   )*/
                                BasicTools.setToken(
                                    this@SplashActivity,
                                    result.data?.accessToken!!.toString()
                                )
                                BasicTools.setAgreementsTerms(
                                    this@SplashActivity,
                                    result.data?.user?.is_agree?:false
                                )
                                BasicTools.setUserName(
                                    this@SplashActivity,
                                    result.data?.user?.email!!.toString()
                                )
                                BasicTools.setUserModel(
                                    this@SplashActivity,
                                    result.data
                                )

                                if(!result.data?.user?.phoneNumber.isNullOrEmpty())
                                    BasicTools.setPhoneUser(this@SplashActivity,result.data?.user?.phoneNumber!!.toString())

                                else   BasicTools.setPhoneUser(this@SplashActivity,"")
                                if(!result.data?.user?.countryCode.isNullOrEmpty())
                                    BasicTools.setUserCountryCode(this@SplashActivity,
                                        result.data?.user?.countryCode!!.toString())

                                else   BasicTools.setUserCountryCode(this@SplashActivity,"")

                                //  BasicTools.setPassword(this@SplashActivity)
                                BasicTools.openActivity(
                                    this@SplashActivity,
                                    HomeActivity::class.java, true
                                )
                            }




                            else{

                                BasicTools.logOut(this@SplashActivity)
                                showToastMessage(R.string.faild_to_login)
                                BasicTools.openActivity(this@SplashActivity,
                                    LoginAcitivty::class.java,true)
                            }


                        }
                        override fun onFailed(status: Int) {

                            //  showShimmerLoginBtn(false)
                            BasicTools.logOut(this@SplashActivity)
                            showToastMessage(R.string.faild_to_login)
                            BasicTools.openActivity(this@SplashActivity,
                                LoginAcitivty::class.java,true)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {

            showToastMessage(R.string.no_connection)
            BasicTools.logOut(this@SplashActivity)
            BasicTools.openActivity(this@SplashActivity,
                LoginAcitivty::class.java,true)


        }
    }

    fun loginRQ(loginType:String,email:String,providerID:String){
        if (BasicTools.isConnected(this@SplashActivity)) {





            var map = HashMap<String, String>()
            map.put("social_token",providerID)
             map.put("social_type",loginType)
            map.put("email",email)


            val shopApi = ApiClient.getClient(BasicTools.getProtocol(this@SplashActivity).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.loginSoical(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<LoginResponseModel>(this@SplashActivity) {
                        override fun onSuccess(result: LoginResponseModel) {


                            if(result.status!!) {
                                TemplateActivity.loginResponse = result
                                Log.i("TEST_TEST","ssssss")
                                BasicTools.setIsSocial(this@SplashActivity, true)
                                BasicTools.setSocialProviderID(this@SplashActivity,providerID)
                                BasicTools.setLoginType(
                                    this@SplashActivity,
                                    loginType
                                )
                                BasicTools.setToken(
                                    this@SplashActivity,
                                    result.data?.accessToken!!.toString()
                                )
                                BasicTools.setAgreementsTerms(
                                    this@SplashActivity,
                                    result.data?.user?.is_agree?:false
                                )
                                BasicTools.setUserName(
                                    this@SplashActivity,
                                    result.data?.user?.email!!.toString()
                                )
                                BasicTools.setUserModel(
                                    this@SplashActivity,
                                    result.data
                                )

                                //  BasicTools.setPassword(this@SplashActivity)
                                BasicTools.openActivity(
                                    this@SplashActivity,
                                    HomeActivity::class.java, true
                                )
                            }




                            else{

                                BasicTools.logOut(this@SplashActivity)
                                showToastMessage(R.string.faild_to_login)
                                BasicTools.openActivity(this@SplashActivity,
                                    LoginAcitivty::class.java,true)
                            }


                        }
                        override fun onFailed(status: Int) {


                            BasicTools.logOut(this@SplashActivity)
                            showToastMessage(R.string.faild_to_login)
                            BasicTools.openActivity(this@SplashActivity,
                                LoginAcitivty::class.java,true)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            BasicTools.openActivity(this@SplashActivity,
                LoginAcitivty::class.java,true)
            showToastMessage(R.string.no_connection)}
    }


}
