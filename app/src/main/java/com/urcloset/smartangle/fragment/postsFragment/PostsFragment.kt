package com.urcloset.smartangle.fragment.postsFragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.activity.homeActivity.HomeViewModel
import com.urcloset.smartangle.activity.notificationActivity.NotificationActivity
import com.urcloset.smartangle.activity.searchActivity.SearchActivity
import com.urcloset.smartangle.adapter.postAdapter.PostAdapter
import com.urcloset.smartangle.adapter.SpinnerAdapter
import com.urcloset.smartangle.adapter.postAdapter.PostGridAdapter
import com.urcloset.smartangle.adapter.project105.CountrySpinnerAdapter
import com.urcloset.smartangle.adapter.project105.CountryWithCitySpinnerAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.data.CountryCityRepository
import com.urcloset.smartangle.databinding.BottomsheetLayoutPostDistanceBinding
import com.urcloset.smartangle.model.CategoryModel
import com.urcloset.smartangle.model.NotificationModel
import com.urcloset.smartangle.model.PostsModel
import com.urcloset.smartangle.model.project_105.CountryModel
import com.urcloset.smartangle.model.project_105.CountryWithCity
import com.urcloset.smartangle.notification.MyFirebaseMessaging
import com.urcloset.smartangle.tools.*
import com.urcloset.smartangle.tools.BasicTools.getFirebaseFcmTokenBeforeStart
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class PostsFragment():TemplateFragment() {
    lateinit var spinner: Spinner
    lateinit var ivFilterPosts:ImageView
    lateinit var shimmerCategories:ShimmerFrameLayout
    lateinit var activityToolbar:androidx.appcompat.widget.Toolbar
    lateinit var rvPosts:RecyclerView
    lateinit var userShimmer1:ShimmerFrameLayout
    lateinit var userShimmer2:ShimmerFrameLayout
    lateinit var productShimmer1:ShimmerFrameLayout
    lateinit var productShimmer2:ShimmerFrameLayout
    lateinit var postGridShimmer:ShimmerFrameLayout
    lateinit var gridLayout: LinearLayout
    lateinit var listLayout: LinearLayout
    lateinit var  cview:View
    lateinit var swipeRefreshLayout : SwipeRefreshLayout
    lateinit var progressBar: ProgressBar
    lateinit var postsModel:PostsModel
    lateinit var tvUserLocation: TextView
    lateinit var ivProfile:ImageView
    lateinit var tvUsername:TextView
    lateinit var rlSearch:RelativeLayout
    lateinit var ivNotification:ImageView
    var postAdapter: PostAdapter?=null
    var posts =  ArrayList<PostsModel.Data.Post>()
    var postGridAdapter:PostGridAdapter?=null
    var selectedViewType=0 // 0 for grid 1 for list
    var currentPage =1
    var distance:String?=null
    var cityId : String?= null
    var countryId : String? = null
    var categoryId:String?=null
    var categories:ArrayList<CategoryModel.Category>?=null
    var isOpened=false
    var  isLoading = false
    var itemloaded = false
    override fun onResume() {
        super.onResume()
  /*      if(HomeActivity.bottomNavigation?.currentItem!=0){
            HomeActivity.doNothing =  true
            HomeActivity.bottomNavigation?.currentItem=0
            HomeActivity.doNothing =  false

        }*/
    }
   // val viewModelHome : HomeViewModel by activityViewModels()
    var sameFragmentCreated = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cview =inflater.inflate(R.layout.posts_fragment, container, false)
        spinner = cview.findViewById<Spinner>(R.id.sp_posts_filter)
        ivFilterPosts =cview.findViewById<ImageView>(R.id.iv_filter_posts)
        shimmerCategories = cview.findViewById<ShimmerFrameLayout>(R.id.shimmer_categories)
        activityToolbar = cview.findViewById(R.id.activity_toolbar)
        rvPosts=cview.findViewById(R.id.rv_posts)
        rvPosts.layoutManager = LinearLayoutManager(parent!!,LinearLayoutManager.VERTICAL,false)
        userShimmer1 = cview.findViewById(R.id.user_section_shimmer_1)
        userShimmer2 = cview.findViewById(R.id.user_section_shimmer_2)
        productShimmer1 = cview.findViewById(R.id.product_section_shimmer_1)
        productShimmer2 = cview.findViewById(R.id.product_section_shimmer_2)
        gridLayout = cview.findViewById(R.id.ly_grid)
        listLayout = cview.findViewById(R.id.ly_list)
        ivProfile = cview.findViewById(R.id.iv_profile_avatar)
        tvUsername = cview.findViewById(R.id.tv_username)
        progressBar = cview.findViewById(R.id.progress_bar)
        postGridShimmer = cview.findViewById(R.id.product_grid_shimmer)
        tvUserLocation=cview.findViewById(R.id.tv_user_location)
        rlSearch = cview.findViewById(R.id.rl_search)
        ivNotification = cview.findViewById(R.id.iv_notification)
        swipeRefreshLayout = cview.findViewById(R.id.swipe)
        if(selectedViewType==0)
            setGridViewType(cview)
        else setListViewType(cview)
        setWelcomeUser()
        setUserLocation()
        if (BasicTools.getToken(requireContext()).isNotEmpty()) // is logged in
        cityId =
            BasicTools.getUserModel(context?.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)?.getString(Constants.USER_MODEL,"")?:"")?.user?.cityId?:"1"
        if (posts.isEmpty())
        getCategories()
        else {
            sameFragmentCreated = true
            //listLayout.performClick()
            if (selectedViewType == 0) // grid
                setGridList()
            else setNormalList()
            setCategoriesAdaptor(ArrayList<String>().also { it.add("") }, ArrayList<String>().also { it.add("") },categories)

        }
        setNotification()
        getFirebaseFcmTokenBeforeStart(callBackTokenIsComing)


        return cview
    }
    val callBackTokenIsComing : (String?)->Unit = {
        if(!BasicTools.getToken(requireContext()).isEmpty()){
            MyFirebaseMessaging.saveTokenRQ(it?:"", requireContext())

        }
    }
    override fun init_events() {
        if (arguments?.getBoolean("show_review",false) == true
            &&!BasicTools.getRevviewedBefore(requireActivity()))
            BasicTools.openReviewGoogle(requireActivity())
        ivFilterPosts.setOnClickListener {
            if(!isOpened)
            openDialog()
        }
        rlSearch.setOnClickListener {
            BasicTools.openActivity(parent!!, SearchActivity::class.java, false)
        }
        swipeRefreshLayout.setOnRefreshListener {
            getPost(categoryId, distance, countryId, cityId)
            swipeRefreshLayout.isRefreshing = false
        }
        // category filter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position!=0)
                categoryId = categories?.get(position-1)?.id.toString()
                else categoryId = null
                if (!sameFragmentCreated)
                getPost(categoryId,distance,countryId,cityId)
                else
                    sameFragmentCreated = false

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        rvPosts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if(!isLoading) {
                        if (currentPage < postsModel.data?.lastPage!!) {
                            progressBar.visibility = View.VISIBLE
                            currentPage = currentPage + 1
                            getNextPost(categoryId,distance,currentPage,countryId,cityId)
                        }
                    }


                }
            }
        })
        gridLayout.setOnClickListener {
            if(selectedViewType==1) {
                if(itemloaded) {
                    setGridList()
                }
            }
        }
        listLayout.setOnClickListener {
            if(selectedViewType==0) {
                if(itemloaded) {
                    setNormalList()
                }

            }
        }
        ivNotification.setOnClickListener {
            if (TemplateActivity.loginResponse?.data?.accessToken!=null) {
                TemplateActivity.isNotificationVisited =1
                BasicTools.openActivity(parent!!, NotificationActivity::class.java, false)
            }

        }
        viewModelHome.setPreviousNavBottom(R.id.posts)


    }

    private fun setGridList() {
      /*  rvPosts.layoutManager =
            GridLayoutManager(parent, 3, GridLayoutManager.VERTICAL, false)
        postGridAdapter = PostGridAdapter(parent!!, ArrayList<PostsModel.Data.Post>(posts))
        rvPosts.adapter = postGridAdapter*/
        BasicTools.setRecycleView(rvPosts,postGridAdapter!!,
            null,requireContext(), GridModel(3,0),false)
        setGridViewType(cview)
        selectedViewType = 0
    }

    private fun setNormalList() {
        rvPosts.layoutManager =
            LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false)
        postAdapter = PostAdapter(parent!!, ArrayList<PostsModel.Data.Post>(posts))

        rvPosts.adapter = postAdapter
        setListViewType(cview)
        selectedViewType = 1
    }

    override fun init_fragment(savedInstanceState: Bundle?) {

    }

    override fun on_back_pressed(): Boolean {
        return true
    }
    fun openDialog(){ // dialog filter home
        isOpened =true
        val dialog = Dialog(parent!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding =  BottomsheetLayoutPostDistanceBinding.inflate(layoutInflater)
        dialog.setContentView(/*R.layout.bottomsheet_layout_post_distance*/binding.root)
        dialog.show()
        val tvDistance =dialog.findViewById<TextView>(R.id.tv_seek_bar)
        val seekBar =dialog.findViewById<SeekBar>(R.id.seek_bar_distance)
        val submit =dialog.findViewById<CardView>(R.id.cv_apply)
        submit.setOnClickListener {
            getPost(categoryId, distance, countryId, cityId)

            dialog.cancel()
        }

        if(!distance.isNullOrEmpty()) {
            seekBar.progress = distance.toString().toInt()
            tvDistance.setText("$distance  KM")

        }
        var adaptorCountry : CountrySpinnerAdapter? = null
        var adaptorCity : CountryWithCitySpinnerAdapter? = null
        binding.cardReset.setOnClickListener{
            seekBar.progress  = 0
            distance = null
            binding.spinnerCountry.setSelection(0)
        }
        val observerCountry =  object : AppObservable<CountryModel>(requireContext()) { // get city list
            override fun onSuccess(result: CountryModel) {
                BasicTools.showShimmer(binding.spinnerCountry,binding.shimmerCountry,false)
                val arrayList = ArrayList<CountryModel.Data>()
                arrayList.add(CountryModel.Data(nameAr = "اختر الدولة",name = "Select Country"))
                arrayList.addAll(result.data!!)
                 adaptorCountry= CountrySpinnerAdapter(requireContext(),
                    arrayList)

                binding.spinnerCountry.adapter=adaptorCountry
            }
            override fun onFailed(status: Int) {
                BasicTools.showShimmer(binding.spinnerCity,binding.shimmerCity,false)

            }
        }
        getCountry(observerCountry,binding) // load country list
        val observerCity =  object : AppObservable<CountryWithCity>(requireContext()) { // get city list
            override fun onSuccess(result: CountryWithCity) {
                BasicTools.showShimmer(binding.spinnerCity,binding.shimmerCity,false)
                val arrayList = ArrayList<CountryWithCity.Data.Citty>()
                arrayList.add(CountryWithCity.Data.Citty(nameAr = "اختر المدينة",name = "Select City"))
                arrayList.addAll(result.data?.citties!!)
                adaptorCity= CountryWithCitySpinnerAdapter(requireContext(),
                   arrayList)

                binding.spinnerCity.adapter=adaptorCity
            }
            override fun onFailed(status: Int) {
                BasicTools.showShimmer(binding.spinnerCity,binding.shimmerCity,false)
                Toast.makeText(requireContext(),getString(R.string.faild),Toast.LENGTH_SHORT).show()
            }
        }

        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position>0){
                    val item: CountryModel.Data= adaptorCountry?.getItem(position)!!
                    getCountryWithCity(item.id.toString(),observerCity,binding)
                    countryId = item.id.toString()

                } else {
                    countryId = null
                    cityId = null
                    adaptorCity?.clearList()
                }

            }
        }


        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position>0){
                    val item: CountryWithCity.Data.Citty= adaptorCity?.getItem(position)!!
                    //getCountryWithCity(item.id.toString())
                    if(item!=null &&item.id!=null)
                        cityId = item.id.toString()
                } else
                    cityId = null

            }
        }
        seekBar.setOnSeekBarChangeListener(
            object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                    distance=progress.toString()
                    tvDistance.setText("$progress  KM")

                }

                override fun onStartTrackingTouch(seek: SeekBar) {

                }

                override fun onStopTrackingTouch(seek: SeekBar) {

                }
            }
        )

        dialog.getWindow()?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.getAttributes()?.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow()?.setGravity(Gravity.BOTTOM)
        dialog.setOnCancelListener {
            isOpened = false

        }

    }
    fun getCountryWithCity(
        id: String,
        observerCity: AppObservable<CountryWithCity>,
        binding: BottomsheetLayoutPostDistanceBinding
    ){
        if (BasicTools.isConnected(requireContext())) {
            BasicTools.showShimmer(binding.spinnerCity,binding.shimmerCity,true)

            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(requireContext()).toString())
                ?.create(AppApi::class.java)

          //  val observable= shopApi!!.getCountryWithCity(id)
            CountryCityRepository(shopApi!!).getCity(id,observerCity)

        }
        else {

            Toast.makeText(requireContext(),R.string.no_connection,Toast.LENGTH_SHORT).show()
        }
    }
    fun getCountry(
        observerCity: AppObservable<CountryModel>,
        binding: BottomsheetLayoutPostDistanceBinding
    ){
        if (BasicTools.isConnected(requireContext())) {
            BasicTools.showShimmer(binding.spinnerCountry,binding.shimmerCountry,true)

            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(requireContext()).toString())
                ?.create(AppApi::class.java)

            //  val observable= shopApi!!.getCountryWithCity(id)
            CountryCityRepository(shopApi!!).getCountry(observerCity)

        }
        else {

            Toast.makeText(requireContext(),R.string.no_connection,Toast.LENGTH_SHORT).show()
        }
    }
    fun saveObserverData() {
        viewModelHome.savePosts.observe(this, Observer{
            it?.let {categoryModel->

            }?: getCategories()
        })
        viewModelHome.postLiveData.observe(this, Observer{
            it?.let {categoryModel->

            }?: getCategories()
        })
    }
    fun getCategories(){
        if(BasicTools.isConnected(parent!!)) {
            beginShimmerCategories()
            val disposable = CompositeDisposable()

            val shopApi =
                ApiClient.getClient(
                    BasicTools.getProtocol(parent!!.applicationContext).toString(), "en")
                    ?.create(
                        AppApi::class.java
                    )

            val observable = shopApi!!.getCategories("no")
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object :
                        AppObservable<CategoryModel>(parent!!.applicationContext) {
                        override fun onSuccess(result: CategoryModel) {
                            endShimmerCategories()
                            val images = ArrayList<String>()
                            val names = ArrayList<String>()

                            if (result.status!!) {
                                 categories = result.categories as ArrayList<CategoryModel.Category>?
                                images.add("")
                                names.add("")
                                setCategoriesAdaptor(images,names,categories)
                                getPost(categoryId, distance, countryId, cityId)

                            }

                        }

                        override fun onFailed(status: Int) {
                            endShimmerCategories()


                            super.onFailed(status)
                        }


                    })
            )
        }
        else {
            Toast.makeText(parent, R.string.no_connection, Toast.LENGTH_SHORT).show()

        }
    }

    private fun setCategoriesAdaptor(images: ArrayList<String>, names: ArrayList<String>, categories: ArrayList<CategoryModel.Category>?) {
        categories!!.forEach {
            images.add(it.mediaPath!!)
            if (!BasicTools.isDeviceLanEn())
                names.add(it.nameAr!!)
            else names.add(it.nameEn!!)


        }

        val postsFilter =
            SpinnerAdapter(parent!!, images = images, names = names)
        spinner.adapter = postsFilter
    }

    fun  beginShimmerCategories(){
        activityToolbar.visibility = View.GONE
        shimmerCategories.visibility = View.VISIBLE
        shimmerCategories.startShimmerAnimation()


    }
    fun  endShimmerCategories(){
        shimmerCategories.stopShimmerAnimation()

        shimmerCategories.visibility = View.GONE

        activityToolbar.visibility = View.VISIBLE

    }
    fun getPost(categoryId: String?, distance: String?, countryId: String?, cityId: String?){ // get products
        if(BasicTools.isConnected(parent!!)) {
            cview.findViewById<LinearLayout>(R.id.root_empty_page).visibility = View.GONE

            itemloaded = false
            currentPage = 1
            beginShimmerPost()
            rvPosts.visibility = View.GONE
            val disposable = CompositeDisposable()
            var shopApi : AppApi?=null
            if(TemplateActivity.loginResponse?.data?.accessToken.isNullOrEmpty())
             shopApi =
                ApiClient.getClient(
                    BasicTools.getProtocol(parent!!.applicationContext).toString(), "en")
                    ?.create(
                        AppApi::class.java
                    )
            else    shopApi =  ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(parent!!.applicationContext).toString(), "en")
                ?.create(
                    AppApi::class.java
                )
            val map = HashMap<String,String>()

            map.put("with_paginate","yes")
          /*  if(TemplateActivity.loginResponse?.data?.accessToken!=null){
                if(!TemplateActivity.loginResponse?.data?.user?.lat.isNullOrEmpty())
                    map.put("lat", TemplateActivity.loginResponse?.data?.user?.lat!!)
                if(!TemplateActivity.loginResponse?.data?.user?.long.isNullOrEmpty())
                    map.put("long", TemplateActivity.loginResponse?.data?.user?.long!!)
                if(!TemplateActivity.loginResponse?.data?.user?.cityId.isNullOrEmpty())
                    map.put("city_id", TemplateActivity.loginResponse?.data?.user?.cityId!!)
                if(!TemplateActivity.loginResponse?.data?.user?.countryId.isNullOrEmpty())
                    map.put("country_id", TemplateActivity.loginResponse?.data?.user?.cityId!!)

            }
            else{
                if(TemplateActivity.currenLocationVistor!=null) {
                    map.put("lat", TemplateActivity.currenLocationVistor!!.latitude.toString())
                    map.put("long", TemplateActivity.currenLocationVistor!!.longitude.toString())
                }
                if(!TemplateActivity.selectedCityVisitor.isNullOrEmpty())
                    map.put("city_id", TemplateActivity.selectedCityVisitor)
                if(!TemplateActivity.selectedCountryVisitor.isNullOrEmpty())
                    map.put("country_id", TemplateActivity.selectedCountryVisitor)

            }*/
            if(!categoryId.isNullOrEmpty())
                map.put("category_id",categoryId.toString())
            if(!distance.isNullOrEmpty())
                map.put("distance",distance.toString())

            countryId?.let { it->
                map.put("country_id", it)
            }
            cityId?.let { it->
                map.put("city_id", it)
            }

            val observable = shopApi!!.getPosts(map)
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object :
                        AppObservable<PostsModel>(parent!!.applicationContext) {
                        override fun onSuccess(result: PostsModel) {
                            endShimmerPost()
                            itemloaded = true

                            if(result.status == true ){
                                postsModel = result
                                postAdapter?.clearPosts()
                                if(result.data?.data?.size!!>0) {
                                    cview.findViewById<LinearLayout>(R.id.root_empty_page).visibility = View.GONE
                                    posts = result.data.data
                                    if(selectedViewType==1) {
                                        rvPosts.visibility = View.VISIBLE

                                        rvPosts.layoutManager =LinearLayoutManager(parent,LinearLayoutManager.VERTICAL,false)
                                        postAdapter = PostAdapter(parent!!, posts)
                                        rvPosts.adapter = postAdapter

                                    }
                                    else{
                                        rvPosts.layoutManager =GridLayoutManager(parent,3,GridLayoutManager.VERTICAL,false)
                                        postGridAdapter = PostGridAdapter(parent!!, posts)
                                        rvPosts.adapter = postGridAdapter
                                    }

                                }
                                else {
                                    rvPosts.visibility = View.GONE
                                    cview.findViewById<LinearLayout>(R.id.root_empty_page).visibility = View.VISIBLE
                                }
                            }

                        }

                        override fun onFailed(status: Int) {
                            endShimmerPost()
                            itemloaded = true

                            super.onFailed(status)
                        }


                    })
            )
        }
        else {
            Toast.makeText(parent, R.string.no_connection, Toast.LENGTH_SHORT).show()

        }

    }
    fun getNextPost(
        categoryId: String?,
        distance: String?,
        currentPage: Int,
        countryId: String?,
        cityId: String?
    ){
        if(BasicTools.isConnected(parent!!)) {
            progressBar.visibility = View.VISIBLE
            val disposable = CompositeDisposable()
            var shopApi : AppApi?=null
            if(TemplateActivity.loginResponse?.data?.accessToken.isNullOrEmpty())
                shopApi =
                    ApiClient.getClient(
                        BasicTools.getProtocol(parent!!.applicationContext).toString(), "en")
                        ?.create(
                            AppApi::class.java
                        )
            else    shopApi =  ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(parent!!.applicationContext).toString(), "en")
                ?.create(
                    AppApi::class.java
                )
            val map = HashMap<String,String>()
            map.put("with_paginate","yes")
     /*       if(TemplateActivity.loginResponse?.data?.accessToken!=null){
                if(!TemplateActivity.loginResponse?.data?.user?.lat.isNullOrEmpty())
                    map.put("lat", TemplateActivity.loginResponse?.data?.user?.lat!!)
                if(!TemplateActivity.loginResponse?.data?.user?.long.isNullOrEmpty())
                    map.put("long", TemplateActivity.loginResponse?.data?.user?.long!!)
                if(!TemplateActivity.loginResponse?.data?.user?.cityId.isNullOrEmpty())
                    map.put("city_id", TemplateActivity.loginResponse?.data?.user?.cityId!!)
                if(!TemplateActivity.loginResponse?.data?.user?.countryId.isNullOrEmpty())
                    map.put("country_id", TemplateActivity.loginResponse?.data?.user?.cityId!!)

            }
            else{
                if(TemplateActivity.currenLocationVistor!=null) {
                    map.put("lat", TemplateActivity.currenLocationVistor!!.latitude.toString())
                    map.put("long", TemplateActivity.currenLocationVistor!!.longitude.toString())
                }
                if(!TemplateActivity.selectedCityVisitor.isNullOrEmpty())
                    map.put("city_id", TemplateActivity.selectedCityVisitor)
                if(!TemplateActivity.selectedCountryVisitor.isNullOrEmpty())
                    map.put("country_id", TemplateActivity.selectedCountryVisitor)

            }*/
            if(!categoryId.isNullOrEmpty())
                map.put("category_id",categoryId.toString())
            if(!distance.isNullOrEmpty())
                map.put("distance",distance.toString())

            map.put("page",currentPage.toString())

            countryId?.let { it->
                map.put("country_id", it)
            }
            cityId?.let { it->
                map.put("city_id", it)
            }

            val observable = shopApi!!.getPosts(map)
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object :
                        AppObservable<PostsModel>(parent!!.applicationContext) {
                        override fun onSuccess(result: PostsModel) {
                            progressBar.visibility = View.GONE
                            endShimmerPost()
                            itemloaded = true

                            if(result.status == true ){
                                postsModel = result
                                if(result.data?.data?.size!!>0) {
                                    result.data.data.forEach {

                                        postAdapter?.addNewItem(it)
                                       postGridAdapter?.addNewItem(it)

                                    }
                                }

                            }

                        }

                        override fun onFailed(status: Int) {
                            progressBar.visibility = View.GONE
                            itemloaded = true

                            super.onFailed(status)
                        }


                    })
            )
        }
        else {
            Toast.makeText(parent, R.string.no_connection, Toast.LENGTH_SHORT).show()

        }

    }

    fun beginShimmerPost(){
        if(selectedViewType==0) {
            rvPosts.visibility = View.GONE
            userShimmer1.visibility = View.VISIBLE
            userShimmer2.visibility = View.VISIBLE
            productShimmer1.visibility = View.VISIBLE
            productShimmer2.visibility = View.VISIBLE
            userShimmer1.startShimmerAnimation()
            userShimmer2.startShimmerAnimation()
            productShimmer1.startShimmerAnimation()
            productShimmer2.startShimmerAnimation()
        }
        else {
            rvPosts.visibility = View.GONE
            postGridShimmer.visibility = View.VISIBLE
            postGridShimmer.startShimmerAnimation()
        }

    }
    fun endShimmerPost(){
        if(selectedViewType==0) {

            userShimmer1.stopShimmerAnimation()
            userShimmer2.stopShimmerAnimation()
            productShimmer1.stopShimmerAnimation()
            productShimmer2.stopShimmerAnimation()
            userShimmer1.visibility = View.GONE
            userShimmer2.visibility = View.GONE
            productShimmer1.visibility = View.GONE
            productShimmer2.visibility = View.GONE
            rvPosts.visibility = View.VISIBLE
        }
        else {
            postGridShimmer.stopShimmerAnimation()
            postGridShimmer.visibility = View.GONE
            rvPosts.visibility = View.VISIBLE

        }

    }
    fun setListViewType(view:View){
        view.findViewById<TextView>(R.id.tv_list).setTextColor(resources.getColor(R.color.colorPrimary))
        view.findViewById<TextView>(R.id.tv_grid).setTextColor(Color.parseColor("#ACACAC"))
        view.findViewById<ImageView>(R.id.iv_list).setColorFilter(resources.getColor(R.color.colorPrimary))
        view.findViewById<ImageView>(R.id.iv_grid).setColorFilter(Color.parseColor("#ACACAC"))

    }
    fun setGridViewType(view:View){
        view.findViewById<TextView>(R.id.tv_grid).setTextColor(resources.getColor(R.color.colorPrimary))
        view.findViewById<TextView>(R.id.tv_list).setTextColor(Color.parseColor("#ACACAC"))
        view.findViewById<ImageView>(R.id.iv_list).setColorFilter(Color.parseColor("#ACACAC"))
        view.findViewById<ImageView>(R.id.iv_grid).setColorFilter((resources.getColor(R.color.colorPrimary)))

    }
    @SuppressLint("SetTextI18n")
    fun setWelcomeUser(){
        if(TemplateActivity.loginResponse?.data?.accessToken != null) {
            val fullName = TemplateActivity.loginResponse?.data?.user?.name
            var firstName: String? = null


            val idx = fullName?.lastIndexOf(' ')
            if (idx == -1) {
                firstName = fullName
            } else {

                firstName = idx?.let { fullName.substring(0, it) }
            }
            if (BasicTools.isDeviceLanEn()) tvUsername.setText("Hi $firstName")
            else
                tvUsername.setText("مرحبا $firstName")
        }
        else {
            tvUsername.visibility = View.INVISIBLE

        }

    }
    fun setUserLocation(){
        if (TemplateActivity.loginResponse?.data?.accessToken != null) {
            if (BasicTools.isDeviceLanEn())
                if (!TemplateActivity.loginResponse?.data?.user?.country?.name.isNullOrEmpty())
                    tvUserLocation.text =
                        TemplateActivity.loginResponse?.data?.user?.country?.name?.toString() + "," + " " +
                                TemplateActivity.loginResponse?.data?.user?.city?.name
                else tvUserLocation.text = resources.getString(R.string.not_avaliable)
            else {
                if (!TemplateActivity.loginResponse?.data?.user?.country?.nameAr.isNullOrEmpty())

                    tvUserLocation.text =
                        TemplateActivity.loginResponse?.data?.user?.country?.nameAr?.toString() + "," + " " +
                                TemplateActivity.loginResponse?.data?.user?.city?.nameAr
                else tvUserLocation.text = resources.getString(R.string.not_avaliable)
            }
            if (TemplateActivity.loginResponse?.data?.user?.image != null) {
                Glide.with(parent!!.applicationContext)
                    .load(
                        BasicTools.getUrlHttpImg(
                            parent!!.applicationContext,
                            TemplateActivity.loginResponse?.data?.user?.image!!
                        )
                    )
                    .into(ivProfile)
            }

        }
        else {
            if(BasicTools.isDeviceLanEn()) {
                if (!TemplateActivity.selectedCountryNameVisitor.toString().isNullOrEmpty())

                    tvUserLocation.text =
                        TemplateActivity.selectedCountryNameVisitor+", "+TemplateActivity.selectedCityNameVisitor
                else tvUserLocation.text = resources.getString(R.string.not_avaliable)


            }
            else {
                if (!TemplateActivity.selectedCountryNameArVisitor.toString().isNullOrEmpty())

                    tvUserLocation.text =
                        TemplateActivity.selectedCountryNameArVisitor+", "+TemplateActivity.selectedCityNameArVisitor.toString()
                else tvUserLocation.text = resources.getString(R.string.not_avaliable)

            }

        }

    }
    fun getNotifications(page: String){
        val disposable= CompositeDisposable()
        val shopApi =  ApiClient.getClientJwt(
            TemplateActivity.loginResponse?.data?.accessToken!!,
            BasicTools.getProtocol(parent!!).toString(), "en"
        )?.create(
            AppApi::class.java
        )
        val observable= shopApi!!.getNotifications(page)
        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<NotificationModel>(parent!!) {
                    override fun onSuccess(result: NotificationModel) {

                        if (result.status!!) {
                            if (result.data?.countUnseen!! > 0)

                                ivNotification.setImageResource(R.drawable.ic_notifiy_with_circle)
                            else {
                                ivNotification.setImageResource(R.drawable.empty_notification)


                            }

                        }
                    }


                    override fun onFailed(status: Int) {


                    }
                })

        )


    }
    fun setNotification(){
        if (TemplateActivity.loginResponse?.data?.accessToken != null) {
            getNotifications("1")
            view?.findViewById<RelativeLayout>(R.id.rl_setting)?.visibility = View.GONE



        }

    }

}