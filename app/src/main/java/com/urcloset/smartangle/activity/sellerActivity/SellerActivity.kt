package com.urcloset.smartangle.activity.sellerActivity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.CustomViewPager
import com.urcloset.smartangle.R
import com.urcloset.smartangle.adapter.CategoryAdapterSeller
import com.urcloset.smartangle.adapter.ProductStateAdapter
import com.urcloset.smartangle.adapter.SellerProductsAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.databinding.ActivitySellerBinding
import com.urcloset.smartangle.dialog.RateDialog
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.model.BasicModel
import com.urcloset.smartangle.model.CategoryModel
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.model.UserProfileModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Locale


class SellerActivity : TemplateActivity() {
    // todo need to update it
    var disposable = CompositeDisposable()
    var disposableNotifiaion = CompositeDisposable()
    var selleruserId: String? = null
    lateinit var tvName: TextView
    lateinit var ivAvatar: ImageView
    lateinit var tvAvailableProduct: TextView
    lateinit var tvLocation: TextView
    var mediaPlayer: MediaPlayer? = null
    lateinit var seekBar: SeekBar
    lateinit var ivPlayMusic: ImageView
    lateinit var lyVid: LinearLayout
    lateinit var tvRateCount: TextView
    lateinit var ivSaveUser: ImageView
    lateinit var star1: ImageView
    lateinit var star2: ImageView
    lateinit var star3: ImageView
    lateinit var star4: ImageView
    lateinit var star5: ImageView
    lateinit var ivBack: ImageView
    lateinit var blocked: TextView
    lateinit var selled: TextView
    var userSeller: UserProfileModel.Data? = null
    lateinit var categories: List<CategoryModel.Category>
    var products = ArrayList<ProductModel.Product>()
    lateinit var rvCategories: RecyclerView
    lateinit var shimmerCategories: ShimmerFrameLayout
    lateinit var productStateAdapter: ProductStateAdapter
    val categoryAdapter = CategoryAdapterSeller()
    lateinit var sellerCategoriesProducts: SellerProductsAdapter
    lateinit var viewPager: CustomViewPager
    var runnable: Runnable? = null
    private var handler = Handler()
    private var allProducts = ArrayList<ArrayList<ProductModel.Product>>()
    lateinit var notificationImage: ImageView
    var operaton = 1
    lateinit var shimmerSeekBar: ShimmerFrameLayout
    var isUserSaved = 0
    lateinit var shimmerMainProduct: ShimmerFrameLayout
    lateinit var shimmerProductState: ShimmerFrameLayout
    lateinit var textInfoShimmer: ShimmerFrameLayout
    lateinit var starShimmer: ShimmerFrameLayout
    lateinit var locationShimmer: ShimmerFrameLayout
    lateinit var shimmerNotification: ShimmerFrameLayout
    lateinit var chooseDialog: RateDialog
    lateinit var rlAllCats: RelativeLayout
    lateinit var cvCats: CardView
    var data: UserProfileModel.Data? = null
    var lang: String = "en"
    var productLoadingFinished = false

    var binding : ActivitySellerBinding?=null
    override fun set_layout() {
        binding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(/*R.layout.activity_seller*/binding!!.root)
    }

    override fun init_activity(savedInstanceState: Bundle?) {
        BasicTools.showShimmer(rvCategories, shimmerCategories, true)
        locationShimmer.visible()
        shimmerMainProduct.visible()
        shimmerSeekBar.visible()
        textInfoShimmer.visible()
        starShimmer.visible()

        findViewById<LinearLayout>(R.id.stars).visibility = View.GONE
        selleruserId =
            intent.getStringExtra("identifier")


        viewPager.setPagingEnabled(false)


        if (!BasicTools.isDeviceLanEn())
            lang = "ar"
        if (BasicTools.isConnected(this)) {
            if (TemplateActivity.loginResponse?.data?.accessToken != null) {
                if (!selleruserId.toString()
                        .equals(TemplateActivity.loginResponse!!.data?.user?.id.toString())
                ) {
                    notificationImage.visibility = View.VISIBLE
                }

                getUser()
            } else {
                ivSaveUser.visibility = View.GONE
                notificationImage.visibility = View.GONE
                getUserByVisitor()
            }
        } else {
            showToastMessage(R.string.no_connection)
        }


    }

    private fun getProducts(id: Int?) {
        val shopApi =
            ApiClient.getClient(
                BasicTools.getProtocol(applicationContext).toString(), lang
            )
                ?.create(
                    AppApi::class.java
                )
        val observable = shopApi!!.getProducts("no", id.toString())
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<ProductModel>(this) {
                    override fun onSuccess(result: ProductModel) {
                        shimmerMainProduct.hide()
                        shimmerMainProduct.visibility = View.GONE
                        viewPager.visibility = VISIBLE

                        if (result.status!!) {

                            products = (result.products as ArrayList<ProductModel.Product>?)!!
                            allProducts.clear()
                            allProducts.add(products.filter {
                                it.itemStatus != 2 && it.itemStatus != 3

                            } as ArrayList<ProductModel.Product>)

                            categories.forEach {
                                val tempProducts = ArrayList<ProductModel.Product>()
                                for (i in 0 until products.size) {
                                    if (it.id.toString()
                                            .equals(products[i].categoryId.toString())
                                    ) {
                                        if (products[i].itemStatus == 1)
                                            tempProducts.add(products[i])
                                    }
                                }

                                allProducts.add(tempProducts)


                            }
                            sellerCategoriesProducts = SellerProductsAdapter(
                                allProducts,
                                this@SellerActivity,
                                categories,
                            )
                            productLoadingFinished = true
                            viewPager.adapter = sellerCategoriesProducts
                            try {
                                if (allProducts[categoryAdapter.selected].size == 0)
                                    ivSaveUser.visibility = GONE
                                else {
                                    if (TemplateActivity.loginResponse?.data?.accessToken != null)
                                        if (!selleruserId.toString()
                                                .equals(TemplateActivity.loginResponse!!.data?.user?.id.toString())
                                        )
                                            ivSaveUser.visibility = VISIBLE
                                        else {
                                            ivSaveUser.visibility = View.GONE


                                        }


                                }
                            } catch (e: Exception) {

                            }

                            selectAllCategories()

                        }


                    }

                    override fun onFailed(status: Int) {
                        shimmerMainProduct.hide()
                        viewPager.visibility = VISIBLE
                        shimmerMainProduct.visibility = View.GONE


                    }
                })
        )

    }


    override fun init_views() {
        rlAllCats =binding!!.rlAllCat
        cvCats =binding!!.cvCat
        tvName =binding!!.tvName
        ivAvatar =binding!!.ivAvatar
        tvAvailableProduct =binding!!.tvAvailableProduct
        tvLocation =binding!!.tvLocation
        seekBar =binding!!.seekBar
        ivPlayMusic =binding!!.icPlayMusic
        lyVid =binding!!.lyVid
        tvRateCount =binding!!.tvRateCount
        star1 =binding!!.ivStar1
        star2 =binding!!.ivStar2
        star3 =binding!!.ivStar3
        star4 =binding!!.ivStar4
        star5 =binding!!.ivStar5
        rvCategories =binding!!.rvCategories
        viewPager =binding!!.sellerViewPager
        ivBack = findViewById(R.id.iv_back)
        notificationImage =binding!!.ivNotification
        ivSaveUser =binding!!.ivSaveUser
        shimmerCategories =binding!!.shimmerCategorySeller
        shimmerSeekBar =binding!!.shimmerSeekBar
        shimmerMainProduct =binding!!.shimmerMainProduct
        textInfoShimmer =binding!!.textInfoShimmer
        starShimmer =binding!!.starShimmer
        locationShimmer =binding!!.shimmerLocation
        shimmerNotification =binding!!.shimmerNotification
        var lang = "en"
        if (!BasicTools.isDeviceLanEn(this)) {
            lang = "ar"
        }
        val config = resources.configuration
        val locale = Locale(lang)
        Locale.setDefault(locale)
        config.setLocale(locale)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)


    }

    override fun init_events() {

        ivSaveUser.setOnClickListener {

            if (allProducts.isNotEmpty()) {
                var pos: Int = 0
                pos = if (categoryAdapter.selected != -1)
                    categoryAdapter.selected + 1
                else 0
                chooseDialog = RateDialog(
                    this@SellerActivity,
                    allProducts[pos][sellerCategoriesProducts.selectedProduct].id.toString()
                )
                chooseDialog.window!!.attributes.windowAnimations = R.style.dialogAnim
                chooseDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                chooseDialog.show()
            }

        }



        notificationImage.setOnClickListener {


            toggleNotification(selleruserId!!, operaton.toString())
        }
        categoryAdapter.setOnCategoryClickListener(object : ItemClickListener {
            override fun onClick(position: Int) {
                if (productLoadingFinished) {
                    rlAllCats.background =
                        resources.getDrawable(R.drawable.seller_category_unselected_item_bg)
                    cvCats.cardElevation = 4f
                    if (allProducts[position + 1].size == 0)
                        ivSaveUser.visibility = INVISIBLE
                    else {
                        if (TemplateActivity.loginResponse?.data?.accessToken != null)
                            ivSaveUser.visibility = VISIBLE


                    }
                    sellerCategoriesProducts.reset()

                    viewPager.setCurrentItem(position + 1)
                }


            }

        })
        rlAllCats.setOnClickListener {
            if (productLoadingFinished)
                selectAllCategories()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (mediaPlayer != null) {
                    mediaPlayer!!.seekTo(progress)


                }


            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        ivBack.setOnClickListener {
            BasicTools.exitActivity(this)
        }

    }

    override fun set_fragment_place() {
    }


    fun setUpPlayVoice(user: UserProfileModel.Data.User) {
        if (user.voicIdentity != null && !user.voicIdentity.mediaPath.isNullOrEmpty()) {
            mediaPlayer = MediaPlayer()

            mediaPlayer?.setDataSource(
                BasicTools.getUrlHttpImg(this@SellerActivity, user.voicIdentity.mediaPath)
            )
            mediaPlayer?.setOnPreparedListener(OnPreparedListener {
                shimmerSeekBar.hide()
                shimmerSeekBar.visibility = GONE
                lyVid.visibility = View.VISIBLE
                seekBar.max = mediaPlayer!!.duration
                //mediaPlayer?.start()
                ivPlayMusic.performClick()

            }
            )
            mediaPlayer?.prepareAsync()


            ivPlayMusic.setOnClickListener {
                if (mediaPlayer != null) {
                    if (mediaPlayer != null && !mediaPlayer!!.isPlaying && runnable != null) {
                        mediaPlayer?.start()
                        handler.postDelayed(runnable!!, 1000)
                        ivPlayMusic.setImageResource(R.drawable.ic_pause)

                    } else {
                        ivPlayMusic.setImageResource(R.drawable.ic_play)

                        mediaPlayer?.pause()


                    }
                }
            }
            if (mediaPlayer != null)
                mediaPlayer!!.setOnCompletionListener {
                    seekBar.progress = 0
                    ivPlayMusic.setImageResource(R.drawable.ic_play)


                }
            runnable = Runnable {
                seekBar.progress = mediaPlayer?.currentPosition?:0
                handler.postDelayed(runnable!!, 1000)
            }

//                handler.postDelayed(runnable!!, 1000)

        } else {
            shimmerSeekBar.hide()
            shimmerSeekBar.visibility = GONE
            lyVid.visibility = View.GONE
            findViewById<TextView>(R.id.title_voice).visibility = View.GONE

        }


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

    fun beginNotificationShimmer() {
        shimmerNotification.visible()
        notificationImage.visibility = View.GONE

    }

    fun endNotificationShimmer() {
        shimmerNotification.hide()
        shimmerNotification.visibility = GONE
        notificationImage.visibility = View.VISIBLE

    }


    fun toggleNotification(id: String, operationType: String) {
        beginNotificationShimmer()
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
        val map = HashMap<String, String>()
        map.put("followed_id", id)
        map.put("operation_type", operationType)
        val observable = shopApi!!.toggleNotification(map)
        disposableNotifiaion.clear()
        disposableNotifiaion.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<BasicModel>(this) {
                    override fun onSuccess(result: BasicModel) {
                        endNotificationShimmer()
                        if (result.status!!) {
                            if (operaton == 1) {
                                operaton = 0
                                notificationImage.setImageResource(R.drawable.ic_notification)
                                Toast.makeText(
                                    applicationContext,
                                    getString(R.string.mst_add_notification),
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            } else {
                                operaton = 1
                                notificationImage.setImageResource(R.drawable.ic_notification_grey)
                                Toast.makeText(
                                    applicationContext,
                                    result.messages,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            }


                        }
                    }


                    override fun onFailed(status: Int) {
                        endNotificationShimmer()

                    }
                })
        )


    }


    fun getUser() {

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
        val observable = shopApi!!.getUserProfile(selleruserId.toString().toInt())
        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<UserProfileModel>(this) {
                    override fun onSuccess(result: UserProfileModel) {
                        BasicTools.showShimmer(rvCategories, shimmerCategories, false)
                        locationShimmer.hide()
                        locationShimmer.visibility = View.GONE
                        lyVid.visibility = View.GONE
                        shimmerCategories.visibility = View.GONE
                        textInfoShimmer.hide()
                        textInfoShimmer.visibility = GONE
                        starShimmer.hide()
                        starShimmer.visibility = View.GONE
                        findViewById<LinearLayout>(R.id.stars).visibility = View.VISIBLE

                        if (result.status!!) {
                            cvCats.visibility = View.VISIBLE
                            val user = result.data!!.user
                            tvName.text = user?.name
                            data = result.data
                            userSeller = result.data
                            categories = result.data.categoires as ArrayList<CategoryModel.Category>
                            if (data?.addtionInfo?.enableNotification!!)
                                operaton = 0
                            else operaton = 1
                            if (!user?.image.isNullOrEmpty())
                                Glide.with(applicationContext).load(
                                    BasicTools.getUrlHttpImg(
                                        this@SellerActivity,
                                        user?.image!!
                                    )
                                )
                                    .circleCrop()
                                    .into(ivAvatar)
                            if (BasicTools.isDeviceLanEn()) {
                                if (user?.country != null) {
                                    if (!user.country.name.isNullOrEmpty())
                                        tvLocation.setText(user.country.name + ", " + user.city?.name)
                                    else tvLocation.setText(getString(R.string.not_avaliable))
                                } else tvLocation.setText(getString(R.string.not_avaliable))

                            } else {
                                if (user?.country != null) {
                                    if (!user.country.nameAr.isNullOrEmpty())
                                        tvLocation.setText(user.country.nameAr + ", " + user.city?.nameAr)
                                    else tvLocation.setText(getString(R.string.not_avaliable))
                                } else tvLocation.setText(getString(R.string.not_avaliable))

                            }
                            if (result.data.addtionInfo?.enableNotification!!) {
                                notificationImage.setImageResource(R.drawable.ic_notification)

                            } else {
                                notificationImage.setImageResource(R.drawable.ic_notification_grey)


                            }
                            if (selleruserId.toString()
                                    .equals(TemplateActivity.loginResponse!!.data?.user?.id.toString())
                            ) {
                                notificationImage.visibility = View.GONE
                            } else {
                                notificationImage.visibility = View.VISIBLE
                            }


                            getProducts(result.data.user?.id!!)
                            setUpPlayVoice(user!!)
                            tvRateCount.text = user.countRaters?.toInt().toString()
                            setUpRateStars(user.rate!!)
                            categoryAdapter.submitList(categories)

                            rvCategories.layoutManager = LinearLayoutManager(
                                this@SellerActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            rvCategories.adapter = categoryAdapter
                            if (lang == "en") {
                                tvAvailableProduct.text =
                                    "Available Product (" + user!!.countAviableProducts + ")"

                            } else {
                                tvAvailableProduct.text =
                                    "المنتجات المتوفرة ( " + result.data!!.user!!.countAviableProducts.toString() + " )"


                            }


                        }


                    }

                    override fun onFailed(status: Int) {
                        locationShimmer.hide()
                        locationShimmer.visibility = View.GONE
                        shimmerCategories.visibility = View.GONE
                        lyVid.visibility = View.GONE
                        shimmerSeekBar.hide()
                        shimmerSeekBar.visibility = GONE
                        textInfoShimmer.hide()
                        textInfoShimmer.visibility = GONE
                        starShimmer.hide()
                        starShimmer.visibility = View.GONE
                        findViewById<LinearLayout>(R.id.stars).visibility = View.VISIBLE


                    }
                })
        )

    }

    fun getUserByVisitor() {
        val shopApi =
            ApiClient.getClient(
                BasicTools.getProtocol(
                    applicationContext
                ).toString(),
                lang
            )
                ?.create(
                    AppApi::class.java
                )
        val observable = shopApi!!.getUserProfile(selleruserId!!.toInt())
        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<UserProfileModel>(this) {
                    override fun onSuccess(result: UserProfileModel) {
                        BasicTools.showShimmer(rvCategories, shimmerCategories, false)
                        locationShimmer.hide()
                        locationShimmer.visibility = View.GONE
                        lyVid.visibility = View.GONE

                        shimmerCategories.visibility = View.GONE
                        textInfoShimmer.hide()
                        textInfoShimmer.visibility = GONE
                        starShimmer.hide()
                        starShimmer.visibility = View.GONE
                        findViewById<LinearLayout>(R.id.stars).visibility = View.VISIBLE
                        rlAllCats.visibility = View.VISIBLE



                        if (result.status!!) {
                            val user = result.data!!.user
                            tvName.text = user?.name

                            userSeller = result.data
                            categories = result.data.categoires as List<CategoryModel.Category>

                            getProducts(result.data.user?.id!!)
                            setUpPlayVoice(user!!)
                            tvRateCount.text = user.countRaters?.toInt().toString()
                            setUpRateStars(user.rate!!)
                            categoryAdapter.submitList(categories)
                            if (BasicTools.isDeviceLanEn()) {
                                if (user.country != null) {
                                    if (!user.country.name.isNullOrEmpty())
                                        tvLocation.setText(user.country.name + ", " + user.city?.name)
                                    else tvLocation.setText(getString(R.string.not_avaliable))
                                } else tvLocation.setText(getString(R.string.not_avaliable))

                            } else {
                                if (user?.country != null) {
                                    if (!user.country.nameAr.isNullOrEmpty())
                                        tvLocation.setText(user.country.nameAr + ", " + user.city?.nameAr)
                                    else tvLocation.setText(getString(R.string.not_avaliable))
                                } else tvLocation.setText(getString(R.string.not_avaliable))

                            }
                            rvCategories.layoutManager = LinearLayoutManager(
                                this@SellerActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            rvCategories.adapter = categoryAdapter
                            if (lang == "en") {
                                tvAvailableProduct.text =
                                    "Available Product (" + user!!.countAviableProducts + ")"

                            } else {
                                tvAvailableProduct.text =
                                    "المنتجات المتوفرة ( " + result.data!!.user!!.countAviableProducts.toString() + " )"

                            }


                        }


                    }

                    override fun onFailed(status: Int) {
                        locationShimmer.hide()
                        locationShimmer.visibility = View.GONE
                        shimmerCategories.visibility = View.GONE
                        lyVid.visibility = View.GONE
                        shimmerSeekBar.hide()
                        shimmerSeekBar.visibility = GONE
                        textInfoShimmer.hide()
                        textInfoShimmer.visibility = GONE
                        starShimmer.hide()
                        starShimmer.visibility = View.GONE
                        findViewById<LinearLayout>(R.id.stars).visibility = View.VISIBLE


                    }
                })
        )

    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }

    fun selectAllCategories() {
        if (allProducts[0].size == 0)
            ivSaveUser.visibility = INVISIBLE
        else {
            if (TemplateActivity.loginResponse?.data?.accessToken != null)
                ivSaveUser.visibility = VISIBLE

        }
        rlAllCats.background = resources.getDrawable(R.drawable.category_selected_item_bg)
        cvCats.cardElevation = 0f
        sellerCategoriesProducts.reset()
        viewPager.setCurrentItem(0)
        categoryAdapter.selected = -1
        categoryAdapter.notifyDataSetChanged()
    }
}