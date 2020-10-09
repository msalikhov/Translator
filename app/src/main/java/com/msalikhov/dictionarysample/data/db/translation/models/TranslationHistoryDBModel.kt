package com.msalikhov.dictionarysample.data.db.translation.models

import androidx.room.ColumnInfo

data class TranslationHistoryDBModel(
    @ColumnInfo(name = "translation_id") val translationId: Int,
    @ColumnInfo(name = "input_text") val inputText: String,
    @ColumnInfo(name = "output_text") val outputText: String,
    @ColumnInfo(name = "input_lang_name") val inputLangName: String,
    @ColumnInfo(name = "output_lang_name") val outputLangName: String,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean
)