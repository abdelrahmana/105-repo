package com.urcloset.smartangle.activity.updateProduct

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
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
import com.google.gson.Gson
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.R
import com.urcloset.smartangle.adapter.*
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.databinding.ActivityUpdateProductBinding
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.model.*
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.Constants
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class UpdateProductActivity : TemplateActivity() {
    var disposable = CompositeDisposable()
    lateinit var rvCategories: RecyclerView
    lateinit var rvConditions: RecyclerView
    lateinit var rvColors: RecyclerView
    val categoryAdapter = CategoryAdapter()
     var conditionAdapter: ConditionAdapter?=null
    val photoAdapter = UpdatePhotoAdapter()
    val colorsAdapter = ColorsAdapter()
    lateinit var sizeAdapter: SizeAdapter
    val photos = ArrayList<UpdatePhotoModel>()
    lateinit var tvName: EditText
    lateinit var tvDes: EditText
    lateinit var etPrice: EditText
    lateinit var etPhone:EditText
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
    var itemsMedia = ArrayList<Int>()
    lateinit var conditions: List<ConditionModel.Condition>
    var boxAvaliable: Int = 1
    var invoiceAvailable: Int = 1
    var isNegotiable: Int = 1
    var negotiable_sentence_id: Int = -1
    var conditionIdPosition: Int = 0
    var conditionId: String?=null
    var selectedUsedSentenceIdIndex: Int = 0
    var usedsentance = ArrayList<SentenceModel.Sentence>()
    var sizeList = ArrayList<SizeModel.Size>()
    val uris = ArrayList<Uri>()
    val RESULT_LOAD_IMAGE = 1234
    var lang = "en"
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var tvCode: EditText
    lateinit var intentProduct: ProductModel.Product
    lateinit var productDetails :ProductDetailsModel.Data
    lateinit var shimmerColor:ShimmerFrameLayout
    lateinit var shimmerSizes:ShimmerFrameLayout
    lateinit var shimmerCategories:ShimmerFrameLayout
    lateinit var shimmerCondition:ShimmerFrameLayout
    var first:String=""
    var next:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        val gson = Gson()
        intentProduct = gson.fromJson<ProductModel.Product>(
            intent.getStringExtra("product"),
            ProductModel.Product::class.java
        )
        super.onCreate(savedInstanceState)
    }
    var binding : ActivityUpdateProductBinding?=null
    override fun set_layout() {
        binding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(/*R.layout.activity_update_product*/binding!!.root)
    }

    override fun init_activity(savedInstanceState: Bundle?) {
        tvName.setText(intentProduct.name!!)
        tvDes.setText(intentProduct.description)
        etPrice.setText(intentProduct.price.toString()!!)
        etPrice.isEnabled = false
        if (!BasicTools.isDeviceLanEn()) {
            lang = "ar"
        }
        sizeAdapter = SizeAdapter(lang)
        conditionAdapter = ConditionAdapter(lang)
        rvCategories.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rvConditions.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rvPhotos.layoutManager = GridLayoutManager(this, 3)
        rvPhotos.adapter =  photoAdapter
        rvSize.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rvSize.adapter = sizeAdapter
        rvColors.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rvColors.adapter = colorsAdapter
        if(BasicTools.isDeviceLanEn()) {
            first = "<font color='#FF5E5E'>Note: </font>"
            next=
                "<font color='#444444'> When You Add The Product.\n The Price cannot Be Modified </font>"
        }
        else{
            first = "<font color='#FF5E5E'>ملاحظة :</font>"
            next =
                "<font color='#444444'>عند إضافة المنتج لايمكنك تعديل السعر</font>"

        }

        findViewById<TextView>(R.id.tv_note).setText(Html.fromHtml(first + next))
        getItemDetails()
    }

    override fun init_views() {
        rvCategories =binding!!.rvCategories
        tvName =binding!!.tvName
        etPhone =binding!!.etPhone
        tvDes =binding!!.tvDes
        rvPhotos =binding!!.rvPhotos
        rvConditions =binding!!.rvConditions
        tvYesNegotiable =binding!!.tvYesNegotiable
        tvNoNegotiable =binding!!.tvNoNegotiable
        tvYesBox =binding!!.tvBaY
        tvNoBox =binding!!.tvBaN
        tvInvoiceAvaliable =binding!!.tvIaY
        tvInvoiceUnAvaliable =binding!!.tvIaN
        duration =binding!!.spDuration
        rvColors =binding!!.rvColors
        rvSize =binding!!.rvSize
        addButton =binding!!.bnAddUpdate
        etPrice =binding!!.etPrice
        shimmerFrameLayout =binding!!.shimmerLayout
        tvCode =binding!!.tvCode
        shimmerColor =binding!!.shimmerColors
        shimmerSizes =binding!!.shimmerSize
        shimmerCondition =binding!!.shimmerCondition
        shimmerCategories =binding!!.shimmerCategory

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val imagesEncodedList = ArrayList<String>()
            if (data.data != null) {
                val selectedImage = data.data

                photos.removeAt(photos.size - 1)
                photos.add(UpdatePhotoModel(null,selectedImage!!,null,0))
                photos.add(UpdatePhotoModel(null, null,null,R.drawable.dr_add_photo))
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
            photos.add(UpdatePhotoModel(null,uri,null,0))

        }
        if(photos.size<6)
        photos.add(UpdatePhotoModel(null,null ,null,R.drawable.dr_add_photo))
        photoAdapter.submitList(photos)
        photoAdapter.notifyDataSetChanged()
    }
    override fun init_events() {
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
        photoAdapter.setOnPhotoListener(object : ItemClickListener {
            override fun onClick(position: Int) {
                val showImageIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                showImageIntent.type = "image/*"
                showImageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                showImageIntent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(showImageIntent, RESULT_LOAD_IMAGE)
            }
        })
        photoAdapter.setOnPhotoCancel(object : ItemClickListener {
            override fun onClick(position: Int) {
                var addExit = false
                if (position < photos.size - 1) {
                    itemsMedia.add(Integer.parseInt(photos[position].id!!))
                    photos.removeAt(position)

                    if(photos.size==5) {
                        photoAdapter.currentList.forEach {
                            if (it.img != 0) {
                                addExit = true

                            }
                        }
                        if(!addExit)
                            photos.add(UpdatePhotoModel(null,null,null, R.drawable.dr_add_photo))

                    }
                    photoAdapter.submitList(photos)
                    photoAdapter.notifyItemRemoved(position)

                }
            }

        })
        addButton.setOnClickListener {
            updateProduct()

        }
        colorsAdapter.setOnColorListener(object : ItemClickListener {
            override fun onClick(position: Int) {

            }

        })
        categoryAdapter.setOnCategoryClickListener(object : ItemClickListener {
            override fun onClick(position: Int) {
                categoryIdIndex = position
            }

        })
        conditionAdapter?.setOnConditionItemSelected(object : ItemClickListener {
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
    }

    override fun set_fragment_place() {
    }
   fun getItemDetails(){
       BasicTools.showShimmer(rvColors,shimmerColor,true)
       shimmerColor.visibility = View.GONE
       BasicTools.showShimmer(rvSize,shimmerSizes,true)
       BasicTools.showShimmer(rvConditions,shimmerCondition,true)
       BasicTools.showShimmer(rvCategories,shimmerCategories,true)


       val shopApi =
           ApiClient.getClientJwt(
               TemplateActivity.loginResponse?.data?.accessToken!!,
               BasicTools.getProtocol(
                   applicationContext
               ).toString(),
               lang
           )
               ?.create(
                   AppApi::class.java
               )
       var map=HashMap<String,String>()
       map.put("item_id",intentProduct.id.toString())
       val observable = shopApi!!.getProductDetails(map)
       disposable.clear()
       disposable.add(
           observable.subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeWith(object : AppObservable<ProductDetailsModel>(this) {
                   override fun onSuccess(result: ProductDetailsModel) {
                       BasicTools.showShimmer(rvColors,shimmerColor,false)
                       BasicTools.showShimmer(rvSize,shimmerSizes,false)
                       BasicTools.showShimmer(rvConditions,shimmerCondition,false)
                       shimmerColor.visibility = View.GONE



                       if (result.status!!) {
                           productDetails= result.productDetails!!
                           getCategories()
                           getConditions(lang)
                           getGeneralSentance("condition")
                           getGeneralSentance("negotiable")
                           sizeAdapter.submitList(productDetails.item?.sizesList)
                           colorsAdapter.submitList(productDetails.item?.colorsList)
                           conditionId = productDetails.item?.conditionId
                           if(conditionId=="2") {
                               findViewById<LinearLayout>(R.id.ly_dur).visibility = View.VISIBLE
                               duration.visibility = View.VISIBLE
                           }
                           tvCode.setText(productDetails.item?.countryCode)
                           etPhone.setText(productDetails.item?.phoneNumber)
                           productDetails.item?.itemMedia?.forEach {
                               photos.add(UpdatePhotoModel(it?.id.toString(),null,it?.mediaPath,0))

                           }
                           if(photos.size<6)
                           photos.add(UpdatePhotoModel(null, null,null,R.drawable.dr_add_photo))
                           photoAdapter.submitList(photos)




                           if(productDetails.item?.isNegotiable!!>0) {
                               isNegotiable = 1
                               selectText(tvYesNegotiable)
                               unSelectText(tvNoNegotiable)
                           }
                           else {
                               isNegotiable = 0
                               selectText(tvNoNegotiable)
                               unSelectText(tvYesNegotiable)
                           }
                           if(productDetails.item?.boxAvailable!!>0){
                               boxAvaliable = 1
                               selectText(tvYesBox)
                               unSelectText(tvNoBox)

                           }
                           else{
                               boxAvaliable = 0
                               selectText(tvNoBox)
                               unSelectText(tvYesBox)

                           }
                           if(productDetails.item?.invoiceAvailable!!>0){
                               invoiceAvailable = 1
                               selectText(tvInvoiceAvaliable)
                               unSelectText(tvInvoiceUnAvaliable)

                           }
                           else{
                               invoiceAvailable = 0
                               selectText(tvInvoiceUnAvaliable)
                               unSelectText(tvInvoiceAvaliable)

                           }



                       }


                   }

                   override fun onFailed(status: Int) {
                       BasicTools.showShimmer(rvColors,shimmerColor,false)
                       BasicTools.showShimmer(rvSize,shimmerSizes,false)
                       BasicTools.showShimmer(rvConditions,shimmerCondition,false)



                   }
               })
       )



   }
    fun getCategories() {
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
                        BasicTools.showShimmer(rvCategories,shimmerCategories,false)
                        shimmerCategories.visibility = View.GONE

                        if (result.status!!) {
                            categories = result.categories as ArrayList<CategoryModel.Category>
                            categoryAdapter.submitList(categories)
                            for(i in 0..categories.size-1){
                                if(categories[i].id.toString()==productDetails.item?.categoryId){
                                    categoryIdIndex = i

                                }
                            }
                            categoryAdapter.selected = categoryIdIndex
                            rvCategories.adapter = categoryAdapter



                        }

                    }

                    override fun onFailed(status: Int) {
                        BasicTools.showShimmer(rvCategories,shimmerCategories,false)
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
                            conditions.forEach {
                               for(i in 0..conditions.size-1){
                                   if(conditions[i].id.toString() == conditionId.toString()){
                                       conditionIdPosition = i
                                   }

                               }
                            }
                            conditionAdapter?.selected = conditionIdPosition
                            conditionAdapter?.submitList(conditions)
                            rvConditions.adapter = conditionAdapter


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
                                for(i in 0..usedsentance.size-1) {
                                    if (usedsentance[i].id.toString() == productDetails.item?.usedSentenceId) {
                                        selectedUsedSentenceIdIndex = i

                                    }
                                }

                                // Duration
                                val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                    this@UpdateProductActivity,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    durations
                                )
                                duration.adapter = arrayAdapter
                                duration.setSelection(selectedUsedSentenceIdIndex)

                            } else {
                                negotiable_sentence_id = result.data?.get(0)!!.id!!
                            }

                        }

                    }


                })
        )

    }
    private fun updateProduct() {
        if(tvName.text.toString().trim().isNotEmpty()) {
            if(tvDes.text.toString().trim().isNotEmpty()) {

                if (etPrice.text.toString().trim().isNotEmpty()) {
                    if(etPhone.text.toString().trim().isNotEmpty()) {
                        if (photoAdapter.currentList.size > 0) {
                            showShimmerAddBtn(true)
                            binding!!.bnAddUpdate.isEnabled = false
                            try {

                                val sizes = ArrayList<Int>()
                                val colors = ArrayList<Int>()

                                sizeAdapter.currentList.forEach {
                                    if (it.isSelected!! > 0) {
                                        sizes.add(it.id!!)
                                    }

                                }
                                colorsAdapter.currentList.forEach {
                                    if (it.isSelected!! > 0) {
                                        colors.add(it.id!!)


                                    }
                                }


                                val map = HashMap<String, String>()
                                map.put("id", productDetails.item?.id.toString())
                                map.put("name", tvName.text.toString().trim())
                                map.put("description", tvDes.text.toString())
                                map.put("price", etPrice.text.toString())
                                map.put("box_available", boxAvaliable.toString())
                                map.put(
                                    "category_id",
                                    categoryAdapter.currentList[categoryIdIndex].id.toString()
                                )
                                map.put(
                                    "country_code",
                                    TemplateActivity.loginResponse?.data?.user?.countryCode!!
                                )
                                map.put(
                                    "phone_number",
                                    TemplateActivity.loginResponse?.data?.user?.phoneNumber!!
                                )
                                map.put(
                                    "condition_id",
                                    conditionAdapter?.currentList?.get(conditionIdPosition)?.id.toString()
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
                                val shopApi =
                                    ApiClient.getClientJwt(
                                        TemplateActivity.loginResponse?.data?.accessToken!!,
                                        BasicTools.getProtocol(applicationContext).toString(), lang
                                    )
                                        ?.create(
                                            AppApi::class.java
                                        )
                                val observable: Observable<CreateProductModel>
                                if (images.size > 0)
                                    observable = shopApi!!.updateProduct(
                                        images,
                                        map,
                                        sizes = sizes,
                                        colors = colors,
                                        media_to_delete = itemsMedia

                                    )
                                else {
                                    observable = shopApi!!.updateProduct(
                                        images,
                                        map,
                                        sizes = sizes,
                                        colors = colors,
                                        media_to_delete = itemsMedia

                                    )

                                }
                                disposable.add(
                                    observable.subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeWith(object :
                                            AppObservable<CreateProductModel>(this) {
                                            override fun onSuccess(result: CreateProductModel) {
                                                showShimmerAddBtn(false)
                                                Toast.makeText(
                                                    this@UpdateProductActivity,
                                                    result.messages,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                               binding!!.bnAddUpdate.isEnabled = true


                                            }

                                            override fun onFailed(status: Int) {
                                                showShimmerAddBtn(false)
                                                showToastMessage(R.string.faild)
                                               binding!!.bnAddUpdate.isEnabled = true
                                                showShimmerAddBtn(false)

                                                super.onFailed(status)
                                            }


                                        })
                                )

                            } catch (e: Exception) {
                              binding!!.bnAddUpdate.isEnabled = true
                                showShimmerAddBtn(false)
                                showToastMessage(R.string.faild)
                            }
                        } else {
                            Toast.makeText(
                                this@UpdateProductActivity,
                                getString(R.string.you_mst_add_photo),
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                    else {
                        Toast.makeText(
                            this@UpdateProductActivity,
                            getString(R.string.phone_required),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        this@UpdateProductActivity,
                        getString(R.string.price_required),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
            else {
                Toast.makeText(
                    this@UpdateProductActivity,
                    getString(R.string.des_required),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        else{
            Toast.makeText(this@UpdateProductActivity, getString(R.string.name_required), Toast.LENGTH_SHORT).show()

        }

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
          binding!!.bnAddUpdate.visibility = View.GONE
            shimmerFrameLayout.visible()
        } else {
           binding!!.bnAddUpdate.visibility = View.VISIBLE
            shimmerFrameLayout.hide()
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
}