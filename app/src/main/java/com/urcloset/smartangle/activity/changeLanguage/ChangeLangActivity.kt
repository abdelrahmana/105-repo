package com.urcloset.smartangle.activity.changeLanguage

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.ContextUtils
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.LocalHelper
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.util.*

class ChangeLangActivity : TemplateActivity() {
    lateinit var  cvEnglish:CardView
    lateinit var cvArabic:CardView
    lateinit var cvSave:CardView
    lateinit var back:ImageView
    lateinit var shimmer:ShimmerFrameLayout

    var disposable= CompositeDisposable()
     var selected :Int =1
    var codeForLang=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun set_layout() {
        setContentView(R.layout.activity_change_lang)

    }

    override fun init_activity(savedInstanceState: Bundle?) {
        if(BasicTools.isDeviceLanEn()){
            selected = 1
            cvEnglish.setCardBackgroundColor(resources.getColor(R.color.colorPrimary))
            cvArabic.setCardBackgroundColor(resources.getColor(R.color.lightGreyBg))
            findViewById<TextView>(R.id.text_en).setTextColor(resources.getColor(R.color.white))
            findViewById<TextView>(R.id.text_ar).setTextColor(resources.getColor(R.color.black))



        }
        else {
            selected = 2
            cvEnglish.setCardBackgroundColor(resources.getColor(R.color.lightGreyBg))
            cvArabic.setCardBackgroundColor(resources.getColor(R.color.colorPrimary))
            findViewById<TextView>(R.id.text_en).setTextColor(resources.getColor(R.color.black))
            findViewById<TextView>(R.id.text_ar).setTextColor(resources.getColor(R.color.white))

        }
    }

    override fun init_views() {
        cvEnglish = findViewById(R.id.cv_english)
        cvArabic  = findViewById(R.id.cv_arabic)
        cvSave    = findViewById(R.id.card_save)
        shimmer    = findViewById(R.id.shimmer_wait)
        back      = findViewById(R.id.iv_back)
    }

    override fun init_events() {
        cvSave.setOnClickListener {

            //hassan
         /*   val localeToSwitchTo = Locale("ar")
            val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(this, localeToSwitchTo)
            BasicTools.clearAllActivity(this,HomeActivity::class.java)*/


            //manhal

         /*   val preferences = getSharedPreferences("app_lang", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            if(selected==1)
            editor.putInt("en",1 )
            else  editor.putInt("en",2 )

            editor.apply()
            var languageToLoad = "en" // your language
            if(selected==2){
                languageToLoad ="ar"
            }

            val locale = Locale(languageToLoad)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            getBaseContext().getResources().updateConfiguration(
                config,
               getBaseContext().getResources().getDisplayMetrics())*/




         //   LocalHelper.setLocale(this@ChangeLangActivity,"ar")


            if(codeForLang.isNotEmpty()){
                BasicTools.setLangCode(this@ChangeLangActivity,codeForLang)


                RQ()
           // LocalHelper.setLocale(this@ChangeLangActivity,"ar")

            }

        }
        cvArabic.setOnClickListener {
            selected = 2
            cvEnglish.setCardBackgroundColor(resources.getColor(R.color.lightGreyBg))
            cvArabic.setCardBackgroundColor(resources.getColor(R.color.colorPrimary))
            findViewById<TextView>(R.id.text_en).setTextColor(resources.getColor(R.color.black))
            findViewById<TextView>(R.id.text_ar).setTextColor(resources.getColor(R.color.white))

            codeForLang="ar"


        }
        cvEnglish.setOnClickListener {
            selected = 1
            cvEnglish.setCardBackgroundColor(resources.getColor(R.color.colorPrimary))
            cvArabic.setCardBackgroundColor(resources.getColor(R.color.lightGreyBg))
            findViewById<TextView>(R.id.text_en).setTextColor(resources.getColor(R.color.white))
            findViewById<TextView>(R.id.text_ar).setTextColor(resources.getColor(R.color.black))
            codeForLang="en"
        }
        back.setOnClickListener {
            BasicTools.exitActivity(this)
        }
    }

    override fun set_fragment_place() {
    }

    fun RQ(){
        if (BasicTools.isConnected(this@ChangeLangActivity)) {


            var map = HashMap<String, String>()



            BasicTools.showShimmer(cvSave,shimmer,true)


            map.put("current_lang",codeForLang)

            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(this@ChangeLangActivity),
                BasicTools.getProtocol(this@ChangeLangActivity).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.changeLang(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(this@ChangeLangActivity) {
                        override fun onSuccess(result: ResponseBody) {


                            BasicTools.showShimmer(cvSave,shimmer,false)





                            showToastMessage(R.string.success)
                            BasicTools.clearAllActivity(this@ChangeLangActivity,HomeActivity::class.java)



                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(cvSave,shimmer,false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            BasicTools.showShimmer(cvSave,shimmer,false)

            showToastMessage(R.string.no_connection)}
    }
}
