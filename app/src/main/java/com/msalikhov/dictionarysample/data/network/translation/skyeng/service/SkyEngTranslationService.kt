package com.msalikhov.dictionarysample.data.network.translation.skyeng.service

import com.google.gson.Gson
import com.msalikhov.dictionarysample.data.network.translation.common.models.Language
import com.msalikhov.dictionarysample.data.network.translation.common.models.Translation
import com.msalikhov.dictionarysample.data.repository.translation.TranslationService
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject

class SkyEngTranslationService @Inject constructor(
    okHttpClient: OkHttpClient,
    gson: Gson
) : TranslationService {

    private val translationApi by lazy {
        Retrofit
            .Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl(TRANSLATE_BASE_URL)
            .build()
            .create<SkyEngTranslationApi>()
    }

    override fun getSupportedLanguages() =
        Single.just(
            listOf(
                Language("en", "English"),
                Language("ru", "Russian")
            )
        )

    override fun getTranslation(texts: Array<out String>, targetLanguageCode: String) =
        Observable
            .fromArray(*texts)
            .flatMapSingle { translationApi.translateWord(it) }
            .filter { it.isNotEmpty() }
            .map {
                val translation = it.first()
                val detectedLanguageCode:String
                val text: String
                when (targetLanguageCode) {
                    "ru" -> {
                        detectedLanguageCode = "en"
                        text = translation.meanings.first().translation.text
                    }
                    "en" -> {
                        detectedLanguageCode = "ru"
                        text = translation.text
                    }
                    else -> throw IllegalArgumentException("Unsupported targetLanguageCode: $targetLanguageCode")
                }
                Translation(text = text, detectedLanguageCode = detectedLanguageCode)
            }
            .toList()

    private companion object {
        const val TRANSLATE_BASE_URL = "https://dictionary.skyeng.ru/api/public/v1/"
    }
}