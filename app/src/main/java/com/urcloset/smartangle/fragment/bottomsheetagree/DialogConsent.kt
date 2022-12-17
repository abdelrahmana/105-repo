package com.urcloset.smartangle.fragment.bottomsheetagree

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.databinding.ConsentLayoutBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//this class make error and animation style
@AndroidEntryPoint
class DialogConsent(val whichLayer: WhichLayer,val callBackAgree: ((Int)-> Unit)? = null) : DialogFragment() {

    var dalogMessages: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    @Inject lateinit var progressDialog : Dialog
    // this is really called when you call show and this is called when we call show on activity it self
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dalogMessages = Dialog(requireContext())
        dalogMessages?.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE)
        val binding : ConsentLayoutBottomSheetBinding = ConsentLayoutBottomSheetBinding.inflate(layoutInflater)
        dalogMessages!!.setContentView(binding.root)
      //  dalogMessages!!.getWindow()!!.getAttributes().windowAnimations = R.style.DialogSlideAnim
        if (dalogMessages!!.isShowing)
            dalogMessages!!.dismiss()
        dalogMessages!!.show()
        dalogMessages!!.window!!.setGravity(Gravity.CENTER)
        dalogMessages?.setCancelable(false)
        dalogMessages!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        dalogMessages?.getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dalogMessages!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        lp.gravity = Gravity.CENTER
        lp.windowAnimations = R.style.DialogAnimation
        dalogMessages!!.window!!.attributes = lp

        //dialogToast?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
// dialogToast!!.getWindow()!!.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dalogMessages!!.show()
        initViews(binding)
        return dalogMessages!!
    }

    private fun initViews(binder: ConsentLayoutBottomSheetBinding?) {

    }

    override fun onDismiss(dialog: DialogInterface) {
       // super.onDismiss(dialog)
        dalogMessages?.dismiss()
    }

    companion object {
        val CaSESUCCESS: String = "Success"
        val ERROR_MESSAGE = "ERROR"
    }
}
