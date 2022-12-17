package com.urcloset.smartangle.fragment.bottomsheetagree

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.currencyapp.ui.base.HomeUserViewModel
import com.urcloset.smartangle.R
import com.urcloset.smartangle.databinding.ConsentLayoutBottomSheetBinding
import com.urcloset.smartangle.tools.BasicTools
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


// used to get area and city besides get the medical
@AndroidEntryPoint
class ConsentBottomSheet(val whichLayer: WhichLayer,val callBackAgree: ((Int)-> Unit)? = null): DialogFragment()/*, CommonPresenter.CommonCallsInterface*/{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for making the bottom sheet background transparent
      //  webService = ApiManagerDefault(activity!!).apiService
       // setStyle(STYLE_NORMAL/*, R.style.CustomBottomSheetDialogTheme*/)
    }
    lateinit var dialogs : Dialog
   lateinit var binding : ConsentLayoutBottomSheetBinding
   val homeViewModel : HomeUserViewModel by viewModels()
    @Inject lateinit var progressDialog : Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//         dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialogs = Dialog(requireContext())
        binding = ConsentLayoutBottomSheetBinding.inflate(layoutInflater)
        dialogs.setContentView(binding.root)
        dialogs.getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialogs!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogs!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = if (whichLayer.shortDescription())WindowManager.LayoutParams.WRAP_CONTENT else WindowManager.LayoutParams.MATCH_PARENT
        lp.gravity = Gravity.BOTTOM
        lp.windowAnimations = R.style.DialogAnimation
        dialogs!!.window!!.attributes = lp
      /*   dialogs.setOnShowListener {
             val castDialog = it as BottomSheetDialog
          //   val bottomSheet = castDialog.findViewById<View?>(R.id.design_bottom_sheet)
           //  val behavior = BottomSheetBehavior.from(bottomSheet!!)
            // behavior.state = BottomSheetBehavior.PEEK_HEIGHT_AUTO
         /*    behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                 override fun onStateChanged(bottomSheet: View, newState: Int) {
                     if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                       //  behavior.state = BottomSheetBehavior.STATE_DRAGGING
                       //behavior?.state = BottomSheetBehavior.STATE_EXPANDED

                     }
                     if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                         dismiss()
                     }
                 }

                 override fun onSlide(bottomSheet: View, slideOffset: Float) {}
             })*/


         }*/
    /*  binding.rateButton.setOnClickListener{
          model.postReviewsData(HashMap<String,Any>().also {
              it.put(RATED_ID,arguments?.getInt(RATED_ID)?:0)
              it.put(RATE,binding.ratingSlider.rating)
              it.put(REVIEW,binding.commentEditText.text.toString())

          })
      }*/
        if (whichLayer.shortDescription())
            binding.descriptionType.text = getString(R.string.short_description)
        else
        homeViewModel.getPolicyText() // load policy
        binding.agree.setOnClickListener{ // this should call send
            if (whichLayer.requireApiCall()) {
                homeViewModel.postAcceptPolicy(HashMap<String, Int>().also { it.put("agree",1) })
            }
            else {
                callBackAgree?.invoke(1)
                dialogs.dismiss()

            }
        }

        binding.cancel.setOnClickListener {
          dialogs.dismiss()
        }
        setViewModelObservers()
        return dialogs
     }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
     /*   binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val dialog = dialog as BottomSheetDialog
                val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                val behavior = BottomSheetBehavior.from(bottomSheet!!)
                //    behavior.state = BottomSheetBehavior.PEEK_HEIGHT_AUTO

                val newHeight = activity?.window?.decorView?.measuredHeight
                val viewGroupLayoutParams = bottomSheet.layoutParams
                viewGroupLayoutParams.height = newHeight ?: 0
                bottomSheet.layoutParams = viewGroupLayoutParams
                BottomSheetBehavior.from(bottomSheet!!).peekHeight = viewGroupLayoutParams.height
                //   behavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        })*/

    }

    private fun setViewModelObservers() {

        homeViewModel.networkLoader.observe(this, Observer{
            it?.let { progress->
                progress.setDialog(progressDialog) // open close principles
                homeViewModel.setNetworkLoader(null)
            }
        })
        homeViewModel.responseAgreements.observe(this, Observer{
            it?.let { responseAgreements->
                addingWebViewLoader(if (BasicTools.isDeviceLanEn())responseAgreements.data?.content?:""
                else responseAgreements.data?.content_ar?:"")
            }
        })
        homeViewModel.responseCreationLiveData.observe(this, Observer{
            it?.let { responseAgreements->
                dialogs?.dismiss() // when result is coming back
             callBackAgree?.invoke(1)
            }
        })
        homeViewModel.errorViewModel.observe(this) {
            it?.let { error ->
              Toast.makeText(requireContext(),error,Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun addingWebViewLoader(htmlOrUrl: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.descriptionType.setText(Html.fromHtml(htmlOrUrl, Html.FROM_HTML_MODE_COMPACT))
        } else {
            binding.descriptionType.setText(Html.fromHtml(htmlOrUrl));
        }
    /*    binding.descriptionType.settings.textZoom = 100
        binding.descriptionType.loadData(htmlOrUrl, "text/html; charset=utf-8", "UTF-8")

        binding.descriptionType.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                progressDialog!!.dismiss()
                // do your stuff here
                /*   if (url.contains("success")) {

                       val intent = Intent(this@WebViewInformationActivity, HomeActivityBottomNav::class.java)
                       showSnackMessage(this@WebViewInformationActivity, getString(R.string.payment_compeleted_successfully))
                       UtilKotlin.setHandlerToFinishCurrentActivity(intent, this@WebViewInformationActivity)
                   }*/
            }

        }*/
    }
    override fun onDestroyView() {
      homeViewModel.clearLiveDataOnActivity(this)
        //   model?.notifedItemSelectedAdaptor?.removeObservers(requireActivity()!!)
        super.onDestroyView()
    }
    companion object {
    }

}