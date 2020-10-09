package com.msalikhov.dictionarysample.di.app

import com.google.gson.Gson
import com.msalikhov.dictionarysample.BuildConfig
import com.msalikhov.dictionarysample.data.network.NetworkApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
object NetworkModule {
    private const val LOCALHOST_URL = "http://localhost/"
    private val logLevel get() = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

    @JvmStatic
    @Singleton
    @Provides
    fun getNetworkApi() : NetworkApi = Retrofit
        .Builder()
        .baseUrl(LOCALHOST_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .client(getOkHttpClient())
        .build()
        .create()

    private fun getOkHttpClient() = OkHttpClient
        .Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
        .build()
}