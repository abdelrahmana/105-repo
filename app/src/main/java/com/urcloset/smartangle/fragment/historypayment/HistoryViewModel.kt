package com.urcloset.smartangle.fragment.historypayment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.data.repo.HomeRepoUser
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import com.urcloset.smartangle.R
import com.urcloset.smartangle.base.BaseViewModels
import com.urcloset.smartangle.data.model.paymenthistory.PaymentHistoryResponse
import com.urcloset.smartangle.tools.DissMissProgress
import com.urcloset.smartangle.tools.ShowProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class HistoryViewModel @Inject constructor(val repoDi : HomeRepoUser) : BaseViewModels() {

    private val _getPaymentHistory = MutableLiveData<PaymentHistoryResponse?>()
    val getHistory :LiveData<PaymentHistoryResponse?> = _getPaymentHistory

    fun getPaymentHistory(context : Context) {
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
          /*  orderListByStatus.getCurrentList(repoDi,hashMap
            )*/
           val res =  repoDi.getPaymentHistory()
            setNetworkLoader(DissMissProgress())
            res.onSuccess {
               // completion(data!! , null)
                _getPaymentHistory.value = data!!



            }
            res?.onException {
                setError(message.toString())
               // completion(null ,message.toString())


            }
            res?.onError {
                //completion(null,context.getString(R.string.error_happend)) // handle error from error body
                setError(context.getString(R.string.error_happend))

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