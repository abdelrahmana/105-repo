package com.urcloset.smartangle.activity.ContactAndSupportActivity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.orderSentActivity.OrderSentActivity
import com.urcloset.smartangle.adapter.ChatAdapter
import com.urcloset.smartangle.adapter.NotificationAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.ChatModel
import com.urcloset.smartangle.model.CreateProductModel
import com.urcloset.smartangle.model.NotificationModel
import com.urcloset.smartangle.model.SendMessageModel
import com.urcloset.smartangle.model.project_105.ColorModelHassan
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_contact_and_support.*
import kotlinx.android.synthetic.main.toolbar_backpress1.*


class ContactAndSupportActivity : TemplateActivity() {
    lateinit var btnSubmit:CardView
    lateinit var email:EditText
    lateinit var subject:EditText
    lateinit var des:EditText
    lateinit var rvBox:RecyclerView
    lateinit var  lybox : LinearLayout
      lateinit var box:TextView
    lateinit var lysupport:LinearLayout
    lateinit var shimmerFrameLayout:ShimmerFrameLayout
    lateinit var chatShimmerFrameLayout:ShimmerFrameLayout
    lateinit var chatAdapter:ChatAdapter
    lateinit var support :TextView
    var disposable = CompositeDisposable()
    var isLoading =false
    lateinit var progressBar: ProgressBar
    var currentPage =1
    lateinit var chatModel: ChatModel
    lateinit var ivBack: ImageView



    var lang:String ="en"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun set_layout() {
        setContentView(R.layout.activity_contact_and_support)
    }

    override fun init_activity(savedInstanceState: Bundle?) {
        if(!BasicTools.isDeviceLanEn())
            lang ="ar"
        rvBox.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)

    }

    override fun init_views() {
        btnSubmit = submit
        email = findViewById(R.id.et_email)
        subject = findViewById(R.id.et_subject)
        des = findViewById(R.id.et_des)
        shimmerFrameLayout = findViewById(R.id.shimmer)
        chatShimmerFrameLayout = findViewById(R.id.chat_shimmer)

        progressBar = progress_bar_chat
        rvBox = findViewById(R.id.rv_box)
        lybox = findViewById(R.id.lybox)
        box = findViewById(R.id.tv_box)
        lysupport = findViewById(R.id.lynew)
        support = findViewById(R.id.tv_new_request)
        ivBack=findViewById(R.id.iv_back)




    }

    override fun init_events() {
        btnSubmit.setOnClickListener {
            sendMessage()

        }
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        rvBox.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if(!isLoading) {
                        if (currentPage < chatModel.data?.chat?.lastPage!!) {
                            progressBar.visibility = View.VISIBLE
                            currentPage = currentPage + 1

                            getNextChat(currentPage.toString())
                        }
                    }


                }
            }
        })
        lysupport.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                rv_box.visibility = View.GONE

                support.setTextColor(Color.WHITE)
                lysupport.background = resources.getDrawable(R.drawable.selected_text_button_bg_for_contact)
                box.setTextColor(Color.BLACK)
                lybox.background = resources.getDrawable(R.drawable.unselected_text_button_bg_for_contact)
                rv_box.visibility = View.GONE
                body.visibility = View.VISIBLE

                findViewById<LinearLayout>(R.id.ly_empty).visibility = View.GONE


            }

        })

        lybox.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                box.setTextColor(Color.WHITE)
                lybox.background = resources.getDrawable(R.drawable.selected_text_button_bg_for_contact)
                support.setTextColor(Color.BLACK)
                lysupport.background = resources.getDrawable(R.drawable.unselected_text_button_bg_for_contact)
                rv_box.visibility = View.VISIBLE
                body.visibility = View.GONE
                getChat()

            }

        })
        ivBack.setOnClickListener {

            BasicTools.exitActivity(this)
        }
    }

    override fun set_fragment_place() {
    }

    fun sendMessage(){
        if(et_email.text.toString().isNotEmpty() && et_subject.text.toString().isNotEmpty() && et_des.text.toString().isNotEmpty()) {
            showShimmerAddBtn(true)

            val shopApi =
                ApiClient.getClientJwt(
                    TemplateActivity.loginResponse?.data?.accessToken!!,
                    BasicTools.getProtocol(applicationContext).toString(), lang
                )
                    ?.create(
                        AppApi::class.java
                    )
            val map = HashMap<String, String>()
            map.put("email", et_email.text.toString())
            map.put("subject", et_subject.text.toString())
            map.put("message", et_des.text.toString())

            val observable = shopApi!!.sendMessage(map)
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<SendMessageModel>(this) {
                        override fun onSuccess(result: SendMessageModel) {
                            Toast.makeText(
                                this@ContactAndSupportActivity,
                                "" + result.messages,
                                Toast.LENGTH_SHORT
                            ).show()
                            showShimmerAddBtn(false)
                            et_email.setText("")
                            et_subject.setText("")
                            et_des.setText("")


                        }

                        override fun onFailed(status: Int) {
                            Toast.makeText(
                                this@ContactAndSupportActivity,
                                "" + R.string.faild,
                                Toast.LENGTH_SHORT
                            ).show()

                            showShimmerAddBtn(false)
                            super.onFailed(status)
                        }


                    })
            )
        }
        else{
            Toast.makeText(applicationContext, R.string.you_must_fill_all_the_fileds, Toast.LENGTH_SHORT).show()
        }
    }
    fun showShimmerAddBtn(state:Boolean){

        if (state) {
            submit.visibility = View.GONE
            shimmerFrameLayout.visible()
        } else {
            submit.visibility = View.VISIBLE
            shimmerFrameLayout.hide()
            shimmerFrameLayout.visibility = View.GONE
        }
    }
    private fun getChat(){
        BasicTools.showShimmer(rvBox,chatShimmerFrameLayout,true)
        val shopApi =
            ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(applicationContext).toString(), lang
            )
                ?.create(
                    AppApi::class.java
                )

        val observable = shopApi!!.getChat(currentPage.toString())
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<ChatModel>(this) {
                    override fun onSuccess(result: ChatModel) {
                        BasicTools.showShimmer(rvBox,chatShimmerFrameLayout,false)
                        chatShimmerFrameLayout.visibility = View.GONE

                        if (result.status!!) {
                            progressBar.visibility = View.GONE

                            chatModel = result
                            if(result.data?.chat?.total!!>0) {
                                findViewById<TextView>(R.id.tv_no_chat_data).visibility = View.GONE

                                chatAdapter = ChatAdapter(
                                    this@ContactAndSupportActivity,
                                    result.data.chat.data as ArrayList<ChatModel.Data.Chat.ChatItems>
                                )

                                rvBox.addItemDecoration(
                                    DividerItemDecoration(
                                        this@ContactAndSupportActivity,
                                        LinearLayoutManager.VERTICAL
                                    )
                                )
                                rvBox.adapter = chatAdapter
                            }
                            else{
                                findViewById<LinearLayout>(R.id.ly_empty).visibility = View.VISIBLE

                            }


                        }
                    }

                    override fun onFailed(status: Int) {
                        BasicTools.showShimmer(rvBox,chatShimmerFrameLayout,false)
                        chatShimmerFrameLayout.visibility = View.GONE

                        super.onFailed(status)
                    }


                })
        )

    }
    private fun getNextChat(page:String){
        val shopApi =
            ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(applicationContext).toString(), lang
            )
                ?.create(
                    AppApi::class.java
                )

        val observable = shopApi!!.getChat(page)
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<ChatModel>(this) {
                    override fun onSuccess(result: ChatModel) {


                    }

                    override fun onFailed(status: Int) {
                        super.onFailed(status)
                    }


                })
        )

    }

    }

