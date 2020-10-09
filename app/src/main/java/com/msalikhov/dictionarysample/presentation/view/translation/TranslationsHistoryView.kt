package com.msalikhov.dictionarysample.presentation.view.translation

import com.msalikhov.dictionarysample.domain.translation.model.TranslationHistoryModel
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface TranslationsHistoryView: MvpView {
    @AddToEndSingle
    fun displayTranslationHistoryItems(items: List<TranslationHistoryModel>)

    @OneExecution
    fun displayError(throwable: Throwable)

    @AddToEndSingle
    fun updateProgress(loading: Boolean)
}