package com.msalikhov.dictionarysample.domain.translation.model

import com.msalikhov.dictionarysample.utils.recycler.Diffable

data class TranslationHistoryModel(
    val translationId: Int,
    val inputText: String,
    val outputText: String,
    val inputLangName: String,
    val outputLangName: String,
    val isFavourite: Boolean
): Diffable<TranslationHistoryModel> {
    override fun areItemsTheSame(item: TranslationHistoryModel) = translationId == item.translationId

    override fun areContentsTheSame(item: TranslationHistoryModel) = this == item
}