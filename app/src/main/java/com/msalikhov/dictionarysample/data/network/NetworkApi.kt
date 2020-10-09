package com.msalikhov.dictionarysample.data.network

import com.msalikhov.dictionarysample.data.network.token.models.IAMTokenRequest
import com.msalikhov.dictionarysample.data.network.token.models.IAMTokenResponse
import com.msalikhov.dictionarysample.data.network.translation.models.SupportedLanguagesRequest
import com.msalikhov.dictionarysample.data.network.translation.models.SupportedLanguagesResponse
import com.msalikhov.dictionarysample.data.network.translation.models.TranslationRequest
import com.msalikhov.dictionarysample.data.network.translation.models.TranslationResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

private const val AUTH_HEADER = "Authorization"

private const val IAM_TOKEN_URL = "https://iam.api.cloud.yandex.net/iam/v1/tokens"

private const val TRANSLATE_BASE_URL = "https://translate.api.cloud.yandex.net/translate/v2/"
private const val TRANSLATE_URL = "${TRANSLATE_BASE_URL}translate"
private const val LANGUAGES_URL = "${TRANSLATE_BASE_URL}languages"

interface NetworkApi {
    @POST(IAM_TOKEN_URL)
    fun getIAMToken(@Body request: IAMTokenRequest): Single<IAMTokenResponse>

    @POST(TRANSLATE_URL)
    fun getTranslation(
        @Body request: TranslationRequest,
        @Header(AUTH_HEADER) authHeader: String
    ): Single<TranslationResponse>

    @POST(LANGUAGES_URL)
    fun getSupportedLanguages(
        @Body request: SupportedLanguagesRequest,
        @Header(AUTH_HEADER) authHeader: String
    ): Single<SupportedLanguagesResponse>
}