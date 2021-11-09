package com.msalikhov.dictionarysample.data.network.translation.yandex.service

import com.msalikhov.dictionarysample.data.network.translation.yandex.models.YandexSupportedLanguagesRequest
import com.msalikhov.dictionarysample.data.network.translation.yandex.models.YandexSupportedLanguagesResponse
import com.msalikhov.dictionarysample.data.network.translation.yandex.models.YandexTranslationRequest
import com.msalikhov.dictionarysample.data.network.translation.yandex.models.YandexTranslationResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface YandexTranslationApi {

    @POST("translate")
    fun getTranslation(
        @Body request: YandexTranslationRequest
    ): Single<YandexTranslationResponse>

    @POST("languages")
    fun getSupportedLanguages(
        @Body request: YandexSupportedLanguagesRequest
    ): Single<YandexSupportedLanguagesResponse>
}