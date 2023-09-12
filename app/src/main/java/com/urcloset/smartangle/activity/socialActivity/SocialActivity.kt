package com.urcloset.smartangle.activity.socialActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.databinding.ActivitySocialBinding
import com.urcloset.smartangle.model.LinkAccountModel
import com.urcloset.smartangle.model.PersonalUserInfoModel
import com.urcloset.smartangle.model.UnLinkAccountModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception


class SocialActivity : TemplateActivity() {
    lateinit var tvFacebook: TextView
    lateinit var tvInstagram: TextView
    lateinit var tvWhatsApp: TextView
    lateinit var tvTwitter: TextView
    lateinit var faceState:TextView
    lateinit var whatstate:TextView
    lateinit var twitterState:TextView
    lateinit var instaState:TextView
    lateinit var backIcon:ImageView
    lateinit var progress:ProgressBar
    var faceLink:String? = null
    var instaLink:String? = null
    var whatsLink:String? = null
    var twitterLink:String? = null

    var disposable = CompositeDisposable()
    var binding : ActivitySocialBinding?=null
    override fun set_layout() {
        binding = ActivitySocialBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


    }

    override fun init_activity(savedInstanceState: Bundle?) {
        progress.visibility = View.VISIBLE
        val shopApi =
            ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(applicationContext).toString(), "en"
            )
                ?.create(
                    AppApi::class.java
                )

        val observable = shopApi!!.getPersonalInformation()
        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<PersonalUserInfoModel>(this) {
                    override fun onSuccess(result: PersonalUserInfoModel) {
                        if (result.status!!) {
                            progress.visibility = View.GONE

                            val socialLinks = result.data!!.user!!.socialLinks
                            socialLinks!!.forEach {
                                if(it!!.socailName.equals("facebook")){

                                    faceLink = it.linkAccount
                                    if(faceLink!="not_set")
                                    findViewById<LinearLayout>(R.id.face).visibility = View.VISIBLE

//                                    if(it.linkAccount.toString()!="not_set")
//                                        setSocialState(faceState,true)

                                }
                                if(it!!.socailName.equals("whatsapp")){
                                    whatsLink = it.linkAccount
                                    if(whatsLink!="not_set")

                                    findViewById<LinearLayout>(R.id.whats).visibility = View.VISIBLE



                                }
                                if(it!!.socailName.equals("twitter")){
                                    twitterLink = it.linkAccount
                                    if(twitterLink!="not_set")
                                    findViewById<LinearLayout>(R.id.twitter).visibility = View.VISIBLE

                                }
                                if(it!!.socailName.equals("instgram")){
                                    instaLink = it.linkAccount
                                    if(instaLink!="not_set")

                                    findViewById<LinearLayout>(R.id.insta).visibility = View.VISIBLE

//                                    if(it.linkAccount.toString()!="not_set"){
//                                        setSocialState(instaState,true)
//                                    }
//                                    else{
//                                        setSocialState(instaState,false)
//
//                                    }
                                }
//                                if(faceLink=="not_set"){
//                                    findViewById<LinearLayout>(R.id.face).visibility = View.GONE
//                                    findViewById<TextView>(R.id.face_sate).visibility = View.GONE
//
//                                }



                            }
                            if(faceLink =="not_set" && instaLink =="not_set" && twitterLink =="not_set" && whatsLink =="not_set"){
                                findViewById<LinearLayout>(R.id.ly_empty).visibility = View.VISIBLE

                            }




                        } else {
                        }

                    }

                    override fun onFailed(status: Int) {
                        progress.visibility = View.GONE
                        showToastMessage(R.string.faild)

                        super.onFailed(status)
                    }
                }
                ))
    }
    fun setSocialState(tv:TextView,active:Boolean){
        if(BasicTools.isDeviceLanEn()) {

            if (!active) {
                tv.text = "link now"
                tv.setTextColor(resources.getColor(R.color.colorPrimary))

            }
            else tv.text = "linked"


        } else {
            if (active)
                tv.text = "متصل"
            else {
                tv.text = "اتصل الآن"
                tv.setTextColor(resources.getColor(R.color.colorPrimary))

            }



            }


    }


    override fun init_views() {
        tvFacebook =binding!!.tvFacebook
        tvWhatsApp =binding!!.tvWhatsapp
        tvTwitter =binding!!.tvTwitter
        tvInstagram =binding!!.tvInstagram
        faceState =binding!!.faceSate
        whatstate =binding!!.whatsSate
        twitterState =binding!!.twitterSate
        instaState =binding!!.instaState
        backIcon = findViewById(R.id.iv_back)
        progress = findViewById(R.id.progress_social)
    }
    override fun init_events() {
        backIcon.setOnClickListener {

            BasicTools.exitActivity(this)
        }
        tvFacebook.setOnClickListener {
            if(faceLink!=null){
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(faceLink))
                    startActivity(intent)
                }
                catch (e:Exception){

                }
            }
        }
//        tvWhatsApp.setOnClickListener {
//            if(whatsLink!=null){
//                try {
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsLink))
//                    startActivity(intent)
//                }
//                catch (e:Exception){
//
//                }
//            }
//
//        }
//        tvTwitter.setOnClickListener {
//            if(twitterLink!=null){
//                try {
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(twitterLink))
//                    startActivity(intent)
//                }
//                catch (e:Exception){
//
//                }
//
//            }
//        }
//        tvInstagram.setOnClickListener {
//            try {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instaLink))
//                startActivity(intent)
//            }
//            catch (e:Exception){
//
//            }
//        }
        faceState.setOnClickListener {
            try {
                BasicTools.open_website(faceState!!.toString(), this@SocialActivity)
            }
            catch (e:Exception){
                showToastMessage(R.string.faild)
            }


        }
        instaState.setOnClickListener {
            try {
                BasicTools.open_website(instaLink!!.toString(), this@SocialActivity)
            }
            catch (e:Exception){
                showToastMessage(R.string.faild)

            }


        }
        whatstate.setOnClickListener {



            BasicTools.open_website(whatsLink!!.toString(),this@SocialActivity)

        }
        twitterState.setOnClickListener {
            try {
                BasicTools.open_website(twitterLink!!.toString(), this@SocialActivity)
            }
            catch (e:Exception){
                showToastMessage(R.string.faild)

            }



        }
        backIcon.setOnClickListener {
            finish()
        }
    }

    override fun set_fragment_place() {
    }
    fun linkAccount(accountId:String,linkAccount:String,tvSocial:TextView){
        val shopApi =
            ApiClient.getClientJwt(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIzIiwianRpIjoiZDdiNGRmYTAzNGMwYTljN2RmZWFiMDFiMjVlMTA0NmFhMjQ5NjU0OGI5ZWQwNjYwODUxMzkzNmU5OGM3MmRlZDhiYWJhYmY5NjY4Y2Y3ZmEiLCJpYXQiOjE2MzA5MjA3MzEuNTg1NzY4OTM4MDY0NTc1MTk1MzEyNSwibmJmIjoxNjMwOTIwNzMxLjU4NTc3NzA0NDI5NjI2NDY0ODQzNzUsImV4cCI6MTY2MjQ1NjczMS40NDk5MzgwNTg4NTMxNDk0MTQwNjI1LCJzdWIiOiI0Iiwic2NvcGVzIjpbXX0.GbM8R7Utsqf1E5tM2BvkJB1JtcX2-iH4n0zmofsc1Jbhcye2-VuSrwO-5rJSjgQ-hRU9ZWBQu1fgCKKG1vNVWUwG7OH9LPkW2OkcSAJOyEF4WFYpqhb_j_T3KKPMagmB40ZaGBG8NkTx-dsclMXl_J3-FoeN4P5PuHi_oh6vnNmUhqXKy4sgEJDc-OP9U-5blDbj4wPgxDj_y0r7NTuKHcQdt1ABHUFjwODWV-D_Vgl_L7soZ7A0XB1SH9l9Jxq8EAbYcwg65puRrm7vQV0DJeZbbP6fHFBeJvUIVyWuUSiYFf5aPNOv0H2LTrbvXrhKH_jFssGZTYeEgdOCy8_B9uh2wiiDhtntk0nBcQ0rc1GsdrrRrc8LLFUpqOlbNNcC1Um3NkVeSni31o2dsobM_L6PUdTEqiUDNazKOfSbwMDTjzpAC9g_TsEfPXENq2wfea2of7rG-1ZYatdDFTO3JL6lkd5J0NDPME8IlZqLA6KAcsPD2oqRLLhmOuGEo49I7vXptFgch5RaYPgiufMonX5DySo3VSy667OU556y_2ZJOEldcPoNI-3Iqyn9wRedBTlqrCbacM3j9yMluxgFtaIc7keLn8Pkk1tHMwueeWFwZSGZFiYFrDpujpTppwlnCtw8syEqgC9DGm3ytAhMOygBUBBfRu9UU9gXtt9mVAY",
                BasicTools.getProtocol(applicationContext).toString(), "en"
            )
                ?.create(
                    AppApi::class.java
                )
        val map = HashMap<String,String>()
        map["id"] = accountId
        map["link_account"] = linkAccount

        val observable = shopApi!!.linkAccount(map)
        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<LinkAccountModel>(this) {
                    override fun onSuccess(result: LinkAccountModel) {
                        if (result.status!!) {
                            if(BasicTools.isDeviceLanEn())
                            tvSocial.text = "linked"
                            else {
                                tvSocial.text = "متصل"


                            }
                        } else {
                        }

                    }
                }
                ))

    }
    fun unlinkAccount(accountId:String,linkAccount:String,tvSocial:TextView){
        val shopApi =
            ApiClient.getClientJwt(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIzIiwianRpIjoiZDdiNGRmYTAzNGMwYTljN2RmZWFiMDFiMjVlMTA0NmFhMjQ5NjU0OGI5ZWQwNjYwODUxMzkzNmU5OGM3MmRlZDhiYWJhYmY5NjY4Y2Y3ZmEiLCJpYXQiOjE2MzA5MjA3MzEuNTg1NzY4OTM4MDY0NTc1MTk1MzEyNSwibmJmIjoxNjMwOTIwNzMxLjU4NTc3NzA0NDI5NjI2NDY0ODQzNzUsImV4cCI6MTY2MjQ1NjczMS40NDk5MzgwNTg4NTMxNDk0MTQwNjI1LCJzdWIiOiI0Iiwic2NvcGVzIjpbXX0.GbM8R7Utsqf1E5tM2BvkJB1JtcX2-iH4n0zmofsc1Jbhcye2-VuSrwO-5rJSjgQ-hRU9ZWBQu1fgCKKG1vNVWUwG7OH9LPkW2OkcSAJOyEF4WFYpqhb_j_T3KKPMagmB40ZaGBG8NkTx-dsclMXl_J3-FoeN4P5PuHi_oh6vnNmUhqXKy4sgEJDc-OP9U-5blDbj4wPgxDj_y0r7NTuKHcQdt1ABHUFjwODWV-D_Vgl_L7soZ7A0XB1SH9l9Jxq8EAbYcwg65puRrm7vQV0DJeZbbP6fHFBeJvUIVyWuUSiYFf5aPNOv0H2LTrbvXrhKH_jFssGZTYeEgdOCy8_B9uh2wiiDhtntk0nBcQ0rc1GsdrrRrc8LLFUpqOlbNNcC1Um3NkVeSni31o2dsobM_L6PUdTEqiUDNazKOfSbwMDTjzpAC9g_TsEfPXENq2wfea2of7rG-1ZYatdDFTO3JL6lkd5J0NDPME8IlZqLA6KAcsPD2oqRLLhmOuGEo49I7vXptFgch5RaYPgiufMonX5DySo3VSy667OU556y_2ZJOEldcPoNI-3Iqyn9wRedBTlqrCbacM3j9yMluxgFtaIc7keLn8Pkk1tHMwueeWFwZSGZFiYFrDpujpTppwlnCtw8syEqgC9DGm3ytAhMOygBUBBfRu9UU9gXtt9mVAY",
                BasicTools.getProtocol(applicationContext).toString(), "en"
            )
                ?.create(
                    AppApi::class.java
                )
        val map = HashMap<String,String>()
        map["id"] = accountId
        val observable = shopApi!!.unLinkAccount(map)
        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<UnLinkAccountModel>(this) {
                    override fun onSuccess(result: UnLinkAccountModel) {
                        if (result.status!!) {
                            if(BasicTools.isDeviceLanEn())
                                tvSocial.text = "link now"
                            else {
                                tvSocial.text = "اتصل الآن"


                            }
                        } else {
                        }

                    }
                }
                ))

    }

}