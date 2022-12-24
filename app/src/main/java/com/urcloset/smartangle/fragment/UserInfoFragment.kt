package com.urcloset.smartangle.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.hbb20.CountryCodePicker
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.urcloset.smartangle.BuildConfig
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.homeActivity.HomeActivity

import com.urcloset.smartangle.activity.verification_code.EnterCodeActivity
import com.urcloset.smartangle.adapter.project105.CountrySpinnerAdapter
import com.urcloset.smartangle.adapter.project105.CountryWithCitySpinnerAdapter
import com.urcloset.smartangle.adapter.project105.LocationsEditProfileSpinnerAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.project_105.CheckEmailPhoneModel
import com.urcloset.smartangle.model.project_105.CountryModel
import com.urcloset.smartangle.model.project_105.CountryWithCity
import com.urcloset.smartangle.model.project_105.LoginResponseModel
import com.urcloset.smartangle.tools.*
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class UserInfoFragment : TemplateFragment(),Validator.ValidationListener {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationPermissionGranted = false


    private var selectedLocation: Location?=null

    lateinit var spinnerCountry: Spinner
    lateinit var spinnerCountryWithCity: Spinner
    lateinit var spinnerLocations: Spinner



    var currentEmail=true
    var currentPhone=true

    var selectedImg=false
    var firstTimeCountry=false
    var firstTimeCountryWithCity=false
    var selectedCurrentLocation=false

    lateinit var cardDone: CardView

    lateinit var shimmer: ShimmerFrameLayout
    lateinit var shimmerCountry: ShimmerFrameLayout
    lateinit var shimmerCountryWithCity: ShimmerFrameLayout

    lateinit var adapterCountry: CountrySpinnerAdapter
    lateinit var adapterCountryWithCity: CountryWithCitySpinnerAdapter


    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    lateinit var editName: EditText


    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    @Email(sequence = 2, messageResId = R.string.enter_a_valid_email_address)
    lateinit var editEmail: EditText

    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    lateinit var editPhone: EditText


    lateinit var countryCode: CountryCodePicker

    lateinit var locationAdapter: LocationsEditProfileSpinnerAdapter
    var disposable= CompositeDisposable()
    private var mAddImages =  ArrayList<Uri>()
    lateinit var ivProfile: ImageView
    var items=ArrayList<Uri>()
    var validator: Validator? = null

    lateinit var ivBack:ImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)

        spinnerCountry=view.findViewById(R.id.spinner_country)
        spinnerCountryWithCity=view.findViewById(R.id.spinner_city)
        spinnerLocations=view.findViewById(R.id.spinner_locations)

        editEmail=view.findViewById(R.id.edit_email)
        editPhone=view.findViewById(R.id.edit_phone)
        editName=view.findViewById(R.id.edit_full_name)

        ivProfile=view.findViewById(R.id.iv_profile)

        cardDone=view.findViewById(R.id.card_done)
        shimmer=view.findViewById(R.id.shimmer_done)
        countryCode=view.findViewById(R.id.country_code)


        shimmerCountry=view.findViewById(R.id.shimmer_country)
        shimmerCountryWithCity=view.findViewById(R.id.shimmer_city)
        ivBack=view.findViewById(R.id.iv_back)

        return view
    }

    override fun init_views() {
        validator = Validator(this)
        validator!!.setValidationListener(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(parent!!)



    }

    override fun init_events() {


        ivBack.setOnClickListener {
            parent!!.onBackPressed()
        }

        var locationsList=BasicTools.returnArrayListFormId(parent!!,R.array.location_selected)

        locationAdapter= LocationsEditProfileSpinnerAdapter(parent!!,locationsList)
        spinnerLocations.adapter=locationAdapter


        editEmail.setText("${TemplateActivity.loginResponse?.data?.user?.email}")
        editPhone.setText("${TemplateActivity.loginResponse?.data?.user?.phoneNumber}")
        editName.setText("${TemplateActivity.loginResponse?.data?.user?.name}")

        if(TemplateActivity.loginResponse?.data?.user?.countryCode!=null)

        countryCode.setCountryForPhoneCode(TemplateActivity.loginResponse?.data?.user?.countryCode!!.toInt())


        if(TemplateActivity.loginResponse?.data?.user?.image!=null)
        BasicTools.loadImage(
            BasicTools.getUrlImg(parent!!,TemplateActivity.loginResponse?.data?.user?.image!!),
            ivProfile,object : DownloadListener {
                override fun completed(status: Boolean, bitmap: Bitmap) {
                    var anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                    anim!!.setDuration(1000)
                    ivProfile.animation= anim




                }
            })

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


        spinnerLocations.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position==0)
                    selectedCurrentLocation=false

              else  if(position==1){
                    selectedCurrentLocation=true
                    if (checkMapServices()) {
                        if (!mLocationPermissionGranted) {
                            getLocationPermission() // get Permission
                        } else {
                            //open map

                            getLastKnownLocation()

                        }
                    }
                }

            }
        }



//             try {
//                val file = File("pathOfFile")
//                if(file.exists()) {
//                    val uri = FileProvider.getUriForFile(parent!!, BuildConfig.APPLICATION_ID + ".provider", file)
//                    val intent = Intent(Intent.ACTION_SEND)
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                    intent.setType("audio/*")
//                    intent.putExtra(Intent.EXTRA_STREAM, uri)
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent)
//                }
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//
//            }
        ivProfile.setOnClickListener {





          pickGalleryImages()
        }

        cardDone.setOnClickListener {
            validator!!.validate()
        }



        getCountry()


    }

    fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(parent!!) //alert dialog object (yes,no)
        builder.setMessage(resources.getString(R.string.gps_hint))
            .setCancelable(true)
            .setPositiveButton(resources.getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->
                // K Button
                val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(enableGpsIntent, Constants.PERMISSIONS_REQUEST_ENABLE_GPS)
            }).setNegativeButton(resources.getString(R.string.no), DialogInterface.OnClickListener{ dialog, id ->
                dialog.dismiss()
            })
        val alert = builder.create()
        alert.show()

    }

    fun isServicesOK(): Boolean {
        Log.d("TEST_TEST", "isServicesOK: checking google services version")

        val available =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(parent!!)

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d("TEST_TEST", "isServicesOK: Google Play Services is working")
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d("TEST_TEST", "isServicesOK: an error occured but we can fix it")
            val dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(parent!!, available, Constants.ERROR_DIALOG_REQUEST)
            dialog?.show()
        } else {
            parent!!.showToastMessage("You can't make map requests")
        }
        return false
    }

    fun isMapsEnabled(): Boolean {

        //get LOCATION_SERVICE object : service state
        val manager =parent!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //if the GPS service not enable show the alert Dialog
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
            return false
        }
        return true
    }




    fun checkMapServices(): Boolean {
        if (isServicesOK()) {  //
            if (isMapsEnabled()) {
                return true
            }
        }
        return false

    }


    fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                parent!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        ) { //PERMISSION_GRANTED : the device accept it the fist run at  lolipop and above and accept in installation below lolipop
            mLocationPermissionGranted = true //GPS Permission is ok

            getLastKnownLocation()
            //TODO OPEN THIS
            //   BasicTools.openActivity(this@LocationActivity,SelectedCityActivity::class.java,false)






        }





        else {
            Log.i("TES_TEST","the permisttion")
            //runtime Permission
            ActivityCompat.requestPermissions(
                parent!!,
                arrayOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
                Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }

    }

    fun getLastKnownLocation() {
        Log.i("TEST_TEST", "getLastKnownLocation: called.")
        if (ActivityCompat.checkSelfPermission(
                parent!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                parent!!,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("TEST_TEST", "getLastKnownLocation: called1.")
            return
        }

        val locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 2000

            numUpdates=1
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY}


        mFusedLocationClient!!.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {


                        selectedLocation=location
                        Log.i("TEST_TEST",location.latitude.toString())
                        Log.i("TEST_TEST",location.longitude.toString())


                    }

                }
            },
            Looper.myLooper()!!
        )


    }

    private fun getFile(uri: Uri): File?{
        val parcelFileDescriptor = parent!!.contentResolver.openFileDescriptor(uri, "r", null)

        parcelFileDescriptor?.let {
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(parent!!.cacheDir, getFileName(uri))
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
        val returnCursor = parent!!.contentResolver.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            Constants.REQUEST_READ_WRITE_STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    launchGallery()
                } else {
                    parent!!.showToastMessage("permission denied")
                }
            }
            Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ){
                    mLocationPermissionGranted = true
                    getLastKnownLocation()

                }

                else{

                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        Log.i("TEST_TEST","not 1")
                    if(grantResults[1] == PackageManager.PERMISSION_GRANTED)
                        Log.i("TEST_TEST","not 2")
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        Log.i("TEST_TEST","not 3")

                }
            }

        }





    }

    private fun launchGallery() {
        mAddImages = java.util.ArrayList()
        FilePickerBuilder.instance.setMaxCount(1)
            .setSelectedFiles(mAddImages)
            .setActivityTheme(R.style.filePickerActivityTheme)
            .pickPhoto(this)
    }

    private fun pickGalleryImages() {
        if (ContextCompat.checkSelfPermission(
                parent!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                parent!!,
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

                    Log.i("TEST_TEST","eAmait")
                }else{

                    Log.i("TEST_TEST"," not eAmait")
                    if (items.isNotEmpty()){
                        Glide.with(parent!!).load(items.get(0)).placeholder(
                            R.drawable.logo1
                        ).error(R.drawable.logo1).into(ivProfile)
                        TemplateActivity.photosUri.clear()
                        TemplateActivity.photosUri.add(items.get(0))
                        selectedImg=true

                        Log.i("TEST_TEST1","${items.get(0)}")
                    }

//                    for (item in items)
//                        imageAdapter.addItem(item, 0)

                }

            }
        }
        else if(requestCode==Constants.PERMISSIONS_REQUEST_ENABLE_GPS){

                if (mLocationPermissionGranted) {
                    // inflateUserListFragment(); //run fragment
                    getLastKnownLocation()


                } else {
                    getLocationPermission()//get Permission
                }

        }

        else {
            super.onActivityResult(requestCode, resultCode, data)

        }
    }


    override fun init_fragment(savedInstanceState: Bundle?) {

    }

    override fun on_back_pressed(): Boolean {
        return true
    }

    override fun onValidationSucceeded() {

        if(TemplateActivity.loginResponse?.data?.user?.email.isNullOrEmpty())
            TemplateActivity.loginResponse?.data?.user?.email=""
        if(TemplateActivity.loginResponse?.data?.user?.phoneNumber.isNullOrEmpty())
            TemplateActivity.loginResponse?.data?.user?.phoneNumber=""
        if(TemplateActivity.loginResponse?.data?.user?.countryCode.isNullOrEmpty())
            TemplateActivity.loginResponse?.data?.user?.countryCode=""
       var  email=editEmail.text.trim().toString()
        var phone=editPhone.text.trim().toString()



        if(!email.equals(TemplateActivity.loginResponse?.data?.user?.email)){

            checkPhoneRQ(Constants.LOGIN_TYPE_EMAIL)
        }

         else if(!phone.equals(TemplateActivity.loginResponse?.data?.user?.phoneNumber)
            ||
            !countryCode.selectedCountryCode.equals(TemplateActivity.loginResponse?.data?.user?.countryCode)){
            checkPhoneRQ(Constants.LOGIN_TYPE_PHONE)
        }

        else  if(currentEmail &&currentPhone){
            if(selectedImg)
                RQWithImage()
            else RQ()
        }

    }





    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        BasicTools.showValidationErrorMessagesForFields(errors!!, parent!!)
    }

    fun getCountry(){
        if (BasicTools.isConnected(parent!!)) {



            BasicTools.showShimmer(spinnerCountry,shimmerCountry,true)

            var map = HashMap<String, String>()
            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(parent!!).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.getCountry()
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CountryModel>(parent!!) {
                        override fun onSuccess(result: CountryModel) {



                            BasicTools.showShimmer(spinnerCountry,shimmerCountry,false)


                            adapterCountry= CountrySpinnerAdapter(parent!!,
                                result.data!!)

                            spinnerCountry.adapter=adapterCountry


                            if(!firstTimeCountry)
                            for((index, i) in result.data!!.withIndex()){
                                if(i.id.toString().equals(TemplateActivity.loginResponse?.data?.user?.countryId)){
                                    spinnerCountry.setSelection(index)
                                break}
                            }

                            firstTimeCountry=true

                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(spinnerCountry,shimmerCountry,false)
                            parent!!.showToastMessage(R.string.faild)

                        }
                    }))

        }
        else {
            BasicTools.showShimmer(spinnerCountry,shimmerCountry,false)
            parent!!.showToastMessage(R.string.no_connection)}
    }


    fun getCountryWithCity(id:String){
        if (BasicTools.isConnected(parent!!)) {


            BasicTools.showShimmer(spinnerCountryWithCity,shimmerCountryWithCity,true)

            var map = HashMap<String, String>()
            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(parent!!).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.getCountryWithCity(id)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CountryWithCity>(parent!!) {
                        override fun onSuccess(result: CountryWithCity) {


                            BasicTools.showShimmer(spinnerCountryWithCity,shimmerCountryWithCity,false)


                            adapterCountryWithCity= CountryWithCitySpinnerAdapter(parent!!,
                                result.data?.citties!!)

                            spinnerCountryWithCity.adapter=adapterCountryWithCity



                            if(!firstTimeCountryWithCity)
                            for((index, i) in result.data?.citties!!.withIndex()){
                                if(i.id.toString().equals(TemplateActivity.loginResponse?.data?.user?.cityId)){
                                    spinnerCountryWithCity.setSelection(index)
                                break}
                            }


                            firstTimeCountryWithCity=true


                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(spinnerCountryWithCity,shimmerCountryWithCity,false)
                            parent!!.showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {

            parent!!.showToastMessage(R.string.no_connection)}
    }

    fun RQWithImage(){
        if (BasicTools.isConnected(parent!!)) {


            val file = getFile(TemplateActivity.photosUri.get(0))!!
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val bodyImage = MultipartBody.Part.createFormData("image", file.getName(), requestFile)


            var map = HashMap<String, String>()


            map.put("email",editEmail.text.trim().toString())
            map.put("name",editName.text.trim().toString())
            map.put("phone_number",editPhone.text.trim().toString())
            map.put("country_code",countryCode.selectedCountryCode)
            map.put("country_id",(spinnerCountry.selectedItem as CountryModel.Data).id.toString())
            map.put("city_id",(spinnerCountryWithCity.selectedItem as CountryWithCity.Data.Citty).id.toString())
            if(selectedCurrentLocation &&selectedLocation!=null){
                map.put("lat",selectedLocation?.latitude.toString())
                map.put("long",selectedLocation?.longitude.toString())
            }

            BasicTools.showShimmer(cardDone,shimmer,true)
            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(parent!!)
                ,BasicTools.getProtocol(parent!!).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.updateProfileWithImage(bodyImage,map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<LoginResponseModel>(parent!!) {
                        override fun onSuccess(result: LoginResponseModel) {

                            BasicTools.showShimmer(cardDone, shimmer, false)

                            if (result.status!!) {


                                TemplateActivity.loginResponse = result
                                BasicTools.setIsSocial(parent!!, false)

                                BasicTools.setToken(parent!!, result.data?.accessToken!!.toString())
                                BasicTools.setAgreementsTerms(
                                    requireContext(),
                                    result.data?.user?.is_agree?:false
                                )
                                BasicTools.setUserName(
                                    parent!!,
                                    result.data?.user?.email!!.toString()
                                )
                                BasicTools.setUserModel(
                                    parent!!,
                                    result.data
                                )
                                BasicTools.setPhoneUser(
                                    parent!!,
                                    result.data?.user?.phoneNumber!!.toString()
                                )
                                BasicTools.setUserCountryCode(
                                    parent!!,
                                    countryCode.selectedCountryCode
                                )

                                parent!!.showToastMessage(R.string.success)
                                parent!!.onBackPressed()
                            }

                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(cardDone,shimmer,false)
                            parent!!.showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            BasicTools.showShimmer(cardDone,shimmer,false)
            parent!!.showToastMessage(R.string.no_connection)}
    }

    fun RQ(){
        if (BasicTools.isConnected(parent!!)) {


            var map = HashMap<String, String>()


            map.put("email",editEmail.text.trim().toString())
            map.put("name",editName.text.trim().toString())
            map.put("phone_number",editPhone.text.trim().toString())
            map.put("country_code",countryCode.selectedCountryCode)
            map.put("country_id",(spinnerCountry.selectedItem as CountryModel.Data).id.toString())
            map.put("city_id",(spinnerCountryWithCity.selectedItem as CountryWithCity.Data.Citty).id.toString())
            if(selectedCurrentLocation){
                map.put("lat",selectedLocation?.latitude.toString())
                map.put("long",selectedLocation?.longitude.toString())
            }
            BasicTools.showShimmer(cardDone,shimmer,true)
            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(parent!!)
                ,BasicTools.getProtocol(parent!!).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.updateProfile(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<LoginResponseModel>(parent!!) {
                        override fun onSuccess(result: LoginResponseModel) {

                            BasicTools.showShimmer(cardDone, shimmer, false)

                            if (result.status!!) {


                                TemplateActivity.loginResponse = result
                                BasicTools.setIsSocial(parent!!, false)

                                BasicTools.setToken(parent!!, result.data?.accessToken!!.toString())
                                BasicTools.setAgreementsTerms(
                                    requireContext(),
                                    result.data?.user?.is_agree?:false
                                )
                                BasicTools.setUserName(
                                    parent!!,
                                    result.data?.user?.email!!.toString()
                                )
                                BasicTools.setPhoneUser(
                                    parent!!,
                                    result.data?.user?.phoneNumber!!.toString()
                                )
                                BasicTools.setUserCountryCode(
                                    parent!!,
                                    countryCode.selectedCountryCode
                                )



                                parent!!.showToastMessage(R.string.success)
                                parent!!.onBackPressed()


                            }

                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(cardDone,shimmer,false)
                            parent!!.showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            BasicTools.showShimmer(cardDone,shimmer,false)
            parent!!.showToastMessage(R.string.no_connection)}
    }



    fun checkPhoneRQ(txt:String){
        if (BasicTools.isConnected(parent!!)) {



            BasicTools.showShimmer(cardDone,shimmer,true)

            var map = HashMap<String, String>()
            var lang = HashMap<String, String>()

            if(BasicTools.isDeviceLanEn())
                lang.put("lang","en")
            else   lang.put("lang","ar")


            if(txt.equals(Constants.LOGIN_TYPE_EMAIL)){
                map.put("email",editEmail.text.trim().toString())
            }

            else if(txt.equals(Constants.LOGIN_TYPE_PHONE)){
                map.put("phone_number",editPhone.text.trim().toString())
                map.put("country_code",countryCode.selectedCountryCode)
            }
            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(parent!!).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.checkPhone(map,lang)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CheckEmailPhoneModel>(parent!!) {
                        override fun onSuccess(result: CheckEmailPhoneModel) {


                            BasicTools.showShimmer(cardDone,shimmer,false)


                            if(!result.messages.isNullOrEmpty()){
                                if(txt.equals(Constants.LOGIN_TYPE_EMAIL)){

                                    currentEmail=false
                                }

                                else if(txt.equals(Constants.LOGIN_TYPE_PHONE)){
                                    currentPhone=false
                                }

                                parent!!.showToastMessage(result.messages!!.get(0))
                            }


                            else{


                                if(txt.equals(Constants.LOGIN_TYPE_EMAIL)){

                                    currentEmail=true
                                    checkPhoneRQ(Constants.LOGIN_TYPE_PHONE)
                                }

                                else if(txt.equals(Constants.LOGIN_TYPE_PHONE)){
                                    currentPhone=true


                                    if(selectedImg)
                                        RQWithImage()
                                    else RQ()
                                }


                            }
                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(cardDone,shimmer,false)
                            parent!!.showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            parent!!.showToastMessage(R.string.no_connection)}
    }
}