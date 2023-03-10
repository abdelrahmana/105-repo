package com.urcloset.smartangle.tools






import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.text.SpannableString
import android.text.format.DateFormat
import android.text.style.UnderlineSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobsandgeeks.saripaar.ValidationError
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.R
import com.urcloset.smartangle.adapter.PagedAdapter
import com.urcloset.smartangle.dialog.BottomSheetRatApplication
import com.urcloset.smartangle.model.project_105.LoginResponseModel
import com.urcloset.smartangle.tools.Constants.USER_MODEL
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt


object BasicTools {


    fun showShimmer(card: RelativeLayout, shimmer: ShimmerFrameLayout, state: Boolean){
        if(state){
            card.visibility= View.GONE
            shimmer.visible()
        }else{
            card.visibility=View.VISIBLE
            shimmer.hide()
        }
    }
    fun changeFragmentBack(activity: FragmentActivity, fragment: Fragment, tag: String, bundle: Bundle?, id : Int ) {

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (bundle != null) {
            fragment.arguments = bundle
        }
        transaction.setCustomAnimations(
            R.anim.enter_from_right, R.anim.exit_to_left,
            R.anim.enter_from_left, R.anim.exit_to_right)
        //R.id.frameLayout_direction+
        transaction.replace(id, fragment, tag)
        transaction.addToBackStack(tag)
        //    transaction.addToBackStack(null)
        transaction.commit()

    }
    fun getFirebaseFcmTokenBeforeStart(callBackToken :(String?)->Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
            //    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                callBackToken(null)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.v("fcm token",token)
            callBackToken(token)

        })
    }
    fun getStringDate(date:String): String? {
      /*  val inst: OffsetDateTime = OffsetDateTime.ofInstant(
            Instant.parse(date),
            ZoneId.systemDefault()
        )
        return DateTimeFormatter.ofPattern("dd,MMM, yyyy").format(inst)*/
           val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val formatter = SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH)
        //return  DateFormat.format("yyyy-MM-dd", cal).toString()
         val newDate = sdf.parse(date)
         val returnedDate = formatter.format(newDate!!)
         return returnedDate
    }
    fun showSnackMessages(
        activity: Activity?,
        error: String?,color : Int= android.R.color.holo_red_dark
    ) {
        if (activity != null) {
            Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                //.title(activity.getString(R.string.errors))
                .message(error!!)
                .backgroundColorRes(color)
                .dismissOnTapOutside()
                .duration(2500)
                .enableSwipeToDismiss()
                .enterAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(550)
                        .alpha()
                        .overshoot()
                )
                .exitAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(200)
                        .anticipateOvershoot()
                )
                .build().show()
        }
    }

    fun showShimmer(card: LinearLayout, shimmer: ShimmerFrameLayout, state: Boolean){
        if(state){
            card.visibility= View.GONE
            shimmer.visible()
        }else{
            card.visibility=View.VISIBLE
            shimmer.hide()
        }
    }
    fun showShimmer(card: CardView, shimmer: ShimmerFrameLayout, state: Boolean){
        if(state){
            card.visibility= View.GONE
            shimmer.visible()
        }else{
            card.visibility=View.VISIBLE
            shimmer.hide()
        }
    }

    fun showShimmer(spinner: Spinner, shimmer: ShimmerFrameLayout, state: Boolean){
        if(state){
            spinner.visibility= View.GONE
            shimmer.visible()
        }else{
            spinner.visibility=View.VISIBLE
            shimmer.hide()
        }
    }


    fun showShimmer(rv: RecyclerView, shimmer: ShimmerFrameLayout, state: Boolean){
        if(state){
            rv.visibility= View.GONE
            shimmer.visible()
        }else{
            rv.visibility=View.VISIBLE
            shimmer.hide()
        }
    }


    fun getSocialProviderID(context: Context):String{
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val value = preferences.getString("SOCIAL_PROVIDER_ID", "")
        return value!!
    }

    fun setSocialProviderID(context: Context, value: String) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("SOCIAL_PROVIDER_ID", value)
        editor.apply()
    }



    fun getLoginType(context: Context):String{
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val value = preferences.getString("LOGIN_TYPE", "")
        return value!!
    }

    fun setLoginType(context: Context, value: String) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("LOGIN_TYPE", value)
        editor.apply()
    }

    fun isTablet(context: Context):Boolean{
        try {


       var dm=context.getResources().getDisplayMetrics()
       var  screenWidth  = dm.widthPixels / dm.xdpi
       var screenHeight = dm.heightPixels / dm.ydpi
        var size = sqrt(
            screenWidth.toDouble().pow(2.toDouble()) +
                screenHeight.toDouble().pow(2.toDouble())
        )
        return size>=7}
        catch (e:Exception){
            Log.i("TEST_TEST","Faild To Find Is Tablet Or Not")
            return true
        }
    }

    fun DpTopixels(context: Context,dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }



    fun getNumberWithSpace(num:String):String{


        if(num.length==1) return " $num "
        else if (num.length==2) return " $num"
        else return  num
    }



    fun logOut(context: Context){
        TemplateActivity.loginResponse=null
        TemplateActivity.currenLocationVistor=null
        TemplateActivity.selectedCountryVisitor=""
        TemplateActivity.selectedCityVisitor=""
        TemplateActivity.bookmarkConditionSelectedCategoryIndex=0
        TemplateActivity.bookmarkConditionSelectedCondtionInedx=0
        TemplateActivity.conditionIdForBookMark=-1
        TemplateActivity.categoryIdForBookMark=-1
      //  setToken(context,"")
        BasicTools.setAgreementsTerms(
            context,
            false
        )
        setPassword(context,"")
        setUserName(context,"")
        setToken(context,"")
        setUserName(context,"")
        setPhoneUser(context,"")
        setPassword(context,"")
        setUserCountryCode(context,"")
        setLoginType(context,"")
        setSocialProviderID(context,"")

    }

    fun isDeviceLanEn():Boolean{

        var lan= Locale.getDefault().getDisplayLanguage()
        Log.i("TEST_TEST_L","$lan")
        return !lan.equals("العربية")
    }
    fun isDeviceLanEn(context:Context):Boolean{
    val pref =   context.getSharedPreferences("app_lang",Context.MODE_PRIVATE)
        if(pref.getInt("en",-1)==-1){
            var lan= Locale.getDefault().getDisplayLanguage()
            return !lan.equals("العربية")

      }
        else return pref.getInt("en",-1)==1


    }


    fun getUrlImg(context:Context,url:String):String{

        return  getProtocol(context)+Constants.api_url_image+url
    }
    fun getUrlHttpImg(context:Context,url:String):String{
        return if(Build.VERSION.SDK_INT==23)
            "http://"+Constants.api_url_image+url
        else getProtocol(context)+Constants.api_url_image+url
    }





    fun setStopMessages(context: Context, state: Boolean) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("STOP_MESSAGES", state)
        editor.apply()
    }

    fun getStopMessages(context: Context): Boolean{
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val state = preferences.getBoolean("STOP_MESSAGES", false)
        return state
    }







    fun setToken(context: Context, token: String) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("TOKEN", token)
        editor.apply()
    }

    fun getAgreementsTerms(context: Context): Boolean{
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val agreements = preferences.getBoolean("terms_agreemnts", false)?:false
        return agreements
    }
    fun setAgreementsTerms(context: Context, agreements: Boolean) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("terms_agreemnts", agreements)
        editor.apply()
    }
    fun setReviewedBefore(context: Context, agreements: Boolean) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("reviewed_before", agreements)
        editor.apply()
    }
    fun getRevviewedBefore(context: Context): Boolean{
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val agreements = preferences.getBoolean("reviewed_before", false)?:false
        return agreements
    }
    fun getToken(context: Context): String{
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val token = preferences.getString("TOKEN", "")?:""
        return token
    }



    fun setIsSocial(context: Context, item: Boolean) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("IS_SOCIAL", item)
        editor.apply()
    }

    fun getIsSocial(context: Context): Boolean{
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val token = preferences.getBoolean("IS_SOCIAL", false)
        return token
    }




    fun setCode(context: Context, value: String) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("CODE", value)
        editor.apply()
    }

    fun getCode(context: Context): String {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val value = preferences.getString("CODE", "")
        return value!!
    }


    fun setUserName(context: Context, userName: String) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("USER_NAME", userName)
        editor.apply()
    }

    fun getUserName(context: Context): String{
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val userName = preferences.getString("USER_NAME", "")
        return userName!!
    }

    fun clearAllActivity(context: TemplateActivity,toActivity: Class<*>){
        val i = Intent(context, toActivity)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(i)
        context.overridePendingTransition(R.anim.enter,R.anim.exit)
    }
    fun getUserModel(objectData : String): LoginResponseModel.Data? { // this should return the registeration model
        val jso = objectData
        val gson = Gson()
        val typeToken = object : TypeToken<LoginResponseModel.Data?>() {}.type
        val obj = gson.fromJson<LoginResponseModel.Data?>(jso, typeToken) ?: null
        return obj

    }
    fun setUserModel(context: Context, loginResponseModel: LoginResponseModel.Data?) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(USER_MODEL, Gson().toJson(loginResponseModel))
        editor.apply()
    }









    fun setPhoneUser(context: Context, userName: String) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("PHONE_USER", userName)
        editor.apply()
    }

    fun getPhoneUser(context: Context): String{
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val userName = preferences.getString("PHONE_USER", "")
        return userName!!
    }

    fun setPassword(context: Context, password: String) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("PASSWROD", password)
        editor.apply()
    }

    fun getPassword(context: Context): String{
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val password = preferences.getString("PASSWROD", "")
        return password!!
    }

    fun setPhoneNum(context: Context, value: String) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("PHONE_NUM", value)
        editor.apply()
    }

    fun getPhoneNum(context: Context): String {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val value = preferences.getString("PHONE_NUM", "")
        return value!!
    }


    fun setLangCode(context: Context, value: String) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("LANG_CODE", value)
        editor.apply()
    }

    fun getLangCode(context: Context): String? {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val value = preferences.getString("LANG_CODE", "en")
        return value
    }

    fun setLanguagePerActivity(activity : Activity, intent: Intent?){
        val currentLanguage =  (getLangCode(activity.applicationContext)?:"en")
        val prefsLang = currentLanguage
        //  if (UtilKotlin.getSharedPrefs(activity).getString(PrefsModel.localLanguage, "en").equals("en")) {
        val locale = Locale(currentLanguage)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        activity.getResources().updateConfiguration(config, activity.resources.displayMetrics)

        setApplicationlanguage(
            activity,
            currentLanguage
        )
        // add current language if default
        setLangCode(activity.applicationContext,prefsLang)
        if (intent!=null) // we need to start activity
        {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            activity.finishAffinity()
            activity.startActivity(intent) // start redirect activity when you set it
        }
    }
    private fun setApplicationlanguage(context: Context, language: String?) {
        val res = context.applicationContext.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        conf.setLocale(Locale(language!!)) // API 17+ only.
        /*  } else {
              conf.locale = Locale(language)
          }

         */
        res.updateConfiguration(conf, dm)
    }
    fun runLayoutAnimation(recyclerView: RecyclerView?, item: AnimationItem) {
        if (recyclerView != null && recyclerView.adapter != null) {
            val context = recyclerView.context
            val controller = AnimationUtils.loadLayoutAnimation(context, item.mResourceId)
            recyclerView.layoutAnimation = controller
            recyclerView.adapter!!.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        }
    }

    fun getAnimationRecyclerItemsHorizontal(): Array<AnimationItem> {
        return arrayOf<AnimationItem>(
            AnimationItem("Fall down", R.anim.layout_animation_fall_down),
            AnimationItem("Slide from right", R.anim.layout_animation_from_right),
            AnimationItem("Slide from left", R.anim.layout_animation_from_left),
            AnimationItem("Slide from bottom", R.anim.layout_animation_from_bottom)
        )
    }


    fun animateFadeIn(view: View, position: Int) {
        var position = position
        val not_first_item = position == -1
        position = position + 1
        view.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 0.5f, 1f)
        ObjectAnimator.ofFloat(view, "alpha", 0f).start()
        animatorAlpha.setStartDelay((if (not_first_item) 500 / 2 else position * 500 / 3).toLong())
        animatorAlpha.duration = 300
        animatorSet.play(animatorAlpha)
        animatorSet.start()
    }




    fun setBottomListener(
        layoutManager: LinearLayoutManager?, recycler: RecyclerView?, adapter: PagedAdapter<*>?,
        listener: OnBottomReached
    ) {
        val scroll_listener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0  ) {
                    val visible_items = layoutManager!!.getChildCount()
                    val total_items = layoutManager!!.getItemCount()
                    val past_visible_items = layoutManager!!.findFirstVisibleItemPosition()
                    if (visible_items + past_visible_items >= total_items) {
                        listener.onReachBottom()
                    } else {
                        listener.onScrolledUp()
                    }
                } else {
                    listener.onScrolledUp()
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recycler!!.clearOnScrollListeners()
            recycler.addOnScrollListener(scroll_listener)
        } else {
            recycler!!.setOnScrollListener(scroll_listener)
        }
    }


    fun dpToPx(dp:Int,context:Context):Int{
        var density=context.resources.displayMetrics.density
        return Math.round((dp*density) as Float)

    }

    fun dpToPxFloat(dp:Int,context:Context):Float{
        var density=context.resources.displayMetrics.density
        return Math.round((dp*density) as Float).toFloat()

    }


    fun drawLine(txt:Int,context:Context):SpannableString{
        val spannableStringObject = SpannableString(context.resources.getString(txt))
        spannableStringObject.setSpan(
            UnderlineSpan(), 0,
            spannableStringObject.length, 0
        )
        return  spannableStringObject
    }

    fun openActivity(context: TemplateActivity, toActivity: Class<*>, finish:Boolean){
        var intent=Intent(context,toActivity)
        context.startActivity(intent)
        if(finish)context.finish()
        context.overridePendingTransition(R.anim.enter,R.anim.exit)

    }

    fun openReviewGoogle(applicationContext: FragmentActivity) {
         /*  val request = ReviewManagerFactory.create(applicationContext).requestReviewFlow()
           request.addOnCompleteListener { task ->
               if (task.isSuccessful) {
                   // We got the ReviewInfo object
                   val reviewInfo : ReviewInfo = task.result
                   BasicTools.setReviewedBefore(applicationContext,true)
               } else {
                   Toast.makeText(applicationContext,task.exception?.message?:"",Toast.LENGTH_SHORT).show()
                   // There was some problem, log or handle the error code.
                   // @ReviewErrorCode val reviewErrorCode = (task.getException()).errorCode
               }
           }*/
        BottomSheetRatApplication()
            .show(applicationContext.supportFragmentManager, "bottom_sheet")
    }
    fun exitActivity(context:TemplateActivity){
        context.finish()
        context.overridePendingTransition(R.anim.enter_prev,R.anim.exit_prev)

    }
    fun showValidationErrorMessagesForFields(errors: List<ValidationError>, context: Context) {
        for (error in errors) {
            val view = error.getView()
            val message = error.getCollatedErrorMessage(context)

            // Display error messages
            if (view is EditText) {
                (view as EditText).error = message
            }
        }
        val validationFailMsg = context.getString(R.string.validation_failed)
        Toast.makeText(context, validationFailMsg, Toast.LENGTH_LONG).show()
    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                val info = cm.activeNetworkInfo

                return info != null && info.isConnectedOrConnecting
            } else {
                val  n = cm.getActiveNetwork();

                if (n != null) {
                    var  nc = cm.getNetworkCapabilities(n)

                    return (nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || nc!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            ||nc!!.hasTransport(NetworkCapabilities.TRANSPORT_VPN))
                }
            }
        }

        return false
    }

    fun checkGPS(activity: Activity): Boolean {
        val gps_manager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!gps_manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(activity)
            return false
        }
        return true
    }


    fun setSystemBarTransparent(act: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = act.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }
    private fun buildAlertMessageNoGps(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(activity.resources.getString(R.string.activate_gps_question))
                .setCancelable(false)
                .setPositiveButton(activity.resources.getString(R.string.yes)) { dialog, id -> activity.startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Constants.GPS_Activity_For_Result) }
                .setNegativeButton(activity.resources.getString(R.string.no)) { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    fun get_dates(date_string: String?): Date? {
        var date: Date? = null
        if (date_string != null && date_string.length >= 10) {
            try {
                val year = Integer.parseInt(date_string.substring(0, 4))
                val month = Integer.parseInt(date_string.substring(5, 7))
                val day = Integer.parseInt(date_string.substring(8, 10))
                date = Date(year, month, day)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return date
    }


    fun setUserCountryCode(context: Context, value: String) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("COUNTRY_CODE", value)
        editor.apply()
    }

    fun getUserCountryCode(context: Context): String {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val value = preferences.getString("COUNTRY_CODE", "")
        return value!!
    }


    fun setFirstLaunch(context: Context, value: Boolean) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("FIRST_LAUNCH", value)
        editor.apply()
    }

    fun isFirstLaunch(context: Context): Boolean {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val value = preferences.getBoolean("FIRST_LAUNCH", true)
        return value
    }

  /*  fun load_anonymous_user(context: Context): User {
        val user = User("")
        return user
    }*/

    fun <T> copy_list(list: List<T>): List<T> {
        val new_list = ArrayList<T>()
        for (i in list.indices)
            new_list.add(list[i])
        return new_list
    }

    //fun areEqualed(first: User?, second: User?): Boolean {
//        if (first == null || second == null)
//            return false
//        if (first.id != second.id)
//            return false
//        if (first.username == null && second.username != null)
//            return false
//        if (first.username != null && second.username == null)
//            return false
//        if (first.username != null && second.username != null && first.username != second.username)
//            return false
//        if (first.password == null && second.password != null)
//            return false
//        if (first.password != null && second.password == null)
//            return false
//        if (first.password != null && second.password != null && first.password != second.password)
//            return false
//        if (first.key == null && second.key != null)
//            return false
//        if (first.key != null && second.key == null)
//            return false
//        if (first.key != null && second.key != null && first.key != second.key)
//            return false
//        if (first.network == null && second.network != null)
//            return false
//        if (first.network != null && second.network == null)
//            return false
//        return if (first.network != null && second.network != null && first.network != second.network) false else true
  // return true
  //  }







    fun setFirstTime(context: Context, state: Boolean) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("FIRST_TIME", state)
        editor.apply()
    }

    fun getFirstTime(context: Context): Boolean?{
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val state = preferences.getBoolean("FIRST_TIME",true )
        return state
    }


    fun setProtocol(context: Context, protocol: String) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("PROTOCOL", protocol)
        editor.apply()
    }

    fun getProtocol(context: Context): String?{
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val protocol = preferences.getString("PROTOCOL", "https://")
        return protocol
    }



    fun setBackgroundSelectable(context: Context, view: View) {
        val attrs = intArrayOf(android.R.attr.selectableItemBackground)
        val ta = context.obtainStyledAttributes(attrs)
        val drawableFromTheme = ta.getDrawable(0)
        ta.recycle()
        setBackground(context, view, drawableFromTheme)
    }

    fun setBackground(context: Context, view: View, drawable: Int) {
        if (Build.VERSION.SDK_INT >= 19)
            view.background = ContextCompat.getDrawable(context, drawable)
        else
            view.setBackgroundDrawable(context.resources.getDrawable(drawable))
    }

    fun setBackground(context: Context, view: View, drawable: Drawable?) {
        if (Build.VERSION.SDK_INT >= 19)
            view.background = drawable
        else
            view.setBackgroundDrawable(drawable)
    }

    fun isValidEmail(target: String?): Boolean {
        return if (target == null) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    fun getVersionNumber(context: Context): String {
        var version = "1.0" //TODO change it in each update
        try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            version = info.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return version
    }

    fun setLastUnamePass(context: Context, username: String, password: String, astro: String, network: String?) {
        val preferences = context.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("LAST_USERNAME", username)
        editor.putString("LAST_PASSWORD", password)
        editor.putString("LAST_ASTRO", astro)
        editor.putString("LAST_NETWORK", network)
        editor.commit()
    }

    fun setPasswordState(edit_password: EditText?, visible: Boolean) {
        if (visible)
            edit_password!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        else
            edit_password!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        edit_password.setSelection(edit_password.text.toString().length)
    }

    /*
    public static boolean is_service_available(Activity activity, boolean with_dialog) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        boolean available = status == ConnectionResult.SUCCESS;
        if (available)
            return true;
        else if (with_dialog) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, activity, Constants.GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        }
        return false;
    }*/

    fun getLastUsername(context: Context): String? {
        val preferences = context.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)
        val username = preferences.getString("LAST_USERNAME", null)
        return preferences.getString("LAST_USERNAME", null)
    }

    fun getLastPassword(context: Context): String? {
        val preferences = context.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)
        val password = preferences.getString("LAST_PASSWORD", null)
        return preferences.getString("LAST_PASSWORD", null)
    }

    fun getLastAstro(context: Context): String? {
        val preferences = context.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)
        val astro = preferences.getString("LAST_ASTRO", null)
        return preferences.getString("LAST_ASTRO", null)
    }

//    fun is_anonymous(user: User): Boolean {
//        return if (user.userName == Constants.LAZY_USER) true else false
//    }



    fun  getMessageType(context: Context,type:String):String{

        if(type.isEmpty()) return ""
        if(type.equals("1")) return context.getString(R.string.only_message)
        if(type.equals("2")) return context.getString(R.string.message_wtih_reply)

        return ""
    }

    fun getLastNetwork(context: Context): String? {
        val preferences = context.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)
        val network = preferences.getString("LAST_NETWORK", null)
        return preferences.getString("LAST_NETWORK", null)
    }

    fun initialize_image_loader(activity: Context) {
        try {
            if (!ImageLoader.getInstance().isInited) {
                val defaultOptions = DisplayImageOptions.Builder()
                        .cacheOnDisk(true).cacheInMemory(true)
                        .imageScaleType(ImageScaleType.EXACTLY)
                        .displayer(FadeInBitmapDisplayer(300)).build()
                val config = ImageLoaderConfiguration.Builder(activity.applicationContext)
                        .defaultDisplayImageOptions(defaultOptions)
                        .memoryCache(WeakMemoryCache())
                        .diskCacheSize(100 * 1024 * 1024).build()
                ImageLoader.getInstance().init(config)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun loadImage(uri: Uri, image_view: ImageView) {
        try {
            val imageLoader = ImageLoader.getInstance()
            val options: DisplayImageOptions
            options = DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisk(true).cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)

                    .displayer(FadeInBitmapDisplayer(300)).build()
            imageLoader.displayImage(uri.toString(), image_view, options)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (er: Error) {
            er.printStackTrace()
        }

    }
    fun setRecycleView(recyclerView: RecyclerView?, adaptor: RecyclerView.Adapter<*>,
                       verticalOrHorizontal: Int?, context:Context, gridModel: GridModel?,
                       includeEdge : Boolean) {
        var layoutManger : RecyclerView.LayoutManager? = null
        if (gridModel==null) // normal linear
            layoutManger = LinearLayoutManager(context, verticalOrHorizontal!!,false)
        else
        {
            layoutManger = GridLayoutManager(context, gridModel.numberOfItems)
            if (recyclerView?.itemDecorationCount==0)
                recyclerView?.addItemDecoration(
                    SpacesItemDecorationGrid(gridModel.numberOfItems
                    , gridModel.space, includeEdge)
                )
        }
        recyclerView?.apply {
            setLayoutManager(layoutManger)
            setHasFixedSize(true)
            adapter = adaptor

        }
    }
    @JvmOverloads
    fun loadImage(url: String, image_view: ImageView?, listener: DownloadListener?, loading_drawable: Drawable? = null) {
        try {
            Glide.with(image_view!!.context).load(url)
                .error(R.color.background_image)/*.placeholder(R.color.background_image)*/.dontAnimate()
                .apply( RequestOptions().override(600, 600)).into(image_view)
          /*  val imageLoader = ImageLoader.getInstance()
            val options: DisplayImageOptions
            if (loading_drawable != null) {
                options = DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisk(true).cacheInMemory(true)
                        .imageScaleType(ImageScaleType.EXACTLY)
                        .showImageOnLoading(loading_drawable)
                        .displayer(FadeInBitmapDisplayer(300)).build()
            } else {
                options = DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisk(true).cacheInMemory(true)
                        .imageScaleType(ImageScaleType.EXACTLY)
                        .displayer(FadeInBitmapDisplayer(300)).build()
            }
            imageLoader.setDefaultLoadingListener(object : ImageLoadingListener {
                override fun onLoadingStarted(s: String, view: View) {

                }

                override fun onLoadingFailed(s: String, view: View, failReason: FailReason) {

                }

                override fun onLoadingComplete(s: String, view: View, bitmap: Bitmap) {
                    if (listener != null)
                        listener.completed(true,bitmap)
                }

                override fun onLoadingCancelled(s: String, view: View) {

                }
            })
            imageLoader.displayImage(url, image_view, options)*/
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (er: Error) {
            er.printStackTrace()
        }

    }
    @JvmOverloads
    fun loadImageWithHero(context: Context,url: String, image_view: ImageView, listener: DownloadListener?, loading_drawable: Drawable? = null) {
        try {
            val imageLoader = ImageLoader.getInstance()
            val options: DisplayImageOptions
            if (loading_drawable != null) {
                options = DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisk(true).cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .showImageOnLoading(loading_drawable)
                    .displayer(FadeInBitmapDisplayer(300)).build()
            } else {
                options = DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisk(true).cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .displayer(FadeInBitmapDisplayer(300)).build()
            }
            imageLoader.setDefaultLoadingListener(object : ImageLoadingListener {
                override fun onLoadingStarted(s: String, view: View) {



                }

                override fun onLoadingFailed(s: String, view: View, failReason: FailReason) {

                }

                override fun onLoadingComplete(s: String, view: View, bitmap: Bitmap) {
                    if (listener != null)
                        listener.completed(true,bitmap)
                    (context as AppCompatActivity).supportStartPostponedEnterTransition()

                }

                override fun onLoadingCancelled(s: String, view: View) {

                }
            })
            imageLoader.displayImage(url, image_view, options)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (er: Error) {
            er.printStackTrace()
        }

    }



    fun open_website(url: String, activity: Activity) {
        try {
            var edited = url
            if (!url.startsWith("https"))
                edited = "https://$url"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            activity.startActivity(browserIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun hideSoftKeyboard(activity: Activity) {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    fun getDeviceUUID(context: Context): String {
        /*TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = tm.getDeviceId();*/
        val uuid = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun split_strings(data: String?, separator: String): ArrayList<String> {
        val list = ArrayList<String>()
        var elements: Array<String>? = null
        if (data != null && data != "") {
            if (data != null && data != "") {
                elements = data.split(separator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            if (elements != null) {
                for (i in elements.indices)
                    list.add(elements[i])
            }
        }
        return list
    }

    fun getDeviceAppID(context: Context): Long {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val device_id = preferences.getLong("DEVICE_ID", 0)
        return preferences.getLong("DEVICE_ID", 0)
    }

    fun setDeviceAppID(context: Context, device_id: Long) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putLong("DEVICE_ID", device_id)
        editor.commit()
    }


    fun getScreenHeight(context: Context): Int {
        val first_dimentsion = context.resources.displayMetrics.widthPixels
        val second_dimentsion = context.resources.displayMetrics.heightPixels
        val height = Math.max(first_dimentsion, second_dimentsion)
        return Math.max(first_dimentsion, second_dimentsion)
    }





    private fun getShamraUrlID(url: String, section: String): String? {
        val index = url.indexOf(section) + 1
        if (index < url.length) {
            var id = url.substring(index)
            val first_slash = id.indexOf("/")
            if (first_slash > 0) {
                id = id.substring(0, first_slash)
            }
            return if (id.length == 0) null else id
        }
        return null
    }


    @Throws(IOException::class)
    fun copyStream(inputStream: InputStream, outputStream: OutputStream, bufferSize: Int) {
        val buffer = ByteArray(bufferSize)
        var readBytes = 0
        do {
            readBytes = inputStream.read(buffer)
            if (readBytes > 0)
                outputStream.write(buffer, 0, readBytes)
        } while (readBytes > 0)
    }

    fun set_db_version(context: Context, version: Int) {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("DB_VERSION", version)
        editor.commit()
    }

    fun get_db_version(context: Context): Int {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val version = preferences.getInt("DB_VERSION", 1)
        return preferences.getInt("DB_VERSION", 1)
    }

    fun setError(context: Context, layout: TextInputLayout, string_id: Int): Int {
        layout.isErrorEnabled = true
        layout.error = context.getString(string_id)
        return Constants.INVALID
    }

    fun setError(layout: TextInputLayout, text: String): Int {
        layout.isErrorEnabled = true
        layout.error = text
        return Constants.INVALID
    }

    fun removeError(layout: TextInputLayout): Int {
        layout.isErrorEnabled = false
        layout.error = null
        return Constants.VALID
    }



    fun isQrCodeFirstLaunch(context: Context): Boolean {
        val preferences = context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        return preferences.getBoolean("FIRST_LAUNCH_QR_Code", true)
    }


    fun normalNumber(num:String):String{

        if(num.length<=1)return  "0"+num
        else return  num

    }



    fun bottomProgressDots(current_index: Int,MAX_STEP:Int,id:Int,view:TemplateActivity,context: Context) {
        var id1=R.id.layoutDots
        var dotsLayout:View?=null
        if(id==0)
            dotsLayout = view.findViewById(id1) as LinearLayout
        else
            dotsLayout = view.findViewById(id) as LinearLayout
        val dots = arrayOfNulls<ImageView>(MAX_STEP)

        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = ImageView(view)
            val width_height = dpToPx(10,context)
            val params =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams(width_height, width_height))
            params.setMargins(10, 10, 10, 10)
            dots[i]!!.setLayoutParams(params)
            dots[i]!!.setImageResource(R.drawable.shape_circle)
            dots[i]!!.setColorFilter(ContextCompat.getColor(context,R.color.overlay_dark_30),
                PorterDuff.Mode.SRC_IN
            )
            dotsLayout.addView(dots[i])
        }

        if (dots.size > 0) {
            dots[current_index]!!.setImageResource(R.drawable.shape_circle)
            dots[current_index]!!.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN
            )
        }
    }


    fun bottomProgressDots1(current_index: Int,MAX_STEP:Int,id:Int,view:TemplateActivity,context: Context) {
        var id1=R.id.layoutDots
        var dotsLayout:View?=null
        if(id==0)
            dotsLayout = view.findViewById(id1) as LinearLayout
        else
            dotsLayout = view.findViewById(id) as LinearLayout
        val dots = arrayOfNulls<ImageView>(MAX_STEP)

        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = ImageView(view)
            val width_height = dpToPx(10,context)
            val params =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams(width_height, width_height))
            params.setMargins(10, 10, 10, 10)
            dots[i]!!.setLayoutParams(params)
            dots[i]!!.setImageResource(R.drawable.shape_circle)
            dots[i]!!.setColorFilter(ContextCompat.getColor(context,R.color.overlay_dark_30),
                PorterDuff.Mode.SRC_IN
            )
            dotsLayout.addView(dots[i])
        }

        if (dots.size > 0) {
            dots[current_index]!!.setImageResource(R.drawable.shape_circle)
            dots[current_index]!!.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN
            )
        }
    }

    fun returnArrayListFormId(context: Context,id:Int):ArrayList<String>{
        var items= (context.resources.getStringArray(id))
        var listItems=ArrayList<String>()

        for (i in items){

            listItems.add(i)
        }

        return  listItems
    }
    fun getShareMessage(name:String,owner:String,price:String):String{
        return "name: $name\nowner: $owner\nprice:$price SAR\n";
    }
    fun sharePdfForWhatsApp(phoneNumber :String): Intent {
        val sendIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" +phoneNumber +
                "&text=" + "")
        )
        return sendIntent//Intent.createChooser(sendIntent, "Sharing Options")
    }
    fun shareProduct(name:String,owner:String,price:String,id: String,image:String?,context: Context){
        val link = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(/*"https://.*urcloset.com.*?id="*/"https://urcloset.page.link/product?id=" + id))
            .setDomainUriPrefix("https://urcloset.page.link/")
            .setSocialMetaTagParameters(
                DynamicLink.SocialMetaTagParameters.Builder()
                    .setTitle(context.resources.getString(R.string.app_name))
                    .setDescription(
                        BasicTools.getShareMessage(
                            name = name,
                            owner =owner,
                            price = price
                        )
                    )
                    .setImageUrl(
                        Uri.parse(
                            "https://" + Constants.api_url_image +image
                        )
                    )
                    .build()
            )
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder("com.urcloset.smartangle")
                    .build()
            )




                .buildShortDynamicLink().addOnCompleteListener {

                    if (it.isSuccessful) {
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_TEXT, it.result.shortLink.toString())
                        context.startActivity(Intent.createChooser(shareIntent, null))

                    }
                    else{
                        try{
                            (context as TemplateActivity).showToastMessage(it.result.toString())
                        }
                        catch (e:Exception){
                            Log.v("exception",e.toString())
                            (context as TemplateActivity).showToastMessage(e.toString())

                        }

                    }
                }
    }
}
