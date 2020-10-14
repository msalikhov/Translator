package com.msalikhov.dictionarysample.domain.translation

import com.msalikhov.dictionarysample.domain.translation.model.LanguageModel
import com.msalikhov.dictionarysample.domain.translation.model.TranslationModel
import io.reactivex.Single

interface TranslationInteractor {
    fun getSupportedLanguages(): Single<List<LanguageModel>>
    fun translateText(text: String, languageModels: List<LanguageModel>): Single<TranslationModel>
}