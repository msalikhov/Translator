package com.msalikhov.dictionarysample.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.msalikhov.dictionarysample.data.db.translation.TranslationDAO
import com.msalikhov.dictionarysample.data.db.translation.models.FavouriteTranslationDBModel
import com.msalikhov.dictionarysample.data.db.translation.models.SupportedLanguageDBModel
import com.msalikhov.dictionarysample.data.db.translation.models.TranslationDBModel

@Database(
    entities = [
        TranslationDBModel::class,
        FavouriteTranslationDBModel::class,
        SupportedLanguageDBModel::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val translationDao: TranslationDAO
}