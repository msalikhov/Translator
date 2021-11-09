package com.msalikhov.dictionarysample.data.network.translation.yandex.models

import com.google.gson.annotations.SerializedName

@Suppress("ArrayInDataClass")
data class YandexTranslationRequest(
    @SerializedName("folder_id") val folderId: String,
    @SerializedName("texts") val texts: Array<out String>,
    @SerializedName("targetLanguageCode") val targetLanguageCode: String
)