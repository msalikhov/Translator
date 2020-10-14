package com.msalikhov.dictionarysample

import com.msalikhov.dictionarysample.data.db.translation.models.TranslationHistoryDBModel
import com.msalikhov.dictionarysample.data.repository.translation.TranslationRepository
import com.msalikhov.dictionarysample.domain.translation.TranslationHistoryInteratorImpl
import com.msalikhov.dictionarysample.domain.translation.model.TranslationHistoryModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Test

class TranslationHistoryInteractorTest : Assert() {
    @Test
    fun testAddToFavourite() {
        val completable = Completable.complete()
        val repo = mock<TranslationRepository> {
            on { addTranslationToFavourites(any()) } doReturn completable
        }
        val model = TranslationHistoryModel(
            0,
            "," +
                    "",
            "",
            "",
            "",
            false
        )
        assertTrue(TranslationHistoryInteratorImpl(repo).toggleFavouriteItem(model) === completable)
    }

    @Test
    fun testRemoveFromFavourite() {
        val completable = Completable.complete()
        val repo = mock<TranslationRepository> {
            on { removeTranslationFromFavourites(any()) } doReturn completable
        }
        val model = TranslationHistoryModel(
            0,
            "," +
                    "",
            "",
            "",
            "",
            true
        )
        assertTrue(TranslationHistoryInteratorImpl(repo).toggleFavouriteItem(model) === completable)
    }

    @Test
    fun testHistoryModelsObserve() {
        val historyDBModelsNoFavourites = listOf(
            TranslationHistoryDBModel(
                0,
                "input",
                "output",
                "eng",
                "rus",
                false
            )
        )
        val historyDBModelsWithFavourites = listOf(
            TranslationHistoryDBModel(
                0,
                "input",
                "output",
                "eng",
                "rus",
                true
            )
        )
        val historyModelsNoFavourites = listOf(
            TranslationHistoryModel(
                0,
                "input",
                "output",
                "eng",
                "rus",
                false
            )
        )
        val historyModelsWithFavourites = listOf(
            TranslationHistoryModel(
                0,
                "input",
                "output",
                "eng",
                "rus",
                true
            )
        )
        val repo = mock<TranslationRepository> {
            on { observeTranslationHistoryModels() } doReturn Observable.just(historyDBModelsNoFavourites, historyDBModelsWithFavourites)
        }
        TranslationHistoryInteratorImpl(repo)
            .observeHistoryModels()
            .test()
            .assertValues(historyModelsNoFavourites, historyModelsWithFavourites)
    }
}