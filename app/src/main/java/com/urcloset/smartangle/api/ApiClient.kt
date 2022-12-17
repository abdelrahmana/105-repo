package com.urcloset.smartangle.api


import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import com.urcloset.smartangle.BuildConfig
import com.urcloset.smartangle.model.User
import com.urcloset.smartangle.tools.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException


object ApiClient {

    private var retrofit: Retrofit? = null
    private var retrofitEncode: Retrofit? = null
    private var last_user: User? = null
    private var authenticated = true
    private var authenticatedEncode = true

    fun getClient(protocol: String?, lang: String = "en"): Retrofit? {
        if (retrofit == null || authenticated) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder().ignoreAllSSLErrors()
            httpClient.addInterceptor(interceptor)
            httpClient.readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)

            httpClient.addInterceptor { chain ->
                //last_user = user
                val original = chain.request()
                val request: Request
                //todo
//                if (user.isSocial()) {
//                    request = original.newBuilder()
//                        .header("Accept", "application/json")
//                        .header("ASTRO-AUTH-KEY", user.getKey())
//                        .method(original.method(), original.body())
//                        .build()
//                } else {

                request = original.newBuilder()
                    .header("Accept", "application/ld+json")
                    .header("Language", lang)
                    .method(original.method, original.body)
                    .build()
//                }

                chain.proceed(request)
            }
            retrofit = Retrofit.Builder()
                .baseUrl(protocol + Constants.api_url + "/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
            authenticated = false
        }
        return retrofit
    }

    fun getClientMap(): Retrofit? {
        if (retrofitEncode == null || authenticatedEncode) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(interceptor)
            retrofitEncode = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/geocode/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
            authenticatedEncode = false
        }
        return retrofitEncode
    }

    fun getClientJwt(token: String, protocol: String, lang: String = "en"): Retrofit? {
        if (retrofit == null || !authenticated ) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder().ignoreAllSSLErrors()
            httpClient.addInterceptor(interceptor)
            httpClient.readTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(3, TimeUnit.MINUTES)
            httpClient.addInterceptor { chain ->
                //last_user = user
                val original = chain.request()
                val request: Request
                //todo
//                if (user.isSocial()) {
//                    request = original.newBuilder()
//                        .header("Accept", "application/json")
//                        .header("ASTRO-AUTH-KEY", user.getKey())
//                        .method(original.method(), original.body())
//                        .build()
//                } else {

                    request = original.newBuilder()
                        .header("Accept", "application/ld+json")
                        .header("Authorization", "Bearer " + token)
                        .header("Language", lang)

                        .method(original.method, original.body)
                        .build()
//                }

                chain.proceed(request)
            }
            retrofit = Retrofit.Builder()
                .baseUrl(protocol + Constants.api_url + "/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
                .client(httpClient.build())
                .build()
        }
        authenticated = true
        return retrofit
    }
    fun OkHttpClient.Builder.ignoreAllSSLErrors(): OkHttpClient.Builder {
        val naiveTrustManager = object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
        }

        val insecureSocketFactory = SSLContext.getInstance("TLSv1.2").apply {
            val trustAllCerts = arrayOf<TrustManager>(naiveTrustManager)
            init(null, trustAllCerts, SecureRandom())
        }.socketFactory

        sslSocketFactory(insecureSocketFactory, naiveTrustManager)
        hostnameVerifier(HostnameVerifier { _, _ -> true })
        return this
    }




}