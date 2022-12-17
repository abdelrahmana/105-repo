package com.urcloset.smartangle.fragment.paymentmethod

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.data.repo.HomeRepoUser
import com.urcloset.smartangle.base.BaseViewModels
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.DissMissProgress
import com.urcloset.smartangle.tools.ShowProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
open class PaymentViewModel @Inject constructor(val repoDi : HomeRepoUser) : BaseViewModels() {

    private val _postPayment = MutableLiveData<ResponseBody?>()
    val postPayment :LiveData<ResponseBody?> = _postPayment

    fun postPaymentAfterPay(hashMap: HashMap<String,Any>?) {
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
          /*  orderListByStatus.getCurrentList(repoDi,hashMap
            )*/
            repoDi.postPaymentMethod(hashMap){ response, errors ->
                response?.let { it ->
                   // offersOnTheWay.value = it as ArrayList
                  //  orderListByStatus.getLiveData().value = it as ArrayList
                    _postPayment.value = response
                }
                errors?.let { it ->
                    setError(it)
                }
                setNetworkLoader(DissMissProgress())

            }

        }
    }
    fun clearLiveDataOnFragment(removeOnFragment: Fragment){
        // errorViewModel.removeObservers(removeOnActivity)
        setError(null)
        networkLoader.removeObservers(removeOnFragment)
        setNetworkLoader(null)


    }

}