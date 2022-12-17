package com.example.currencyapp.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import com.urcloset.smartangle.api.ApiClient.getClient
import com.urcloset.smartangle.api.ApiClient.getClientJwt
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.tools.BasicTools
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
// this class to use custom retrofit buildr with differnt cases
 @Module
 @InstallIn(ViewModelComponent::class, FragmentComponent::class,ServiceComponent::class)
class NetworkModule {
    @Provides
    @ViewModelScoped
    fun getHomeDataSourceUser(@ApplicationContext context: Context?) : AppApi {
        return getClientJwt(BasicTools.getToken(context!!)?:"",BasicTools.getProtocol(context!!).toString())!!.create(
            AppApi::class.java
        )
    }

}