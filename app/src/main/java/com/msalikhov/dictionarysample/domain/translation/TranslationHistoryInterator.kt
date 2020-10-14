package com.msalikhov.dictionarysample.domain.translation

import com.msalikhov.dictionarysample.domain.translation.model.TranslationHistoryModel
import io.reactivex.Completable
import io.reactivex.Observable

interface TranslationHistoryInterator {
    fun observeHistoryModels(): Observable<List<TranslationHistoryModel>>
    fun toggleFavouriteItem(model: TranslationHistoryModel): Completable
}