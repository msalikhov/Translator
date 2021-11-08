package com.msalikhov.dictionarysample.di.app

import android.content.Context
import androidx.room.Room
import com.msalikhov.dictionarysample.data.db.AppDatabase
import com.msalikhov.dictionarysample.data.db.translation.TranslationDAO
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {
    private const val DB_NAME = "app_database"

    @Provides
    @Singleton
    fun getAppDB(context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, DB_NAME)
        .build()

    @Provides
    fun getTranslationDAO(appDatabase: AppDatabase): TranslationDAO = appDatabase.translationDao
}