package com.msalikhov.dictionarysample.data.repository.translation

import com.msalikhov.dictionarysample.BuildConfig
import com.msalikhov.dictionarysample.data.db.translation.TranslationDAO
import com.msalikhov.dictionarysample.data.db.translation.models.FavouriteTranslationDBModel
import com.msalikhov.dictionarysample.data.db.translation.models.SupportedLanguageDBModel
import com.msalikhov.dictionarysample.data.db.translation.models.TranslationDBModel
import com.msalikhov.dictionarysample.data.network.NetworkApi
import com.msalikhov.dictionarysample.data.network.translation.models.SupportedLanguagesRequest
import com.msalikhov.dictionarysample.data.network.translation.models.TranslationRequest
import com.msalikhov.dictionarysample.data.repository.token.TokenRepository
import com.msalikhov.dictionarysample.utils.CacheHelper
import io.reactivex.Single
import javax.inject.Inject

class TranslationRepositoryImpl @Inject constructor(
    private val networkApi: NetworkApi,
    private val translationDAO: TranslationDAO,
    private val tokenRepository: TokenRepository
) : TranslationRepository {

    override fun getSupportedLanguages(): Single<List<SupportedLanguageDBModel>> =
        CacheHelper.dbOrNetwork(
            translationDAO.getSupportedLanguages(),
            tokenRepository.iamToken.flatMap {
                networkApi.getSupportedLanguages(
                    SupportedLanguagesRequest(BuildConfig.YANDEX_FOLDER_ID),
                    it
                )
            },
            { data -> data.isEmpty() },
            { response ->
                translationDAO.addSupportedLanguages(response.languages.map {
                    SupportedLanguageDBModel(
                        languageCode = it.code,
                        languageName = it.name ?: it.code
                    )
                })
            }
        )

    override fun getTranslations(
        targetLanguageCode: String,
        vararg texts: String
    ): Single<List<TranslationDBModel>> =
        CacheHelper.dbOrNetwork(
            translationDAO.findTranslations(targetLanguageCode, *texts),
            tokenRepository.iamToken.flatMap {
                networkApi.getTranslation(
                    TranslationRequest(
                        BuildConfig.YANDEX_FOLDER_ID,
                        texts,
                        targetLanguageCode
                    ), it
                )
            },
            { data -> data.isEmpty() },
            { response ->
                translationDAO.addTranslations(response.translations.mapIndexed { index, translation ->
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