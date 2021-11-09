package com.msalikhov.dictionarysample.data.network.translation.yandex.models

import com.google.gson.annotations.SerializedName

data class YandexSupportedLanguagesResponse(
    @SerializedName("languages") val languages: List<Language>
) {
    data class Language(
        @SerializedName("code") val code: String,
        @SerializedName("name") val name: String?
    )
}