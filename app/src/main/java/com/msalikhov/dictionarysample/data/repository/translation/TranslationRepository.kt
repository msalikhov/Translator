package com.msalikhov.dictionarysample.data.repository.translation

import com.msalikhov.dictionarysample.data.db.translation.models.FavouriteTranslationDBModel
import com.msalikhov.dictionarysample.data.db.translation.models.SupportedLanguageDBModel
import com.msalikhov.dictionarysample.data.db.translation.models.TranslationDBModel
import com.msalikhov.dictionarysample.data.db.translation.models.TranslationHistoryDBModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface TranslationRepository {
    fun getSupportedLanguages(): Single<List<SupportedLanguageDBModel>>
    fun getTranslations(targetLanguageCode: String, texts: Array<out String>): Single<List<TranslationDBModel>>

    fun observeTranslationHistoryModels(): Observable<List<TranslationHistoryDBModel>>
    fun addTranslationToFavourites(favourite: FavouriteTranslationDBModel): Completable
    fun removeTranslationFromFavourites(translationId: Int): Completable
}