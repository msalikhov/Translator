package com.msalikhov.dictionarysample.domain.translation

import com.msalikhov.dictionarysample.data.repository.translation.TranslationRepository
import com.msalikhov.dictionarysample.domain.translation.model.LanguageModel
import com.msalikhov.dictionarysample.domain.translation.model.TranslationModel
import io.reactivex.Single
import javax.inject.Inject

private const val DEFAULT_LANG = "ru"

class TranslationInteractor @Inject constructor(
    private val translationRepository: TranslationRepository
) {
    fun getSupportedLanguages(): Single<List<LanguageModel>> = translationRepository
        .getSupportedLanguages()
        .map { languages ->
            languages.map { (_, code, name) ->
                LanguageModel(code, name, code == DEFAULT_LANG)
            }
        }

    fun translateText(text: String, languageModels: List<LanguageModel>): Single<TranslationModel> {
        if (text.isBlank()) return Single.just(TranslationModel("", "", ""))

        val selectedModel = languageModels.firstOrNull { it.isSelected }
            ?: languageModels.firstOrNull()
        val selectedLangCode = selectedModel?.code ?: DEFAULT_LANG

        return translationRepository
            .getTranslations(selectedLangCode, text)
            .map {
                val translation = it.first()
                val inputLangModel = languageModels.firstOrNull { it.code == translation.inputLangCode }
                TranslationModel(translation.outputText, inputLangModel?.name.orEmpty(), translation.inputLangCode)
            }
    }
}