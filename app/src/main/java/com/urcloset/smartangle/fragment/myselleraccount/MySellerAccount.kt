package com.urcloset.smartangle.fragment.myselleraccount
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.CustomViewPager
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.activity.homeActivity.HomeViewModel
import com.urcloset.smartangle.adapter.*
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.fragment.setting_fragment.SettingFragment
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.model.*
import com.urcloset.smartangle.tools.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MySellerAccount() : TemplateFragment() {
    var disposable = CompositeDisposable()
    lateinit var rlSetting:RelativeLayout
    lateinit var tvName: TextView
    lateinit var ivAvatar: ImageView
    lateinit var tvAvailableProduct: TextView
    lateinit var tvLocation: TextView
    lateinit var tvRateCount: TextView
    lateinit var star1: ImageView
    lateinit var star2: ImageView
    lateinit var star3: ImageView
    lateinit var star4: ImageView
    lateinit var star5: ImageView
    lateinit var ivBack : ImageView
    lateinit var categories:List<CategoryModel.Category>
    var products = ArrayList<ProductModel.Product>()
    lateinit var rvCategories: RecyclerView
    val categoryAdapter= CategoryAdapterSeller()
    lateinit var sellerCategoriesProducts: MySellerProductsAdapter
    lateinit var viewPager: CustomViewPager
    private var allProducts=ArrayList<ArrayList<ProductModel.Product>>()
    var selctedCategory = 0

    var lang ="en"
    lateinit var categoryShimmer :ShimmerFrameLayout
    lateinit var productSimmer :ShimmerFrameLayout
    lateinit var productStateShimmer:ShimmerFrameLayout
    lateinit var starShimmer:ShimmerFrameLayout
    lateinit var myTextShimmer: ShimmerFrameLayout
    lateinit var myLocationSimmer:ShimmerFrameLayout
    lateinit var rlAllProducts:RelativeLayout
    lateinit var cvCats:CardView
    var data : ProductStateModel.State?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.my_seller_account_layout, container, false)
        tvName = view.findViewById(R.id.tv_name)
        ivAvatar = view.findViewById(R.id.iv_avatar)
        tvAvailableProduct = view.findViewById(R.id.tv_available_product)
        tvLocation =  view.findViewById(R.id.tv_location)
        tvRateCount =view.findViewById(R.id.tv_rate_count)
        myLocationSimmer = view.findViewById(R.id.my_shimmer_location)
        star1 = view.findViewById(R.id.iv_star_1)
        star2 = view.findViewById(R.id.iv_star_2)
        star3 = view.findViewById(R.id.iv_star_3)
        star4 = view.findViewById(R.id.iv_star_4)
        star5 = view.findViewById(R.id.iv_star_5)
        rvCategories = view.findViewById(R.id.rv_categories)
        viewPager = view.findViewById(R.id.seller_view_pager)
        viewPager.setPagingEnabled(false)
        ivBack = view.findViewById(R.id.iv_back)
        ivAvatar = view.findViewById(R.id.iv_avatar)
        myTextShimmer = view.findViewById(R.id.my_text_info_shimmer)
        categoryShimmer = view.findViewById(R.id.shimmer_cat)
        productSimmer = view.findViewById(R.id.shimmer_my_main_product)
        rlAllProducts = view.findViewById(R.id.rl_all_cat)
        tvName.setText(TemplateActivity.loginResponse?.data?.user?.name)
        cvCats = view.findViewById(R.id.cv_cat)
        rlSetting = view.findViewById(R.id.rl_setting)
        if(BasicTools.isConnected(parent!!)) {
            BasicTools.showShimmer(rvCategories, categoryShimmer, true)
            viewPager.visibility = View.GONE
            productStateShimmer = view.findViewById(R.id.shimmer_product_state)
            productSimmer.visible()

            starShimmer = view.findViewById(R.id.my_star_shimmer)
            starShimmer.visible()
            myTextShimmer.visible()
            myLocationSimmer.visible()

            getPersonaluserProfile()
        }
        else {
            Toast.makeText(parent!!, R.string.no_connection, Toast.LENGTH_SHORT).show()
        }


        viewModelHome.setPreviousNavBottom(R.id.setting)

        return view
    }

    override fun init_events() {
        rlSetting.setOnClickListener {
            val f = SettingFragment()
            parent!!.show_fragment2(f, false, false, R.id.root_fragment_home)
        }
        categoryAdapter.setOnCategoryClickListener(object : ItemClickListener {
            override fun onClick(position: Int) {
                rlAllProducts.background= resources.getDrawable(R.drawable.seller_category_unselected_item_bg)
                cvCats.cardElevation = 4f
                selctedCategory = position
                viewPager.setCurrentItem(position+1)
                sellerCategoriesProducts.reset()


            }


        })
        rlAllProducts.setOnClickListener {
        selelctAllCategores()
        }


        ivBack.setOnClickListener {
            parent?.onBackPressed()
        }

    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }

    private fun selelctAllCategores() {
        categoryAdapter.selected =-1
        viewPager.setCurrentItem(0)
        sellerCategoriesProducts.reset()
        cvCats.cardElevation = 0f
        rlAllProducts.background = resources.getDrawable(R.drawable.category_selected_item_bg)
        categoryAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
   /*     if(HomeActivity.bottomNavigation?.currentItem!=4){
            HomeActivity.doNothing =  true
            HomeActivity.bottomNavigation?.currentItem=4
            HomeActivity.doNothing =  false

        }*/
        super.onResume()
    }
    fun getPersonaluserProfile(){

        if(!BasicTools.isDeviceLanEn())
            lang ="ar"
        val shopApi =
            ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(
                    parent!!
                ).toString(),
                lang
            )
                ?.create(
                    AppApi::class.java
                )
        val observable = shopApi!!.getUserProfile(TemplateActivity.loginResponse?.data?.user?.id!!)
        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<UserProfileModel>(parent!!) {
                    override fun onSuccess(result: UserProfileModel) {
                        myLocationSimmer.hide()
                        myLocationSimmer.visibility = View.GONE
                        BasicTools.showShimmer(rvCategories,categoryShimmer,false)
                        categoryShimmer.visibility = View.GONE
                        starShimmer.hide()
                        cvCats.visibility = View.VISIBLE
                        starShimmer.visibility = View.GONE
                        view?.findViewById<LinearLayout>(R.id.mystar)?.visibility = View.VISIBLE
                        myTextShimmer.hide()
                        myTextShimmer.visibility = View.GONE
                        tvAvailableProduct.visibility = View.VISIBLE





                        if (result.status!!) {

                            val user = result.data!!.user
                            categories = result.data.categoires as List<CategoryModel.Category>
                            rlAllProducts.visibility = View.VISIBLE
                            getProducts(result.data.user?.id!!)
                            tvRateCount.text = user!!.countRaters?.toInt().toString()
                            setUpRateStars(user.rate!!)
                            categoryAdapter.submitList(categories)

                            rvCategories.layoutManager = LinearLayoutManager(
                                parent,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            rvCategories.adapter = categoryAdapter
                            if (lang == "en") {
                                tvAvailableProduct.text =
                                    "Available Product (" + user!!.countAviableProducts + ")"

                                tvLocation.text =
                                user.country?.name
                            } else {
                                tvAvailableProduct.text =
                                    " المنتجات المتوفرة ( "+result.data.user.countAviableProducts.toString()+" )"
                                tvLocation.text = user.country?.nameAr

                            }
                            if(user.image!="") {
                                user.image?.let {
                                    Glide.with(parent!!.applicationContext)
                                        .load(
                                            BasicTools.getUrlHttpImg(
                                                parent!!.applicationContext,
                                                user.image
                                            )
                                        )
                                        .circleCrop()
                                        .into(ivAvatar)
                                }
                            }

                            tvName.text = user.name


                        }

                    }

                    override fun onFailed(status: Int) {
                        BasicTools.showShimmer(rvCategories,categoryShimmer,false)
                        starShimmer.hide()
                        myLocationSimmer.hide()
                        myLocationSimmer.visibility = View.GONE
                        starShimmer.visibility = View.GONE
                        view?.findViewById<LinearLayout>(R.id.mystar)?.visibility = View.VISIBLE
                        categoryShimmer.visibility = View.GONE
                        myTextShimmer.hide()
                        myTextShimmer.visibility = View.GONE
                        tvAvailableProduct.visibility = View.VISIBLE





                    }
                }));
    }

    override fun init_fragment(savedInstanceState: Bundle?) {

    }

    override fun on_back_pressed(): Boolean {
        return true

    }
    private fun getProducts(id: Int?) {


        val shopApi =
            ApiClient.getClient(
                BasicTools.getProtocol(parent!!).toString(), lang
            )
                ?.create(
                    AppApi::class.java
                )
        val observable = shopApi!!.getProducts("no", id.toString())
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<ProductModel>(parent!!) {
                    override fun onSuccess(result: ProductModel) {
                        productSimmer.hide()
                        productSimmer.visibility = View.GONE
                        viewPager.visibility = View.VISIBLE
                        if (result.status!!) {
                            products = (result.products as ArrayList<ProductModel.Product>?)!!
                            allProducts.clear()
                            allProducts.add( products.filter {
                                it.itemStatus!=2 && it.itemStatus!=3

                            } as ArrayList<ProductModel.Product>)
                            categories.forEach {
                                val tempProducts = ArrayList<ProductModel.Product>()
                                for (i in 0 until products.size) {
                                    if (it.id.toString()
                                            .equals(products[i].categoryId.toString())
                                    ) {
                                        if(products[i].itemStatus!=2 && products[i].itemStatus!=3)
                                        tempProducts.add(products[i])
                                    }
                                }

                                allProducts.add(tempProducts)


                            }


                            sellerCategoriesProducts = MySellerProductsAdapter(
                                allProducts,
                                parent!!,
                                categories
                            )


                            viewPager.adapter = sellerCategoriesProducts
                            viewPager.visibility = View.VISIBLE
                            selelctAllCategores()

                        }


                    }

                    override fun onFailed(status: Int) {
                        productSimmer.hide()
                        productSimmer.visibility = View.GONE


                    }
                })
        )

    }


    fun setUpRateStars(rated: Double){
        val rate:Int = rated.toInt()
        if(rate == 0){

        }
        else if(rate == 1){
            star1.setImageResource(R.drawable.ic_star_filled)

        }
        else if(rate == 2){
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)


        }
        else if(rate == 3){
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)
            star3.setImageResource(R.drawable.ic_star_filled)


        }
        else if(rate == 4){
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)
            star3.setImageResource(R.drawable.ic_star_filled)
            star4.setImageResource(R.drawable.ic_star_filled)
            star5.setImageResource(R.drawable.ic_star_filled)
        }

        else if(rate == 5){
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)
            star3.setImageResource(R.drawable.ic_star_filled)
            star4.setImageResource(R.drawable.ic_star_filled)
            star5.setImageResource(R.drawable.ic_star_filled)
        }
    }




}