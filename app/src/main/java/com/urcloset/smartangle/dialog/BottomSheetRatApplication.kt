package com.urcloset.smartangle.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.databinding.RateAppLayoutBinding
import com.urcloset.smartangle.tools.BasicTools
import dagger.hilt.android.AndroidEntryPoint

// used to get area and city besides get the medical
@AndroidEntryPoint
class BottomSheetRatApplication(val callBackUpdateListWithPosition: ((Int)-> Unit)? = null): DialogFragment()/*, CommonPresenter.CommonCallsInterface*/{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for making the bottom sheet background transparent
      //  webService = ApiManagerDefault(activity!!).apiService
    }
   lateinit var dialogs : Dialog
   lateinit var binding : RateAppLayoutBinding


    private fun setLayoutItemsAndActions(binding: RateAppLayoutBinding) {
        binding.send.setOnClickListener{
        //    startActivity(Intent(requireContext(), HomeActivity::class.java).putExtra("show_review",true))
            BasicTools.open_website("https://play.google.com/store/apps/details?id=com.urcloset.smartangle",requireActivity())
            BasicTools.setReviewedBefore(requireContext(),true)
        //    requireActivity().finish()
        }
        binding.later.setOnClickListener{
       //     startActivity(Intent(requireContext(), HomeActivity::class.java).putExtra("show_review",true))
            dismiss()
       //     requireActivity().finish()

        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
         dialogs = Dialog(requireContext()) //super.onCreateDialog(savedInstanceState) as BottomSheetDialog
         binding = RateAppLayoutBinding.inflate(layoutInflater)
         dialogs.setContentView(binding.root)
         dialogs.getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialogs!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogs!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        lp.windowAnimations = R.style.DialogAnimation
        dialogs!!.window!!.attributes = lp
        dialogs.setCancelable(false)
        dialogs.setCanceledOnTouchOutside(false)
        setLayoutItemsAndActions(binding)
         return dialogs
     }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
      /*  binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
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
                BottomSheetBehavior.from(bottomSheet!!).peekHeight = 1000
                //   behavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        })*/
    }

    override fun onDestroyView() {
        //   model?.notifedItemSelectedAdaptor?.removeObservers(requireActivity()!!)
        super.onDestroyView()
    }
    companion object {
        val other_customer = "other_customer"

    }

}