package com.urcloset.smartangle.activity.forgotPassword


import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.loginActivity.LoginAcitivty
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
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Password
import com.urcloset.smartangle.databinding.ActivityForgotPasswrodBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ForgotPassowrdActivity : TemplateActivity(), Validator.ValidationListener {

    var disposable= CompositeDisposable()

    var validator: Validator? = null

    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    @Password
    lateinit var editNewPassword: EditText
    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    @ConfirmPassword(messageResId = R.string.password_donot_match)
    lateinit var editRePassword: EditText
    var binding : ActivityForgotPasswrodBinding ?= null

    
    override fun set_layout() {
        binding = ActivityForgotPasswrodBinding.inflate(layoutInflater)
        setContentView(/*R.layout.activity_forgot_passwrod*/binding!!.root)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {
        validator = Validator(this)
        validator!!.setValidationListener(this)

        editNewPassword=binding?.editNewPassword!!
        editRePassword=binding?.editReEnterPassword!!

    }

    override fun init_events() {
        binding?.toolbar?.ivBack?.setOnClickListener {
            BasicTools.exitActivity(this@ForgotPassowrdActivity)
        }


        /*new */

        binding?.eyePasswordNew?.setOnClickListener {
            if(editNewPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                binding?.eyePasswordNew?.setImageResource(R.drawable.visibility_3x)
                editNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                binding?.eyePasswordNew?.setImageResource(R.drawable.visibility_off_3x)
                editNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }





        /*re Enter */

        binding?.eyePasswordReEnter?.setOnClickListener {
            if(editRePassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                binding?.eyePasswordReEnter?.setImageResource(R.drawable.visibility_3x)
                editRePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                binding?.eyePasswordReEnter?.setImageResource(R.drawable.visibility_off_3x)
                editRePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }


        binding?.cardSave?.setOnClickListener{
            validator!!.validate()

        }

    }

    override fun set_fragment_place() {

    }

    override fun onValidationFailed(errors:List<ValidationError>?) {
        BasicTools.showValidationErrorMessagesForFields(errors!!, this@ForgotPassowrdActivity)
    }

    fun showShimmerBtn(state: Boolean){

        if(state){
            binding?.cardSave?.visibility= View.GONE
            binding?.shimmerWait?.visible()
        }else{
            binding?.cardSave?.visibility= View.VISIBLE
            binding?.shimmerWait?.hide()
        }
    }

    override fun onValidationSucceeded() {



       RQ()



    }


    fun RQ(){
        if (BasicTools.isConnected(this@ForgotPassowrdActivity)) {



            showShimmerBtn(true)

            var email_phone=""
            if(TemplateActivity.phoneForgotPasswrod.isNotEmpty())
                email_phone=TemplateActivity.phoneForgotPasswrod
            else if(TemplateActivity.emailForgotPasswrod.isNotEmpty())
                email_phone=TemplateActivity.emailForgotPasswrod
            var map = HashMap<String, String>()
            map.put("email_phone",email_phone)
            map.put("password",editNewPassword.text.trim().toString())
            val shopApi = ApiClient.getClient(
              BasicTools.getProtocol(this@ForgotPassowrdActivity).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.fogotPassword(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<MessageModel>(this@ForgotPassowrdActivity) {
                        override fun onSuccess(result: MessageModel) {


                            showShimmerBtn(false)


                            if(result.status!!){
                                TemplateActivity.phoneForgotPasswrod=""
                                TemplateActivity.emailForgotPasswrod=""
                                showToastMessage(R.string.success)
                                BasicTools.setPassword(this@ForgotPassowrdActivity,editNewPassword.text.trim().toString())
                                BasicTools.clearAllActivity(this@ForgotPassowrdActivity,LoginAcitivty::class.java)
                            }


                        }
                        override fun onFailed(status: Int) {

                            showShimmerBtn(false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            showShimmerBtn(false)
            showToastMessage(R.string.no_connection)}
    }



}
