package com.msalikhov.dictionarysample.data.db.translation.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TranslationDBModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "input_text") val inputText: String,
    @ColumnInfo(name = "output_text") val outputText: String,
    @ColumnInfo(name = "input_lang_code") val inputLangCode: String,
    @ColumnInfo(name = "output_lang_code") val outputLangCode: String
)