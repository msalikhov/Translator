package com.msalikhov.dictionarysample.data.network.translation.yandex.service

import android.content.Context
import com.google.gson.Gson
import com.msalikhov.dictionarysample.BuildConfig
import com.msalikhov.dictionarysample.data.network.translation.yandex.models.YandexIAMTokenRequest
import com.msalikhov.dictionarysample.data.network.translation.yandex.models.YandexIAMTokenResponse
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import javax.inject.Inject

class YandexServiceTokenInterceptor @Inject constructor(
    context: Context,
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) : Interceptor {

    private val preferences = context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    private var localIAMTokenValue: String
        get() = preferences.getString(IAM_TOKEN_VALUE_KEY, null).orEmpty()
        set(value) = preferences.edit().putString(IAM_TOKEN_VALUE_KEY, value).apply()

    private var localIAMTokenExpiration: Long
        get() = preferences.getLong(IAM_TOKEN_EXPIRATION_KEY, 0L)
        set(value) = preferences.edit().putLong(IAM_TOKEN_EXPIRATION_KEY, value).apply()

    private val localIAMTokenIfNotExpired: String?
        get() = localIAMTokenValue.takeIf { it.isNotBlank() && localIAMTokenExpiration > System.currentTimeMillis() }

    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = localIAMTokenIfNotExpired ?: Request
            .Builder()
            .url(IAM_TOKEN_URL)
            .post(
                gson
                    .toJson(YandexIAMTokenRequest(BuildConfig.YANDEX_OAUTH_TOKEN))
                    .toRequestBody("application/json".toMediaType())
            )
            .build()
            .let { okHttpClient.newCall(it).execute() }
            .let { gson.fromJson(it.body!!.charStream(), YandexIAMTokenResponse::class.java) }
            .let { (token, expiration) ->
                localIAMTokenValue = token
                localIAMTokenExpiration = expiration.time
                token
            }

        val newRequest = chain
            .request()
            .newBuilder()
            .addHeader(AUTHORIZATION, "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }

    private companion object {
        const val STORE_NAME = "tokenStore"
        const val IAM_TOKEN_VALUE_KEY = "iam_token_key"
        const val IAM_TOKEN_EXPIRATION_KEY = "iam_token_expiration_key"
        const val IAM_TOKEN_URL = "https://iam.api.cloud.yandex.net/iam/v1/tokens"
        const val AUTHORIZATION = "Authorization"
    }
}