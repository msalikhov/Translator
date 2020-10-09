package com.msalikhov.dictionarysample

import com.msalikhov.dictionarysample.data.db.translation.models.SupportedLanguageDBModel
import com.msalikhov.dictionarysample.data.db.translation.models.TranslationDBModel
import com.msalikhov.dictionarysample.data.repository.translation.TranslationRepository
import com.msalikhov.dictionarysample.domain.translation.TranslationInteractor
import com.msalikhov.dictionarysample.domain.translation.model.LanguageModel
import com.msalikhov.dictionarysample.domain.translation.model.TranslationModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import org.junit.Assert
import org.junit.Test

class TranslationInteractorTest: Assert() {

    @Test
    fun testSupportedLanguages() {
        val dbModels = listOf(
            SupportedLanguageDBModel(
                0,
                "en",
                "eng"
            ),
            SupportedLanguageDBModel(
                1,
                "ru",
                "rus"
            )
        )
        val models = listOf(
            LanguageModel(
                "en",
                "eng",
                false
            ),
            LanguageModel(
                "ru",
                "rus",
                true
            )
        )
        val repo = mock<TranslationRepository> {
            on { getSupportedLanguages() } doReturn Single.just(dbModels)
        }

        TranslationInteractor(repo)
            .getSupportedLanguages()
            .test()
            .assertValue(models)
    }

    @Test
    fun testGetTranslation() {
        val translationDbModels = listOf(
            TranslationDBModel(
                0,
                "input",
                "output",
                "en",
                "ru"
            )
        )
        val langModels = listOf(
            LanguageModel(
                "en",
                "eng",
                false
            ),
            LanguageModel(
                "ru",
                "rus",
                true
            )
        )
        val translationModel = TranslationModel(
            "output",
            "eng",
            "en"
        )
        val repo = mock<TranslationRepository> {
            on { getTranslations(any(), any()) } doReturn Single.just(translationDbModels)
        }
        TranslationInteractor(repo)
            .translateText("input", langModels)
            .test()
            .assertValue(translationModel)
    }
}