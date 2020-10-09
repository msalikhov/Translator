package com.msalikhov.dictionarysample.di.translation

import com.msalikhov.dictionarysample.presentation.view.translation.TranslationActivity
import dagger.Subcomponent

@Subcomponent(modules = [TranslationModule::class])
@TranslationScope
interface TranslationComponent {
    fun inject(activity: TranslationActivity)
}