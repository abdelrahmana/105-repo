package com.urcloset.smartangle.activity.changePasswordActivity


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
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_change_passwrod.*
import kotlinx.android.synthetic.main.toolbar_backpress1.*
import okhttp3.ResponseBody

class ChangePasswrodActivity  : TemplateActivity(),Validator.ValidationListener {

    var disposable= CompositeDisposable()

    var validator: Validator? = null
    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    lateinit var editOldPassword: EditText
    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    @Password
    lateinit var editNewPassword: EditText
    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    @ConfirmPassword(messageResId = R.string.password_donot_match)
    lateinit var editRePassword: EditText
    override fun set_layout() {
        setContentView(R.layout.activity_change_passwrod)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {
        validator = Validator(this)
        validator!!.setValidationListener(this)
        editOldPassword=edit_old_password
        editNewPassword=edit_new_password
        editRePassword=edit_re_enter_password

    }

    override fun init_events() {

        iv_back.setOnClickListener {
            BasicTools.exitActivity(this@ChangePasswrodActivity)
        }

        /*old*/
        eye_password_old.setOnClickListener {
            if(editOldPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                eye_password_old.setImageResource(R.drawable.visibility_3x)
                editOldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                eye_password_old.setImageResource(R.drawable.visibility_off_3x)
                editOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }


        /*new */

        eye_password_new.setOnClickListener {
            if(editNewPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                eye_password_new.setImageResource(R.drawable.visibility_3x)
                editNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                eye_password_new.setImageResource(R.drawable.visibility_off_3x)
                editNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }





        /*re Enter */

        eye_password_re_enter.setOnClickListener {
            if(editRePassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                eye_password_re_enter.setImageResource(R.drawable.visibility_3x)
                editRePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                eye_password_re_enter.setImageResource(R.drawable.visibility_off_3x)
                editRePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }


        card_save.setOnClickListener{
            validator!!.validate()

        }


    }

    override fun set_fragment_place() {

    }

    override fun onValidationFailed(errors:List<ValidationError>?) {
        BasicTools.showValidationErrorMessagesForFields(errors!!, this@ChangePasswrodActivity)
    }




    fun showShimmerBtn(state: Boolean){

        if(state){
            card_save.visibility= View.GONE
            shimmer_wait.visible()
        }else{
            card_save.visibility= View.VISIBLE
            shimmer_wait.hide()
        }
    }

    override fun onValidationSucceeded() {

        if(!editOldPassword.text.trim().toString().equals(BasicTools.getPassword(this@ChangePasswrodActivity))){
            showToastMessage(R.string.old_password_wrong)
            return
        }

        RQ()



    }



    fun RQ(){
        if (BasicTools.isConnected(this@ChangePasswrodActivity)) {


            var map = HashMap<String, String>()

         /*   var user=TemplateActivity.loginResponse!!.data!!.user
            if(!user?.email.isNullOrEmpty()){
                map.put("reset_by","email")
                map.put("email",user?.email!!)
            }else if(!user?.phoneNumber.isNullOrEmpty()&&!user?.countryCode.isNullOrEmpty()){
                map.put("reset_by","email")
                map.put("country_code",user?.countryCode!!)
                map.put("phone_number",user?.phoneNumber!!)
            }
*/




            showShimmerBtn(true)


            map.put("old_password",editOldPassword.text.trim().toString())
            map.put("new_password",editNewPassword.text.trim().toString())
            map.put("c_password",editRePassword.text.trim().toString())
            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(this@ChangePasswrodActivity),  BasicTools.getProtocol(this@ChangePasswrodActivity).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.editPassword(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(this@ChangePasswrodActivity) {
                        override fun onSuccess(result: ResponseBody) {


                            showShimmerBtn(false)




                                showToastMessage(R.string.success)
                                BasicTools.setPassword(this@ChangePasswrodActivity,editNewPassword.text.trim().toString())
                                BasicTools.exitActivity(this@ChangePasswrodActivity)



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
