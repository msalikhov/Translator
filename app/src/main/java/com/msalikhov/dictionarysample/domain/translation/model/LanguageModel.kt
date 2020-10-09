package com.msalikhov.dictionarysample.domain.translation.model

import com.msalikhov.dictionarysample.utils.recycler.Diffable

data class LanguageModel(
    val code: String,
    val name: String,
    val isSelected: Boolean
): Diffable<LanguageModel> {
    override fun areItemsTheSame(item: LanguageModel) = code == item.code

    override fun areContentsTheSame(item: LanguageModel) = this == item
}