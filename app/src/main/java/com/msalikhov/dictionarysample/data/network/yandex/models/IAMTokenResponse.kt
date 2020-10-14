package com.msalikhov.dictionarysample.data.network.yandex.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class IAMTokenResponse(
    @SerializedName("iamToken")
    val iamToken: String,
    @SerializedName("expiresAt")
    val expiresAt: Date
)