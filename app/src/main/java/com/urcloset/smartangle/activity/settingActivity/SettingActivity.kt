package com.urcloset.smartangle.activity.settingActivity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout

import androidx.cardview.widget.CardView
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.ContactAndSupportActivity.ContactAndSupportActivity
import com.urcloset.smartangle.activity.aboutApp.AboutAppActivity
import com.urcloset.smartangle.activity.cardActivity.CardActivityInsideSetting
import com.urcloset.smartangle.activity.changeLanguage.ChangeLangActivity

import com.urcloset.smartangle.activity.changePasswordActivity.ChangePasswrodActivity
import com.urcloset.smartangle.activity.loginActivity.LoginAcitivty
import com.urcloset.smartangle.activity.privacy.PrivacyActivity
import com.urcloset.smartangle.activity.publishStatusActivity.PublicationStatus
import com.urcloset.smartangle.activity.socialActivity.SocialActivity
import com.urcloset.smartangle.activity.soundActivity.SoundActivity
import com.urcloset.smartangle.activity.terms.TermsActivity
import com.urcloset.smartangle.activity.visitorActivity.VisitorActivity
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.databinding.ActivitySettingBinding
import com.urcloset.smartangle.databinding.ActivitySettingsBinding
import com.urcloset.smartangle.fragment.UserInfoFragment
import com.urcloset.smartangle.model.project_105.CardResultModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import kotlin.collections.HashMap
class SettingActivity : TemplateActivity() {
    lateinit var voiceIdentifiy: RelativeLayout
    lateinit var rootAccountSetting: CardView
    lateinit var fragmentView: View
    lateinit var cvVisitor: CardView
    lateinit var cvTerms: CardView
    lateinit var cvSocial: CardView
    lateinit var cvAbout: CardView
    lateinit var cvPublishState: CardView
    lateinit var changePass: CardView
    lateinit var btnLogout: CardView
    lateinit var shimmerLogout: ShimmerFrameLayout
    lateinit var cvSupport: CardView
    lateinit var cvVoiceIdentifiy: CardView
    lateinit var rootVoice: RelativeLayout
    lateinit var cvPrivacy: CardView
    lateinit var tvVoice: RelativeLayout
    lateinit var line: View
    lateinit var cvLang: CardView
    lateinit var myCard: CardView
    lateinit var ivBack:ImageView
    lateinit var shimmerRootMyCard: ShimmerFrameLayout
    var disposable = CompositeDisposable()
    var binding : ActivitySettingsBinding ?=null
    override fun set_layout() {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_settings)

    }

    override fun init_activity(savedInstanceState: Bundle?) {
    }

    override fun init_views() {
        cvVisitor =binding!!.cvVisitors
        cvTerms =binding!!.cvTerms
        cvSocial =binding!!.cvSocial
        cvAbout =binding!!.cvAbout
        cvPublishState =binding!!.cvPublishState
        changePass =binding!!.cardChangePass
        cvSupport =binding!!.cvSupport
        cvVoiceIdentifiy =binding!!.cvVoiceIdentifiy
        cvPrivacy =binding!!.cvPrivacy
        cvLang =binding!!.cvLang
        btnLogout = findViewById(R.id.card_logout)
        shimmerLogout = findViewById(R.id.shimmer_logout)
        rootAccountSetting = findViewById(R.id.root_account_setting)
        rootVoice = findViewById(R.id.rl_voice_identifiy)
        myCard = findViewById(R.id.card_my_card)
        shimmerRootMyCard = findViewById(R.id.shimmer_card)
        line = findViewById(R.id.line3)
        tvVoice = findViewById(R.id.tv_voices)
        ivBack = findViewById(R.id.iv_back)


    }


    override fun onResume() {
//        if (HomeActivity.bottomNavigation?.currentItem != 3) {
//            HomeActivity.doNothing = true
//            HomeActivity.bottomNavigation?.currentItem = 3
//            HomeActivity.doNothing = false
//
//        }
        super.onResume()
    }

    override fun init_events() {
//        voiceIdentifiy.setOnClickListener{
//        val trans =      fragmentManager?.beginTransaction()
//            trans!!.add(BookMarkFragment,)
//       }
        ivBack.setOnClickListener {
            BasicTools.exitActivity(this)
        }



        if (BasicTools.getIsSocial(this) || TemplateActivity.loginResponse == null) {
            rootAccountSetting.visibility = View.GONE
            changePass.visibility = View.GONE
            Log.i("TEST_TEST","1w")
        } else {
            rootAccountSetting.visibility = View.VISIBLE
            changePass.visibility = View.VISIBLE
            Log.i("TEST_TEST","2w")
        }

        if (TemplateActivity.loginResponse == null) {

            cvVisitor.visibility = View.GONE
            cvSupport.visibility = View.GONE
            cvVoiceIdentifiy.visibility = View.GONE
            rootVoice.visibility = View.GONE
            myCard.visibility = View.GONE
            cvSocial.visibility = View.GONE
            cvPublishState.visibility = View.GONE
            line.visibility = View.GONE
            tvVoice.visibility = View.GONE

        } else {


            cvVisitor.visibility = View.VISIBLE
            cvSupport.visibility = View.VISIBLE
            cvVoiceIdentifiy.visibility = View.VISIBLE
            rootVoice.visibility = View.VISIBLE
            myCard.visibility = View.VISIBLE
            cvSocial.visibility = View.VISIBLE
            cvPublishState.visibility = View.VISIBLE
            line.visibility = View.VISIBLE
            tvVoice.visibility = View.VISIBLE
        }

        cvVoiceIdentifiy.setOnClickListener {
            showActivity(SoundActivity::class.java)


        }
        cvAbout.setOnClickListener {
           showActivity(AboutAppActivity::class.java)
        }
        cvVisitor.setOnClickListener {
           showActivity(VisitorActivity::class.java)
        }
        cvTerms.setOnClickListener {
            showActivity(TermsActivity::class.java)


        }
        cvSocial.setOnClickListener {
            showActivity(SocialActivity::class.java)
        }
        cvPublishState.setOnClickListener {
            showActivity(PublicationStatus::class.java)


        }
        cvSupport.setOnClickListener {
           showActivity(ContactAndSupportActivity::class.java)


        }

        changePass.setOnClickListener {
            BasicTools.openActivity(this, ChangePasswrodActivity::class.java, false)
        }


        btnLogout.setOnClickListener {
            logoutRQ()
        }
        cvLang.setOnClickListener {
            BasicTools.openActivity(this, ChangeLangActivity::class.java, false)
        }

        rootVoice.setOnClickListener {
            BasicTools.openActivity(this, SoundActivity::class.java, false)
        }
        cvPrivacy.setOnClickListener {
            BasicTools.openActivity(this, PrivacyActivity::class.java, false)


        }

        myCard.setOnClickListener {


       /*     if (TemplateActivity.loginResponse?.data!!.user?.phoneNumber != null
                &&
                TemplateActivity.loginResponse?.data!!.user?.countryCode != null
            )*/

            BasicTools.openActivity(
                this,
                CardActivityInsideSetting::class.java,
                false
            )
               // getMyCard()
          /*  else
                parent!!.showToastMessage(R.string.donot_have_phone)*/
        }

        rootAccountSetting.setOnClickListener {



            if (TemplateActivity.loginResponse == null)
                showToastMessage(R.string.you_have_to_login_first)
            else {
                var f = UserInfoFragment()
               show_fragment2(f, false, false, R.id.root_fragment_home)
            }
        }

    }

    override fun set_fragment_place() {
    }






    fun logoutRQ() {
        if (BasicTools.isConnected(this)) {


            BasicTools.showShimmer(btnLogout, shimmerLogout, true)

            var map = HashMap<String, String>()
            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(this@SettingActivity),
                BasicTools.getProtocol(this@SettingActivity).toString()
            )
                ?.create(AppApi::class.java)
            val observable = shopApi!!.logout()
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(this) {
                        override fun onSuccess(result: ResponseBody) {



                            BasicTools.showShimmer(btnLogout, shimmerLogout, false)
                            BasicTools.logOut(this@SettingActivity)
                            BasicTools.clearAllActivity(this@SettingActivity, LoginAcitivty::class.java)


                        }

                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(btnLogout, shimmerLogout, false)

                            BasicTools.logOut(this@SettingActivity)

                            BasicTools.clearAllActivity(this@SettingActivity, LoginAcitivty::class.java)

                        }
                    })
            )

        } else {

          showToastMessage(R.string.no_connection)
        }
    }


    fun getMyCard() {
        if (BasicTools.isConnected(this)) {


            BasicTools.showShimmer(myCard, shimmerRootMyCard, true)

            var map = HashMap<String, String>()
            map.put("phone_number", TemplateActivity.loginResponse?.data?.user?.phoneNumber!!)
            map.put("country_code", TemplateActivity.loginResponse?.data?.user?.countryCode!!)


            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(this).toString()
            )
                ?.create(AppApi::class.java)
            val observable = shopApi!!.getCard(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CardResultModel>(this) {
                        override fun onSuccess(result: CardResultModel) {


                            BasicTools.showShimmer(myCard, shimmerRootMyCard, false)
                            if (result.data == null) {
                          showToastMessage(R.string.no_data_available)
                            } else {
                                TemplateActivity.cardResult = result
                                BasicTools.openActivity(
                                    this@SettingActivity,
                                    CardActivityInsideSetting::class.java,
                                    false
                                )
                            }


                        }

                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(myCard, shimmerRootMyCard, false)


                        }
                    })
            )

        } else {

        showToastMessage(R.string.no_connection)
        }
    }

}