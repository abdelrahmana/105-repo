package com.urcloset.smartangle.activity.signUp


import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
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
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.demo105.ActivitySellerProfile
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.adapter.project105.CountrySpinnerAdapter
import com.urcloset.smartangle.adapter.project105.CountryWithCitySpinnerAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.project_105.*
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.Constants
import com.urcloset.smartangle.tools.TemplateActivity
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_complete_your_data_2.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class CompleteDataAct : TemplateActivity() ,Validator.ValidationListener{

    lateinit var spinnerCountry:Spinner
    lateinit var spinnerCountryWithCity:Spinner


    var selectedImage=false
    lateinit var cardDone:CardView

    lateinit var shimmer:ShimmerFrameLayout
    lateinit var shimmerCountry:ShimmerFrameLayout
    lateinit var shimmerCountryWithCity:ShimmerFrameLayout

    lateinit var adapterCountry:CountrySpinnerAdapter
    lateinit var adapterCountryWithCity:CountryWithCitySpinnerAdapter


    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    lateinit var editName:EditText


    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    @Email(sequence = 2, messageResId = R.string.enter_a_valid_email_address)
    lateinit var editEmail:EditText

    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    lateinit var editPhone:EditText


    lateinit var countryCode: CountryCodePicker
    var disposable= CompositeDisposable()
    private lateinit var disposableContanct: Disposable

    private var mAddImages =  ArrayList<Uri>()
    lateinit var ivProfile: ImageView
    var items=ArrayList<Uri>()
    var validator: Validator? = null
    override fun set_layout() {
        setContentView(R.layout.activity_complete_your_data_2)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {
        validator = Validator(this)
        validator!!.setValidationListener(this)

        spinnerCountry=findViewById(R.id.spinner_country)
        spinnerCountryWithCity=findViewById(R.id.spinner_city)

        editEmail=findViewById(R.id.edit_email)
        editPhone=findViewById(R.id.edit_phone)
        editName=findViewById(R.id.edit_full_name)

        ivProfile=findViewById(R.id.iv_profile)

        cardDone=findViewById(R.id.card_done)
        shimmer=findViewById(R.id.shimmer_done)
        countryCode=findViewById(R.id.country_code)


        shimmerCountry=findViewById(R.id.shimmer_country)
        shimmerCountryWithCity=findViewById(R.id.shimmer_city)

    }

    override fun init_events() {


        getCountry()

        if(registerType.equals(Constants.LOGIN_TYPE_EMAIL)){

            editEmail.setText(registerValue)
            editEmail.isEnabled=false

        }

        else if(registerType.equals(Constants.LOGIN_TYPE_PHONE)){

            editPhone.setText(registerValue)
            editPhone.isEnabled=false
            countryCode.isEnabled=false
            countryCode.setCountryForPhoneCode(registerValueCode.toInt())
        }

        cardDone.setOnClickListener {
            validator!!.validate()

        }
        
        
        ivProfile.setOnClickListener {

            pickGalleryImages()
        }



        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position>=0){

                    var item:CountryModel.Data= adapterCountry.getItem(position)!!
                    getCountryWithCity(item.id.toString())


                }

            }
        }

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

            Constants.PERMISSIONS_REQUEST_READ_CONTACTS->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    retrieveContactsList()
                } else {
                    BasicTools.openActivity(this@CompleteDataAct,HomeActivity::class.java,true)
                    //  toast("Permission must be granted in order to display contacts information")
                }
            }
        }
    }

    private fun launchGallery() {
        mAddImages = java.util.ArrayList()
        FilePickerBuilder.instance.setMaxCount(1)
            .setSelectedFiles(mAddImages)
            .setActivityTheme(R.style.filePickerActivityTheme)
            .pickPhoto(this@CompleteDataAct)
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
                        Glide.with(this@CompleteDataAct).load(items.get(0)).placeholder(
                            R.drawable.logo1
                        ).error(R.drawable.logo1).into(ivProfile)
                        TemplateActivity.photosUri.clear()
                        TemplateActivity.photosUri.add(items.get(0))
                        selectedImage=true
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

    override fun onValidationSucceeded() {

       // retrieveContactsList()
        if(selectedImage)
        SignUpRQ()
        else SignUpRQ2()
    }

    override fun onValidationFailed(errors: List<ValidationError>?) {
        BasicTools.showValidationErrorMessagesForFields(errors!!, this@CompleteDataAct)
    }



    fun getCountry(){
        if (BasicTools.isConnected(this@CompleteDataAct)) {



            BasicTools.showShimmer(spinnerCountry,shimmerCountry,true)

            var map = HashMap<String, String>()
            val shopApi = ApiClient.getClient(
               BasicTools.getProtocol(this@CompleteDataAct).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.getCountry()
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CountryModel>(this@CompleteDataAct) {
                        override fun onSuccess(result: CountryModel) {


                            BasicTools.showShimmer(spinnerCountry,shimmerCountry,false)


                            adapterCountry= CountrySpinnerAdapter(this@CompleteDataAct,
                            result.data!!)

                            spinnerCountry.adapter=adapterCountry


                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(spinnerCountry,shimmerCountry,false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            BasicTools.showShimmer(spinnerCountry,shimmerCountry,false)
            showToastMessage(R.string.no_connection)}
    }


    fun getCountryWithCity(id:String){
        if (BasicTools.isConnected(this@CompleteDataAct)) {



            BasicTools.showShimmer(spinnerCountryWithCity,shimmerCountryWithCity,true)

            var map = HashMap<String, String>()
            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(this@CompleteDataAct).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.getCountryWithCity(id)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CountryWithCity>(this@CompleteDataAct) {
                        override fun onSuccess(result: CountryWithCity) {


                            BasicTools.showShimmer(spinnerCountryWithCity,shimmerCountryWithCity,false)


                            adapterCountryWithCity= CountryWithCitySpinnerAdapter(this@CompleteDataAct,
                                result.data?.citties!!)

                            spinnerCountryWithCity.adapter=adapterCountryWithCity


                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(spinnerCountryWithCity,shimmerCountryWithCity,false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {

            showToastMessage(R.string.no_connection)}
    }


    private fun getFile(uri: Uri): File?{
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r", null)

        parcelFileDescriptor?.let {
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(cacheDir, getFileName(uri))
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                FileUtils.copy(inputStream,outputStream)
            }*/
            return file
        }
        return null
    }

    fun getFileName(fileUri: Uri): String {

        var name = ""
        val returnCursor = contentResolver.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }
    fun SignUpRQ(){
        if (BasicTools.isConnected(this@CompleteDataAct)) {


            val file = getFile(TemplateActivity.photosUri.get(0))!!
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val bodyImage = MultipartBody.Part.createFormData("image", file.getName(), requestFile)


            var map = HashMap<String, String>()
            map.put("password",TemplateActivity.passwrodSignUp)
            map.put("email",editEmail.text.trim().toString())
            map.put("name",editName.text.trim().toString())
            map.put("phone_number",editPhone.text.trim().toString())
            map.put("country_code",countryCode.selectedCountryCode)

            map.put("city_id",(spinnerCountryWithCity.selectedItem as CountryWithCity.Data.Citty).id.toString())
            map.put("country_id",(spinnerCountry.selectedItem as CountryModel.Data).id.toString())

            BasicTools.showShimmer(cardDone,shimmer,true)
            val shopApi = ApiClient.getClient(BasicTools.getProtocol(this@CompleteDataAct).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.signUp(bodyImage,map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<LoginResponseModel>(this@CompleteDataAct) {
                        override fun onSuccess(result: LoginResponseModel) {

                            BasicTools.showShimmer(cardDone,shimmer,false)

                            if(result.status!!){



                                TemplateActivity.loginResponse=result
                                BasicTools.setIsSocial(this@CompleteDataAct,false)
                                BasicTools.setLoginType(this@CompleteDataAct, registerType)
                                BasicTools.setToken(this@CompleteDataAct,result.data?.accessToken!!.toString())
                                BasicTools.setAgreementsTerms(
                                    this@CompleteDataAct,
                                    result.data?.user?.is_agree?:false
                                )
//                                BasicTools.setPhoneUser(this@CompleteDataAct,result.data?.user?.phoneNumber!!.toString())
//                                BasicTools.setUserCountryCode(this@CompleteDataAct,countryCode.selectedCountryCode)
                                BasicTools.setPassword(this@CompleteDataAct,TemplateActivity.passwrodSignUp)

                                BasicTools.setUserModel(
                                    this@CompleteDataAct,
                                    result.data
                                )
                                if(!result.data?.user?.email.isNullOrEmpty())
                                BasicTools.setUserName(this@CompleteDataAct,result.data?.user?.email.toString())
                                else BasicTools.setUserName(this@CompleteDataAct,"")

                                if(!result.data?.user?.phoneNumber.isNullOrEmpty())
                                    BasicTools.setPhoneUser(this@CompleteDataAct,result.data?.user?.phoneNumber!!.toString())

                                else  BasicTools.setPhoneUser(this@CompleteDataAct,"")
                                if(!result.data?.user?.countryCode.isNullOrEmpty())
                                    BasicTools.setUserCountryCode(this@CompleteDataAct,
                                        result.data?.user?.countryCode!!.toString())

                                else  BasicTools.setUserCountryCode(this@CompleteDataAct,"")



                                retrieveContactsList()
//                                BasicTools.openActivity(this@CompleteDataAct,HomeActivity::class.java,true)
                            }



                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(cardDone,shimmer,false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            BasicTools.showShimmer(cardDone,shimmer,false)
            showToastMessage(R.string.no_connection)}
    }

    fun SignUpRQ2(){
        if (BasicTools.isConnected(this@CompleteDataAct)) {


            var map = HashMap<String, String>()
            map.put("password",TemplateActivity.passwrodSignUp)
            map.put("email",editEmail.text.trim().toString())
            map.put("name",editName.text.trim().toString())
            map.put("phone_number",editPhone.text.trim().toString())
            map.put("country_code",countryCode.selectedCountryCode)
            map.put("city_id",(spinnerCountry.selectedItem as CountryModel.Data).id.toString())
            map.put("country_id",(spinnerCountryWithCity.selectedItem as CountryWithCity.Data.Citty).id.toString())

            BasicTools.showShimmer(cardDone,shimmer,true)
            val shopApi = ApiClient.getClient(BasicTools.getProtocol(this@CompleteDataAct).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.signUpWithoutImage(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<LoginResponseModel>(this@CompleteDataAct) {
                        override fun onSuccess(result: LoginResponseModel) {

                            BasicTools.showShimmer(cardDone,shimmer,false)

                            if(result.status!!){



                                TemplateActivity.loginResponse=result
                                BasicTools.setIsSocial(this@CompleteDataAct,false)
                                BasicTools.setLoginType(this@CompleteDataAct, registerType)
                                BasicTools.setToken(this@CompleteDataAct,result.data?.accessToken!!.toString())
                             //   BasicTools.setUserName(this@CompleteDataAct,result.data?.user?.email!!.toString())
                              //  BasicTools.setPhoneUser(this@CompleteDataAct,result.data?.user?.phoneNumber!!.toString())
                              //  BasicTools.setUserCountryCode(this@CompleteDataAct,countryCode.selectedCountryCode)
                                BasicTools.setPassword(this@CompleteDataAct,TemplateActivity.passwrodSignUp)
                                BasicTools.setAgreementsTerms(
                                    this@CompleteDataAct,
                                    result.data?.user?.is_agree?:false
                                )

                                BasicTools.setUserModel(
                                    this@CompleteDataAct,
                                    result.data
                                )
                                if(!result.data?.user?.email.isNullOrEmpty())
                                    BasicTools.setUserName(this@CompleteDataAct,result.data?.user?.email.toString())
                                else BasicTools.setUserName(this@CompleteDataAct,"")

                                if(!result.data?.user?.phoneNumber.isNullOrEmpty())
                                    BasicTools.setPhoneUser(this@CompleteDataAct,result.data?.user?.phoneNumber!!.toString())

                                else  BasicTools.setPhoneUser(this@CompleteDataAct,"")


                                if(!result.data?.user?.countryCode.isNullOrEmpty())
                                    BasicTools.setUserCountryCode(this@CompleteDataAct,
                                        result.data?.user?.countryCode!!.toString())

                                else  BasicTools.setUserCountryCode(this@CompleteDataAct,"")



                                retrieveContactsList()
                             //BasicTools.openActivity(this@CompleteDataAct,HomeActivity::class.java,true)
                            }



                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(cardDone,shimmer,false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            BasicTools.showShimmer(cardDone,shimmer,false)
            showToastMessage(R.string.no_connection)}
    }


    private fun retrieveContactsList() {

        var list=ArrayList<ContactModel>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    Log.i("TEST_TEST","FRIST")
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                Constants.PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //callback onRequestPermissionsResult
        } else {
            Log.i("TEST_TEST","SECOND")

            disposableContanct = getContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    var contactsList = it
                    if (contactsList.isNotEmpty()) {

                        Log.i("TEST_TEST","THREED")
                        list.addAll(contactsList)

                     //    list.add(contactsList.get(0))

                        for (i in contactsList) {

                            Log.i("TEST_TEST", "${i.toString()}")
                        }
                        sendContacntToServer(list)

                    }else{
                        Log.i("TEST_TEST","FORD")
                        BasicTools.openActivity(this@CompleteDataAct,HomeActivity::class.java,true)

                    }
                }






        }
    }

    fun sendContacntToServer(items:ArrayList<ContactModel>){
        if (BasicTools.isConnected(this@CompleteDataAct)) {

            BasicTools.showShimmer(cardDone,shimmer,true)



            var item=ContactModelList(items)



           // var body=ContactModelList(items)
            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(this@CompleteDataAct).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.sendContact(item)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(this@CompleteDataAct) {
                        override fun onSuccess(result: ResponseBody) {

                            BasicTools.showShimmer(cardDone,shimmer,false)
                            BasicTools.openActivity(this@CompleteDataAct,HomeActivity::class.java,true)

                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(cardDone,shimmer,false)
                            BasicTools.openActivity(this@CompleteDataAct,HomeActivity::class.java,true)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            BasicTools.showShimmer(spinnerCountry,shimmerCountry,false)
            showToastMessage(R.string.no_connection)}
    }

    private fun getContacts(): Observable<MutableList<ContactModel>> {
        return Observable.create { emitter ->
            val list: MutableList<ContactModel> = ArrayList()
            val contentResolver: ContentResolver = contentResolver

            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.CUSTOM_RINGTONE,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )
            val cursor =
                contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER + ">0 AND LENGTH(" + ContactsContract.CommonDataKinds.Phone.NUMBER + ")>0",
                    null,
                    "display_name ASC"
                )

            if (cursor != null && cursor.count > 0) {

                var lastHeader = ""
                while (cursor.moveToNext()) {
                    val id =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                    val person =
                        ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI,
                            id.toLong()
                        )
                    val ring =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CUSTOM_RINGTONE))
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val mobileNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    //    val header = getFirstChar(name)
                    /*      if (header != lastHeader) {
                              lastHeader = header
                              list.add(
                                  ContactItemViewState(
                                      header,
                                      View.GONE,
                                      View.VISIBLE,
                                      "",
                                      name,
                                      "",
                                      "",
                                      null
                                  )
                              )
                          }*/

                    list.add(
                        ContactModel(
                            name,
                            mobileNumber

                        )
                    )
                }
                cursor.close()
            }
            emitter.onNext(list)
            emitter.onComplete()
        }
    }

}
