package com.msalikhov.dictionarysample.di.translation

import com.msalikhov.dictionarysample.data.network.translation.yandex.service.YandexServiceTokenInterceptor
import com.msalikhov.dictionarysample.data.network.translation.yandex.service.YandexTranslationService
import com.msalikhov.dictionarysample.data.repository.translation.TranslationService
import dagger.Binds
import dagger.Module
import okhttp3.Interceptor

@Module
interface YandexTranslationServiceModule {
    @Binds
    @TranslationScope
    fun YandexTranslationService.bindTranslationService(): TranslationService

    @Binds
    @TranslationScope
    fun YandexServiceTokenInterceptor.bindTokenInterceptor(): Interceptor
}