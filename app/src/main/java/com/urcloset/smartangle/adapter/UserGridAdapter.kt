package com.urcloset.smartangle.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.sellerActivity.SellerActivity
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.NearbyUsersModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.HashMap

class UserGridAdapter(var context: Context?, val nearbyUsersModel: NearbyUsersModel) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    var index =0
    var users = nearbyUsersModel.data!!.data
    val lastPage = nearbyUsersModel.data!!.lastPage
    var page = nearbyUsersModel.data!!.currentPage
    var disposable= CompositeDisposable()
    lateinit var progress:ProgressBar

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater =
            context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = layoutInflater!!.inflate(R.layout.users_gride_items, container, false)
        progress =view.findViewById(R.id.progress)
        val image1=view.findViewById(R.id.iv_user_img1)as ImageView
        val image2=view.findViewById(R.id.iv_user_img2)as ImageView
        val image3=view.findViewById(R.id.iv_user_img3)as ImageView
        val image4=view.findViewById(R.id.iv_user_img4)as ImageView
        val image5=view.findViewById(R.id.iv_user_img5)as ImageView
        val image6=view.findViewById(R.id.iv_user_img6)as ImageView
        val title1=view.findViewById(R.id.tv_title1)as TextView
        val title2=view.findViewById(R.id.tv_title2)as TextView
        val title3=view.findViewById(R.id.tv_title3)as TextView
        val title4=view.findViewById(R.id.tv_title4)as TextView
        val title5=view.findViewById(R.id.tv_title5)as TextView
        val title6=view.findViewById(R.id.tv_title6)as TextView
        val visit1=view.findViewById(R.id.visit1)as CardView
        val visit2=view.findViewById(R.id.visit2)as CardView
        val visit3=view.findViewById(R.id.visit3)as CardView
        val visit4=view.findViewById(R.id.visit4)as CardView
        val visit5=view.findViewById(R.id.visit5)as CardView
        val visit6=view.findViewById(R.id.visit6)as CardView
        val one = 0+(position*6)
        val two = 1+(position*6)
        val three = 2+(position*6)
        val four = 3+(position*6)
        val five = 4+(position*6)
        val six = 5+(position*6)


        if(one<users.size){
            val sellerProfile = users[one]
            if(!users[one].image.isNullOrEmpty()) {

                Glide.with(context!!.applicationContext).load(
                    BasicTools.getUrlHttpImg(
                        context!!.applicationContext,
                        users[one].image!!
                    )
                ).circleCrop().into(image1)
            }
            title1.setText(users[one].name)
            visit1.setOnClickListener {
                val intent = Intent(context, SellerActivity::class.java)
                intent.putExtra("identifier", sellerProfile.id.toString())
                context!!.startActivity(intent)
            }

            if(two<users.size){
                val sellerProfileTwo = users[two]
                if(!users[two].image.isNullOrEmpty()) {

                    Glide.with(context!!.applicationContext)
                        .load(BasicTools.getUrlHttpImg(context!!, users[two].image!!))
                        .circleCrop()
                        .into(image2)
                }
                title2.setText(users[two].name)
                visit2.setOnClickListener {
                    val intent = Intent(context, SellerActivity::class.java)
                    intent.putExtra("identifier", sellerProfile.id.toString())
                    context!!.startActivity(intent)
                }
                if(three<users.size){
                    val sellerProfileThree = users[three]
                    if(!users[three].image.isNullOrEmpty()) {

                        Glide.with(context!!.applicationContext)
                            .load(BasicTools.getUrlHttpImg(context!!, users[three].image!!)
                            ).circleCrop()
                            .into(image3)
                    }
                    title3.setText(users[three].name)
                    visit3.setOnClickListener {
                        val intent = Intent(context, SellerActivity::class.java)
                        intent.putExtra("identifier", sellerProfile.id.toString())
                        context!!.startActivity(intent)
                    }
                    if(four<users.size){
                        val sellerProfileFour = users[four]
                        if(!users[four].image.isNullOrEmpty()) {

                            Glide.with(context!!.applicationContext)
                                .load(BasicTools.getUrlHttpImg(context!!, users[four].image!!))
                                .circleCrop()
                                .into(image4)
                        }
                        title4.setText(users[four].name)
                        visit4.setOnClickListener {
                            val intent = Intent(context, SellerActivity::class.java)
                            intent.putExtra("identifier", sellerProfile.id.toString())
                            context!!.startActivity(intent)
                        }
                        if(five<users.size){
                            val sellerProfileFive = users[five]
                            if(!users[five].image.isNullOrEmpty()) {

                                Glide.with(context!!.applicationContext)
                                    .load(BasicTools.getUrlHttpImg(context!!, users[five].image!!))
                                    .circleCrop()
                                    .into(image5)
                            }
                            title5.setText(users[five].name)
                            visit5.setOnClickListener {
                                val intent = Intent(context, SellerActivity::class.java)
                                intent.putExtra("identifier", sellerProfile.id.toString())
                                context!!.startActivity(intent)
                            }
                            if(six<users.size){
                                val sellerProfileSix = users[six]
                                if(!users[six].image.isNullOrEmpty()) {

                                    Glide.with(context!!.applicationContext)
                                        .load(BasicTools.getUrlHttpImg(context!!, users[six].image!!))
                                        .circleCrop()
                                        .into(image6)
                                }
                                title6.setText(users[six].name)
                                visit6.setOnClickListener {
                                    val intent = Intent(context, SellerActivity::class.java)
                                    intent.putExtra("identifier",sellerProfile.id.toString())
                                    context!!.startActivity(intent)
                                }

                            }
                            else {
                                if(page!! <lastPage!!){
                                    getMoreUsers()
                                }
                                else
                                view.findViewById<RelativeLayout>(R.id.six).visibility =View.GONE

                            }

                        }
                        else {
                            if(page!! <lastPage!!){
                                getMoreUsers()
                            }
                            else {
                                view.findViewById<RelativeLayout>(R.id.five).visibility = View.GONE
                                view.findViewById<RelativeLayout>(R.id.six).visibility = View.GONE                            }

                        }


                    }
                    else{
                        if(page!! <lastPage!!){
                            getMoreUsers()
                        }
                        else {
                            view.findViewById<RelativeLayout>(R.id.four).visibility = View.GONE
                            view.findViewById<RelativeLayout>(R.id.five).visibility = View.GONE
                            view.findViewById<RelativeLayout>(R.id.six).visibility = View.GONE                        }

                    }


                }
                else{
                    if(page!! <lastPage!!){
                        getMoreUsers()
                    }
                    else {
                        view.findViewById<RelativeLayout>(R.id.three).visibility = View.GONE
                        view.findViewById<RelativeLayout>(R.id.four).visibility = View.GONE
                        view.findViewById<RelativeLayout>(R.id.five).visibility = View.GONE
                        view.findViewById<RelativeLayout>(R.id.six).visibility = View.GONE
                    }

                }


            }
            else{
                if(page!! <lastPage!!){
                    progress.visibility = View.VISIBLE

                    getMoreUsers()
                }
                    else {
                    view.findViewById<RelativeLayout>(R.id.two).visibility = View.GONE
                    view.findViewById<RelativeLayout>(R.id.three).visibility = View.GONE
                    view.findViewById<RelativeLayout>(R.id.four).visibility = View.GONE
                    view.findViewById<RelativeLayout>(R.id.five).visibility = View.GONE
                    view.findViewById<RelativeLayout>(R.id.six).visibility = View.GONE




                }

            }

        }
        else {
            if(page!! <lastPage!!){
                getMoreUsers()
            }
            view.findViewById<RelativeLayout>(R.id.one).visibility = View.GONE

        }


        index++

        container.addView(view)

        return view
    }
    fun getMoreUsers(){

        if(BasicTools.isConnected(context as TemplateActivity)) {


            page=page!!+1
            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol((context as TemplateActivity).applicationContext).toString(), "en"
            )?.create(
                AppApi::class.java
            )
            val map = HashMap<String, String>()
            map.put("page", page.toString())
            map.put("per_page","36")


            if(TemplateActivity.loginResponse?.data?.accessToken!=null){
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

            }
            val observable = shopApi!!.getUsers(map)
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<NearbyUsersModel>(context!!) {
                        override fun onSuccess(result: NearbyUsersModel) {
                            if (result.status!!) {
                                users.addAll(result.data!!.data)
                                notifyDataSetChanged()

                                }

                            }

                        override fun onComplete() {
                            progress.visibility = View.GONE

                            super.onComplete()
                        }

                    })

            )
        }
        else{
            Toast.makeText(context, R.string.no_connection, Toast.LENGTH_SHORT).show()
        }
    }
   public fun update(){
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as RelativeLayout

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as RelativeLayout
        container.removeView(view)
    }
    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun getCount(): Int {
        return Math.ceil(nearbyUsersModel.data?.total!!/6.0).toInt()
    }
}