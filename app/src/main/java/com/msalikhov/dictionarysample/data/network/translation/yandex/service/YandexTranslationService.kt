package com.msalikhov.dictionarysample.data.network.translation.yandex.service

import com.google.gson.Gson
import com.msalikhov.dictionarysample.BuildConfig
import com.msalikhov.dictionarysample.data.network.translation.common.models.Language
import com.msalikhov.dictionarysample.data.network.translation.common.models.Translation
import com.msalikhov.dictionarysample.data.network.translation.yandex.models.YandexSupportedLanguagesRequest
import com.msalikhov.dictionarysample.data.network.translation.yandex.models.YandexTranslationRequest
import com.msalikhov.dictionarysample.data.repository.translation.TranslationService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject


class YandexTranslationService @Inject constructor(
    okHttpClient: OkHttpClient,
    interceptor: Interceptor,
    gson: Gson
) : TranslationService {

    private val translationApi by lazy {
        Retrofit
            .Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(
                okHttpClient
                    .newBuilder()
                    .addInterceptor(interceptor)
                    .build()
            )
            .baseUrl(TRANSLATE_BASE_URL)
            .build()
            .create<YandexTranslationApi>()
    }

    override fun getSupportedLanguages() =
        translationApi
            .getSupportedLanguages(YandexSupportedLanguagesRequest(BuildConfig.YANDEX_FOLDER_ID))
            .map {
                it.languages.map { lang ->
                    Language(code = lang.code, name = lang.name)
                }
            }

    override fun getTranslation(texts: Array<out String>, targetLanguageCode: String) =
        translationApi
            .getTranslation(YandexTranslationRequest(BuildConfig.YANDEX_FOLDER_ID, texts, targetLanguageCode))
            .map {
                it.translations.map { trans ->
                    Translation(text = trans.text, detectedLanguageCode = trans.detectedLanguageCode)
                }
            }

    private companion object {
        const val TRANSLATE_BASE_URL = "https://translate.api.cloud.yandex.net/translate/v2/"
    }
}