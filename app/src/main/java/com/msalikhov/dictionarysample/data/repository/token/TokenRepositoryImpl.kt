package com.msalikhov.dictionarysample.data.repository.token

import android.content.Context
import com.msalikhov.dictionarysample.BuildConfig
import com.msalikhov.dictionarysample.data.network.NetworkApi
import com.msalikhov.dictionarysample.data.network.token.models.IAMTokenRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import javax.inject.Inject

private const val STORE_NAME = "tokenStore"
private const val IAM_TOKEN_VALUE_KEY = "iam_token_key"
private const val IAM_TOKEN_EXPIRATION_KEY = "iam_token_expiration_key"

class TokenRepositoryImpl @Inject constructor(
    private val networkApi: NetworkApi,
    context: Context
) : TokenRepository {
    private val preferences = context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()

    private var localIAMTokenValue: String
        get() = preferences.getString(IAM_TOKEN_VALUE_KEY, null).orEmpty()
        set(value) = preferences.edit().putString(IAM_TOKEN_VALUE_KEY, value).apply()

    private var localIAMTokenExpiration: Long
        get() = preferences.getLong(IAM_TOKEN_EXPIRATION_KEY, 0L)
        set(value) = preferences.edit().putLong(IAM_TOKEN_EXPIRATION_KEY, value).apply()

    private val localIAMTokenIfNotExpired
        get() = localIAMTokenValue.takeIf { it.isNotBlank() && localIAMTokenExpiration > System.currentTimeMillis() }

    override val iamToken: Single<String>
        get() = Single
            .fromCallable { localIAMTokenIfNotExpired }
            .onErrorResumeNext(networkApi
                .getIAMToken(IAMTokenRequest(BuildConfig.YANDEX_OAUTH_TOKEN))
                .map { (token, expiration) ->
                    localIAMTokenValue = token
                    localIAMTokenExpiration = expiration.time
                    token
                }
            )
            .map { "Bearer $it" }
            .subscribeOn(Schedulers.from(singleThreadExecutor))
}