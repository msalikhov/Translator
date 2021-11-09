package com.msalikhov.dictionarysample.data.network.translation.yandex.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class YandexIAMTokenResponse(
    @SerializedName("iamToken") val iamToken: String,
    @SerializedName("expiresAt") val expiresAt: Date
)