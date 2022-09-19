package com.urcloset.smartangle.activity.loginActivity


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.BuildConfig
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.hbb20.CountryCodePicker
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.Password
import com.urcloset.shop.tools.gone
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.activity.locatonActivity.LocationActivity
import com.urcloset.smartangle.activity.resetPasswrod.ResetPasswrodSelectWay
import com.urcloset.smartangle.activity.signUp.SignUpAcitivty
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.project_105.LoginResponseModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.BasicTools.openActivity
import com.urcloset.smartangle.tools.BasicTools.showValidationErrorMessagesForFields
import com.urcloset.smartangle.tools.Constants
import com.urcloset.smartangle.tools.Constants.LOGIN_TYPE_EMAIL
import com.urcloset.smartangle.tools.Constants.LOGIN_TYPE_PHONE
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*


class LoginAcitivty : TemplateActivity(), Validator.ValidationListener {
    var validator: Validator? = null
    lateinit var countryCode: CountryCodePicker
    lateinit var callbackManager: CallbackManager

    lateinit var cardFaceBook:CardView
    lateinit var cardGoogle:CardView
    lateinit var mGoogleSignClient: GoogleSignInClient

    @Email(sequence = 2, messageResId = R.string.enter_a_valid_email_address)
  //@NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    lateinit var edituserName: EditText



    lateinit var editPhone: EditText


    @Password(sequence = 1, messageResId = R.string.password_lenght,min = 6)
    lateinit var editPassword: EditText


    var loginType=LOGIN_TYPE_EMAIL
    lateinit var cardEmail:CardView
    lateinit var cardTextEmail:TextView
    lateinit var cardIconEmail:ImageView
    lateinit var cardPhone:CardView
    lateinit var cardIconPhone:ImageView
    lateinit var cardTextPhone:TextView
    var disposable:CompositeDisposable=CompositeDisposable()


    private var animIn: Animation? = null
    private var animOut: Animation? = null


    lateinit var  rootEmail:LinearLayout
    lateinit var  rootPhone:LinearLayout
    override fun set_layout() {
        setContentView(R.layout.activity_login)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>, accessToken: String) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!


            Log.i("TEST_TEST",account.email!!)
            Log.i("TEST_TEST",account.id!!)
            Log.i("TEST_TEST",account.displayName!!)


            if(account.photoUrl!=null)
            Log.i("TEST_TESTPhoto",account.photoUrl.toString())
            if(account.photoUrl!=null){
            TemplateActivity.socialImage=account.photoUrl.toString()

            }


            loginRQ(Constants.LOGIN_TYPE_GMAIL,account.email!!,accessToken,
                account.displayName!!,account.photoUrl.toString())
            // Signed in successfully, show authenticated UI.

        } catch (e: ApiException) {
            Log.i("TEST_TEST_error",e.printStackTrace().toString())

           // showToastMessage("${e.statusCode}/ ${e.localizedMessage}/${e.printStackTrace()}")
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.


        }
    }



    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {


        callbackManager.onActivityResult(requestCode, resultCode, data)
        //  callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)



        if (requestCode == 7000) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.


            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)

            if (account!!.serverAuthCode != null) {
                Log.d("google", "serverAuthCode $account.serverAuthCode")
                getGoogleToken(account.serverAuthCode!!, account?.displayName
                    ?: "", account?.email ?: "",task)
            } else {
                Toast.makeText(this,getString(R.string.token_isnull),Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun getGoogleToken(
        code: String,
        name: String,
        email: String,
        task: Task<GoogleSignInAccount>
    ) {
        //showLoader()
        showShimmerLoginBtn(true)

        val client = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("client_id", getString(R.string.server_client_id))
            .add("client_secret", getString(R.string.client_secret))
            .add("code", code)
            .build()
        val request = Request.Builder()
            .url("https://www.googleapis.com/oauth2/v4/token")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //hideLoader()
                showShimmerLoginBtn(false)
                Toast.makeText(this@LoginAcitivty,e.message.toString(),Toast.LENGTH_SHORT).show()

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: okhttp3.Response) {
                try {
                    val jsonObject = JSONObject(response.body!!.string())
                    val accessToken = jsonObject.getString("access_token")
                    if (accessToken != null) {
                        Log.d("google", "accessToken $accessToken")
                        mGoogleSignClient?.signOut() // sign out from google
                        // should call api to login now
                        handleSignInResult(task,accessToken)

                        //socialLogin(access_token, com.waysgroup.vroucare.Views.Fragments.AuthFragments.LoginFragmentAuth.Social.GOOGLE)
                    }
                } catch (e: JSONException) {
                    // hideLoader()
                    showShimmerLoginBtn(false)

                    e.printStackTrace()
                    Toast.makeText(this@LoginAcitivty,e.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun showShimmerLoginBtn(state: Boolean){
        runOnUiThread {
            // Stuff that updates the UI

            if (state) {
                card_login.visibility = View.GONE
                shimmer_login.visible()
            } else {
                card_login.visibility = View.VISIBLE
                shimmer_login.hide()
            }
        }
    }

    override fun init_views() {


        cardGoogle=card_google
        cardFaceBook=card_facebook

        BasicTools.initialize_image_loader(this@LoginAcitivty)
        countryCode=findViewById<CountryCodePicker>(R.id.country_code)

       // countryCode.setCustomMasterCountries("aw")
      //  countryCode.setCustomMasterCountries("SA,KW,QA,BH,AE,OM,JO")
      //  countryCode.setCustomMasterCountries("SA,KW,QA,BH,ae,OM,JO")
        callbackManager = CallbackManager.Factory.create()
        AppEventsLogger.activateApp(getApplication());
        animIn = AnimationUtils.loadAnimation(this@LoginAcitivty, android.R.anim.fade_in)
        animOut = AnimationUtils.loadAnimation(this@LoginAcitivty, android.R.anim.fade_out)
        edituserName=edit_email
        editPassword=edit_password
        editPhone=edit_phone


        rootEmail=root_email
        rootPhone=root_phone

        cardEmail=card_email
        cardTextEmail=tv_email
        cardIconEmail=iv_email
        cardPhone=card_phone
        cardTextPhone=tv_phone
        cardIconPhone=iv_phone




    }
    override fun init_events() {
//      .requestIdToken(Constants.API_TOKEN_LOGIN_GMAIL)
        validator = Validator(this)
        validator!!.setValidationListener(this)
        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        mGoogleSignClient = GoogleSignIn.getClient(this, gso)


        cardGoogle.setOnClickListener {
            mGoogleSignClient.signOut().addOnCompleteListener {

                val signInIntent: Intent = mGoogleSignClient.getSignInIntent()
                startActivityForResult(signInIntent, 7000)
            }






        }



        BasicTools.setProtocol(this@LoginAcitivty,"https://")

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {

                    showShimmerLoginBtn(false)
                    getUserProfile(loginResult?.accessToken, loginResult?.accessToken?.userId)

                    Log.i("TEST_TEST","facebook ${loginResult?.accessToken?.userId}")

                }
                override fun onCancel() {
                    showShimmerLoginBtn(false)
                }
                override fun onError(exception: FacebookException) {
                    showShimmerLoginBtn(false)
                }
                val accessToken = AccessToken.getCurrentAccessToken()
                // accessToken != null && !accessToken.isExpired
            })

        tv_sign_up.setOnClickListener {

            TemplateActivity.SignUpModel.smsCode=""
            openActivity(this@LoginAcitivty, SignUpAcitivty::class.java,false)
        }
        card_login.setOnClickListener {

            validator!!.validate()
          // openActivity(this@LoginAcitivty,SignUpAcitivty::class.java,false)
        }


        eye_password.setOnClickListener {
            if(edit_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                eye_password.setImageResource(R.drawable.visibility_3x)
                edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                eye_password.setImageResource(R.drawable.visibility_off_3x)
                edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }




        cardFaceBook.setOnClickListener {
            showShimmerLoginBtn(true)

            LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile","email") /*listOf("public_profile", "email")*/)


        }


        tv_skip.setOnClickListener {

            BasicTools.openActivity(this@LoginAcitivty, LocationActivity::class.java,false)
          //  BasicTools.clearAllActivity(this@LoginAcitivty,HomeActivity::class.java)
        }




        tv_forgot_password.setOnClickListener {
            BasicTools.openActivity(this@LoginAcitivty,ResetPasswrodSelectWay::class.java,false)
        }


        cardEmail.setOnClickListener{
            cardEmail.setCardBackgroundColor(ContextCompat.getColor(this@LoginAcitivty,R.color.login_btn))
            cardPhone.setCardBackgroundColor(ContextCompat.getColor(this@LoginAcitivty,R.color.white))

            tv_email.setTextColor(ContextCompat.getColor(this@LoginAcitivty,R.color.white))
            tv_phone.setTextColor(ContextCompat.getColor(this@LoginAcitivty,R.color.login_btn))

            iv_email.setColorFilter(ContextCompat.getColor(this@LoginAcitivty, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
            iv_phone.setColorFilter(ContextCompat.getColor(this@LoginAcitivty, R.color.login_btn), android.graphics.PorterDuff.Mode.MULTIPLY)

            cardEmail.radius=BasicTools.dpToPxFloat(10,this)
            cardPhone.radius=BasicTools.dpToPxFloat(10,this)

            rootEmail.visible()
            rootEmail.animation=animIn
            animIn!!.setDuration(2000)

            rootPhone.gone()
            rootPhone.animation=animOut
            animOut!!.setDuration(2000)


            loginType= LOGIN_TYPE_EMAIL


        }


        cardPhone.setOnClickListener {

            cardPhone.setCardBackgroundColor(ContextCompat.getColor(this@LoginAcitivty,R.color.login_btn))
            cardEmail.setCardBackgroundColor(ContextCompat.getColor(this@LoginAcitivty,R.color.white))

            tv_phone.setTextColor(ContextCompat.getColor(this@LoginAcitivty,R.color.white))
            tv_email.setTextColor(ContextCompat.getColor(this@LoginAcitivty,R.color.login_btn))

            iv_phone.setColorFilter(ContextCompat.getColor(this@LoginAcitivty, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
            iv_email.setColorFilter(ContextCompat.getColor(this@LoginAcitivty, R.color.login_btn), android.graphics.PorterDuff.Mode.MULTIPLY)
            cardEmail.radius=BasicTools.dpToPxFloat(10,this)
            cardPhone.radius=BasicTools.dpToPxFloat(10,this)

            rootPhone.visible()
            rootPhone.animation=animIn
            animIn!!.setDuration(2000)

            rootEmail.gone()
            rootEmail.animation=animOut
            animOut!!.setDuration(2000)

            loginType= LOGIN_TYPE_PHONE
        }







    }





    override fun set_fragment_place() {

    }



    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?) {

        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id, first_name, middle_name, last_name, name, picture, email"
        )
        GraphRequest(token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->
                val jsonObject = response.jsonObject

                Log.i("Facebook Id: ", jsonObject!!.toString(2))
                // Facebook Access Token
                // You can see Access Token only in Debug mode.
                // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }

                // Facebook Id
                if (jsonObject!!.has("id")) {
                    val facebookId = jsonObject.getString("id")
                    Log.i("Facebook Id: ", facebookId.toString())
                } else {
                    Log.i("Facebook Id: ", "Not exists")
                }


                // Facebook First Name
                if (jsonObject!!.has("first_name")) {
                    val facebookFirstName = jsonObject.getString("first_name")
                    Log.i("Facebook First Name: ", facebookFirstName)
                } else {
                    Log.i("Facebook First Name: ", "Not exists")
                }


                // Facebook Middle Name
                if (jsonObject.has("middle_name")) {
                    val facebookMiddleName = jsonObject.getString("middle_name")
                    Log.i("Facebook Middle Name: ", facebookMiddleName)
                } else {
                    Log.i("Facebook Middle Name: ", "Not exists")
                }


                // Facebook Last Name
                if (jsonObject.has("last_name")) {
                    val facebookLastName = jsonObject.getString("last_name")
                    Log.i("Facebook Last Name: ", facebookLastName)
                } else {
                    Log.i("Facebook Last Name: ", "Not exists")
                }

                var facebookName =""

                // Facebook Name
                if (jsonObject.has("name")) {
                     facebookName = jsonObject.getString("name")
                    Log.i("Facebook Name: ", facebookName)
                } else {
                    Log.i("Facebook Name: ", "Not exists")
                }


                // Facebook Profile Pic URL
                if (jsonObject.has("picture")) {
                    val facebookPictureObject = jsonObject.getJSONObject("picture")
                    if (facebookPictureObject.has("data")) {
                        val facebookDataObject = facebookPictureObject.getJSONObject("data")
                        if (facebookDataObject.has("url")) {
                            val facebookProfilePicURL = facebookDataObject.getString("url")
                            Log.i("Facebook Profile Pic URL: ", facebookProfilePicURL)
                            TemplateActivity.socialImage=facebookProfilePicURL
                        }
                    }
                } else {
                    Log.i("Facebook Profile Pic URL: ", "Not exists")
                }


                // Facebook Email
                if (jsonObject.has("email")) {
                    val facebookEmail = jsonObject.getString("email")
                    Log.i("Facebook Email: ", facebookEmail)

                    val facebookId = jsonObject.getString("id")
                    loginRQ(Constants.LOGIN_TYPE_FACEBOOK,facebookEmail,facebookId.toString(),
                        facebookName,TemplateActivity.socialImage
                    )

                } else {
                    Log.i("Facebook Email: ", "Not exists")
                }
            }).executeAsync()
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        showValidationErrorMessagesForFields(errors!!, this@LoginAcitivty)
    }

    override fun onValidationSucceeded() {

        if(loginType.equals(LOGIN_TYPE_EMAIL) &&edituserName.text.isEmpty()){
            showToastMessage(R.string.enter_a_valid_email_address)
        return
        }
        else if(loginType.equals(LOGIN_TYPE_PHONE) &&editPhone.text.isEmpty()){
            showToastMessage(R.string.enter_phone_for_account)

        return
        }

        loginRQ()
    }






    fun loginRQ(){
        if (BasicTools.isConnected(this@LoginAcitivty)) {




            var phone=editPhone.text.trim().toString()
            var code=countryCode.selectedCountryCode.toString()

            var map = HashMap<String, String>()
            map.put("password",editPassword.text.trim().toString())
            if(loginType.equals(LOGIN_TYPE_EMAIL)){
            map.put("email",edituserName.text.trim().toString())
                map.put("type_login","1")

            }
            else if(loginType.equals(LOGIN_TYPE_PHONE)){
              map.put("phone_number","$phone")
              map.put("country_code","$code")
              map.put("type_login","2")

            }
            showShimmerLoginBtn(true)
            val shopApi = ApiClient.getClient(BasicTools.getProtocol(this@LoginAcitivty).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.loginNormal(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<LoginResponseModel>(this@LoginAcitivty) {
                        override fun onSuccess(result: LoginResponseModel) {

                            showShimmerLoginBtn(false)

                        if(result.status!!){


                            TemplateActivity.loginResponse=result
                            BasicTools.setIsSocial(this@LoginAcitivty,false)
                            BasicTools.setLoginType(this@LoginAcitivty,loginType)
                            BasicTools.setToken(this@LoginAcitivty,result.data?.accessToken!!.toString())
                            BasicTools.setUserName(this@LoginAcitivty,result.data?.user?.email!!.toString())

                            BasicTools.setPassword(this@LoginAcitivty,editPassword.text.trim().toString())

                            if(!result.data?.user?.phoneNumber.isNullOrEmpty())
                                BasicTools.setPhoneUser(this@LoginAcitivty,result.data?.user?.phoneNumber!!.toString())

                            else  BasicTools.setPhoneUser(this@LoginAcitivty,"")
                            if(!result.data?.user?.countryCode.isNullOrEmpty())
                                BasicTools.setUserCountryCode(this@LoginAcitivty,
                                    result.data?.user?.countryCode!!.toString())

                            else  BasicTools.setUserCountryCode(this@LoginAcitivty,"")

                            BasicTools.openActivity(this@LoginAcitivty,HomeActivity::class.java,true)
                        }
                            else{
                            showShimmerLoginBtn(false)
                            BasicTools.logOut(this@LoginAcitivty)
                            showToastMessage(R.string.faild_to_login)
                        }


                        }
                        override fun onFailed(status: Int) {

                            showShimmerLoginBtn(false)
                            BasicTools.logOut(this@LoginAcitivty)
                            showToastMessage(R.string.faild_to_login)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            showShimmerLoginBtn(false)
            showToastMessage(R.string.no_connection)}
    }


    private fun loginRQ(loginType: String, email: String, token: String,
    name:String,img:String) {
        if (BasicTools.isConnected(this@LoginAcitivty)) {






            var map = HashMap<String, String>()
            map.put("social_type",loginType)
            map.put("name",name)
            map.put("email",email)
            map.put("social_token",token)
            if(img.isNotEmpty())
                map.put("image",img)

            showShimmerLoginBtn(true)
            val shopApi = ApiClient.getClient(BasicTools.getProtocol(this@LoginAcitivty).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.loginSoical(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<LoginResponseModel>(this@LoginAcitivty) {
                        override fun onSuccess(result: LoginResponseModel) {

                            showShimmerLoginBtn(false)

                            if(result.status!!){

                                TemplateActivity.loginResponse=result
                                BasicTools.setSocialProviderID(this@LoginAcitivty,token)
                                BasicTools.setIsSocial(this@LoginAcitivty,true)
                                BasicTools.setLoginType(this@LoginAcitivty,loginType)
                                BasicTools.setToken(this@LoginAcitivty,result.data?.accessToken!!.toString())
                                BasicTools.setUserName(this@LoginAcitivty,result.data?.user?.email!!.toString())
                                //BasicTools.setPhoneUser(this@LoginAcitivty,result.data?.user?.phoneNumber!!.toString())
                                //BasicTools.setUserCountryCode(this@LoginAcitivty,code)
                               // BasicTools.setPassword(this@LoginAcitivty,editPassword.text.trim().toString())
                                BasicTools.openActivity(this@LoginAcitivty,HomeActivity::class.java,true)
                            }
                            else{
                                showShimmerLoginBtn(false)
                                BasicTools.logOut(this@LoginAcitivty)
                                showToastMessage(R.string.faild_to_login)
                            }


                        }
                        override fun onFailed(status: Int) {

                            showShimmerLoginBtn(false)
                            BasicTools.logOut(this@LoginAcitivty)
                            showToastMessage(R.string.faild_to_login)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            showShimmerLoginBtn(false)
            showToastMessage(R.string.no_connection)}
    }






}
