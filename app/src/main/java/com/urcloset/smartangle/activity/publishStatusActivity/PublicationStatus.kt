package com.urcloset.smartangle.activity.publishStatusActivity

import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.CustomViewPager
import com.urcloset.smartangle.R
import com.urcloset.smartangle.adapter.ProductPublishStateAdapter
import com.urcloset.smartangle.adapter.SellerProductsAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.databinding.ActivityPublicationStatusBinding
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.model.ProductStateModel
import com.urcloset.smartangle.model.PublishStateModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList
@AndroidEntryPoint
class PublicationStatus : TemplateActivity() {
    var lang:String ="en"
    var disposable = CompositeDisposable()
    lateinit var viewPager: ViewPager2
    var selectState:Int=1
    lateinit var tvPublished:TextView
    lateinit var tvRejected:TextView
    lateinit var tvInReview:TextView
    lateinit var rvShimmer:ShimmerFrameLayout
    lateinit var ivBack:ImageView
    lateinit var pagerAdapter: ProductPublishStateAdapter
    var binding : ActivityPublicationStatusBinding?=null
    override fun set_layout() {
        binding = ActivityPublicationStatusBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    override fun init_activity(savedInstanceState: Bundle?) {
        if(!BasicTools.isDeviceLanEn())
            lang ="ar"
        if(TemplateActivity.loginResponse?.data!!.accessToken!=null){
          //  getProductsByPublishState()
            setViewPagerCategories(ArrayList<InterfacePublication>().also {
                it.add(PublicationOrdersImplementer())
                it.add(SoldOrdersImplementer())
                it.add(RejectedProductsImplementer())
            })
        }
    }
    private fun setViewPagerCategories(arrayList: List<InterfacePublication>) {

        val viewPagerAdaptor =
            ViewPagerProducts(
                this, arrayList as ArrayList<InterfacePublication>

            )
        viewPager.adapter = viewPagerAdaptor



    }
    override fun init_views() {
        viewPager =binding!!.viewpager
        tvPublished =binding!!.tvPublished
        tvInReview =binding!!.tvReview
        tvRejected =binding!!.tvRejected
        rvShimmer =binding!!.rvShimmer
        ivBack = findViewById(R.id.iv_back)
    }

    override fun init_events() {
        ivBack.setOnClickListener {
            finish()
        }
        tvPublished.setOnClickListener {
            tvPublished.background = getDrawable(R.drawable.publish_state_bg)
            tvInReview.background = getDrawable(R.drawable.unselected_publish_state_bg)
            tvRejected.background = getDrawable(R.drawable.unselected_publish_state_bg)
            tvPublished.setTextColor(resources.getColor(R.color.white))
            tvInReview.setTextColor(Color.parseColor("#ACACAC"))
            tvRejected.setTextColor(Color.parseColor("#ACACAC"))
            viewPager.setCurrentItem(0)
        }
        tvInReview.setOnClickListener {
            tvPublished.background = getDrawable(R.drawable.unselected_publish_state_bg)
            tvInReview.background = getDrawable(R.drawable.publish_state_bg)
            tvRejected.background = getDrawable(R.drawable.unselected_publish_state_bg)
            tvInReview.setTextColor(resources.getColor(R.color.white))
            tvPublished.setTextColor(Color.parseColor("#ACACAC"))
            tvRejected.setTextColor(Color.parseColor("#ACACAC"))
            viewPager.setCurrentItem(1)

        }
        tvRejected.setOnClickListener {
            tvPublished.background = getDrawable(R.drawable.unselected_publish_state_bg)
            tvInReview.background = getDrawable(R.drawable.unselected_publish_state_bg)
            tvRejected.background = getDrawable(R.drawable.publish_state_bg)
            tvRejected.setTextColor(resources.getColor(R.color.white))
            tvPublished.setTextColor(Color.parseColor("#ACACAC"))
            tvInReview.setTextColor(Color.parseColor("#ACACAC"))
            viewPager.setCurrentItem(2)

        }
       viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 0){
                    tvPublished.background = getDrawable(R.drawable.selected_text_button_bg_small)
                    tvInReview.background = getDrawable(R.drawable.unselected_text_button_bg_small)
                    tvRejected.background = getDrawable(R.drawable.unselected_text_button_bg_small)
                    tvPublished.setTextColor(resources.getColor(R.color.white))
                    tvInReview.setTextColor(Color.parseColor("#ACACAC"))
                    tvRejected.setTextColor(Color.parseColor("#ACACAC"))
                    viewPager.setCurrentItem(0)

                }
                if (position == 1){
                    tvPublished.background = getDrawable(R.drawable.unselected_text_button_bg_small)
                    tvInReview.background = getDrawable(R.drawable.selected_text_button_bg_small)
                    tvRejected.background = getDrawable(R.drawable.unselected_text_button_bg_small)
                    tvInReview.setTextColor(resources.getColor(R.color.white))
                    tvPublished.setTextColor(Color.parseColor("#ACACAC"))
                    tvRejected.setTextColor(Color.parseColor("#ACACAC"))
                    viewPager.setCurrentItem(1)
                }
                if (position == 2){
                    tvPublished.background = getDrawable(R.drawable.unselected_text_button_bg_small)
                    tvInReview.background = getDrawable(R.drawable.unselected_text_button_bg_small)
                    tvRejected.background = getDrawable(R.drawable.selected_text_button_bg_small)
                    tvRejected.setTextColor(resources.getColor(R.color.white))
                    tvPublished.setTextColor(Color.parseColor("#ACACAC"))
                    tvInReview.setTextColor(Color.parseColor("#ACACAC"))
                 //   pagerAdapter.notifyDataSetChanged()
                    viewPager.setCurrentItem(2)


                }
            }

            override fun onPageScrollStateChanged(state: Int) {
           }

        })







    }

    override fun set_fragment_place() {
    }
/*    fun getProductsByPublishState(whenSwipping : Boolean = false,selectedSwipePosition : Int = 0){
        showShimmer(true)

        val shopApi =
            ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(this).toString(), lang
            )
                ?.create(
                    AppApi::class.java
                )
        val observable = if (selectedSwipePosition==0)shopApi!!.getPublicationStatus()
        else if (selectedSwipePosition == 1) shopApi!!.getPublicationStatus()
        else shopApi!!.getPublicationStatus()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<PublishStateModel>(this) {
                    override fun onSuccess(result: PublishStateModel) {
                        showShimmer(false)


                        if (result.status!!) {
                                pagerAdapter = ProductPublishStateAdapter(
                                    context = this@PublicationStatus,
                                    rejected = result.data?.rejected as ArrayList<ProductModel.Product>,
                                    published = result.data.published as ArrayList<ProductModel.Product>,
                                    inReview = result.data.inReview as ArrayList<ProductModel.Product>


                                )
                                viewPager.adapter = pagerAdapter



                        }


                    }

                    override fun onFailed(status: Int) {
                        showShimmer(false)


                    }


                })
        )




    }*/
    fun showShimmer(state: Boolean){

        if(state){
            viewPager.visibility= View.GONE
            rvShimmer.visible()
        }else{
            viewPager.visibility= View.VISIBLE
            rvShimmer.hide()
        }
    }
}
