package com.msalikhov.dictionarysample.domain.translation

import com.msalikhov.dictionarysample.data.db.translation.models.FavouriteTranslationDBModel
import com.msalikhov.dictionarysample.data.repository.translation.TranslationRepository
import com.msalikhov.dictionarysample.domain.translation.model.TranslationHistoryModel
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class TranslationHistoryInteratorImpl @Inject constructor(
    private val translationRepository: TranslationRepository
) : TranslationHistoryInterator {

    override fun observeHistoryModels(): Observable<List<TranslationHistoryModel>> =
        translationRepository
            .observeTranslationHistoryModels()
            .map { list ->
                list.map {
                    TranslationHistoryModel(
                        translationId = it.translationId,
                        inputText = it.inputText,
                        outputText = it.outputText,
                        inputLangName = it.inputLangName,
                        outputLangName = it.outputLangName,
                        isFavourite = it.isFavourite
                    )
                }
            }

    override fun toggleFavouriteItem(model: TranslationHistoryModel): Completable =
        if (model.isFavourite) {
            translationRepository.removeTranslationFromFavourites(model.translationId)
        } else {
            translationRepository.addTranslationToFavourites(
                FavouriteTranslationDBModel(
                    translationId = model.translationId
                )
            )
        }
}