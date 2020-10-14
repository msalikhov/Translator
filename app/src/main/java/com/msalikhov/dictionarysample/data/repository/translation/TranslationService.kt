package com.msalikhov.dictionarysample.data.repository.translation

import com.msalikhov.dictionarysample.data.network.translation.models.SupportedLanguagesResponse
import com.msalikhov.dictionarysample.data.network.translation.models.TranslationResponse
import io.reactivex.Single

interface TranslationService {
    fun getSupportedLanguages(): Single<SupportedLanguagesResponse>
    fun getTranslation(
        texts: Array<out String>,
        targetLanguageCode: String
    ): Single<TranslationResponse>
}