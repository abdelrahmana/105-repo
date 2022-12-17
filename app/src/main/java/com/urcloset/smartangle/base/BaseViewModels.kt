package com.urcloset.smartangle.base

import android.location.Location
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.urcloset.smartangle.tools.ProgressDialog

open class BaseViewModels : ViewModel() {
    private val _errorViewModel = MutableLiveData<String?>()
    val errorViewModel :LiveData<String?> = _errorViewModel
    private val _networkLoader = MutableLiveData<ProgressDialog?>()
    val networkLoader :LiveData<ProgressDialog?> = _networkLoader

    fun setNetworkLoader(loader : ProgressDialog?) {
        _networkLoader.postValue(loader)
    }
    fun setError(error : String?) {
        _errorViewModel.postValue(error)
    }

}