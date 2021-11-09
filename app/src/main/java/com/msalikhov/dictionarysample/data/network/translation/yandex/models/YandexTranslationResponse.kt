package com.msalikhov.dictionarysample.data.network.translation.yandex.models

import com.google.gson.annotations.SerializedName

data class YandexTranslationResponse(
    @SerializedName("translations") val translations: List<Translation>
) {
    data class Translation(
        @SerializedName("text") val text: String,
        @SerializedName("detectedLanguageCode") val detectedLanguageCode: String
    )
}