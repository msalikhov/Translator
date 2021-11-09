package com.msalikhov.dictionarysample.di.translation

import com.msalikhov.dictionarysample.data.repository.translation.TranslationRepository
import com.msalikhov.dictionarysample.data.repository.translation.TranslationRepositoryImpl
import com.msalikhov.dictionarysample.domain.translation.TranslationHistoryInterator
import com.msalikhov.dictionarysample.domain.translation.TranslationHistoryInteratorImpl
import com.msalikhov.dictionarysample.domain.translation.TranslationInteractor
import com.msalikhov.dictionarysample.domain.translation.TranslationInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface TranslationModule {

    @Binds
    @TranslationScope
    fun TranslationRepositoryImpl.bindTranslationRepository(): TranslationRepository

    @Binds
    @TranslationScope
    fun TranslationHistoryInteratorImpl.bindTranslationHistoryInteractor(): TranslationHistoryInterator

    @Binds
    @TranslationScope
    fun TranslationInteractorImpl.bindTranslationInteractor(): TranslationInteractor
}