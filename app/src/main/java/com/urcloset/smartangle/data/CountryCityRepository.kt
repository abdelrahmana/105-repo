package com.urcloset.smartangle.data

import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.project_105.CountryModel
import com.urcloset.smartangle.model.project_105.CountryWithCity
import com.urcloset.smartangle.tools.AppObservable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CountryCityRepository(val appApi: AppApi){
     fun getCity(countryId : String,
                 observerCity: AppObservable<CountryWithCity>,
                            // completion: (CountryWithCity?, String?) -> Unit
    ) {
        val observable = appApi.getCountryWithCity(countryId)//webService.postIgnoreOrder(hashMap)
         observable.subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribeWith(observerCity)
    }

    fun getCountry(
                observerCountry: AppObservable<CountryModel>,
        // completion: (CountryWithCity?, String?) -> Unit
    ) {
        val observable = appApi.getCountry()//webService.postIgnoreOrder(hashMap)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observerCountry)
    }
}