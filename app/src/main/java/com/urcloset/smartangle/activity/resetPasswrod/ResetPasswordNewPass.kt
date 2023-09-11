package com.urcloset.smartangle.activity.resetPasswrod


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Password
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.loginActivity.LoginAcitivty
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.databinding.ActivityForgotPasswrodBinding
import com.urcloset.smartangle.model.project21.MessageModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.Constants
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class ResetPasswordNewPass : TemplateActivity() , Validator.ValidationListener {

    var disposable= CompositeDisposable()

    var validator: Validator? = null

    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    @Password
    lateinit var editNewPassword: EditText
    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    @ConfirmPassword(messageResId = R.string.password_donot_match)
    lateinit var editRePassword: EditText

    var binding : ActivityForgotPasswrodBinding? =null
    override fun set_layout() {
        binding = ActivityForgotPasswrodBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {
        validator = Validator(this)
        validator!!.setValidationListener(this)

        editNewPassword=binding!!.editNewPassword
        editRePassword=binding!!.editReEnterPassword

    }

    override fun init_events() {
        binding!!.toolbar.ivBack.setOnClickListener {
            BasicTools.exitActivity(this@ResetPasswordNewPass)
        }


        /*new */

        binding!!.eyePasswordNew.setOnClickListener {
            if(editNewPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                binding!!.eyePasswordNew.setImageResource(R.drawable.visibility_3x)
                editNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                binding!!.eyePasswordNew.setImageResource(R.drawable.visibility_off_3x)
                editNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }





        /*re Enter */

        binding!!.eyePasswordReEnter.setOnClickListener {
            if(editRePassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                binding!!.eyePasswordReEnter.setImageResource(R.drawable.visibility_3x)
                editRePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                binding!!.eyePasswordReEnter.setImageResource(R.drawable.visibility_off_3x)
                editRePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }


        binding!!.cardSave.setOnClickListener{
            validator!!.validate()

        }

    }

    override fun set_fragment_place() {

    }

    override fun onValidationFailed(errors:List<ValidationError>?) {
        BasicTools.showValidationErrorMessagesForFields(errors!!, this@ResetPasswordNewPass)
    }

    fun showShimmerBtn(state: Boolean){

        if(state){
            binding!!.cardSave.visibility= View.GONE
            binding!!.shimmerWait.visible()
        }else{
            binding!!.cardSave.visibility= View.VISIBLE
            binding!!.shimmerWait.hide()
        }
    }

    override fun onValidationSucceeded() {



        RQ()



    }


    fun RQ(){
        if (BasicTools.isConnected(this@ResetPasswordNewPass)) {

            var map = HashMap<String, String>()
            if(TemplateActivity.registerType.equals(Constants.LOGIN_TYPE_EMAIL)){
                map.put("email", TemplateActivity.registerValue)
                map.put("reset_by", "email")



            }

            else if(TemplateActivity.registerType.equals(Constants.LOGIN_TYPE_PHONE)){
                map.put("phone_number",TemplateActivity.registerValue)
                map.put("country_code",TemplateActivity.registerValueCode)
                map.put("reset_by", "phone")


            }

            map.put("code",TemplateActivity.codeForEmailOrPhone)

            showShimmerBtn(true)

            var email_phone=""

            if(TemplateActivity.registerValueCode.isNotEmpty())
                email_phone=TemplateActivity.registerValueCode+TemplateActivity.registerValue

            else
                email_phone=TemplateActivity.registerValue

          //  map.put("email_phone",email_phone)
            map.put("password",editNewPassword.text.trim().toString())
            map.put("c_password",editNewPassword.text.trim().toString())
            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(this@ResetPasswordNewPass).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.resetPass(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(this@ResetPasswordNewPass) {
                        override fun onSuccess(result: ResponseBody) {


                            showShimmerBtn(false)

                            TemplateActivity.phoneForgotPasswrod=""
                            TemplateActivity.emailForgotPasswrod=""
                            TemplateActivity.registerValueCode=""
                            TemplateActivity.registerValue=""
                            showToastMessage(R.string.success)
                            BasicTools.setPassword(this@ResetPasswordNewPass,editNewPassword.text.trim().toString())
                            BasicTools.clearAllActivity(this@ResetPasswordNewPass,
                                LoginAcitivty::class.java)
                         /*   if(result.status!!){

                            }

*/

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
