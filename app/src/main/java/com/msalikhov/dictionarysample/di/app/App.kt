package com.msalikhov.dictionarysample.di.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.msalikhov.dictionarysample.di.translation.TranslationComponent

@SuppressLint("StaticFieldLeak")
private lateinit var context: Context

class App : Application() {

    init {
        context = this
    }
}

object ComponentHolder {
    private val appComponent by lazy {
        DaggerAppComponent.factory().create(context)
    }

    val translationComponent: TranslationComponent get() = appComponent.translationComponent
}