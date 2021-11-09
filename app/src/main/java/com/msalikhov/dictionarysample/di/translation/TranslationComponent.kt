package com.msalikhov.dictionarysample.di.translation

import com.msalikhov.dictionarysample.presentation.translation.viewmodel.TranslationHistoryViewModel
import com.msalikhov.dictionarysample.presentation.translation.viewmodel.TranslationViewModel
import dagger.Subcomponent

@Subcomponent(modules = [TranslationModule::class, SkyEngTranslationServiceModule::class])
@TranslationScope
interface TranslationComponent {
    val translationViewModelFactory: TranslationViewModel.Factory
    val translationsHistoryViewModelFactory: TranslationHistoryViewModel.Factory
}