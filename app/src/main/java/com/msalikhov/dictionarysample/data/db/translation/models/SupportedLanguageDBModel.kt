package com.msalikhov.dictionarysample.data.db.translation.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SupportedLanguageDBModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "lang_code") val languageCode: String,
    @ColumnInfo(name = "lang_name") val languageName: String
)