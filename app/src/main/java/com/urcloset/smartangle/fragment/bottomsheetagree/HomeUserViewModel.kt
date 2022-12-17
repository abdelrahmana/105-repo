package com.example.currencyapp.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.currencyapp.data.repo.HomeRepoUser
import com.urcloset.smartangle.data.model.agreement_terms.AgreementResponseTerms
import com.urcloset.smartangle.tools.DissMissProgress
import com.urcloset.smartangle.tools.ProgressDialog
import com.urcloset.smartangle.tools.ShowProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import droidninja.filepicker.viewmodels.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
open class HomeUserViewModel @Inject constructor(val repoDi : HomeRepoUser) : ViewModel() {
    private val responseItem = MutableLiveData<ResponseBody?>() // when post new update
    val responseCreationLiveData :LiveData<ResponseBody?> = responseItem
    private val _responseAgreements = MutableLiveData<AgreementResponseTerms?>()
    val responseAgreements :LiveData<AgreementResponseTerms?> = _responseAgreements

    private val _networkLoader = MutableLiveData<ProgressDialog?>()
    val networkLoader :LiveData<ProgressDialog?> = _networkLoader
    private val _errorViewModel = MutableLiveData<String?>()
    val errorViewModel :LiveData<String?> = _errorViewModel
    fun setError(error : String?) {
        _errorViewModel.postValue(error)
    }
    fun setNetworkLoader(loader : ProgressDialog?) {
        _networkLoader.postValue(loader)
    }
    fun postAcceptPolicy(hashMap: HashMap<String,Int>
    ) {
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
            repoDi.postAgreeConditions(hashMap){ response, errors ->
                response?.let { it ->
                    responseItem.value =it
                }
                errors?.let { it ->
                    setError(it)
                }
                setNetworkLoader(DissMissProgress())

            }

        }
    }

    fun getPolicyText() {
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
            repoDi.getAgreeCondition{ response, errors ->
                response?.let { it ->
                    _responseAgreements.value =it
                }
                errors?.let { it ->
                    setError(it)
                }
                setNetworkLoader(DissMissProgress())

            }

        }
    }

    fun clearLiveDataOnActivity(removeOnActivity: LifecycleOwner){
        errorViewModel.removeObservers(removeOnActivity)
        setError(null)
        networkLoader.removeObservers(removeOnActivity)
        setNetworkLoader(null)
        responseItem.removeObservers(removeOnActivity)
        responseAgreements.removeObservers(removeOnActivity)

    }
}