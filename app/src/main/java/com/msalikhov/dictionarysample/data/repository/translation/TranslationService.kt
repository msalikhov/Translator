package com.msalikhov.dictionarysample.data.repository.translation

import com.msalikhov.dictionarysample.data.network.translation.common.models.Language
import com.msalikhov.dictionarysample.data.network.translation.common.models.Translation
import com.msalikhov.dictionarysample.data.network.translation.yandex.models.YandexSupportedLanguagesResponse
import com.msalikhov.dictionarysample.data.network.translation.yandex.models.YandexTranslationResponse
import io.reactivex.Single

interface TranslationService {
    fun getSupportedLanguages(): Single<List<Language>>
    fun getTranslation(texts: Array<out String>, targetLanguageCode: String): Single<List<Translation>>
}