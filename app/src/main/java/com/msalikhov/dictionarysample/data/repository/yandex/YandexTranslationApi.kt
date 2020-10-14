package com.msalikhov.dictionarysample.data.repository.yandex

import com.msalikhov.dictionarysample.data.network.translation.models.SupportedLanguagesRequest
import com.msalikhov.dictionarysample.data.network.translation.models.SupportedLanguagesResponse
import com.msalikhov.dictionarysample.data.network.translation.models.TranslationRequest
import com.msalikhov.dictionarysample.data.network.translation.models.TranslationResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface YandexTranslationApi {

    @POST("translate")
    fun getTranslation(
        @Body request: TranslationRequest
    ): Single<TranslationResponse>

    @POST("languages")
    fun getSupportedLanguages(
        @Body request: SupportedLanguagesRequest
    ): Single<SupportedLanguagesResponse>
}