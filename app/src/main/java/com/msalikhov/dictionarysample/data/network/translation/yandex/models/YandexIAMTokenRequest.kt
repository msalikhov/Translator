package com.msalikhov.dictionarysample.data.network.translation.yandex.models

import com.google.gson.annotations.SerializedName

data class YandexIAMTokenRequest(
    @SerializedName("yandexPassportOauthToken") val yandexPassportOauthToken: String
)