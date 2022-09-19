package com.urcloset.smartangle.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.urcloset.shop.tools.gone
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.loginActivity.LoginAcitivty
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.AllCompanyModel
import com.urcloset.smartangle.model.FeatureProductModel
import com.urcloset.smartangle.model.NewArrivalsModel
import com.urcloset.smartangle.model.SubCategroyModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.ResponseBody
import ru.nikartm.support.BadgePosition


class MainActivity : TemplateActivity() {



    var badgetItem=0

    var tabItem=0
    var selectedItem:SubCategroyModel.DataArray?=null
    var selectedProductList:SubCategroyModel?=null
    var selectedNewsArrialList:NewArrivalsModel?=null
    var selectedFeatureList:FeatureProductModel?=null
    var selectedAllProductList:AllCompanyModel?=null


    var extendAboutUS=false
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null


    lateinit var rootHome:LinearLayout
    lateinit var rootAboutUs:LinearLayout
    lateinit var rootAboutUsItem:LinearLayout
    lateinit var rootAbout1:TextView
    lateinit var rootAbout2:TextView
    lateinit var rootAbout3:TextView
    lateinit var ivArrowDown:ImageView
    lateinit var ivArrowRight:ImageView

    override fun set_layout() {
        setContentView(R.layout.navigation_drawer)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        drawerLayout =findViewById(R.id.drawer_layout)
        navigationView =findViewById(R.id.nav_view)
        val parentView = navigationView!!.getHeaderView(0)
        rootHome=parentView.findViewById(R.id.root_home)
        rootAboutUs=parentView.findViewById(R.id.root_about_us)
        rootAboutUsItem=parentView.findViewById(R.id.root_about_us_item)
        rootAboutUsItem.gone()
        rootAbout1=parentView.findViewById(R.id.root_menu_2_1)
        rootAbout2=parentView.findViewById(R.id.root_menu_2_2)
        rootAbout3=parentView.findViewById(R.id.root_menu_2_3)
        ivArrowRight=parentView.findViewById(R.id.iv_arrow_drawer)
        ivArrowDown=parentView.findViewById(R.id.iv_arrow_drawer_down)






    }


    fun setActivityTitle(txt:Int){
        tv_title_toolbar.setText(resources.getString(txt))
    }

    fun setActivityTitle(txt:String){
        tv_title_toolbar.setText(txt)
    }
    override fun init_events() {


        setActivityTitle(R.string.app_name)
        setSupportActionBar(activity_toolbar)
        val toggle : ActionBarDrawerToggle =
            object :
                ActionBarDrawerToggle(this, drawerLayout, activity_toolbar , R.string.item4, R.string.close_drawer){



                override fun onDrawerClosed(view: View){
                    super.onDrawerClosed(view)
                    //toast("Drawer closed")
                }

                override fun onDrawerOpened(drawerView: View){
                    super.onDrawerOpened(drawerView)

                }
            }
        drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()
        BasicTools.hideSoftKeyboard(this@MainActivity)

       // BasicTools.initialize_image_loader(this@MainActivity)



        rootHome.setOnClickListener{
            drawerLayout?.closeDrawers()

        }

        rootAboutUs.setOnClickListener{
           // drawerLayout?.closeDrawers()
            if(extendAboutUS){
                extendAboutUS=false
                rootAboutUsItem.gone()
               ivArrowRight.visibility=View.VISIBLE
                ivArrowDown.visibility=View.GONE

            }else{
                extendAboutUS=true
                rootAboutUsItem.visible()
                ivArrowRight.visibility=View.GONE
                ivArrowDown.visibility=View.VISIBLE
            }

        }





        iv_logout.setOnClickListener{
            var intent=Intent(this@MainActivity,LoginAcitivty::class.java)
            startActivity(intent)
            finish()
        }


        iv_shpping_cart.setBadgeValue(badgetItem)
            .setBadgeOvalAfterFirst(false)
            .setMaxBadgeValue(99)
            .setBadgePosition(BadgePosition.TOP_RIGHT)
            .setBadgeTextStyle(Typeface.NORMAL)
            .setShowCounter(true)
            .setBadgePadding(4)



    }

    override fun set_fragment_place() {
        this.fragment_place=root_fragment
    }


    fun resetBadget(item:Int){
        badgetItem+=item
        iv_shpping_cart.setBadgeValue(badgetItem)
            .setBadgeOvalAfterFirst(true)
            .setMaxBadgeValue(99)
            .setBadgeTextSize(12F)
            .setBadgePosition(BadgePosition.TOP_RIGHT)
            .setBadgeTextStyle(Typeface.NORMAL)
            .setShowCounter(true)
            .setBadgePadding(4)
    }

}
