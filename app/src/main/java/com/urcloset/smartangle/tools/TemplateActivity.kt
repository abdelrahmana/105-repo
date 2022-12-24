package com.urcloset.smartangle.tools






import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources

import android.graphics.PorterDuff
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.transition.Fade
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat

import com.urcloset.smartangle.R
import com.urcloset.smartangle.model.User
import com.urcloset.smartangle.model.project21.*
import com.google.firebase.auth.PhoneAuthProvider
import com.urcloset.smartangle.activity.ContextUtils
import com.urcloset.smartangle.model.project_105.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@AndroidEntryPoint
abstract class TemplateActivity : AppCompatActivity() {

    var current_fragment: TemplateFragment? = null



        //var db:AppDatabase?=null
        protected set
    protected var fragment_place: ViewGroup? = null
    var user: User?=null
    // private FirebaseAnalytics mFirebaseAnalytics;

    companion object {



        var storedVerificationId=""
        var tokenResendOtp: PhoneAuthProvider.ForceResendingToken?=null


        var mapToRQ=HashMap<String,String>()
        var categoryID="0"
        private var back_pressed: Long = 0

        var photosUri=ArrayList<Uri>()
        var SignUpModel=SignUpModel()
        var loginResponse: LoginResponseModel?=null
        var homeData:HomeModel?=null
        var trendingDataList:TrendingModel?=null
        var featuredDataList:TrendingModel?=null
        var favorityDataList:TrendingModel?=null
        var newAndOtherDataList:TrendingModel?=null
        var CategoryDataList:JobTitleModel?=null
        var messageModel:MessageInboxSendModel.Data?=null







        var phoneForgotPasswrod=""
        var countryCodeForgotPasswrod=""
        var emailForgotPasswrod=""


        var socialImage=""


        //to signup

        var signupEmail=""

        var registerType= Constants.LOGIN_TYPE_EMAIL
        var registerValue= ""
        var registerValueCode= ""
        var passwrodSignUp= ""



        //Bookmark Data

        var bookMarkListV2:ArrayList<BookmarkMV3.Data.Data>?=null
        var SeeAllUser:ArrayList<ProivderSeachModel.Data.Data>?=null


        var colorSearchList:ArrayList<ColorModelHassan.Color>?=ArrayList()
        var sizeSearchList:ArrayList<SizeModelHassan.Size>?=ArrayList()
        var conditionSearchList:ArrayList<ConditionBookMarkModel.Data.Data>?=ArrayList()


        var selectedCountryVisitor=""
        var selectedCityVisitor=""
        var selectedCountryNameVisitor=""
        var selectedCityNameVisitor=""
        var selectedCountryNameArVisitor=""
        var selectedCityNameArVisitor=""
        var lastPageBookMark=1
        var lastPageSeeAllUser=1
        var isNotificationVisited = 0


        var cardResult:CardResultModel?=null

        var currenLocationVistor: Location?=null

        var bookmarkConditionSelectedCategoryIndex=0
        var bookmarkConditionSelectedCondtionInedx=0

        var conditionIdForBookMark=0
        var categoryIdForBookMark=0
        var bookmarkSeachText=""
        var codeForEmailOrPhone=""
        var sellerRate:Double=0.0
        var sellerNumProducts:Int=0
        var sellerName:String=""
        var sellerNumRaters:Double=0.0
        var sellerImage:String =""


    }

    fun getMapForProduct(subid:String,start_row:String,limit_to:String):HashMap<String,String>{
        categoryID=subid
        var map=HashMap<String,String>()
        map.put("token",BasicTools.getToken(this))
        map.put("fun","getproductsbysubcategory")
        map.put("subid",""+subid)
        map.put("start_row",""+start_row)
        map.put("limit_to",""+limit_to)
        TemplateActivity.mapToRQ=map
        return map
    }

    override fun onCreate(savedInstanceState: Bundle?) {
/*        var lang = "en"
        if(!BasicTools.isDeviceLanEn(this)){
            lang ="ar"
        }
        val config = resources.configuration
        val locale = Locale(lang)
        Locale.setDefault(locale)
        config.setLocale(locale)*/
        super.onCreate(savedInstanceState)
        BasicTools.setLanguagePerActivity(this,null)
        //db= AppDatabase(this)
        setStatusBarColor()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        set_layout()
        init_views()
        set_fragment_place()
        init_activity(savedInstanceState)
        init_events()
        init_analytics()

    }

    override fun attachBaseContext(newBase: Context) {
// get chosen language from shread preference
        var lang=BasicTools.getLangCode(newBase)
        Log.i("TEST_TEST","$lang")
        if(lang==null|| lang.isEmpty())
            lang="en"

      val localeToSwitchTo = Locale(lang)



        val resources: Resources = newBase.resources
        val configuration: Configuration = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(localeToSwitchTo)
            LocaleList(localeToSwitchTo)
            configuration.setLocales(localeList)
            Locale.setDefault(localeToSwitchTo)
            Log.i("TEST_TEST","SADAS")

            // resources.updateConfiguration(configuration, resources.displayMetrics)
            var c= newBase.createConfigurationContext(configuration)
            //  val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeToSwitchTo)
            super.attachBaseContext(c)


        } else {

            //   val locale = Locale(languageToLoad)
            Locale.setDefault(localeToSwitchTo)
            val config = Configuration()
            config.setLocale(localeToSwitchTo)
            configuration.locale = localeToSwitchTo
            val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeToSwitchTo)
            super.attachBaseContext(localeUpdatedContext)

       } /*else {

        //   val locale = Locale(languageToLoad)
           Locale.setDefault(localeToSwitchTo)
           val config = Configuration()
           config.setLocale(localeToSwitchTo)
           configuration.locale = localeToSwitchTo
             val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeToSwitchTo)
           super.attachBaseContext(localeUpdatedContext)
        }*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            newBase.createConfigurationContext(configuration)
        } else {
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        val res = newBase.applicationContext.resources
        val dm = res.displayMetrics
      //  val conf = res.configuration
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        configuration.setLocale(Locale(lang)) // API 17+ only.
        /*  } else {
              conf.locale = Locale(language)
          }

         */
        res.updateConfiguration(configuration, dm)
       // super.attachBaseContext(localeUpdatedContext)
    }



    abstract fun set_layout()

    abstract fun init_activity(savedInstanceState: Bundle?)

    abstract fun init_views()

    abstract fun init_events()

    //abstract fun load_background()

    abstract fun set_fragment_place()

    private fun init_analytics() {
        try {
            //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun clearStack(main: Boolean) {
        while (supportFragmentManager.backStackEntryCount > if (main) 1 else 2) {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    @JvmOverloads
    fun show_fragment2(
        fragment: TemplateFragment,
        clearStack: Boolean,
        main: Boolean = false,
        viewId: Int
    ) {
        if (current_fragment !== fragment) {
            if (fragment_place != null) {
                val runnable = Runnable {
                    if (clearStack) {
                        while (supportFragmentManager.backStackEntryCount > 1) {
                            supportFragmentManager.popBackStackImmediate()
                        }
                        if (main) {
                            while (supportFragmentManager.backStackEntryCount > 0) {
                                supportFragmentManager.popBackStackImmediate()
                            }
                        }
                    }
                    replaceFragment(fragment,viewId)
                    current_fragment = fragment
                }
                runOnUiThread(runnable)
            }
        }
    }

 /*   @JvmOverloads
    fun show_fragment(fragment: TemplateFragment, forward: Boolean = true) {
        if (current_fragment != fragment) {
            if (fragment_place != null) {
                val runnable = Runnable {
                    if (current_fragment != null)
                        current_fragment!!.removeSnackbar()
                    //fragment.setForward(forward);
                    replaceFragment(fragment)
                    current_fragment = fragment
                }
                runOnUiThread(runnable)
            }
        }
    }*/


    @JvmOverloads
    fun show_fragment(fragment: TemplateFragment, originView: View, sharedView: String, forward: Boolean = true) {
        if (current_fragment !== fragment) {
            if (fragment_place != null) {
                val runnable = Runnable {
                    if (current_fragment != null)
                        current_fragment!!.removeSnackbar()
                    //fragment.setForward(forward);
                    replaceFragment(fragment, originView, sharedView)
                    current_fragment = fragment
                }
                runOnUiThread(runnable)
            }
        }
    }

    private fun replaceFragment(fragment: TemplateFragment,viewId : Int) {
        val backStateName = fragment.javaClass.name+System.currentTimeMillis();
        val manager = supportFragmentManager
       // val fragmentPopped = manager.popBackStackImmediate(backStateName, 0)
      //  if (!fragmentPopped) {
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(R.anim.fade_out_short,R.anim.fade_in)
            transaction.replace(fragment_place!!.id, fragment)
            transaction.addToBackStack(backStateName)
            transaction.commit()
          //  manager.executePendingTransactions()
    //    }
    }

    private fun replaceFragmentWithAnim(fragment: TemplateFragment, anim1:Int, anim2:Int) {

        val animRes1= anim1
        val animRes2= anim2

        val backStateName = fragment.javaClass.name+System.currentTimeMillis();
        val manager = supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(backStateName, 0)
        if (!fragmentPopped) {
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(anim1,anim2)
            transaction.replace(fragment_place!!.id, fragment)
            transaction.addToBackStack(backStateName)
            transaction.commit()
        }
    }

    private fun replaceFragmentWithAnim(fragment: TemplateFragment, enterDirection:Int) {

        val animRes1:Int
        var animRes2: Int
        if (enterDirection == Constants.FRAGMENT_TRANSITION_ENTER_DIRECTION) {
            //Entry
            animRes1 = R.anim.enter_from_right
            animRes2 = R.anim.exit_to_left
        } else {
            animRes1 = R.anim.enter_from_left
            animRes2 = R.anim.exit_to_right
        }

        val backStateName = fragment.javaClass.name+System.currentTimeMillis();
        val manager = supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(backStateName, 0)
        if (!fragmentPopped) {
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(animRes1,animRes2)
            transaction.replace(fragment_place!!.id, fragment)
            transaction.addToBackStack(backStateName)
            transaction.commit()
        }
    }

    private fun replaceFragment(fragment: TemplateFragment, originView: View, sharedView: String) {
        val backStateName = fragment.javaClass.name+System.currentTimeMillis();
        val manager = supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(backStateName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.sharedElementEnterTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.move)
            fragment.enterTransition = Fade()
            current_fragment!!.exitTransition = Fade()
            fragment.sharedElementReturnTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.move)
        }
        if (!fragmentPopped) {
            val transaction = manager.beginTransaction()
            transaction.replace(fragment_place!!.id, fragment)
            transaction.addSharedElement(originView, sharedView)
            transaction.addToBackStack(backStateName)
            transaction.commit()
        }
    }



    @JvmOverloads
    fun show_fragment2WithAnimation(fragment: TemplateFragment, clearStack: Boolean, main: Boolean = false
                                    , enterDirection:Int) {
        if (current_fragment !== fragment) {
            if (fragment_place != null) {
                val runnable = Runnable {
                    if (clearStack) {
                        while (supportFragmentManager.backStackEntryCount > 1) {
                            supportFragmentManager.popBackStackImmediate()
                        }
                        if (main) {
                            while (supportFragmentManager.backStackEntryCount > 0) {
                                supportFragmentManager.popBackStackImmediate()
                            }
                        }
                    }
                    replaceFragmentWithAnim(fragment,enterDirection)
                    current_fragment = fragment
                }
                runOnUiThread(runnable)
            }
        }
    }



    fun send_data(message: DataMessage) {
        message._receiver.onReceive(message._extra)
    }

    fun send_data(message: ObjectMessage) {
        message._receiver.onReceive(message._object)
    }


    override fun onBackPressed() {
        if (current_fragment != null && current_fragment!!.on_back_pressed()) {
            current_fragment!!.cancel()
            if (supportFragmentManager.backStackEntryCount == 1) {

                doubleClickToExit()
               // finish()
                /* if (this.getClass().getName().equals("MainActivity"))
                     finish();
                else
                    doubleClickToExit();*/
            } else {
                super.onBackPressed()
                // MainActivityNew.setSelectedByFragmentManager(getSupportFragmentManager());
                current_fragment = supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1] as TemplateFragment
            }
        } else {
            super.onBackPressed()

        }
    }

    private fun doubleClickToExit() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            finish()
        else
            showToastMessageShort(R.string.double_click)
        back_pressed = System.currentTimeMillis()
    }

    override fun onStart() {
        super.onStart()
        //BasicTools.logScreen(getClass().getName(), mFirebaseAnalytics);
    }




    fun showToastMessage(msg: String?) {
        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        val toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG)
/*        val view = toast.view

        //Gets the actual oval background of the Toast then sets the colour filter
        view!!.background.setColorFilter(fetchColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)

        //Gets the TextView from the Toast so it can be editted
        val text = view.findViewById<TextView>(android.R.id.message)
        text.setTextColor(fetchColor(R.color.tabs_white))
        //BasicTools.setDroidFont(this,text);*/

        toast.show()
    }


    fun showToastMessage(msg: Int) {
        //Toast.makeText(getApplicationContext(), getResources().getString(msg), Toast.LENGTH_LONG).show();
        val toast = Toast.makeText(applicationContext, resources.getString(msg), Toast.LENGTH_LONG)
/*        val view = toast.view

        //Gets the actual oval background of the Toast then sets the colour filter
        view?.background?.setColorFilter(fetchColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)

        //Gets the TextView from the Toast so it can be editted
        val text = view.findViewById<TextView>(android.R.id.message)
        text.setTextColor(fetchColor(R.color.tabs_white))*/
        //BasicTools.setDroidFont(this,text);

        toast.show()
    }

    fun showToastMessageShort(msg: String?) {
        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        val toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT)
    /*    val view = toast.view

        //Gets the actual oval background of the Toast then sets the colour filter
        view!!.background.setColorFilter(fetchColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)

        //Gets the TextView from the Toast so it can be editted
        val text = view.findViewById<TextView>(android.R.id.message)
        text.setTextColor(fetchColor(R.color.tabs_white))
        //BasicTools.setDroidFont(this,text);*/


        toast.show()
    }


    fun showToastMessageShort(msg: Int) {
        //Toast.makeText(getApplicationContext(), getResources().getString(msg), Toast.LENGTH_SHORT).show();
//        val toast = Toast.makeText(applicationContext, resources.getString(msg), Toast.LENGTH_SHORT)
//        val view = toast.view
//        //Gets the actual oval background of the Toast then sets the colour filter
//        view!!.background.setColorFilter(fetchColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)

        val toast = Toast.makeText(applicationContext, resources.getString(msg), Toast.LENGTH_SHORT)
        //Gets the TextView from the Toast so it can be editted
//        val text = view!!.findViewById<TextView>(android.R.id.message)
//        text.setTextColor(fetchColor(R.color.tabs_white))
        //BasicTools.setDroidFont(this,text);

        toast.show()
    }

    fun fetchColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(this, color)
    }

    fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
            window.navigationBarColor = resources.getColor(R.color.colorPrimaryDark)
            //actionBar!!.elevation = 0F;

        }

        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT <= 22) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
            window.navigationBarColor = resources.getColor(R.color.colorPrimaryDark)

        }


        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //
        //            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //            getWindow().setStatusBarColor(Color.WHITE);
        //
        //        }
    }

    public fun showActivity(toActivity: Class<*>) {
        val intent = Intent(this, toActivity)
        startActivity(intent)
    }


}

