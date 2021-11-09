package com.msalikhov.dictionarysample.di.translation

import com.msalikhov.dictionarysample.data.network.translation.skyeng.service.SkyEngTranslationService
import com.msalikhov.dictionarysample.data.repository.translation.TranslationService
import dagger.Binds
import dagger.Module

@Module
interface SkyEngTranslationServiceModule {
    @Binds
    @TranslationScope
    fun SkyEngTranslationService.bindTranslationService(): TranslationService
}