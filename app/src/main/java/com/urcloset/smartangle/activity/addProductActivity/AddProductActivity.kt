package com.urcloset.smartangle.activity.addProductActivity

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.adapter.*
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.dialog.BottomSheetRatApplication
import com.urcloset.smartangle.dialog.HintAddProductDialog
import com.urcloset.smartangle.fragment.bottomsheetagree.ConsentBottomSheet
import com.urcloset.smartangle.fragment.bottomsheetagree.ImplementerPublishConsent
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.model.*
import com.urcloset.smartangle.tools.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AddProductActivity : TemplateActivity() ,IAddProduct
{
    var disposable = CompositeDisposable()
    lateinit var rvCategories: RecyclerView
    lateinit var dialog: HintAddProductDialog
    lateinit var rvConditions: RecyclerView
    lateinit var rvColors: RecyclerView
    val categoryAdapter = CategoryAdapter()
    lateinit var conditionAdapter: ConditionAdapter
    val photoAdapter = PhotoAdapter()
    val colorsAdapter = ColorsAdapter()
    lateinit var sizeAdapter: SizeAdapter
    val photos = ArrayList<AddPhotoModel>()
    lateinit var tvName: EditText
    lateinit var tvPhone : EditText
    lateinit var tvDes: EditText
    lateinit var etPrice: EditText
    lateinit var rvPhotos: RecyclerView
    lateinit var tvYesNegotiable: TextView
    lateinit var tvNoNegotiable: TextView
    lateinit var tvYesBox: TextView
    lateinit var tvNoBox: TextView
    lateinit var tvInvoiceAvaliable: TextView
    lateinit var tvInvoiceUnAvaliable: TextView
    lateinit var duration: Spinner
    lateinit var rvSize: RecyclerView
    lateinit var addButton: Button
    var categoryIdIndex = 0
    var categories = ArrayList<CategoryModel.Category>()
    lateinit var conditions: List<ConditionModel.Condition>
    lateinit var colorList :List<ColorModel.Color>
    var boxAvaliable: Int = 1
    var invoiceAvailable: Int = 1
    var isNegotiable: Int = 1
    var negotiable_sentence_id: Int = -1
    var conditionIdPosition: Int = 0
    lateinit var myaccount: PersonalUserInfoModel.Data.User
    var selectedUsedSentenceIdIndex: Int = 0
    var usedsentance = ArrayList<SentenceModel.Sentence>()
    var sizeList = ArrayList<SizeModel.Size>()
    val uris = ArrayList<Uri>()
    val RESULT_LOAD_IMAGE = 1234
    var lang = "en"
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var shimmerColor:ShimmerFrameLayout
    lateinit var shimmerSizes:ShimmerFrameLayout
    lateinit var shimmerCategories:ShimmerFrameLayout
    lateinit var tvCode:EditText
    lateinit var editPhone:EditText
    var first:String=""
    var next:String=""
    var isOpened = false
    var productMessage=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //openReviewGoogle(applicationContext)

    }

    override fun set_layout() {
        setContentView(R.layout.activity_add_product)

    }

    fun openReviewGoogle(applicationContext: Context) {
     /*   val request = ReviewManagerFactory.create(applicationContext).requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo : ReviewInfo = task.result
                BasicTools.setReviewedBefore(applicationContext,true)
            } else {
                Toast.makeText(this,task.exception?.message?:"",Toast.LENGTH_SHORT).show()
                // There was some problem, log or handle the error code.
                // @ReviewErrorCode val reviewErrorCode = (task.getException()).errorCode
            }
        }*/
        BottomSheetRatApplication()
            .show(supportFragmentManager, "bottom_sheet")
    }
    override fun init_activity(savedInstanceState: Bundle?) {
        lang = "en"
        photos.add(AddPhotoModel(null, R.drawable.dr_add_photo))

        rvCategories.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvCategories.adapter = categoryAdapter
        rvPhotos.adapter = photoAdapter
       rvPhotos.layoutManager = GridLayoutManager(this, 3)
        photoAdapter.submitList(photos)
        rvConditions.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        conditionAdapter = ConditionAdapter(lang)

        rvConditions.adapter = conditionAdapter

        sizeAdapter = SizeAdapter(lang)
        rvSize.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvSize.adapter = sizeAdapter
        getUserProfile()
        getCategories()
        getConditions(lang)
        getGeneralSentance("condition")
        getGeneralSentance("negotiable")
        getColors()
        getSizes()

        // Note
        if(BasicTools.isDeviceLanEn()) {
             first = "<font color='#FF5E5E'>Note: </font>"
             next=
                "<font color='#444444'> When You Add The Product.\n The Price cannot Be Modified </font>"
        }
        else{
             first = "<font color='#FF5E5E'>ملاحظة: </font>"
             next =
                "<font color='#444444'>عند إضافة المنتج لايمكنك تعديل السعر</font>"

        }

       findViewById<TextView>(R.id.tv_note).setText(Html.fromHtml(first + next))

    }


    override fun init_views() {
        rvCategories = findViewById(R.id.rv_categories)
        tvName =findViewById(R.id.tv_name)
        //tvPhone = findViewById(R.id.phoneEdit)
        tvDes = findViewById(R.id.tv_des)
        rvPhotos =findViewById(R.id.rv_photos)
        rvConditions =findViewById(R.id.rv_conditions)
        tvYesNegotiable = findViewById(R.id.tv_yes_negotiable)
        tvNoNegotiable = findViewById(R.id.tv_no_negotiable)
        tvYesBox = findViewById(R.id.tv_ba_y)
        tvNoBox = findViewById(R.id.tv_ba_n)
        tvInvoiceAvaliable = findViewById(R.id.tv_ia_y)
        tvInvoiceUnAvaliable =findViewById(R.id.tv_ia_n)
        duration = findViewById(R.id.sp_duration)
        rvColors =findViewById(R.id.rv_colors)
        rvSize =findViewById(R.id.rv_size)
        addButton = findViewById(R.id.bn_add)
        etPrice =findViewById(R.id.et_price)
        shimmerFrameLayout =findViewById(R.id.shimmer_layout)
        tvCode = findViewById(R.id.tv_code)
        editPhone =findViewById(R.id.phone)
        shimmerColor =findViewById(R.id.shimmer_color)
        shimmerSizes =findViewById(R.id.shimmer_sizes)
        shimmerCategories = findViewById(R.id.shimmer_categories)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val imagesEncodedList = ArrayList<String>()
            if (data.data != null) {
                val selectedImage = data.data

                photos.removeAt(photos.size - 1)
                photos.add(AddPhotoModel(selectedImage!!))
                photos.add(AddPhotoModel(null, R.drawable.dr_add_photo))
                if(photos.size==7){
                    photos.removeAt(photos.size - 1)


                }
                photoAdapter.submitList(photos)
                photoAdapter.notifyDataSetChanged()
                uris.add(data.data!!)


            } else {
                if (data.clipData != null) {
                    val mClipData: ClipData = data.clipData!!
                    val mArrayUri = ArrayList<Uri>()
                    for (i: Int in 0..mClipData.itemCount - 1) {
                        if(photos.size+i<7) {
                            val item = mClipData.getItemAt(i)
                            val uri = item.uri
                            uris.add(uri)
                            mArrayUri.add(uri)
                            val cursor =
                                contentResolver.query(uri, filePathColumn, null, null, null)
                            cursor!!.moveToFirst()
                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            val imageEncoded = cursor.getString(columnIndex)
                            imagesEncodedList.add(imageEncoded)
                            cursor.close()
                        }


                    }
                    addToPhotos(mArrayUri)

                }
            }





            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun addToPhotos(uris: List<Uri>) {
        photos.removeAt(photos.size - 1)

        for (uri in uris) {
            photos.add(AddPhotoModel(uri))

        }
        if(photos.size<6)
        photos.add(AddPhotoModel(null, R.drawable.dr_add_photo))
        photoAdapter.submitList(photos)
        photoAdapter.notifyDataSetChanged()
    }

    override fun init_events() {
        photoAdapter.setOnPhotoListener(object : ItemClickListener {
            override fun onClick(position: Int) {
                val intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE)
            }
        })
        photoAdapter.setOnPhotoCancel(object : ItemClickListener {
            override fun onClick(position: Int) {
                var addExit = false

                if (position < photos.size - 1) {
                    photos.removeAt(position)
                    if (photos.size == 5) {
                        photoAdapter.currentList.forEach {
                            if (it.img != 0) {
                                addExit = true

                            }
                        }
                        if (!addExit)
                            photos.add(AddPhotoModel(null, R.drawable.dr_add_photo))

                    }
                    photoAdapter.submitList(photos)
                    photoAdapter.notifyDataSetChanged()
                }

            }


        })

        tvYesNegotiable.setOnClickListener {
            isNegotiable = 1
            selectText(tvYesNegotiable)
            unSelectText(tvNoNegotiable)
        }
        tvNoNegotiable.setOnClickListener {
            isNegotiable = 0
            selectText(tvNoNegotiable)
            unSelectText(tvYesNegotiable)

        }
        tvNoBox.setOnClickListener {
            boxAvaliable = 0
            selectText(tvNoBox)
            unSelectText(tvYesBox)

        }
        tvYesBox.setOnClickListener {
            boxAvaliable = 1
            selectText(tvYesBox)
            unSelectText(tvNoBox)

        }
        tvInvoiceAvaliable.setOnClickListener {
            invoiceAvailable = 1
            selectText(tvInvoiceAvaliable)
            unSelectText(tvInvoiceUnAvaliable)
        }
        tvInvoiceUnAvaliable.setOnClickListener {
            invoiceAvailable = 0
            selectText(tvInvoiceUnAvaliable)
            unSelectText(tvInvoiceAvaliable)
        }
        rvColors.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvColors.adapter = colorsAdapter
        colorsAdapter.setOnColorListener(object : ItemClickListener {
            override fun onClick(position: Int) {

            }

        })
        addButton.setOnClickListener {
            if (BasicTools.getAgreementsTerms(this))
            addProduct()
            else
                ConsentBottomSheet(ImplementerPublishConsent(),callBackConfirmConsent) // need to add this feature
                    .show(supportFragmentManager, "consent_bottom_sheet")

        }
        categoryAdapter.setOnCategoryClickListener(object : ItemClickListener {
            override fun onClick(position: Int) {
                categoryIdIndex = position
            }

        })
        conditionAdapter.setOnConditionItemSelected(object : ItemClickListener {
            override fun onClick(position: Int) {
                conditionIdPosition = position
                if(position == 1){
                    duration.visibility = View.VISIBLE
                    findViewById<LinearLayout>(R.id.ly_dur).visibility = View.VISIBLE

                }
                else {
                    findViewById<LinearLayout>(R.id.ly_dur).visibility = View.GONE
                    duration.visibility = View.GONE

                }
            }

        })

        duration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                selectedUsedSentenceIdIndex = pos

            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {

            }

        }

        tvCode.setText(TemplateActivity.loginResponse?.data?.user?.countryCode)
        editPhone.setText(TemplateActivity.loginResponse?.data?.user?.phoneNumber)
    }
    val callBackConfirmConsent :(Int)->Unit = {isAgree->
        if (isAgree==1) {
            BasicTools.setAgreementsTerms(this ,true)
            addProduct()
        }

    }


    fun selectText(tv: TextView) {
        tv.background = resources.getDrawable(R.drawable.opt_yes_background)
        tv.setTextColor(resources.getColor(R.color.white))

    }

    fun unSelectText(tv: TextView) {
        tv.background = resources.getDrawable(R.drawable.opt_ng_background)
        tv.setTextColor(resources.getColor(R.color.black))

    }

    override fun set_fragment_place() {
    }

    fun getCategories() {
      BasicTools.showShimmer(rvCategories, shimmerCategories, true)
        val shopApi =
            ApiClient.getClient(

                BasicTools.getProtocol(applicationContext).toString(), "en"
            )
                ?.create(
                    AppApi::class.java
                )

        val observable = shopApi!!.getCategories("no")
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<CategoryModel>(this) {
                    override fun onSuccess(result: CategoryModel) {
                        BasicTools.showShimmer(rvCategories, shimmerCategories, false)
                        shimmerCategories.visibility = View.GONE

                        if (result.status!!) {
                            categories = result.categories as ArrayList<CategoryModel.Category>
                            categoryAdapter.submitList(categories)


                        }

                    }

                    override fun onFailed(status: Int) {
                        BasicTools.showShimmer(rvCategories, shimmerCategories, false)
                        shimmerCategories.visibility = View.GONE

                        super.onFailed(status)
                    }


                })
        )
    }

    fun getConditions(lang: String) {
        val shopApi =
            ApiClient.getClient(
                BasicTools.getProtocol(applicationContext).toString(), lang
            )
                ?.create(
                    AppApi::class.java
                )

        val observable = shopApi!!.getConditions("no")
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<ConditionModel>(this) {
                    override fun onSuccess(result: ConditionModel) {
                        if (result.status!!) {
                            conditions = result.data as List<ConditionModel.Condition>
                            conditionAdapter.submitList(conditions)


                        }

                    }


                })
        )

    }

    fun getGeneralSentance(key: String) {
        val shopApi =
            ApiClient.getClient(
                BasicTools.getProtocol(applicationContext).toString(), lang
            )
                ?.create(
                    AppApi::class.java
                )

        val observable = shopApi!!.getGeneralSentence(key)
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<SentenceModel>(this) {
                    override fun onSuccess(result: SentenceModel) {
                        if (result.status!!) {
                            if (key == "condition") {
                                usedsentance =
                                    (result?.data as ArrayList<SentenceModel.Sentence>?)!!
                                val durations = ArrayList<String>()
                                result.data!!.forEach {
                                    if (BasicTools.isDeviceLanEn())
                                        durations.add(it!!.english!!)
                                    else durations.add(it!!.arabic!!)


                                }

                                // Duration
                                val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                    this@AddProductActivity,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    durations
                                )
                                duration.adapter = arrayAdapter
                            } else {
                                negotiable_sentence_id = result.data?.get(0)!!.id!!
                            }

                        }

                    }


                })
        )

    }

    private fun getColors() {
        BasicTools.showShimmer(rvColors, shimmerColor, true)

        val shopApi =
            ApiClient.getClient(
                BasicTools.getProtocol(applicationContext).toString(), lang
            )
                ?.create(
                    AppApi::class.java
                )

        val observable = shopApi!!.getColors("no")
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<ColorModel>(this) {
                    override fun onSuccess(result: ColorModel) {
                        BasicTools.showShimmer(rvColors, shimmerColor, false)
                        shimmerColor.visibility = View.GONE

                        if (result.status!!) {
                            colorList = result.data as List<ColorModel.Color>
                           val colors =    ArrayList<ColorModel.Color>()
                            colors.clear()
                           result.data.forEach {
                               val color :ColorModel.Color = it
                               color.isSelected = 0
                               colors.add(color)
                           }
                            colorsAdapter.submitList(result.data)

                        }

                    }

                    override fun onFailed(status: Int) {
                        BasicTools.showShimmer(rvColors, shimmerColor, false)
                        shimmerColor.visibility = View.GONE
                        super.onFailed(status)
                    }


                })
        )

    }

    private fun getSizes() {
        BasicTools.showShimmer(rvSize, shimmerSizes, true)
        val shopApi =
            ApiClient.getClient(
                BasicTools.getProtocol(applicationContext).toString(), lang
            )
                ?.create(
                    AppApi::class.java
                )

        val observable = shopApi!!.getSizes("no")
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<SizeModel>(this) {
                    override fun onSuccess(result: SizeModel) {
                        BasicTools.showShimmer(rvSize, shimmerSizes, false)

                        if (result.status!!) {
                            sizeList = result.data as ArrayList<SizeModel.Size>
                            val sizes = result.data
                            sizes.forEach {
                                val size = it
                                if(size.isSelected!!>0)
                                    size.isSelected = 0

                            }
                            sizeAdapter.submitList(sizes)


                        }

                    }

                    override fun onFailed(status: Int) {
                        BasicTools.showShimmer(rvSize, shimmerSizes, false)

                        super.onFailed(status)
                    }


                })
        )

    }

    private fun getFile(uri: Uri): File? {
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

    fun showShimmerAddBtn(state: Boolean) {

        if (state) {
            addButton.visibility = View.GONE
            shimmerFrameLayout.visible()
        } else {
            addButton.visibility = View.VISIBLE
            shimmerFrameLayout.hide()
        }
    }

    private fun addProduct() {
        if(tvName.text.toString().trim().isNotEmpty()) {
            if (tvDes.text.toString().trim().isNotEmpty()) {
                if (etPrice.text.toString().trim().isNotEmpty()) {
                    if(editPhone.text.toString().trim().isNotEmpty()) {
                        if (uris.size > 0) {


                            addButton.isEnabled = false
                            try {

                                val sizes = ArrayList<Int>()
                                val colors = ArrayList<Int>()

                                sizeList.forEach {
                                    if (it.isSelected!! > 0) {
                                        sizes.add(it.id!!)
                                    }

                                }
                                colorList.forEach {
                                    if (it.isSelected!! > 0) {
                                        colors.add(it.id!!)


                                    }
                                }


                                val map = HashMap<String, String>()
                                map.put("name", tvName.text.toString().trim())
                                map.put("description", tvDes.text.toString())
                                map.put("price", etPrice.text.toString())
                                map.put("box_available", boxAvaliable.toString())
                                map.put(
                                    "category_id",
                                    categoryAdapter.currentList[categoryIdIndex].id.toString()
                                )
                                map.put("country_code", tvCode.text.toString())
                                map.put("phone_number", editPhone.text.toString())
                                map.put(
                                    "condition_id",
                                    conditionAdapter.currentList[conditionIdPosition].id.toString()
                                )
                                map.put("invoice_available", invoiceAvailable.toString())
                                map.put("is_negotiable", isNegotiable.toString())
                                map.put("negotiable_sentence_id", "")
                                if (selectedUsedSentenceIdIndex == 1)
                                    map.put(
                                        "used_sentence_id",
                                        usedsentance[selectedUsedSentenceIdIndex].id.toString()
                                    )
                                val files = ArrayList<File>()
                                val images = ArrayList<MultipartBody.Part>()
                                uris.forEach {
                                    val file = getFile(it)!!
                                    files.add(file)
                                    val requestFile =
                                        RequestBody.create(
                                            "multipart/form-data".toMediaTypeOrNull(),
                                            file
                                        )
                                    val bodyImage = MultipartBody.Part.createFormData(
                                        "item_media[]",
                                        file.getName(),
                                        requestFile
                                    )
                                    images.add(bodyImage)

                                }



                               // openDialog(images,sizes,map,colors)
                                addButton.isEnabled = true
                             //   getProductCommision(etPrice.text.toString(),images,sizes,map,colors,this)
                                addProductRQDialog(images,sizes,map,colors)

                            } catch (e: Exception) {
                                e.printStackTrace()
                                    addButton.isEnabled = true
                                showShimmerAddBtn(false)
                                Log.d("Photo", "addProduct: " + e.message.toString())

                            }
                        } else {
                            Toast.makeText(
                                this@AddProductActivity,
                                getString(R.string.you_mst_add_photo),
                                Toast.LENGTH_SHORT
                            ).show()


                        }
                    }
                    else {
                        Toast.makeText(
                            this@AddProductActivity,
                            getString(R.string.phone_required),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        this@AddProductActivity,
                        getString(R.string.price_required),
                        Toast.LENGTH_SHORT
                    ).show()


                }
            } else {
                Toast.makeText(
                    this@AddProductActivity,
                    getString(R.string.des_required),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
            else{
            Toast.makeText(
                this@AddProductActivity,
                getString(R.string.name_required),
                Toast.LENGTH_SHORT
            ).show()
            }


    }

    fun addProductRQ(  images:ArrayList<MultipartBody.Part>,sizes:ArrayList<Int>,map:Map<String,String>,colors:ArrayList<Int>){
        showShimmerAddBtn(true)
        val shopApi =
            ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(applicationContext).toString(), lang
            )
                ?.create(
                    AppApi::class.java
                )

        val observable = shopApi!!.createProduct(
            images,
            map,
            sizes = sizes,
            colors = colors

        )
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    AppObservable<CreateProductModel>(this) {
                    override fun onSuccess(result: CreateProductModel) {
                        showShimmerAddBtn(false)
                        Toast.makeText(this@AddProductActivity,getString(R.string.add_compeleted)
                            ,Toast.LENGTH_SHORT).show()
                 //       if (!BasicTools.getRevviewedBefore(applicationContext))
                  //      openReviewGoogle(applicationContext)
                    /*    if (!BasicTools.getRevviewedBefore(this@AddProductActivity))
                            BasicTools.openReviewGoogle(this@AddProductActivity)*/
                        startActivity(Intent(this@AddProductActivity, HomeActivity::class.java).putExtra("show_review",true))
                        finish()
                        /*   BasicTools.openActivity(
                            this@AddProductActivity,
                            HomeActivity::class.java,
                            true
                        ) //OrderSentActivity*/
                        addButton.isEnabled = true

                    }

                    override fun onFailed(status: Int) {
                        showShimmerAddBtn(false)
                        Log.d("Photo", "addProduct: " + status.toString())

                     //   showToastMessage(getString(R.string.faild))
                        addButton.isEnabled = true
                        showShimmerAddBtn(false)

                        super.onFailed(status)
                    }
                    override fun onFailed(error: String) {
                        FirebaseCrashlytics.getInstance().recordException(Exception(error))
                        showToastMessage(error)
                        super.onFailed(error)
                    }


                })
        )
    }
    fun getUserProfile() {
        val shopApi =
            ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(applicationContext).toString(), "en"
            )
                ?.create(
                    AppApi::class.java
                )

        val observable = shopApi!!.getPersonalInformation()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<PersonalUserInfoModel>(this) {
                    override fun onSuccess(result: PersonalUserInfoModel) {
                        if (result.status!!) {
                            myaccount = result.data!!.user!!


                        }

                    }
                }
                ))
    }

    override fun addProductRQDialog(
        images: ArrayList<MultipartBody.Part>,
        sizes: ArrayList<Int>,
        map: Map<String, String>,
        colors: ArrayList<Int>
    ) {
        addProductRQ(images,sizes,map,colors)
    }
    fun getProductCommision(price:String, images:ArrayList<MultipartBody.Part>,
                             sizes:ArrayList<Int>,
                            map:Map<String,String>,
                             colors:ArrayList<Int>,iview:IAddProduct){
        var commision :String?=""
        val lang =  if(BasicTools.isDeviceLanEn(this))
             "en"
        else "ar"
        if(BasicTools.isConnected(this)) {
            showShimmerAddBtn(true)

            val shopApi =
                ApiClient.getClient(
                    BasicTools.getProtocol(applicationContext).toString(),
                    lang
                )
                    ?.create(
                        AppApi::class.java
                    )

            val observable = shopApi!!.getCommission(price)
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CommissionModel>(this) {
                        override fun onSuccess(result: CommissionModel) {

                            if (result.status!!) {
//                                result.data.let {
//                                    if (it?.isFree!! > 0) {
//                                        if (it.type != "fixed") {
//                                            commision =
//                                                (it.value)?.toDouble()?.times(etPrice.text.toString().toDouble())
//
//                                            commision= commision?.div(100)
//                                        } else commision = it.value?.toDouble()
//
//                                    }
//
//                                }
                             commision = result.data!!.value!!
                               val message= getCommisionMessage(commision.toString())
                                dialog = HintAddProductDialog(
                                    this@AddProductActivity,
                                    images, sizes, map, colors, iview,message
                                )
                                dialog.window!!.attributes.windowAnimations = R.style.dialogAnim

                                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                dialog.show()

                            }


                        }

                        override fun onComplete() {
                            showShimmerAddBtn(false)

                            super.onComplete()
                        }

                        override fun onFailed(status: Int) {
                            showToastMessage(R.string.faild)
                            super.onFailed(status)
                        }
                    }
                    ))
        }
        else {
            showToastMessage(R.string.no_connection)
        }
    }
    fun getCommisionMessage(price: String):String{
      return String.format(resources.getString(R.string.add_product_message),price)
    }


}
