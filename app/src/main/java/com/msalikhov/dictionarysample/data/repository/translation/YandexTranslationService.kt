package com.msalikhov.dictionarysample.data.repository.translation

import android.content.Context
import com.msalikhov.dictionarysample.BuildConfig
import com.msalikhov.dictionarysample.data.network.token.models.IAMTokenRequest
import com.msalikhov.dictionarysample.data.network.token.models.IAMTokenResponse
import com.msalikhov.dictionarysample.data.network.translation.models.SupportedLanguagesRequest
import com.msalikhov.dictionarysample.data.network.translation.models.SupportedLanguagesResponse
import com.msalikhov.dictionarysample.data.network.translation.models.TranslationRequest
import com.msalikhov.dictionarysample.data.network.translation.models.TranslationResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.Executors
import javax.inject.Inject

private const val STORE_NAME = "tokenStore"
private const val IAM_TOKEN_VALUE_KEY = "iam_token_key"
private const val IAM_TOKEN_EXPIRATION_KEY = "iam_token_expiration_key"
private const val TOKEN_BASE_URL = "https://iam.api.cloud.yandex.net/iam/v1/"
private const val TRANSLATE_BASE_URL = "https://translate.api.cloud.yandex.net/translate/v2/"

class YandexTranslationService @Inject constructor(okHttpClient: OkHttpClient, context: Context) :
    TranslationService {

    val callAdapterFactory: CallAdapter.Factory = RxJava2CallAdapterFactory.create()
    val converterFactory: Converter.Factory = GsonConverterFactory.create()

    private val tokenApi = Retrofit
        .Builder()
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .client(okHttpClient)
        .baseUrl(TOKEN_BASE_URL)
        .build()
        .create<YandexTokenApi>()

    private val translationApi = Retrofit
        .Builder()
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .client(okHttpClient)
        .baseUrl(TRANSLATE_BASE_URL)
        .build()
        .create<YandexTranslationApi>()

    private val preferences = context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()

    private var localIAMTokenValue: String
        get() = preferences.getString(IAM_TOKEN_VALUE_KEY, null).orEmpty()
        set(value) = preferences.edit().putString(IAM_TOKEN_VALUE_KEY, value).apply()

    private var localIAMTokenExpiration: Long
        get() = preferences.getLong(IAM_TOKEN_EXPIRATION_KEY, 0L)
        set(value) = preferences.edit().putLong(IAM_TOKEN_EXPIRATION_KEY, value).apply()

    private val localIAMTokenIfNotExpired: String?
        get() = localIAMTokenValue.takeIf { it.isNotBlank() && localIAMTokenExpiration > System.currentTimeMillis() }

    private val iamToken: Single<String>
        get() = Single
            .fromCallable { localIAMTokenIfNotExpired }
            .onErrorResumeNext(tokenApi
                .getIAMToken(IAMTokenRequest(BuildConfig.YANDEX_OAUTH_TOKEN))
                .map { (token, expiration) ->
                    localIAMTokenValue = token
                    localIAMTokenExpiration = expiration.time
                    token
                }
            )
            .map { "Bearer $it" }
            .subscribeOn(Schedulers.from(singleThreadExecutor))

    override fun getSupportedLanguages(): Single<SupportedLanguagesResponse> = iamToken.flatMap {
        translationApi.getSupportedLanguages(
            SupportedLanguagesRequest(BuildConfig.YANDEX_FOLDER_ID), it
        )
    }

    override fun getTranslation(
        texts: Array<out String>,
        targetLanguageCode: String
    ): Single<TranslationResponse> = iamToken.flatMap {
        translationApi.getTranslation(
            TranslationRequest(
                BuildConfig.YANDEX_FOLDER_ID,
                texts,
                targetLanguageCode
            ), it
        )
    }
}

private interface YandexTokenApi {

    @POST("tokens")
    fun getIAMToken(@Body request: IAMTokenRequest): Single<IAMTokenResponse>
}

private interface YandexTranslationApi {

    @POST("translate")
    fun getTranslation(
        @Body request: TranslationRequest,
        @Header("Authorization") authHeader: String
    ): Single<TranslationResponse>

    @POST("languages")
    fun getSupportedLanguages(
        @Body request: SupportedLanguagesRequest,
        @Header("Authorization") authHeader: String
    ): Single<SupportedLanguagesResponse>
}