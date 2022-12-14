package com.urcloset.smartangle.activity.forgotPassword


import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.urcloset.smartangle.R
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.project21.MessageModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_email_forgotpassword.*
import kotlinx.android.synthetic.main.toolbar_backpress1.*

class EnterEmailActivityForgotPassword : TemplateActivity(), Validator.ValidationListener {
    var disposable= CompositeDisposable()
    var validator: Validator? = null

    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    @Email(sequence = 2, messageResId = R.string.enter_a_valid_email_address)
    lateinit var editEmail: EditText
    override fun set_layout() {
        setContentView(R.layout.activity_enter_email_forgotpassword)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {
        
        editEmail=edit_email

    }

    override fun init_events() {

        iv_back.setOnClickListener {
            BasicTools.exitActivity(this@EnterEmailActivityForgotPassword)
        }

        validator = Validator(this)
        validator!!.setValidationListener(this)

        card_contintue.setOnClickListener{
            validator!!.validate()

        }

    }

    override fun set_fragment_place() {

    }

    override fun onValidationFailed(errors:List<ValidationError>?) {
        BasicTools.showValidationErrorMessagesForFields(errors!!, this@EnterEmailActivityForgotPassword)
    }

    override fun onValidationSucceeded() {
       // checkPhoneRQ(editEmail.text.trim().toString())

    }


    fun showShimmerBtn(state: Boolean){

        if(state){
            card_contintue.visibility= View.GONE
            shimmer_wait.visible()
        }else{
            card_contintue.visibility= View.VISIBLE
            shimmer_wait.hide()
        }
    }

//    fun checkPhoneRQ(txt:String){
//        if (BasicTools.isConnected(this@EnterEmailActivityForgotPassword)) {
//
//
//
//            showShimmerBtn(true)
//
//            var map = HashMap<String, String>()
//            map.put("email_phone",txt)
//            val shopApi = ApiClient.getClientJwt(
//                BasicTools.getToken(this@EnterEmailActivityForgotPassword),
//                BasicTools.getProtocol(this@EnterEmailActivityForgotPassword).toString())
//                ?.create(AppApi::class.java)
//            val observable= shopApi!!.checkPhone(map)
//            disposable.clear()
//            disposable.add(
//                observable.subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeWith(object : AppObservable<MessageModel>(this@EnterEmailActivityForgotPassword) {
//                        override fun onSuccess(result: MessageModel) {
//
//
//                            showShimmerBtn(false)
//
//                            if(result.status!!){
//
//                                TemplateActivity.emailForgotPasswrod=editEmail.text.trim().toString()
//                                TemplateActivity.phoneForgotPasswrod=""
//
//
//                                BasicTools.openActivity(this@EnterEmailActivityForgotPassword,ForgotPassowrdActivity::class.java,false)
//
//
//
//                            }else{
//                                showToastMessage(R.string.not_found_email)
//                            }
//
//
//                        }
//                        override fun onFailed(status: Int) {
//
//                            showShimmerBtn(false)
//                            showToastMessage(R.string.faild)
//                            //BasicTools.logOut(parent!!)
//                        }
//                    }))
//
//        }
//        else {
//            showShimmerBtn(false)
//            showToastMessage(R.string.no_connection)}
//    }


}
