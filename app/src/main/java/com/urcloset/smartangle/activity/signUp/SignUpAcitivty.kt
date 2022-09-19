package com.urcloset.smartangle.activity.signUp


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.hbb20.CountryCodePicker
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.*
import com.urcloset.shop.tools.gone
import com.urcloset.smartangle.R

import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.Constants
import com.urcloset.smartangle.tools.TemplateActivity
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.activity.cardActivity.CardActivity
import com.urcloset.smartangle.activity.locatonActivity.LocationActivity
import com.urcloset.smartangle.activity.verification_code.EnterCodeActivity
import com.urcloset.smartangle.activity.verification_code.VerificationCodeEmailActivity
import com.urcloset.smartangle.model.project_105.CardResultModel
import com.urcloset.smartangle.model.project_105.CheckEmailPhoneModel
import com.urcloset.smartangle.tools.Constants.LOGIN_TYPE_EMAIL
import com.urcloset.smartangle.tools.Constants.LOGIN_TYPE_PHONE
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.eye_password
import kotlinx.android.synthetic.main.activity_sign_up.iv_email
import kotlinx.android.synthetic.main.activity_sign_up.iv_phone
import kotlinx.android.synthetic.main.toolbar_backpress1.*
import okhttp3.ResponseBody
import java.io.File

class SignUpAcitivty : TemplateActivity(), Validator.ValidationListener{
    var disposable= CompositeDisposable()
    private var mAddImages =  ArrayList<Uri>()
    lateinit var ivProfile:ImageView
    var items=ArrayList<Uri>()
    lateinit var countryCode: CountryCodePicker
    var validator: Validator? = null


    lateinit var ivBack:ImageView
    lateinit var tvSkip:TextView






    lateinit var cardEmail: CardView
    lateinit var cardTextEmail: TextView
    lateinit var cardIconEmail:ImageView
    lateinit var cardPhone: CardView
    lateinit var cardIconPhone:ImageView
    lateinit var cardTextPhone: TextView


    private var animIn: Animation? = null
    private var animOut: Animation? = null

    lateinit var  rootEmail: LinearLayout
    lateinit var  rootPhone: LinearLayout

    @Email(sequence = 2, messageResId = R.string.enter_a_valid_email_address)
    //@NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    lateinit var edituserName: EditText



    lateinit var editPhone: EditText


    @Password(sequence = 1, messageResId = R.string.password_lenght,min = 6)
    lateinit var editPassword: EditText


    @ConfirmPassword(sequence = 1, messageResId = R.string.password_donot_match)
    lateinit var editRePassword: EditText
    override fun set_layout() {
        setContentView(R.layout.activity_sign_up)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }



    fun showShimmerBtn(state: Boolean){

        if(state){
            card_sign_up.visibility= View.GONE
            shimmer_wait.visible()
        }else{
            card_sign_up.visibility= View.VISIBLE
            shimmer_wait.hide()
        }
    }

    override fun init_views() {
      //  ivProfile=iv_profile
        validator = Validator(this)
        validator!!.setValidationListener(this)


        editPassword=findViewById(R.id.edit_password)
        edituserName=findViewById(R.id.edit_email)
        editPhone=findViewById(R.id.edit_phone)
        editRePassword=findViewById(R.id.edit_re_enter_password)
        countryCode=findViewById<CountryCodePicker>(R.id.country_code)


        animIn = AnimationUtils.loadAnimation(this@SignUpAcitivty, android.R.anim.fade_in)
        animOut = AnimationUtils.loadAnimation(this@SignUpAcitivty, android.R.anim.fade_out)




        cardEmail=card_email
        rootEmail=findViewById(R.id.root_email)
        rootPhone=findViewById(R.id.root_phone)
        cardTextEmail=findViewById(R.id.tv_email)
        cardIconEmail=findViewById(R.id.iv_email)
        cardPhone=card_phone
        cardTextPhone=findViewById(R.id.tv_phone)
        cardIconPhone=findViewById(R.id.iv_phone)






        ivBack=findViewById(R.id.iv_back)
        tvSkip=findViewById(R.id.tv_skip)

    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        // Called when you request permission to read and write to external storage
        when (requestCode) {
            Constants.REQUEST_READ_WRITE_STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    launchGallery()
                } else {
                    Toast.makeText(this, "permission_denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun openSoftKeyboard(view: View) {
        view.requestFocus()
        // open the soft keyboard
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun launchGallery() {
        mAddImages = java.util.ArrayList()
        FilePickerBuilder.instance.setMaxCount(1)
            .setSelectedFiles(mAddImages)
            .setActivityTheme(R.style.filePickerActivityTheme)
            .pickPhoto(this@SignUpAcitivty)
    }

    override fun init_events() {


        validator = Validator(this)
        validator!!.setValidationListener(this)




      //  cardEmail.performClick()
        registerType= Constants.LOGIN_TYPE_EMAIL


        eye_password.setOnClickListener {
            if(editPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                eye_password.setImageResource(R.drawable.visibility_3x)
                editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                eye_password.setImageResource(R.drawable.visibility_off_3x)
                editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }

        eye_password_re_enter.setOnClickListener {
            if(editRePassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                eye_password_re_enter.setImageResource(R.drawable.visibility_3x)
                editRePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                eye_password_re_enter.setImageResource(R.drawable.visibility_off_3x)
                editRePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }

        cardEmail.setOnClickListener{
            cardEmail.setCardBackgroundColor(ContextCompat.getColor(this@SignUpAcitivty,R.color.login_btn))
            cardPhone.setCardBackgroundColor(ContextCompat.getColor(this@SignUpAcitivty,R.color.white))


            editPhone.setText("")
            cardTextEmail.setTextColor(ContextCompat.getColor(this@SignUpAcitivty,R.color.white))
            cardTextPhone.setTextColor(ContextCompat.getColor(this@SignUpAcitivty,R.color.login_btn))

            iv_email.setColorFilter(ContextCompat.getColor(this@SignUpAcitivty, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
            iv_phone.setColorFilter(ContextCompat.getColor(this@SignUpAcitivty, R.color.login_btn), android.graphics.PorterDuff.Mode.MULTIPLY)

            cardEmail.radius=BasicTools.dpToPxFloat(10,this)
            cardPhone.radius=BasicTools.dpToPxFloat(10,this)

            rootEmail.visible()
            rootEmail.animation=animIn
            animIn!!.setDuration(2000)

            rootPhone.gone()
            rootPhone.animation=animOut
            animOut!!.setDuration(2000)


            registerType= Constants.LOGIN_TYPE_EMAIL



            openSoftKeyboard(edituserName)



        }


        cardPhone.setOnClickListener {

            cardPhone.setCardBackgroundColor(ContextCompat.getColor(this@SignUpAcitivty,R.color.login_btn))
            cardEmail.setCardBackgroundColor(ContextCompat.getColor(this@SignUpAcitivty,R.color.white))


            edituserName.setText("")
            cardTextPhone.setTextColor(ContextCompat.getColor(this@SignUpAcitivty,R.color.white))
            cardTextEmail.setTextColor(ContextCompat.getColor(this@SignUpAcitivty,R.color.login_btn))

            iv_phone.setColorFilter(ContextCompat.getColor(this@SignUpAcitivty, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
            iv_email.setColorFilter(ContextCompat.getColor(this@SignUpAcitivty, R.color.login_btn), android.graphics.PorterDuff.Mode.MULTIPLY)

            cardEmail.radius=BasicTools.dpToPxFloat(10,this)
            cardPhone.radius=BasicTools.dpToPxFloat(10,this)

            rootPhone.visible()
            rootPhone.animation=animIn
            animIn!!.setDuration(2000)

            rootEmail.gone()
            rootEmail.animation=animOut
            animOut!!.setDuration(2000)

            registerType= Constants.LOGIN_TYPE_PHONE

            openSoftKeyboard(editPhone)
        }




//        iv_select_img.setOnClickListener {
//            pickGalleryImages()
//
//        }


        card_sign_up.setOnClickListener {
            validator!!.validate()
          //  BasicTools.openActivity(this@SignUpAcitivty, VCodeActivity::class.java, false)
        }


        tvSkip.setOnClickListener {
            BasicTools.openActivity(this@SignUpAcitivty, LocationActivity::class.java,false)
        }

        ivBack.setOnClickListener {
            BasicTools.exitActivity(this@SignUpAcitivty)
        }

    }


    private fun pickGalleryImages() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                Constants.REQUEST_READ_WRITE_STORAGE_PERMISSION_CODE
            )
        } else
            launchGallery()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // If the image capture activity was called and was successful
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
            if (resultCode == Activity.RESULT_OK && data != null) {


                //  mAddImages!!.clear()
                mAddImages.addAll(data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA) as ArrayList<Uri>)

                items = data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)!!
                if(items==null){
                    Log.i("signup","IMAGES=NULL")
                }else{


                    if (items.isNotEmpty()){
                    Glide.with(this@SignUpAcitivty).load(items.get(0)).placeholder(
                        R.drawable.logo1
                    ).error(R.drawable.logo1).into(ivProfile)
                    TemplateActivity.photosUri.clear()
                    TemplateActivity.photosUri.add(items.get(0))
                    }

//                    for (item in items)
//                        imageAdapter.addItem(item, 0)

                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)

        }
    }

    override fun set_fragment_place() {

    }

    override fun onValidationFailed(errors: List<ValidationError>?) {
        BasicTools.showValidationErrorMessagesForFields(errors!!, this@SignUpAcitivty)
    }

    override fun onValidationSucceeded() {

        if(registerType.equals(Constants.LOGIN_TYPE_EMAIL) &&edituserName.text.isEmpty()){
            showToastMessage(R.string.enter_a_valid_email_address)
            return
        }
        else if(registerType.equals(Constants.LOGIN_TYPE_PHONE) &&editPhone.text.isEmpty()){
            showToastMessage(R.string.enter_phone_for_account)

            return
        }

    /*    if( TemplateActivity.photosUri.isNullOrEmpty()){
            showToastMessage(R.string.select_photo)
        return
        }*/

        checkPhoneRQ(editPhone.text.trim().toString())


    }

    fun checkPhoneRQ(txt:String){
        if (BasicTools.isConnected(this@SignUpAcitivty)) {



            showShimmerBtn(true)

            var map = HashMap<String, String>()
            var lang = HashMap<String, String>()

            if(BasicTools.isDeviceLanEn())
                lang.put("lang","en")
            else   lang.put("lang","ar")


            if(registerType.equals(LOGIN_TYPE_EMAIL)){
            map.put("email",edituserName.text.trim().toString())
                registerValue=edituserName.text.trim().toString()
                registerValueCode=""


            }

         else if(registerType.equals(Constants.LOGIN_TYPE_PHONE)){
                map.put("phone_number",editPhone.text.trim().toString())
                map.put("country_code",countryCode.selectedCountryCode)
                registerValueCode= countryCode.selectedCountryCode
                registerValue= editPhone.text.trim().toString()

            }
            val shopApi = ApiClient.getClient(
               BasicTools.getProtocol(this@SignUpAcitivty).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.checkPhone(map,lang)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CheckEmailPhoneModel>(this@SignUpAcitivty) {
                        override fun onSuccess(result: CheckEmailPhoneModel) {


                            showShimmerBtn(false)


                            passwrodSignUp=editPassword.text.toString()

                            if(!result.messages.isNullOrEmpty())
                                showToastMessage(result.messages!!.get(0))

                            else{


                                if(registerType.equals(LOGIN_TYPE_EMAIL))
                                sendCodeToEmail()

                                else if(registerType.equals(LOGIN_TYPE_PHONE)){
                                    checkCard()

                                }
                            }


                        }
                        override fun onFailed(status: Int) {

                            showShimmerBtn(false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            showShimmerBtn(false)
            showToastMessage(R.string.no_connection)}
    }



    fun checkCard(){
        if (BasicTools.isConnected(this@SignUpAcitivty)) {



            showShimmerBtn(true)

            var map = HashMap<String, String>()





                map.put("phone_number",editPhone.text.trim().toString())
                map.put("country_code",countryCode.selectedCountryCode)
                registerValueCode= countryCode.selectedCountryCode
                registerValue= editPhone.text.trim().toString()


            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(this@SignUpAcitivty).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.getCard(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CardResultModel>(this@SignUpAcitivty) {
                        override fun onSuccess(result: CardResultModel) {


                            showShimmerBtn(false)

                            if(result.data==null)
                                    BasicTools.openActivity(this@SignUpAcitivty,
                                        EnterCodeActivity::class.java,false)

                            else {
                                TemplateActivity.cardResult=result
                                BasicTools.openActivity(this@SignUpAcitivty,
                                    CardActivity::class.java,false)
                            }




                        }
                        override fun onFailed(status: Int) {

                            showShimmerBtn(false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            showShimmerBtn(false)
            showToastMessage(R.string.no_connection)}
    }



    fun sendCodeToEmail(){
        if (BasicTools.isConnected(this@SignUpAcitivty)) {



            showShimmerBtn(true)





            var map=HashMap<String,String>()

            map.put("email",edituserName.text.trim().toString())


            TemplateActivity.signupEmail=edituserName.text.trim().toString()


            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(this@SignUpAcitivty),  BasicTools.getProtocol(this@SignUpAcitivty).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.sendCodeToEmail(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(this@SignUpAcitivty) {
                        override fun onSuccess(result: ResponseBody) {


                            showShimmerBtn(false)

                            BasicTools.openActivity(this@SignUpAcitivty,VerificationCodeEmailActivity::class.java,false)




                        }
                        override fun onFailed(status: Int) {

                            showShimmerBtn(false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            showShimmerBtn(false)
            showToastMessage(R.string.no_connection)}
    }


}
