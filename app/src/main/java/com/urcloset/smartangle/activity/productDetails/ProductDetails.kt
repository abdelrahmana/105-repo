package com.urcloset.smartangle.activity.productDetails

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.number.Precision.currency
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Explode
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.CustomViewPager
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.sellerActivity.SellerActivity
import com.urcloset.smartangle.adapter.*
import com.urcloset.smartangle.adapter.project105.SimilarProductAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.databinding.ActivityProductDetailsBinding
import com.urcloset.smartangle.model.BasicModel
import com.urcloset.smartangle.model.ProductDetailsModel
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.Constants
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ProductDetails : TemplateActivity() ,IProductDetailsActivity{
    var lang = "en"
    var disposable = CompositeDisposable()
    lateinit var product: ProductModel.Product
    lateinit var tvDes: TextView
    lateinit var tvPrice: TextView
    lateinit var tvTitle: TextView
    lateinit var tvCondition: TextView
    lateinit var tvUsed: TextView
    lateinit var boxAvailable: TextView
    lateinit var invoiceAvailable: TextView
    lateinit var negotable: TextView
    lateinit var ivBack: ImageView
    lateinit var rvColors: RecyclerView
    lateinit var rvSizes: RecyclerView
    lateinit var rlBookMark: ConstraintLayout
    lateinit var rlReport: ConstraintLayout
    lateinit var viewPager: CustomViewPager
    lateinit var ivAvatar: ImageView
    val colorAdapter = ColorAdapterProductDetail()
    lateinit var sizeAdapter: SizeAdapterProductDetails
    lateinit var photoGalleryAdapter: PhotoGalleryProductDetailAdapter
    lateinit var rlWhats: ConstraintLayout
    lateinit var shimmerColor: ShimmerFrameLayout
    lateinit var shimmerSizes: ShimmerFrameLayout
    lateinit var shimmerCondition: ShimmerFrameLayout
    lateinit var bookShimmer: ShimmerFrameLayout
    lateinit var name: TextView
    lateinit var tvUserAvailableProduct: TextView
    lateinit var tvRateCount: TextView
    lateinit var star1: ImageView
    lateinit var star2: ImageView
    lateinit var star3: ImageView
    lateinit var star4: ImageView
    lateinit var star5: ImageView
    lateinit var star:LinearLayout
    lateinit var shimmerName: ShimmerFrameLayout
    lateinit var shimmerNumProducts: ShimmerFrameLayout
    lateinit var shimmerStar: ShimmerFrameLayout
    lateinit var userSection:LinearLayout
    lateinit  var rlShare :ConstraintLayout
    var productDetailModel:ProductDetailsModel.Data?=null
    var savedBefore = false
    var counter: Int = 0
    var slepped: Boolean = false
    var count: Int = 1
    var productID:String?=null
    //similar product
    lateinit var rvSimilar:RecyclerView
    lateinit var similarProductAdapter: SimilarProductAdapter
    lateinit var tvMayLikeToo:TextView
    //location
    lateinit var tvLocation:TextView

    var binding : ActivityProductDetailsBinding?=null

    override fun set_layout() {
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportPostponeEnterTransition()
    }
    override fun init_activity(savedInstanceState: Bundle?)
    {

        try {
            val gson = Gson()
            product = gson.fromJson<ProductModel.Product>(
                intent.getStringExtra("product"),
                ProductModel.Product::class.java
            )
            val productsImages = ArrayList<ProductDetailsModel.Data.Item.ItemMedia>()
            product.itemMedia?.forEach {
                productsImages.add(ProductDetailsModel.Data.Item.ItemMedia(
                    fullPath = it?.fullPath,
                    id = it?.id,
                    itemId = it?.itemId,
                    mediaPath = it?.mediaPath,
                    mediaType = it?.mediaType
                ))

            }
            photoGalleryAdapter = PhotoGalleryProductDetailAdapter(
                context = this@ProductDetails,
                photos = productsImages,
                count = productsImages.size
            )
            viewPager.adapter = photoGalleryAdapter
            getProductDetails(product.id.toString())
        }
        catch (e:java.lang.Exception){
            productID = intent.getStringExtra("id")

            getProductDetails(productID.toString())
        }


        viewPager.setPagingEnabled(true)


        rvColors.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvColors.adapter = colorAdapter
        rvSizes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)



    }

    override fun init_views() {
        tvTitle = binding!!.tvTitle
        tvDes = binding!!.tvDes
        tvPrice =binding!!.tvPrice
        tvCondition =binding!!.tvCondition
        tvUsed =binding!!.tvUsed
        boxAvailable =binding!!.tvBox
        invoiceAvailable =binding!!.tvInvoice
        negotable =binding!!.tvNeg
        rvColors =binding!!.rvColors
        rvSizes =binding!!.rvSizes
        viewPager =binding!!.customViewPager
        ivBack =binding!!.toolbar.ivBack
        rlReport =binding!!.lyReport
        rlBookMark =binding!!.rlBookmark
        rlWhats =binding!!.rlWhats
        shimmerColor =binding!!.shimmerColors
        shimmerSizes =binding!!.shimmerSizes
        shimmerCondition =binding!!.shimmerCondition
        bookShimmer =binding!!.bookShimmer
        ivAvatar =binding!!.ivAvatar
        name =binding!!.tvUsername
        star =binding!!.starRate
        tvUserAvailableProduct =binding!!.tvUserAvailableProduct
        tvRateCount =binding!!.tvRateCount
        star1 =binding!!.ivStar1
        star2 =binding!!.ivStar2
        star3 =binding!!.ivStar3
        star4 =binding!!.ivStar4
        star5 =binding!!.ivStar5
        shimmerName =binding!!.shimmerName
        shimmerNumProducts =binding!!.shimmerNumProducts
        shimmerStar =binding!!.shimmerStar
        userSection =binding!!.userSection

        rvSimilar=binding!!.rvSimilarProduct
        tvMayLikeToo=binding!!.tvMayLikeToo
        tvLocation=binding!!.tvLocation
        rlShare =binding!!.rlShare
    }

    override fun init_events() {

        ivBack.setOnClickListener {
            this.supportFinishAfterTransition()
        }
        rlReport.setOnClickListener {
            openReportDiaog()
        }
        rlBookMark.setOnClickListener {
            var operationType: String = "1"
            if (savedBefore) {
                operationType = "0"
            }
            saveProduct(operationType)
        }
        val timer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {


                val alertDialogBuilder = AlertDialog.Builder(this@ProductDetails)
                alertDialogBuilder.setMessage(getString(R.string.order_message))
                alertDialogBuilder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
                    val contact = product.countryCode + "" + product.phoneNumber
                    val text = "The product id ${product.id}\n the quantity is $counter"
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                    "https://api.whatsapp.com/send?phone=$contact Number&text=$text"
                                )
                            )
                        )
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@ProductDetails,
                            "Whatsapp app not installed in your phone",
                            Toast.LENGTH_SHORT
                        ).show()


                    }

                    counter = 0
                    slepped = false
                    dialog.dismiss()

                }
                alertDialogBuilder.setNegativeButton(R.string.no) { dialog, which ->
                    counter = 0
                    slepped = false
                    dialog.cancel()

                }
                alertDialogBuilder.show()

            }


        }


        rlWhats.setOnClickListener {
            counter++
            if (!slepped) {
                timer.start()
                slepped = true
            }

        }
        userSection.setOnClickListener {
            if(productDetailModel!=null){
                val intent = Intent(this, SellerActivity::class.java)
                intent.putExtra("identifier", productDetailModel?.item?.owner?.id.toString())
                startActivity(intent)

            }

        }
        rlShare.setOnClickListener {
            if (!productDetailModel?.item?.name.isNullOrEmpty() &&
                !productDetailModel?.item?.owner?.name.isNullOrEmpty() &&
                !productDetailModel?.item?.price.toString().isNullOrEmpty()
            ) {
                BasicTools.shareProduct(
                    name=productDetailModel?.item?.name.toString(),
                    owner =productDetailModel?.item?.owner?.name.toString(),
                    price = productDetailModel?.item?.price.toString(),
                    id = productDetailModel?.item?.id.toString(),
                    image = productDetailModel?.item?.itemMedia?.get(0)?.mediaPath.toString(),
                    this

                )
            }


        }
    }

    override fun set_fragment_place() {
    }

    fun getProductDetails(id: String) {

        if (BasicTools.isConnected(this)) {

            beginProductShimmer()



            if (!BasicTools.isDeviceLanEn())
                lang = "ar"
            val shopApi: AppApi?
            if (TemplateActivity.loginResponse?.data?.accessToken != null) {

                shopApi = ApiClient.getClientJwt(
                    TemplateActivity.loginResponse?.data?.accessToken!!,
                    BasicTools.getProtocol(applicationContext).toString(), lang
                )
                    ?.create(
                        AppApi::class.java
                    )
            } else {
                shopApi =
                    ApiClient.getClient(
                        BasicTools.getProtocol(applicationContext).toString(), lang
                    )
                        ?.create(
                            AppApi::class.java
                        )
                hideController()


            }

            var map=HashMap<String,String>()
            map.put("item_id",id.toString())
            map.put("count_similar","8")
            val observable = shopApi!!.getProductDetails(map)
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ProductDetailsModel>(this) {
                        @SuppressLint("SetTextI18n")
                        override fun onSuccess(result: ProductDetailsModel) {

                            endProductShimmer()
                            if (result.status!!) {
                                val productDetails = result.productDetails
                                val item = productDetails.item


                                if (item?.itemMedia?.size!! > 3) {
                                    count = 2
                                }
                                if(!::photoGalleryAdapter.isInitialized) {
                                    photoGalleryAdapter = PhotoGalleryProductDetailAdapter(
                                        context = this@ProductDetails,
                                        photos = (item.itemMedia),
                                        count = count
                                    )
                                    viewPager.adapter = photoGalleryAdapter

                                }
                                tvTitle.text = item.name
                                tvDes.text = item.description
                                tvPrice.text = item.price.toString()+resources.getString(R.string.currency)

                                //set similar product----
                                if(result.productDetails.similarProducts.isNullOrEmpty())
                                    tvMayLikeToo.visibility=View.GONE
                                else   tvMayLikeToo.visibility=View.VISIBLE
                                similarProductAdapter= SimilarProductAdapter(this@ProductDetails,
                                    result.productDetails.similarProducts!!,this@ProductDetails)
                                rvSimilar.adapter=similarProductAdapter

                                //-------


                                //--set location

                                if(BasicTools.isDeviceLanEn()) {
                                    if(result.productDetails!!.item?.owner?.country!=null) {
                                        if (!result.productDetails!!.item?.owner?.country?.name.isNullOrEmpty())
                                            tvLocation.setText(result.productDetails!!.item?.owner?.country?.name+", "+result.productDetails!!.item?.owner?.city?.name)
                                        else tvLocation.setText(getString(R.string.not_avaliable))
                                    }


                                    else tvLocation.setText(getString(R.string.not_avaliable))

                                }
                                else   {
                                    if(result.productDetails!!.item?.owner?.country!=null) {
                                        if (!result.productDetails!!.item?.owner?.country?.nameAr.isNullOrEmpty())
                                            tvLocation.setText(result.productDetails!!.item?.owner?.country?.nameAr+", "+result.productDetails!!.item?.owner?.city?.nameAr)

                                        else  tvLocation.setText(getString(R.string.not_avaliable))
                                    }
                                    else tvLocation.setText(getString(R.string.not_avaliable))

                                }

                                //-----

                                productDetailModel = result.productDetails

                                var image: String? = ""
                                if (result.productDetails!!.item?.owner != null)

                                    if (result.productDetails?.item?.owner?.image != null)
                                        image = BasicTools.getUrlHttpImg(
                                            this@ProductDetails,
                                            result.productDetails?.item?.owner?.image!!
                                        )
                                Log.i("TEST_TEST","${result.productDetails!!.item?.owner.toString()}")
                                setUser(
                                    result.productDetails.item?.owner!!.name!!,

                                    result.productDetails.item?.owner?.rate!!,
                                    image,
                                    result.productDetails.item?.owner?.countAviableProducts!!,
                                    result.productDetails.item?.owner?.countRaters!!.toInt()
                                )
                                tvTitle.text = result.productDetails.item?.name
                                tvPrice.text = result.productDetails.item?.price.toString()+resources.getString(R.string.currency)
                                tvDes.text = result.productDetails.item?.description

                                colorAdapter.submitList(result.productDetails.item?.selectedColors)
                                sizeAdapter = SizeAdapterProductDetails(lang)
                                rvSizes.adapter = sizeAdapter
                                sizeAdapter.submitList(result.productDetails.item?.selectedSizes)
                                savedBefore = result.productDetails.item?.savedBefore!!

                                if (item.connectWhatsapp!! <= 0)
                                    rlWhats.visibility = View.GONE
                                else rlWhats.visibility = View.VISIBLE
                                if (result.productDetails.item?.itemStatus == 2) {
                                    hideController()


                                } else {
                                    if (TemplateActivity.loginResponse?.data?.accessToken != null) {
                                        if (!TemplateActivity.loginResponse?.data?.user?.id.toString()
                                                .equals(
                                                    result.productDetails?.item?.owner?.id.toString()
                                                )
                                        )
                                            showController()
                                        else {
                                            hideController()
                                        }

                                    } else hideController()
                                }



                                if (savedBefore) {
                                    rlBookMark.background =
                                        resources.getDrawable(R.drawable.category_selected_item_bg)
                                } else {
                                    rlBookMark.background =
                                        resources.getDrawable(R.drawable.category_unselected_item_bg)


                                }
                                if (BasicTools.isDeviceLanEn()) {
                                    tvCondition.text = result.productDetails?.item?.condition?.nameEn
                                    if (result.productDetails?.item?.usedSentence != null && !result.productDetails?.item?.usedSentence?.equals(
                                            ""
                                        )!!
                                    ) {
                                        tvUsed.text = result.productDetails?.item?.usedSentence?.english!!

                                    }
                                    if (result.productDetails.item?.boxAvailable!! > 0)
                                        boxAvailable.text = "Yes"
                                    else boxAvailable.text = "No"
                                    if (result.productDetails.item?.isNegotiable!! > 0)
                                        negotable.text = "Yes"
                                    else negotable.text = "No"
                                    if (result.productDetails.item?.invoiceAvailable!! > 0)
                                        invoiceAvailable.text = "Yes"
                                    else invoiceAvailable.text = "No"


                                } else {
                                    tvCondition.text = result.productDetails?.item?.condition?.nameAr
                                    if (result.productDetails?.item?.usedSentence != null && !result.productDetails?.item?.usedSentence?.equals(
                                            ""
                                        )!!
                                    ) {
                                        tvUsed.text = result.productDetails?.item?.usedSentence?.arabic

                                    }
                                    if (result.productDetails?.item?.boxAvailable!! > 0)
                                        boxAvailable.text = "نعم"
                                    else boxAvailable.text = "لا"
                                    if (result.productDetails?.item?.isNegotiable!! > 0)
                                        negotable.text = "نعم"
                                    else negotable.text = "لا"
                                    if (result.productDetails?.item?.invoiceAvailable!! > 0)
                                        invoiceAvailable.text = "نعم"
                                    else invoiceAvailable.text = "لا"


                                }

                            }


                        }

                        override fun onFailed(status: Int) {
                            endProductShimmer()


                        }
                    })
            )
        } else {
            showToastMessage(R.string.no_connection)
        }
    }


    fun openReportDiaog() {
        val dialog = Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);
        val report = dialog.findViewById<CardView>(R.id.cv_report)
        val etMessage = dialog.findViewById<EditText>(R.id.edit_text)
        val shimmerBtn = dialog.findViewById<ShimmerFrameLayout>(R.id.shimmer_report)
        report.setOnClickListener {
            if (etMessage.text.toString().isNotEmpty())
                if(BasicTools.isConnected(this))
                    sendReport(etMessage.text.toString(), shimmerBtn, report, dialog)
                else showToastMessage(R.string.no_connection)
        }

        dialog.show();
        dialog.getWindow()?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.getAttributes()?.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow()?.setGravity(Gravity.BOTTOM);
    }


    fun sendReport(message: String, shimmer: ShimmerFrameLayout, btn: CardView, dialog: Dialog) {
        btn.visibility = View.GONE
        shimmer.visibility =View.VISIBLE
        shimmer.startShimmerAnimation()
        val shopApi =
            ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(applicationContext).toString(), lang
            )
                ?.create(
                    AppApi::class.java
                )
        val map = HashMap<String, String>()
        map.put("product_id", product.id.toString())
        map.put("message", message)
        val observable = shopApi!!.reportProduct(map)
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<BasicModel>(this) {
                    override fun onSuccess(result: BasicModel) {
                        shimmer.stopShimmerAnimation()
                        shimmer.visibility = View.GONE
                        btn.visibility = View.VISIBLE
                        dialog.cancel()


                        if (result.status!!) {
                            Toast.makeText(this@ProductDetails, result.messages, Toast.LENGTH_SHORT)
                                .show()

                        }


                    }

                    override fun onFailed(status: Int) {
                        shimmer.stopShimmerAnimation()
                        shimmer.visibility = View.GONE
                        btn.visibility = View.VISIBLE
                        dialog.cancel()


                    }
                })
        )

    }

    fun beginShimmer() {
        findViewById<LinearLayout>(R.id.controller).visibility = View.GONE
        findViewById<RelativeLayout>(R.id.shimmer_report).visibility = View.VISIBLE
        findViewById<LinearLayout>(R.id.shimmer_ly).visibility = View.VISIBLE
        findViewById<ShimmerFrameLayout>(R.id.book_shimmer).visibility = View.VISIBLE


        rlBookMark.visibility = View.GONE
        bookShimmer.visible()
    }

    fun endShimmer() {
        bookShimmer.hide()
        bookShimmer.visibility = View.GONE
        rlBookMark.visibility = View.VISIBLE
        findViewById<ShimmerFrameLayout>(R.id.book_shimmer).visibility = View.GONE


        findViewById<LinearLayout>(R.id.shimmer_ly).visibility = View.GONE

        findViewById<LinearLayout>(R.id.controller).visibility = View.VISIBLE
        findViewById<RelativeLayout>(R.id.shimmer_report).visibility = View.GONE

    }

    fun saveProduct(operationType: String) {
        beginShimmer()

        val shopApi =
            ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(applicationContext).toString(), lang
            )
                ?.create(
                    AppApi::class.java
                )
        val map = HashMap<String, String>()
        map.put("operation_type", operationType)
        map.put("model_id", product.id.toString())
        map.put("model_name", "Items")
        val observable = shopApi!!.saveProduct(map)
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<BasicModel>(this) {
                    override fun onSuccess(result: BasicModel) {
                        if (result.status!!) {
                            endShimmer()
                            if (operationType == "1") {
                                savedBefore = true
                                rlBookMark.background =
                                    resources.getDrawable(R.drawable.category_selected_item_bg)
                                Toast.makeText(
                                    this@ProductDetails,
                                    getString(R.string.add_to_favourite_msg),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                savedBefore = false
                                rlBookMark.background =
                                    resources.getDrawable(R.drawable.category_unselected_item_bg)
                                Toast.makeText(
                                    this@ProductDetails,
                                    result.messages,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            }


                        }


                    }

                    override fun onFailed(status: Int) {
                        endShimmer()

                    }
                })
        )

    }

    fun hideController() {
        findViewById<RelativeLayout>(R.id.rl_whats).visibility = View.GONE
        findViewById<LinearLayout>(R.id.controller).visibility = View.GONE

    }

    fun showController() {
        findViewById<RelativeLayout>(R.id.rl_whats).visibility = View.VISIBLE
        findViewById<LinearLayout>(R.id.controller).visibility = View.VISIBLE

    }

    fun setUser(userName: String, rate: Double, image: String?, productNumber: Int, rateCount: Int) {
        name.setText(userName)
        if (!image.isNullOrEmpty())
            Glide.with(applicationContext).load(image)
                .circleCrop()
                .into(ivAvatar)
        setUpRateStars(rate)
        setProductNumber(productNumber)
        tvRateCount.setText(rateCount.toString())
    }

    fun setUpRateStars(rated: Double) {
        val rate: Int = rated.toInt()
        if (rate == 0) {

        } else if (rate == 1) {
            star1.setImageResource(R.drawable.ic_star_filled)

        } else if (rate == 2) {
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)


        } else if (rate == 3) {
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)
            star3.setImageResource(R.drawable.ic_star_filled)


        } else if (rate == 4) {
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)
            star3.setImageResource(R.drawable.ic_star_filled)
            star4.setImageResource(R.drawable.ic_star_filled)
            star5.setImageResource(R.drawable.ic_star_filled)
        } else if (rate == 5) {
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)
            star3.setImageResource(R.drawable.ic_star_filled)
            star4.setImageResource(R.drawable.ic_star_filled)
            star5.setImageResource(R.drawable.ic_star_filled)
        }
    }

    fun setProductNumber(countAviableProducts: Int) {
        if (BasicTools.isDeviceLanEn())
            tvUserAvailableProduct.text =
                "$countAviableProducts Available Product"
        else
            tvUserAvailableProduct.text =
                "$countAviableProducts منتج متوفر "


    }
    fun beginProductShimmer(){
        BasicTools.showShimmer(rvColors, shimmerColor, true)
        BasicTools.showShimmer(rvSizes, shimmerSizes, true)
        tvUsed.visibility = View.GONE
        shimmerCondition.visibility = View.VISIBLE
        shimmerCondition.visible()
        tvUserAvailableProduct.visibility = View.GONE
        shimmerNumProducts.visibility = View.VISIBLE
        shimmerNumProducts.visible()
        name.visibility = View.GONE
        shimmerName.visibility = View.VISIBLE
        shimmerName.visible()
        star.visibility = View.GONE
        shimmerStar.visibility = View.VISIBLE
        shimmerStar.visible()



    }
    fun endProductShimmer(){
        BasicTools.showShimmer(rvColors, binding!!.shimmerColors, false)
        BasicTools.showShimmer(rvSizes, shimmerSizes, false)
        shimmerSizes.visibility = View.GONE
        shimmerColor.visibility = View.GONE
        shimmerCondition.hide()
        shimmerCondition.visibility = View.GONE
        tvUsed.visibility = View.VISIBLE
        shimmerNumProducts.hide()
        shimmerNumProducts.visibility = View.GONE
        tvUserAvailableProduct.visibility = View.VISIBLE
        shimmerName.hide()
        shimmerName.visibility = View.GONE
        name.visibility = View.VISIBLE
        shimmerStar.hide()
        shimmerStar.visibility = View.GONE
        star.visibility = View.VISIBLE


    }

    override fun onProductChange(position: Int, state: Int, product: ProductModel.Product) {
        // changeProductState(state,product,position)
    }



}