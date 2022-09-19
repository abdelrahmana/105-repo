package com.urcloset.smartangle.activity.searchActivity


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.mohammedalaa.seekbar.DoubleValueSeekBarView
import com.mohammedalaa.seekbar.OnDoubleValueSeekBarChangeListener
import com.urcloset.smartangle.R
import com.urcloset.smartangle.adapter.project105.*
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.dialog.ChooseColorDialog
import com.urcloset.smartangle.model.project_105.*
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.Constants.FILTER_TYPE_ITEM
import com.urcloset.smartangle.tools.Constants.FILTER_TYPE_PROVIDER
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : TemplateActivity(), ISearchActivity {
    lateinit var rvColors : RecyclerView
    lateinit var rvSize : RecyclerView
    lateinit var rvCondition : RecyclerView
    lateinit var rvSearch : RecyclerView


    lateinit var adapterSearch: ResultProductSearchAdapter
    lateinit var layoutManagerSearch:GridLayoutManager

    lateinit var tvMinSeekbar:TextView
    var succesSize=false
    var succesColor=false
    var succesCondition=false
    lateinit var tvMaxSeekbar:TextView

    lateinit var btnSearch:CardView
    lateinit var shimmerSearch:ShimmerFrameLayout

    lateinit var dSeekbar: DoubleValueSeekBarView
    lateinit var shimmerColor:ShimmerFrameLayout
    lateinit var shimmerSize:ShimmerFrameLayout
    lateinit var shimmerCondition:ShimmerFrameLayout


    lateinit var adapterSize:SizeSearchAdapter
    lateinit var adapterCondition:ConditionSearchAdapter
    lateinit var adapterColor:ColorSearchAdapter

    lateinit var ivBack:ImageView

    var disposableCondtion: CompositeDisposable = CompositeDisposable()

    var disposableColor: CompositeDisposable = CompositeDisposable()

    var disposableSize: CompositeDisposable = CompositeDisposable()

    var disposableSearch: CompositeDisposable = CompositeDisposable()


    var requestBegin=false
    var page=0
    var lastPage=-1
    lateinit var prgs:ProgressBar

    lateinit var rvSearchHistory: RecyclerView
    lateinit var addColor :ImageView
    val colors  = ArrayList<String>()
    val colorAdapter= ColorsSearchAdapter()

    lateinit var editSearch:EditText

    lateinit var chooseColorDialog:ChooseColorDialog

    lateinit var ivFilter:ImageView
     var filterType=FILTER_TYPE_ITEM
    lateinit var  popupMenu: PopupMenu
  //  val productAdapter  = ProductAdapter()

    lateinit var rootEmpty:LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun set_layout() {
        setContentView(R.layout.activity_search)
    }




    override fun init_activity(savedInstanceState: Bundle?) {
        rvColors.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        colorAdapter.submitList(colors)
        rvColors.adapter = colorAdapter
        colors.add("#FF5E5E")
        colors.add("#FFC45E")
        colors.add("#5EFF99")
        colors.add("#5EC8FF")
        colors.add("#885EFF")
        colors.add("#FF5EAF")
      //  rvs.layoutManager = GridLayoutManager(this@SearchActivity, 2)





        //  val products = ArrayList<Product>()
      //  products.add(Product(1, 541, "","","Selled", R.drawable.zebra_tshirt))
      //  products.add(Product(2, 541, "","","Blocked" ,R.drawable.tshirt))
       // productAdapter.submitList(products)
     //   rvSearchHistory.adapter = productAdapter


    }

    override fun init_views() {

        rootEmpty=findViewById(R.id.root_empty_search)
        prgs=findViewById(R.id.prgs_search)

        btnSearch=findViewById(R.id.btn_search)
        editSearch=findViewById(R.id.edit_search_product)
        tvMinSeekbar=findViewById(R.id.tv_min_seekbar)
        tvMaxSeekbar=findViewById(R.id.tv_max_seekbar)

       dSeekbar=findViewById(R.id.double_range_seekbar)
        rvColors = findViewById<RecyclerView>(R.id.rv_colors)
        rvCondition=findViewById(R.id.rv_conditions)
        rvSize=findViewById(R.id.rv_sizes)
        rvSearch=findViewById(R.id.rv_search_history)

        shimmerColor=findViewById(R.id.shimmer_colors)
        shimmerCondition=findViewById(R.id.shimmer_condition)
        shimmerSize=findViewById(R.id.shimmer_sizes)
        shimmerSearch=findViewById(R.id.shimmer_search)

        ivBack=findViewById(R.id.iv_back)

        addColor = iv_add_color
       // rvSearchHistory = findViewById<RecyclerView>(R.id.rv_search_history)

        ivFilter=findViewById(R.id.iv_filter)
         popupMenu = PopupMenu(this,ivFilter)
        if(filterType.equals(FILTER_TYPE_ITEM))
            popupMenu.menuInflater.inflate(R.menu.menu_search_item,popupMenu.menu)
        else if(filterType.equals(FILTER_TYPE_PROVIDER))
            popupMenu.menuInflater.inflate(R.menu.menu_search_item_provider,popupMenu.menu)


    }




    override fun init_events() {


        btnSearch.setOnClickListener {

            if(succesSize&&succesColor&&succesCondition){
                 page=0
                 lastPage=-1
                getSearch()

            }
        }

        dSeekbar.setOnRangeSeekBarViewChangeListener(object : OnDoubleValueSeekBarChangeListener
           {
            override fun onStartTrackingTouch(
                seekBar: DoubleValueSeekBarView?,
                min: Int,
                max: Int
            ) {

            }

            override fun onStopTrackingTouch(
                seekBar: DoubleValueSeekBarView?,
                min: Int,
                max: Int
            ) {

            }

            override fun onValueChanged(
                seekBar: DoubleValueSeekBarView?,
                min: Int,
                max: Int,
                fromUser: Boolean
            ) {

                tvMinSeekbar.setText("${min}$")
                tvMaxSeekbar.setText("${max}$")

            }

           })


        ivBack.setOnClickListener {

            TemplateActivity.colorSearchList!!.clear()
            TemplateActivity.conditionSearchList!!.clear()
            TemplateActivity.sizeSearchList!!.clear()

            BasicTools.exitActivity(this@SearchActivity)
        }


        adapterColor= ColorSearchAdapter(this@SearchActivity,ArrayList())
        adapterCondition= ConditionSearchAdapter(this@SearchActivity,ArrayList())
        adapterSize= SizeSearchAdapter(this@SearchActivity,ArrayList())
        adapterSearch= ResultProductSearchAdapter(this@SearchActivity,ArrayList(),this)

        layoutManagerSearch= GridLayoutManager(this,2,LinearLayoutManager.HORIZONTAL,false)


        rvCondition.adapter=adapterCondition
        rvColors.adapter=adapterColor
        rvSize.adapter=adapterSize
        rvSearch.layoutManager=layoutManagerSearch
        rvSearch.adapter=adapterSearch



      //  if(TemplateActivity.conditionSearchList!!.isEmpty()){
            getCondition()
      /*  }else {
            adapterCondition.change_data(TemplateActivity.conditionSearchList!!)
        }
*/
      //  if(TemplateActivity.colorSearchList!!.isEmpty()){
            getColor()
      /*  }else {
            setColorData(TemplateActivity.colorSearchList!!)
           // adapterColor.change_data(TemplateActivity.colorSearchList!!)
        }*/

     //   if(TemplateActivity.sizeSearchList!!.isEmpty()){
            getSize()
      /*  }else {
            adapterSize.change_data(TemplateActivity.sizeSearchList!!)
        }*/





//        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
//            when(item.itemId) {
//                R.id.product ->
//                    showToastMessage("3")
//                R.id.provider ->
//                    showToastMessage("2")
//                R.id.product2 ->
//                    showToastMessage("1")
//                R.id.provider2 ->
//                    showToastMessage("1")
//            }
//            true
//        })
      //  popupMenu.show()

        ivFilter.setOnClickListener {
           // popupMenu.show()

            val popupMenu = popupMenu {
                style = R.style.Widget_MPM_Menu_Dark_CustomBackground


                section {

                    item {
                        labelRes = R.string.product
                        labelColor = ContextCompat.getColor(this@SearchActivity, R.color.black)
                        iconDrawable = ContextCompat.getDrawable(this@SearchActivity, R.drawable.selected_icon)
                        callback = { //optional

                        }
                        iconColor = resources.getColor(R.color.colorPrimary)
                    }
                    item {
                        labelRes = R.string.porvider
                        labelColor = ContextCompat.getColor(this@SearchActivity, R.color.black)
                        iconDrawable = ContextCompat.getDrawable(this@SearchActivity, R.drawable.selected_icon)

                        callback = { //optional
                            BasicTools.openActivity(this@SearchActivity,SearchActivityProvider::class.java,true)
                        }
                        iconColor = resources.getColor(R.color.white)

                    }

                }
            }


            popupMenu.show(applicationContext, ivFilter)
        }






        addColor.setOnClickListener {

            if(TemplateActivity.colorSearchList!!.isNotEmpty()){
            chooseColorDialog=ChooseColorDialog(this@SearchActivity,
            TemplateActivity.colorSearchList!!,this)
            chooseColorDialog.window!!.attributes.windowAnimations=R.style.dialogAnim
            chooseColorDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            chooseColorDialog.show()

            }

          /*  ColorPickerPopup.Builder(this)
                .initialColor(resources.getColor(R.color.colorPrimary)) // Set initial color
                .enableBrightness(false) // Enable brightness slider or not
                .enableAlpha(true) // Enable alpha slider or not
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(it, object : ColorPickerObserver() {
                    override fun onColorPicked(color: Int) {
                        val hexColor = "#" + Integer.toHexString(color).substring(2)

                        colors.add(hexColor)
                        Log.d("color selected", "onColorPicked: " + hexColor)
                        print("color =" + hexColor)
                        colorAdapter.submitList(colors)
                        colorAdapter.notifyDataSetChanged()


                    }

                    fun onColor(color: Int, fromUser: Boolean) {}
                })*/
        }



        rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val firstVisiblePosition: Int = layoutManagerSearch.findFirstVisibleItemPosition()

                val findLastVisibleItemPosition: Int = layoutManagerSearch.findLastVisibleItemPosition()


                Log.i("TEST_TEST","$firstVisiblePosition")
                Log.i("TEST_TEST","$findLastVisibleItemPosition")




                var direction=1
                if(BasicTools.isDeviceLanEn())
                 direction=1
                else direction=-1

                if (!recyclerView.canScrollHorizontally(direction) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if(!requestBegin &&lastPage!=page)
                        getSearch()
                }
            }
        })
    }

    override fun set_fragment_place() {

    }

    fun getCondition(){
        if (BasicTools.isConnected(this@SearchActivity)) {

            BasicTools.showShimmer(rvCondition,shimmerCondition,true)

            val shopApi = ApiClient.getClient(BasicTools.getProtocol(this@SearchActivity).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.getCondition()
            disposableCondtion.clear()
            disposableCondtion.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ConditionBookMarkModel>(this@SearchActivity) {
                        override fun onSuccess(result: ConditionBookMarkModel) {
                            BasicTools.showShimmer(rvCondition,shimmerCondition,false)

                            if(result.status!!) {

                                succesCondition=true
                                TemplateActivity.conditionSearchList!!.clear()
                                TemplateActivity.conditionSearchList!!.addAll(result.data?.data!!)
                                adapterCondition.change_data(result.data?.data!!)

                            }


                        }
                        override fun onFailed(status: Int) {
                            BasicTools.showShimmer(rvCondition,shimmerCondition,false)
                            showToastMessage(R.string.faild)

                        }
                    }))
        }
        else {

            showToastMessage(R.string.no_connection)}
    }


    fun getColor(){
        if (BasicTools.isConnected(this@SearchActivity)) {

            BasicTools.showShimmer(rvColors,shimmerColor,true)

            val shopApi = ApiClient.getClient(BasicTools.getProtocol(this@SearchActivity).toString())
                ?.create(AppApi::class.java)


            val observable= shopApi!!.getColorsSearch("no")
            disposableColor.clear()
            disposableColor.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ColorModelHassan>
                        (this@SearchActivity) {
                        override fun onSuccess(result: ColorModelHassan) {
                            BasicTools.showShimmer(rvColors,shimmerColor,false)

                            if(result.status!!) {
                                succesColor=true
                                TemplateActivity.colorSearchList!!.clear()
                                TemplateActivity.colorSearchList!!.addAll(result.data!!)
                               //adapterColor.change_data(result.data)

                            }


                        }
                        override fun onFailed(status: Int) {
                            BasicTools.showShimmer(rvColors,shimmerColor,false)
                            showToastMessage(R.string.faild)

                        }
                    }))
        }
        else {

            showToastMessage(R.string.no_connection)}
    }

    fun getSize(){
        if (BasicTools.isConnected(this@SearchActivity)) {

            BasicTools.showShimmer(rvSize,shimmerSize,true)

            val shopApi = ApiClient.getClient(BasicTools.getProtocol(this@SearchActivity).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.getSizesSearch("no")
            disposableSize.clear()
            disposableSize.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<SizeModelHassan>(this@SearchActivity) {
                        override fun onSuccess(result: SizeModelHassan) {
                            BasicTools.showShimmer(rvSize,shimmerSize,false)

                            if(result.status!!) {
                                succesSize=true
                                TemplateActivity.sizeSearchList!!.clear()
                                TemplateActivity.sizeSearchList!!.addAll(result.data!!)
                                adapterSize.change_data(result.data!!)

                            }


                        }
                        override fun onFailed(status: Int) {
                            BasicTools.showShimmer(rvSize,shimmerSize,false)
                            showToastMessage(R.string.faild)

                        }
                    }))
        }
        else {

            showToastMessage(R.string.no_connection)}
    }




    fun getSearch(){
        if (BasicTools.isConnected(this@SearchActivity)) {




            val shopApi = ApiClient.getClient(BasicTools.getProtocol(this@SearchActivity).toString())
                ?.create(AppApi::class.java)


            var map=HashMap<String,String>()

            map.put("page",(++page).toString())



            if(editSearch.text.isNotEmpty())
            map.put("search_text",editSearch.text.trim().toString())

            var conditionID= adapterCondition.getSelectedItem()
            if(conditionID?.id != null)
            map.put("condition_id",conditionID.id.toString())


            map.put("min_price",dSeekbar.currentMinValue.toString())
            map.put("max_price",dSeekbar.currentMaxValue.toString())


            var color=adapterColor.getList()
            var colorArray=ArrayList<String>()
            for(i in color)
                colorArray.add(i.id.toString())

            var colorBody=colorArray.toString().replace(" ","")
            if(colorBody.isNotEmpty())
            map.put("colors",colorBody)
        /*    var arrayColor= arrayOf(String)
            for((index, i) in color.withIndex()){
                var id:String=""
                arrayColor[index]="$id"
            }
*/
            var size=adapterSize.getList()
            var sizeArray=ArrayList<String>()
            for(i in size){
                if(i.isSelected!=null && i.isSelected!!)
                    sizeArray.add(i.id.toString())}

            var sizeBody=sizeArray.toString().replace(" ","")
            //.toString().replace(" ","")
            if(sizeBody.isNotEmpty())
            map.put("sizes",sizeBody)

            Log.i("TEST_TEST1234","$sizeBody")
            Log.i("TEST_TEST1234","${sizeArray.toString()}")
            //map.put("","")

            if(page==1){
                BasicTools.showShimmer(btnSearch,shimmerSearch,true)
                prgs.visibility=View.GONE
            }
            else{
                prgs.visibility=View.VISIBLE
            }
            val observable= shopApi!!.searchProduct(map)
            disposableSearch.clear()
            disposableSearch.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<SearchProductV2Model>(this@SearchActivity) {
                        override fun onSuccess(result: SearchProductV2Model) {
                            BasicTools.showShimmer(btnSearch,shimmerSearch,false)

                            page=result.data!!.currentPage!!
                            lastPage=result.data!!.lastPage!!
                            prgs.visibility=View.GONE
                            requestBegin=false
                            if(result.status!!) {



                                if(page==1)
                                adapterSearch.change_data(result.data?.data!!)
                                else adapterSearch.addRefersh(result.data?.data!!)


                            }


                        }
                        override fun onFailed(status: Int) {
                            requestBegin=false
                            prgs.visibility=View.GONE
                            BasicTools.showShimmer(btnSearch,shimmerSearch,false)
                            showToastMessage(R.string.faild)

                        }
                    }))
        }
        else {

            showToastMessage(R.string.no_connection)}
    }


    override fun setColorData(item: ArrayList<ColorModelHassan.Color>) {
        var list=ArrayList<ColorModelHassan.Color>()
        for(i in item){
            if(i.isSelected!=null && i.isSelected!!)
                list.add(i)
        }


        Log.i("TEST_TEST11","${list}")
        adapterColor.change_data(list)
        adapterColor.notifyDataSetChanged()


    }

    override fun checkIfAdapterEmpty(item: Boolean) {

        if(item){

            rootEmpty.visibility=View.VISIBLE

        }else{
            rootEmpty.visibility=View.GONE
        }
    }


}