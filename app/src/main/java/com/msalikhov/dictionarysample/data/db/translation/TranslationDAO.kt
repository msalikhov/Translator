package com.msalikhov.dictionarysample.data.db.translation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.msalikhov.dictionarysample.data.db.translation.models.FavouriteTranslationDBModel
import com.msalikhov.dictionarysample.data.db.translation.models.SupportedLanguageDBModel
import com.msalikhov.dictionarysample.data.db.translation.models.TranslationDBModel
import com.msalikhov.dictionarysample.data.db.translation.models.TranslationHistoryDBModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface TranslationDAO {
    @Query("SELECT * FROM SupportedLanguageDBModel")
    fun getSupportedLanguages(): Single<List<SupportedLanguageDBModel>>

    @Query("SELECT * FROM TranslationDBModel")
    fun getAllTranslations(): Single<List<TranslationDBModel>>

    @Query("SELECT * FROM FavouriteTranslationDBModel")
    fun observeFavouriteTranslations(): Observable<List<FavouriteTranslationDBModel>>

    @Query("""SELECT 
TranslationDBModel.id AS translation_id, 
TranslationDBModel.input_text, 
TranslationDBModel.output_text,
(SELECT SupportedLanguageDBModel.lang_name FROM SupportedLanguageDBModel WHERE SupportedLanguageDBModel.lang_code = TranslationDBModel.input_lang_code) AS input_lang_name,
(SELECT SupportedLanguageDBModel.lang_name FROM SupportedLanguageDBModel WHERE SupportedLanguageDBModel.lang_code = TranslationDBModel.output_lang_code) AS output_lang_name,
EXISTS(SELECT FavouriteTranslationDBModel.translation_id FROM FavouriteTranslationDBModel WHERE FavouriteTranslationDBModel.translation_id = TranslationDBModel.id) AS is_favourite
FROM
TranslationDBModel
ORDER BY
is_favourite DESC,
input_lang_name ASC,
input_text ASC""")
    fun observeTranslationHistoryModels(): Observable<List<TranslationHistoryDBModel>>

    @Query("SELECT * FROM TranslationDBModel WHERE input_text IN (:texts) AND output_lang_code LIKE :targetLangCode")
    fun findTranslations(targetLangCode: String, vararg texts: String): Single<List<TranslationDBModel>>

    @Insert
    fun addTranslations(translations: List<TranslationDBModel>): Completable

    @Insert
    fun addTranslationToFavourites(favourite: FavouriteTranslationDBModel): Completable

    @Insert
    fun addSupportedLanguages(languages: List<SupportedLanguageDBModel>): Completable

    @Query("DELETE FROM FavouriteTranslationDBModel WHERE translation_id = :translationId")
    fun removeTranslationFromFavourites(translationId: Int): Completable
}