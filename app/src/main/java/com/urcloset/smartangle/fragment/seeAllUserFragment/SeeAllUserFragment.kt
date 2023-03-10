package com.urcloset.smartangle.fragment.seeAllUserFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.activity.searchActivity.ISearchProviderActivity
import com.urcloset.smartangle.adapter.project105.ConditionListAdapter
import com.urcloset.smartangle.adapter.project105.ProviderSearchAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.project_105.ProivderSeachModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import com.urcloset.smartangle.tools.TemplateFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_see_all_user.*

class SeeAllUserFragment : TemplateFragment() , ISearchProviderActivity {

    lateinit var rv:RecyclerView

    lateinit var adapter: ProviderSearchAdapter
    lateinit var shimmer: ShimmerFrameLayout
    lateinit var ivBack: ImageView
    var requestBegin=false
    var page=0
    var lastPage=-1
    lateinit var prgs: ProgressBar
    lateinit var rootEmpty: LinearLayout
    var disposableSearch: CompositeDisposable = CompositeDisposable()
    lateinit var layoutManager: GridLayoutManager
    lateinit var swip: SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_see_all_user, container, false)


        return view
    }

    override fun init_views() {

        rv=rv_seeall_user
        shimmer=shimmer_wait_seeall_user
        rootEmpty=root_empty_page
        prgs=prgs_seeall_user
        swip=swip_seeall_user
        ivBack=iv_back

        layoutManager= GridLayoutManager(parent!!,3, LinearLayoutManager.VERTICAL,false)

        adapter= ProviderSearchAdapter(parent!!,ArrayList(),this)
        rv.adapter=adapter
        rv.layoutManager=layoutManager

    }
    override fun onResume() {
        super.onResume()
        if(adapter!=null&& TemplateActivity.SeeAllUser!=null){
            page=1
            lastPage= TemplateActivity.lastPageSeeAllUser
            adapter?.change_data(TemplateActivity.SeeAllUser!!)
        }
  /*      if(HomeActivity.bottomNavigation?.currentItem!=0){
            HomeActivity.doNothing =  true
            HomeActivity.bottomNavigation?.currentItem=0
            HomeActivity.doNothing =  false

        }*/

    }

    override fun init_events() {

        ivBack.setOnClickListener {
            parent!!.onBackPressed()
        }



        if(TemplateActivity.SeeAllUser==null)
            getData()
        else
            adapter!!.change_data(TemplateActivity.SeeAllUser!!)



        swip.setOnRefreshListener {

            page=0
            lastPage=-1

            TemplateActivity.SeeAllUser=null
             getData()
        }



        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if(!requestBegin &&lastPage!=page)
                        getData()
                }
            }
        })

    }

    override fun init_fragment(savedInstanceState: Bundle?) {    }

    override fun on_back_pressed(): Boolean {
        return true
    }

    override fun checkIfAdapterEmpty(item: Boolean) {
        if(item){

            rootEmpty.visibility=View.VISIBLE

        }else{
            rootEmpty.visibility=View.GONE
        }

    }
    fun getData(){
        if (BasicTools.isConnected(parent!!)) {
            val shopApi = ApiClient.getClient(BasicTools.getProtocol(parent!!).toString())
                ?.create(AppApi::class.java)


            var map=HashMap<String,String>()
            map.put("page",(++page).toString())






            if(page==1){
                BasicTools.showShimmer(rv,shimmer,true)
                prgs.visibility=View.GONE
            }
            else{
                prgs.visibility=View.VISIBLE
            }
            val observable= shopApi!!.searchProvider(map)
            disposableSearch.clear()
            disposableSearch.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ProivderSeachModel>(parent!!) {
                        override fun onSuccess(result: ProivderSeachModel) {
                            BasicTools.showShimmer(rv,shimmer,false)
                            page=result.data!!.currentPage!!
                            TemplateActivity.lastPageBookMark=result.data!!.lastPage!!
                        lastPage=result.data!!.lastPage!!

                            swip.isRefreshing=false
                            prgs.visibility= View.GONE
                            requestBegin=false

                            if(result.status!!) {



                                if(TemplateActivity.SeeAllUser==null){
                                    TemplateActivity.SeeAllUser= ArrayList()
                                    TemplateActivity.SeeAllUser?.clear()
                                    TemplateActivity.SeeAllUser?.addAll(result.data!!.data!!)
                                    adapter!!.change_data(result.data!!.data!!)
                                }else{
                                    TemplateActivity.SeeAllUser!!.addAll(result.data!!.data!!)
                                    adapter!!.addRefersh(result.data!!.data!!)
                                }





                            }


                        }
                        override fun onFailed(status: Int) {
                            requestBegin=false
                            prgs.visibility=View.GONE
                            BasicTools.showShimmer(rv,shimmer,false)
                            parent!!.showToastMessage(R.string.faild)

                        }
                    }))
        }
        else {

            parent!!.showToastMessage(R.string.no_connection)}
    }
}