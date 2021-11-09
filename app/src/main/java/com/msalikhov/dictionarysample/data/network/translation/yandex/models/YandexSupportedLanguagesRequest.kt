package com.msalikhov.dictionarysample.data.network.translation.yandex.models

import com.google.gson.annotations.SerializedName

data class YandexSupportedLanguagesRequest(
    @SerializedName("folder_id") val folderId: String
)