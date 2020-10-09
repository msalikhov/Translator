package com.msalikhov.dictionarysample.di.app

import android.app.Application
import android.content.Context

private lateinit var context: Context

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}

object ComponentHolder {
    private val appComponent by lazy {
        DaggerAppComponent
            .builder()
            .appContext(context)
            .build()
    }

    val translationComponent get() = appComponent.translationComponent
}