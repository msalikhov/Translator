package com.msalikhov.dictionarysample.domain.translation.model

data class TranslationModel(
    val text: String,
    val inputLangName: String,
    val inputLangCode: String
)