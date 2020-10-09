package com.msalikhov.dictionarysample.presentation.view.translation

import com.msalikhov.dictionarysample.domain.translation.model.LanguageModel
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface TranslationView : MvpView {
    @AddToEndSingle
    fun updateInputLanguage(name: String)

    @AddToEndSingle
    fun setInput(text: String)

    @AddToEndSingle
    fun displayTranslationResult(result: String)

    @AddToEndSingle
    fun displaySupportedLanguages(results: List<LanguageModel>)

    @OneExecution
    fun displayError(throwable: Throwable)

    @AddToEndSingle
    fun updateTranslationProgress(loading: Boolean)

    @AddToEndSingle
    fun updateSupportedLanguagesProgress(loading: Boolean)
}