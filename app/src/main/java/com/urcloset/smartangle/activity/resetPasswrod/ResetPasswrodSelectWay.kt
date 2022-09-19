package com.urcloset.smartangle.activity.resetPasswrod


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.facebook.shimmer.ShimmerFrameLayout
import com.hbb20.CountryCodePicker
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.Password
import com.urcloset.shop.tools.gone
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.verification_code.EnterCodeActivity
import com.urcloset.smartangle.activity.verification_code.VerificationCodeEmailActivity
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.project_105.CheckEmailPhoneModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.Constants
import com.urcloset.smartangle.tools.TemplateActivity
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reset_passwrod_selected_way.*

import okhttp3.ResponseBody

class ResetPasswrodSelectWay : TemplateActivity(),Validator.ValidationListener{
    var disposable= CompositeDisposable()
    private var mAddImages =  ArrayList<Uri>()
    lateinit var ivProfile: ImageView
    lateinit var ivBack: ImageView
    var items=ArrayList<Uri>()
    lateinit var countryCode: CountryCodePicker
    var validator: Validator? = null









    lateinit var cardEmail: CardView
    lateinit var cardTextEmail: TextView
    lateinit var cardIconEmail: ImageView
    lateinit var cardPhone: CardView
    lateinit var cardIconPhone: ImageView
    lateinit var cardTextPhone: TextView


    lateinit var btnNext: CardView
    lateinit var shimmer:ShimmerFrameLayout


    private var animIn: Animation? = null
    private var animOut: Animation? = null

    lateinit var  rootEmail: LinearLayout
    lateinit var  rootPhone: LinearLayout

    @Email(sequence = 2, messageResId = R.string.enter_a_valid_email_address)
    //@NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    lateinit var edituserName: EditText



    lateinit var editPhone: EditText






    override fun set_layout() {
        setContentView(R.layout.activity_reset_passwrod_selected_way)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }





    override fun init_views() {
        //  ivProfile=iv_profile
        validator = Validator(this)
        validator!!.setValidationListener(this)

        TemplateActivity.registerType = Constants.LOGIN_TYPE_EMAIL
        ivBack=findViewById(R.id.iv_back)
        btnNext=findViewById(R.id.card_next)
        shimmer=findViewById(R.id.shimmer_wait)

        edituserName=findViewById(R.id.edit_email)
        editPhone=findViewById(R.id.edit_phone)

        countryCode=findViewById<CountryCodePicker>(R.id.country_code)


        animIn = AnimationUtils.loadAnimation(this@ResetPasswrodSelectWay, android.R.anim.fade_in)
        animOut = AnimationUtils.loadAnimation(this@ResetPasswrodSelectWay, android.R.anim.fade_out)




        cardEmail=card_email
        rootEmail=findViewById(R.id.root_email)
        rootPhone=findViewById(R.id.root_phone)
        cardTextEmail=findViewById(R.id.tv_email)
        cardIconEmail=findViewById(R.id.iv_email)
        cardPhone=card_phone
        cardTextPhone=findViewById(R.id.tv_phone)
        cardIconPhone=findViewById(R.id.iv_phone)






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
        val imm = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun launchGallery() {
        mAddImages = java.util.ArrayList()
        FilePickerBuilder.instance.setMaxCount(1)
            .setSelectedFiles(mAddImages)
            .setActivityTheme(R.style.filePickerActivityTheme)
            .pickPhoto(this@ResetPasswrodSelectWay)
    }

    override fun init_events() {


        validator = Validator(this)
        validator!!.setValidationListener(this)







        cardEmail.setOnClickListener{
            cardEmail.setCardBackgroundColor(ContextCompat.getColor(this@ResetPasswrodSelectWay,R.color.login_btn))
            cardPhone.setCardBackgroundColor(ContextCompat.getColor(this@ResetPasswrodSelectWay,R.color.white))


            editPhone.setText("")
            cardTextEmail.setTextColor(ContextCompat.getColor(this@ResetPasswrodSelectWay,R.color.white))
            cardTextPhone.setTextColor(ContextCompat.getColor(this@ResetPasswrodSelectWay,R.color.login_btn))

            iv_email.setColorFilter(ContextCompat.getColor(this@ResetPasswrodSelectWay, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
            iv_phone.setColorFilter(ContextCompat.getColor(this@ResetPasswrodSelectWay, R.color.login_btn), android.graphics.PorterDuff.Mode.MULTIPLY)

            cardEmail.radius=BasicTools.dpToPxFloat(10,this@ResetPasswrodSelectWay)
            cardPhone.radius=BasicTools.dpToPxFloat(10,this@ResetPasswrodSelectWay)

            rootEmail.visible()
            rootEmail.animation=animIn
            animIn!!.setDuration(2000)

            rootPhone.gone()
            rootPhone.animation=animOut
            animOut!!.setDuration(2000)


            TemplateActivity.registerType = Constants.LOGIN_TYPE_EMAIL



            openSoftKeyboard(edituserName)



        }


        cardPhone.setOnClickListener {

            cardPhone.setCardBackgroundColor(ContextCompat.getColor(this@ResetPasswrodSelectWay,R.color.login_btn))
            cardEmail.setCardBackgroundColor(ContextCompat.getColor(this@ResetPasswrodSelectWay,R.color.white))


            edituserName.setText("")
            cardTextPhone.setTextColor(ContextCompat.getColor(this@ResetPasswrodSelectWay,R.color.white))
            cardTextEmail.setTextColor(ContextCompat.getColor(this@ResetPasswrodSelectWay,R.color.login_btn))

            iv_phone.setColorFilter(ContextCompat.getColor(this@ResetPasswrodSelectWay, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
            iv_email.setColorFilter(ContextCompat.getColor(this@ResetPasswrodSelectWay, R.color.login_btn), android.graphics.PorterDuff.Mode.MULTIPLY)

            cardEmail.radius=BasicTools.dpToPxFloat(10,this@ResetPasswrodSelectWay)
            cardPhone.radius=BasicTools.dpToPxFloat(10,this@ResetPasswrodSelectWay)


            rootPhone.visible()
            rootPhone.animation=animIn
            animIn!!.setDuration(2000)

            rootEmail.gone()
            rootEmail.animation=animOut
            animOut!!.setDuration(2000)

            TemplateActivity.registerType = Constants.LOGIN_TYPE_PHONE

            openSoftKeyboard(editPhone)
        }






        btnNext.setOnClickListener {
            validator!!.validate()
        }


        ivBack.setOnClickListener {
            BasicTools.exitActivity(this@ResetPasswrodSelectWay)
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
                        Glide.with(this@ResetPasswrodSelectWay).load(items.get(0)).placeholder(
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
        BasicTools.showValidationErrorMessagesForFields(errors!!, this@ResetPasswrodSelectWay)
    }

    override fun onValidationSucceeded() {

        if(TemplateActivity.registerType.equals(Constants.LOGIN_TYPE_EMAIL) &&edituserName.text.isEmpty()){
            showToastMessage(R.string.enter_a_valid_email_address)
            return
        }
        else if(TemplateActivity.registerType.equals(Constants.LOGIN_TYPE_PHONE) &&editPhone.text.isEmpty()){
            showToastMessage(R.string.enter_phone_for_account)

            return
        }



        checkPhoneRQ(editPhone.text.trim().toString())


    }

    fun checkPhoneRQ(txt:String){
        if (BasicTools.isConnected(this@ResetPasswrodSelectWay)) {



            BasicTools.showShimmer(btnNext,shimmer,true)

            var map = HashMap<String, String>()
            var lang = HashMap<String, String>()

            if(BasicTools.isDeviceLanEn())
                lang.put("lang","en")
            else   lang.put("lang","ar")


            if(TemplateActivity.registerType.equals(Constants.LOGIN_TYPE_EMAIL)){
                map.put("email",edituserName.text.trim().toString())
                TemplateActivity.registerValue =edituserName.text.trim().toString()
                TemplateActivity.registerValueCode =""


            }

            else if(TemplateActivity.registerType.equals(Constants.LOGIN_TYPE_PHONE)){
                map.put("phone_number",editPhone.text.trim().toString())
                map.put("country_code",countryCode.selectedCountryCode)
                TemplateActivity.registerValueCode = countryCode.selectedCountryCode
                TemplateActivity.registerValue = editPhone.text.trim().toString()

            }
            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(this@ResetPasswrodSelectWay).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.checkPhone(map,lang)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CheckEmailPhoneModel>(this@ResetPasswrodSelectWay) {
                        override fun onSuccess(result: CheckEmailPhoneModel) {


                            BasicTools.showShimmer(btnNext,shimmer,false)


                          //  TemplateActivity.passwrodSignUp =editPassword.text.toString()

                            if(!result.messages.isNullOrEmpty()){
                                if(TemplateActivity.registerType.equals(Constants.LOGIN_TYPE_EMAIL))
                                    sendCodeToEmail()

                                else if(TemplateActivity.registerType.equals(Constants.LOGIN_TYPE_PHONE))
                                    BasicTools.openActivity(this@ResetPasswrodSelectWay,
                                        ResetPasswordPhoneCode::class.java,false)
                            }


                            else{
                                showToastMessage(R.string.faild)
                              //  showToastMessage(result.messages!!.get(0))

                            }


                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(btnNext,shimmer,false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {

            showToastMessage(R.string.no_connection)}
    }



    fun sendCodeToEmail(){
        if (BasicTools.isConnected(this@ResetPasswrodSelectWay)) {



            BasicTools.showShimmer(btnNext,shimmer,true)





            var map=HashMap<String,String>()

            map.put("email",edituserName.text.trim().toString())


            TemplateActivity.signupEmail=edituserName.text.trim().toString()


            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(this@ResetPasswrodSelectWay),  BasicTools.getProtocol(this@ResetPasswrodSelectWay).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.sendCodeToEmail(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(this@ResetPasswrodSelectWay) {
                        override fun onSuccess(result: ResponseBody) {


                            BasicTools.showShimmer(btnNext,shimmer,false)

                            BasicTools.openActivity(this@ResetPasswrodSelectWay,
                                ResetPasswordEmailCode::class.java,false)




                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(btnNext,shimmer,false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {

            showToastMessage(R.string.no_connection)}
    }


}
