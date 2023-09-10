package com.urcloset.smartangle.fragment.setting_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.ContactAndSupportActivity.ContactAndSupportActivity
import com.urcloset.smartangle.activity.aboutApp.AboutAppActivity
import com.urcloset.smartangle.activity.cardActivity.CardActivityInsideSetting
import com.urcloset.smartangle.activity.changeLanguage.ChangeLangActivity

import com.urcloset.smartangle.activity.changePasswordActivity.ChangePasswrodActivity
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.activity.homeActivity.HomeViewModel
import com.urcloset.smartangle.activity.loginActivity.LoginAcitivty
import com.urcloset.smartangle.activity.privacy.PrivacyActivity
import com.urcloset.smartangle.activity.publishStatusActivity.PublicationStatus
import com.urcloset.smartangle.activity.socialActivity.SocialActivityV2
import com.urcloset.smartangle.activity.soundActivity.SoundActivity
import com.urcloset.smartangle.activity.terms.TermsActivity
import com.urcloset.smartangle.activity.visitorActivity.VisitorActivity
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.fragment.UserInfoFragment
import com.urcloset.smartangle.fragment.paymentmethod.PaymentMethodFragment
import com.urcloset.smartangle.fragment.unpaid.UnpaidCommissionsFragment
import com.urcloset.smartangle.model.project_105.CardResultModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.BasicTools.changeFragmentBack
import com.urcloset.smartangle.tools.BasicTools.sharePdfForWhatsApp
import com.urcloset.smartangle.tools.TemplateActivity
import com.urcloset.smartangle.tools.TemplateFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_settings.card_change_pass
import kotlinx.android.synthetic.main.activity_settings.cv_about
import kotlinx.android.synthetic.main.activity_settings.cv_lang
import kotlinx.android.synthetic.main.activity_settings.cv_privacy
import kotlinx.android.synthetic.main.activity_settings.cv_publish_state
import kotlinx.android.synthetic.main.activity_settings.cv_social
import kotlinx.android.synthetic.main.activity_settings.cv_support
import kotlinx.android.synthetic.main.activity_settings.cv_terms
import kotlinx.android.synthetic.main.activity_settings.cv_visitors
import kotlinx.android.synthetic.main.activity_settings.cv_voice_identifiy
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.toolbar_backpress1.*
import okhttp3.ResponseBody
import kotlin.collections.HashMap


class SettingFragment : TemplateFragment() {
    lateinit var rootAccountSetting: CardView
    lateinit var fragmentView: View
    lateinit var cvVisitor: CardView
    lateinit var cvTerms: CardView
    lateinit var cvSocial: CardView
    lateinit var cvAbout: CardView
    lateinit var cvPublishState: CardView
    lateinit var changePass: CardView
    lateinit var btnLogout: CardView
    lateinit var textLoginLogOut: TextView
    lateinit var shimmerLogout: ShimmerFrameLayout
    lateinit var cvSupport: CardView
    lateinit var cvVoiceIdentifiy: CardView
    lateinit var rootVoice: RelativeLayout
    lateinit var cvPrivacy: CardView
    lateinit var tvVoice: RelativeLayout
    lateinit var line: View
    lateinit var cvLang: CardView
    lateinit var myCard: CardView
    lateinit var shimmerRootMyCard: ShimmerFrameLayout
    lateinit var ivBack:ImageView
    lateinit var share:CardView
    lateinit var whatsAppSupport : CardView
    var disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_settings, container, false)

        btnLogout = fragmentView.findViewById(R.id.card_logout)
        shimmerLogout = fragmentView.findViewById(R.id.shimmer_logout)
        rootAccountSetting = fragmentView.findViewById(R.id.root_account_setting)
        textLoginLogOut  = fragmentView.findViewById(R.id.signOut)
        rootVoice = fragmentView.findViewById(R.id.rl_voice_identifiy)
        myCard = fragmentView.findViewById(R.id.card_my_card)
        shimmerRootMyCard = fragmentView.findViewById(R.id.shimmer_card)
        line = fragmentView.findViewById(R.id.line3)
        tvVoice = fragmentView.findViewById(R.id.tv_voices)
        share = fragmentView.findViewById(R.id.share)

        viewModelHome.setPreviousNavBottom(R.id.setting)

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (BasicTools.getToken(requireContext()).isNotEmpty())
            cardCommission.visibility = View.VISIBLE
        cardCommission?.setOnClickListener{
            val fragment = UnpaidCommissionsFragment()
            changeFragmentBack(requireActivity(),fragment,"commissions",null,R.id.root_fragment_home)
      //      parent?.show_fragment2(fragment, false, false, R.id.root_fragment_home)
        }
    }

    override fun init_views() {
        cvVisitor = cv_visitors
        cvTerms = cv_terms
        cvSocial = cv_social
        cvAbout = cv_about
        cvPublishState = cv_publish_state
        changePass = card_change_pass
        cvSupport = cv_support
        cvVoiceIdentifiy = cv_voice_identifiy
        cvPrivacy = cv_privacy
        cvLang = cv_lang
        ivBack = iv_back


    }


    override fun onResume() {
     /*   if (HomeActivity.bottomNavigation?.currentItem != 4) {
            HomeActivity.doNothing = true
            HomeActivity.bottomNavigation?.currentItem = 4
            HomeActivity.doNothing = false

        }*/
        super.onResume()
    }

    override fun init_events() {

        ivBack.setOnClickListener {
            parent?.onBackPressed()
        }

        if (/*BasicTools.getIsSocial(parent!!) ||*/ TemplateActivity.loginResponse == null) {
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
            textLoginLogOut.text = getString(R.string.sign_in)

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
            parent?.showActivity(SoundActivity::class.java)


        }
        cvAbout.setOnClickListener {
            parent?.showActivity(AboutAppActivity::class.java)
        }
        cvVisitor.setOnClickListener {
            parent?.showActivity(VisitorActivity::class.java)
        }
        cvTerms.setOnClickListener {
            parent?.showActivity(TermsActivity::class.java)


        }
        cvSocial.setOnClickListener {
            parent?.showActivity(SocialActivityV2::class.java)
        }
        cvPublishState.setOnClickListener {
            parent?.showActivity(PublicationStatus::class.java)


        }
        cvSupport.setOnClickListener {
            parent?.showActivity(ContactAndSupportActivity::class.java)


        }

        changePass.setOnClickListener {
            BasicTools.openActivity(parent!!, ChangePasswrodActivity::class.java, false)
        }


        btnLogout.setOnClickListener {
            logoutRQ()
        }
        cvLang.setOnClickListener {
            BasicTools.openActivity(parent!!, ChangeLangActivity::class.java, false)
        }

        rootVoice.setOnClickListener {
            BasicTools.openActivity(parent!!, SoundActivity::class.java, false)
        }
        cvPrivacy.setOnClickListener {
            BasicTools.openActivity(parent!!, PrivacyActivity::class.java, false)


        }
        supportWhats?.setOnClickListener{
            startActivity(sharePdfForWhatsApp("+966554015105"))
        }
        myCard.setOnClickListener {


            /*     if (TemplateActivity.loginResponse?.data!!.user?.phoneNumber != null
                     &&
                     TemplateActivity.loginResponse?.data!!.user?.countryCode != null
                 )*/

            BasicTools.openActivity(
                parent!!,
                CardActivityInsideSetting::class.java,
                false
            )
            // getMyCard()
            /*  else
                  parent!!.showToastMessage(R.string.donot_have_phone)*/
        }

        rootAccountSetting.setOnClickListener {



            if (TemplateActivity.loginResponse == null)
                parent!!.showToastMessage(R.string.you_have_to_login_first)
            else {
                val f = UserInfoFragment()
                parent!!.show_fragment2(f, false, false, R.id.root_fragment_home)
            }
        }
        share.setOnClickListener {
            BasicTools.open_website("https://play.google.com/store/apps/details?id=com.urcloset.smartangle",parent!!)
        }

    }


    override fun init_fragment(savedInstanceState: Bundle?) {

    }

    override fun on_back_pressed(): Boolean {
        return true
    }

    fun logoutRQ() {
        if (BasicTools.isConnected(parent!!)) {


            BasicTools.showShimmer(btnLogout, shimmerLogout, true)

            var map = HashMap<String, String>()
            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(parent!!).toString()
            )
                ?.create(AppApi::class.java)
            val observable = shopApi!!.logout()
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(parent!!) {
                        override fun onSuccess(result: ResponseBody) {


                            BasicTools.showShimmer(btnLogout, shimmerLogout, false)
                            BasicTools.logOut(parent!!)
                            BasicTools.clearAllActivity(parent!!, LoginAcitivty::class.java)


                        }

                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(btnLogout, shimmerLogout, false)

                            BasicTools.logOut(parent!!)

                            BasicTools.clearAllActivity(parent!!, LoginAcitivty::class.java)

                        }
                    })
            )

        } else {

            parent!!.showToastMessage(R.string.no_connection)
        }
    }


    fun getMyCard() {
        if (BasicTools.isConnected(parent!!)) {


            BasicTools.showShimmer(myCard, shimmerRootMyCard, true)

            var map = HashMap<String, String>()
            map.put("phone_number", TemplateActivity.loginResponse?.data?.user?.phoneNumber!!)
            map.put("country_code", TemplateActivity.loginResponse?.data?.user?.countryCode!!)


            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(parent!!).toString()
            )
                ?.create(AppApi::class.java)
            val observable = shopApi!!.getCard(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CardResultModel>(parent!!) {
                        override fun onSuccess(result: CardResultModel) {


                            BasicTools.showShimmer(myCard, shimmerRootMyCard, false)
                            if (result.data == null) {
                                parent!!.showToastMessage(R.string.no_data_available)
                            } else {
                                TemplateActivity.cardResult = result
                                BasicTools.openActivity(
                                    parent!!,
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

            parent!!.showToastMessage(R.string.no_connection)
        }
    }

}