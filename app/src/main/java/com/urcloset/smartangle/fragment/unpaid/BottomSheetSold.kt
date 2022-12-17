package com.urcloset.smartangle.fragment.unpaid

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.databinding.BottomSheetSelectSoldBinding
import com.urcloset.smartangle.fragment.paymentmethod.PaymentMethodFragment
import com.urcloset.smartangle.fragment.paymentmethod.PaymentMethodFragment.Companion.IDS
import com.urcloset.smartangle.fragment.paymentmethod.PaymentMethodFragment.Companion.SELECTED_PRODUCTS
import com.urcloset.smartangle.fragment.paymentmethod.PaymentMethodFragment.Companion.SOLD_IDS
import com.urcloset.smartangle.fragment.unpaid.adaptor.AdaptorSold
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.GetObjectGson
import com.urcloset.smartangle.tools.GridModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// used to get area and city besides get the medical
@AndroidEntryPoint
class BottomSheetSold(val callBackUpdateListWithPosition: ((Int)-> Unit)? = null): BottomSheetDialogFragment()/*, CommonPresenter.CommonCallsInterface*/{

    @Inject
    lateinit var getObjectGson : GetObjectGson
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for making the bottom sheet background transparent
      //  webService = ApiManagerDefault(activity!!).apiService
    }
   lateinit var dialog : BottomSheetDialog
   lateinit var binding : BottomSheetSelectSoldBinding
    var hashMap :   HashMap<String,Any>? =null
    lateinit var adaptorSold: AdaptorSold
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetSelectSoldBinding.inflate(layoutInflater)
        hashMap = getObjectGson.getGenericMap(arguments?.getString(PaymentMethodFragment.PAYMENTPUBLISH)?:"")
        binding.send.setOnClickListener{ // this should call send

        }
        setAdaptorLayoutItemsAndActions(binding,hashMap?.get(SELECTED_PRODUCTS)as List<ProductModel.Product>)
        return binding.root
    }

    private fun setAdaptorLayoutItemsAndActions(binding: BottomSheetSelectSoldBinding, list: List<ProductModel.Product>) {
       adaptorSold = AdaptorSold(requireContext(), list as ArrayList<ProductModel.Product>,null)
        BasicTools.setRecycleView(binding.soldProducts,adaptorSold,
            null,requireContext(), GridModel(3,10),false)
        binding.send.setOnClickListener{
        val soldProducts =  adaptorSold.arrayList.filter { it.soldItem } // sold
            val unsoldProducts =  adaptorSold.arrayList.filter { !it.soldItem }
            hashMap!![IDS] =soldProducts.map { it.id.toString() }
            hashMap!![SOLD_IDS] =unsoldProducts.map { it.id.toString() }
            val bundle = bundleOf(PaymentMethodFragment.PAYMENTPUBLISH to Gson().toJson(hashMap))
            BasicTools.changeFragmentBack(
                requireActivity(),
                PaymentMethodFragment(),
                "payment_pay",
                bundle,
                R.id.root_fragment_home
            )
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
         dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
         binding = BottomSheetSelectSoldBinding.inflate(layoutInflater)
         dialog.setContentView(binding.root)
         dialog.getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
         return dialog
     }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
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
        })
    }

    override fun onDestroyView() {
        //   model?.notifedItemSelectedAdaptor?.removeObservers(requireActivity()!!)
        super.onDestroyView()
    }
    companion object {
        val other_customer = "other_customer"

    }

}