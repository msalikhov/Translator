package com.msalikhov.dictionarysample.data.repository.yandex

import com.google.gson.Gson
import com.msalikhov.dictionarysample.BuildConfig
import com.msalikhov.dictionarysample.data.network.translation.models.SupportedLanguagesRequest
import com.msalikhov.dictionarysample.data.network.translation.models.SupportedLanguagesResponse
import com.msalikhov.dictionarysample.data.network.translation.models.TranslationRequest
import com.msalikhov.dictionarysample.data.network.translation.models.TranslationResponse
import com.msalikhov.dictionarysample.data.repository.translation.TranslationService
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject

private const val TRANSLATE_BASE_URL = "https://translate.api.cloud.yandex.net/translate/v2/"

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

    override fun getSupportedLanguages(): Single<SupportedLanguagesResponse> =
        translationApi.getSupportedLanguages(
            SupportedLanguagesRequest(BuildConfig.YANDEX_FOLDER_ID)
        )

    override fun getTranslation(
        texts: Array<out String>,
        targetLanguageCode: String
    ): Single<TranslationResponse> = translationApi.getTranslation(
        TranslationRequest(BuildConfig.YANDEX_FOLDER_ID, texts, targetLanguageCode)
    )
}