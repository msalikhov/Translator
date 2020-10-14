package com.msalikhov.dictionarysample.di.app

import com.google.gson.Gson
import com.msalikhov.dictionarysample.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
object NetworkModule {
    private val logLevel get() = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

    @JvmStatic
    @Singleton
    @Provides
    fun getOkHttpClient() = OkHttpClient
        .Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
        .build()

    @JvmStatic
    @Singleton
    @Provides
    fun getGson() = Gson()
}