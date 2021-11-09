package com.msalikhov.dictionarysample.data.repository.translation

import com.msalikhov.dictionarysample.data.db.translation.TranslationDAO
import com.msalikhov.dictionarysample.data.db.translation.models.FavouriteTranslationDBModel
import com.msalikhov.dictionarysample.data.db.translation.models.SupportedLanguageDBModel
import com.msalikhov.dictionarysample.data.db.translation.models.TranslationDBModel
import com.msalikhov.dictionarysample.utils.CacheHelper
import io.reactivex.Single
import javax.inject.Inject

class TranslationRepositoryImpl @Inject constructor(
    private val translationDAO: TranslationDAO,
    private val translationService: TranslationService
) : TranslationRepository {

    override fun getSupportedLanguages(): Single<List<SupportedLanguageDBModel>> =
        CacheHelper.dbOrNetwork(
            dbSingleProvider = translationDAO.getSupportedLanguages(),
            networkSingleProvider = translationService.getSupportedLanguages(),
            getFromNetworkCondition = { data -> data.isEmpty() },
            dbWriteProvider = { languages ->
                translationDAO.addSupportedLanguages(languages.map {
                    SupportedLanguageDBModel(
                        languageCode = it.code,
                        languageName = it.name ?: it.code
                    )
                })
            }
        )

    override fun getTranslations(
        targetLanguageCode: String,
        texts: Array<out String>
    ): Single<List<TranslationDBModel>> =
        CacheHelper.dbOrNetwork(
            dbSingleProvider = translationDAO.findTranslations(targetLanguageCode, texts),
            networkSingleProvider = translationService.getTranslation(texts, targetLanguageCode),
            getFromNetworkCondition = { data -> data.isEmpty() },
            dbWriteProvider = { translations ->
                translationDAO.addTranslations(translations.mapIndexed { index, translation ->
                    TranslationDBModel(
                        inputText = texts[index],
                        inputLangCode = translation.detectedLanguageCode,
                        outputText = translation.text,
                        outputLangCode = targetLanguageCode
                    )
                })
            }
        )

    override fun observeTranslationHistoryModels() =
        translationDAO.observeTranslationHistoryModels()

    override fun addTranslationToFavourites(favourite: FavouriteTranslationDBModel) =
        translationDAO.addTranslationToFavourites(favourite)

    override fun removeTranslationFromFavourites(translationId: Int) =
        translationDAO.removeTranslationFromFavourites(translationId)
}