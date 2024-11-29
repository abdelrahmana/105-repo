package com.urcloset.smartangle.activity.publishStatusActivity

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.adapter.PublishStateAdapter
import com.urcloset.smartangle.databinding.ProductPublishStateLayoutBinding
import com.urcloset.smartangle.fragment.paymentmethod.PaymentMethodFragment
import com.urcloset.smartangle.fragment.unpaid.UnpaidCommissionsFragment.Companion.STATICPERPRODUCTCOMMISSION
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.BasicTools
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductOneItemFragment(val checkCurrentFragmentInterface: InterfacePublication) : Fragment() {

    lateinit var binding: ProductPublishStateLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    val model: ProductViewModel by viewModels()
    @Inject
    lateinit var progressDialog: Dialog

    //  @Inject lateinit var util: Util
    lateinit var adapterOneItem: PublishStateAdapter
    val arrayList = ArrayList<ProductModel.Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // model.setItemActionClicked(itemClicked,itemAskPay,itemChat,compeleteOrder,itemRate,itemSubscripers)
        binding = ProductPublishStateLayoutBinding.inflate(layoutInflater, container, false)
        // checkCurrentFragmentInterface.setLiveData(model.getMutableLiveOnReview()) // set listener callback
        // set item clicked actions
        adapterOneItem =
            PublishStateAdapter(requireContext(), arrayList, checkCurrentFragmentInterface) {
                // when pay commission clicked
                val hashMap = HashMap<String, Any?>()
                val productArray = ArrayList<ProductModel.Product>()
                productArray.add(it)
                val productsIds = productArray.map { (it.id?.toString()) }
                hashMap[PaymentMethodFragment.IDS] = productsIds
                //   hashMap[PaymentMethodFragment.SELECTED_PRODUCTS] = selectedProducts

                hashMap[PaymentMethodFragment.AMOUNT] =/*(binding.valueCommission.text.toString())*/
                     STATICPERPRODUCTCOMMISSION
                val bundle =
                    bundleOf(PaymentMethodFragment.PAYMENTPUBLISH to Gson().toJson(hashMap))
                BasicTools.changeFragmentBack(
                    requireActivity(),
                    PaymentMethodFragment(),
                    "payment_pay",
                    bundle,
                    R.id.root_fragment_home
                )
            }
        BasicTools.setRecycleView(
            binding.rvProducts, adapterOneItem, LinearLayoutManager.VERTICAL,
            requireContext(), null, false
        )
        setViewModelObservers()
        return binding.root//inflater.inflate(R.layout.fragment_join_request_one_item, container, false)
    }

    /*    val callBackUpdateListWithPosition : (Int)->Unit = {
            if (adapterOneItem.arrayListOfImagessValues.isEmpty())
                binding.noResult.hideShow(View.VISIBLE)
            else
                binding.noResult.hideShow(View.GONE)

        }
    */
    fun setViewModelObservers() {
        model.getOffersDependOnStatus(checkCurrentFragmentInterface)
        model.offersOnReviewLiveData.observe(
            viewLifecycleOwner,
            Observer { // for sending to maintenance
                if (it != null) {
                    if (it.isNotEmpty()) {

                        binding.lyEmpty.visibility = (View.GONE)

                        adapterOneItem.clearProduct() // for now delete old data
                        adapterOneItem.updateList(it)

                    } else
                        binding.lyEmpty.visibility = (View.VISIBLE)

                }

            })
        /*   model.actionOrderLiveData.observe(requireActivity(), Observer{
               it?.let { pair->
                   callBackUpdateListWithPosition.invoke(pair.first) // remove item from list
                   BasicTools.showSnackMessages(requireActivity(),pair.second, R.color.green)
               }
           })*/

        model.networkLoader.observe(viewLifecycleOwner, Observer {
            it?.let { progress ->
                progress.setDialog(progressDialog) // open close principles
                model.setNetworkLoader(null)
            }
        })

        model.errorViewModel.observe(viewLifecycleOwner) {
            it?.let { error ->
                BasicTools.showSnackMessages(requireActivity(), error)

            }
        }
    }


}
