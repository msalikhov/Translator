package com.msalikhov.dictionarysample.data.network.token.models

import com.google.gson.annotations.SerializedName

data class IAMTokenRequest(
    @SerializedName("yandexPassportOauthToken")
    val yandexPassportOauthToken: String
)