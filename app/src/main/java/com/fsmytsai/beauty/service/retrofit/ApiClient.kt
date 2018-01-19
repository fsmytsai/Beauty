package com.fsmytsai.beauty.service.retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient private constructor() {
    private val mRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://laravelcommunity.azurewebsites.net/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    companion object {
        val instance: ApiClient by lazy { ApiClient() }
    }

    fun getServer(): ApiStores {
        return mRetrofit.create(ApiStores::class.java)
    }
}