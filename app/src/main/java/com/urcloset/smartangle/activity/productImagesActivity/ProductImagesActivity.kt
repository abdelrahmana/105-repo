package com.urcloset.smartangle.activity.productImagesActivity

import android.Manifest
import android.app.DownloadManager
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.urcloset.smartangle.R
import com.urcloset.smartangle.adapter.ProductImagesAdapter
import com.urcloset.smartangle.globals.Global
import com.urcloset.smartangle.model.ProductDetailsModel
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity

class ProductImagesActivity() : TemplateActivity(),OnImageDownloadListen {
    lateinit var  viewPager:ViewPager
     var  images:ArrayList<ProductDetailsModel.Data.Item.ItemMedia>? =null
    var link =""
    var name =""
    var type=""
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
            super.onCreate(savedInstanceState)
        }
    }

    override fun set_layout() {
        setContentView(R.layout.activity_product_images)
    }

    override fun init_activity(savedInstanceState: Bundle?) {
        if(!Global.productImages.isNullOrEmpty()) {
            images = Global.productImages!!
            val adapter =  ProductImagesAdapter(images!!, context = this)
            adapter.setOnImageDownload(this)
            viewPager.adapter = adapter
            viewPager.setCurrentItem(Global.position)

        }




    }

    override fun init_views() {
        viewPager = findViewById(R.id.viewPager)


    }

    override fun init_events() {
    }

    override fun set_fragment_place() {
    }

    override fun onClick(link: String, name: String, type: String) {
        this.link = link
        this.name = name
        this.type = type
        if (checkWritePremission()) {
            val manager: DownloadManager
            val request = DownloadManager.Request(
                Uri.parse(
                    BasicTools.getUrlHttpImg(this, link)
                )
            )
            request.setDescription("Downloading " + name)
            request.setTitle(name)
            request.setAllowedOverMetered(true)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                name + "." + type
            )
            manager = this.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        } else {
             ActivityCompat.requestPermissions(
                 this,
                 arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                 111
             )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 111) {
        }
    }

    private fun checkWritePremission(): Boolean {

            if (Build.VERSION.SDK_INT >= 23) {
                //do your check here
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //File write logic here
                    return true
                } else return false
            } else {
                return true
            }

    }
    fun downloadImage(){
        val manager: DownloadManager
        val request = DownloadManager.Request(
            Uri.parse(
                BasicTools.getUrlHttpImg(this, link)
            )
        )
        request.setDescription("Downloading " + name)
        request.setTitle(name)
        request.setAllowedOverMetered(true)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            name + "." + type
        )
        manager = this.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)

    }
}
