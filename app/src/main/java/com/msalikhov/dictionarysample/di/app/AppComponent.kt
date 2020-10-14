package com.msalikhov.dictionarysample.di.app

import android.content.Context
import com.msalikhov.dictionarysample.di.translation.TranslationComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, DatabaseModule::class])
@Singleton
interface AppComponent {
    val translationComponent: TranslationComponent

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContext(context: Context): Builder

        fun build(): AppComponent
    }
}