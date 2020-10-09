package com.msalikhov.dictionarysample.data.network.translation.models

import com.google.gson.annotations.SerializedName

data class TranslationResponse(
    @SerializedName("translations")
    val translations: List<Translation>
) {
    data class Translation(
        @SerializedName("text")
        val text: String,
        @SerializedName("detectedLanguageCode")
        val detectedLanguageCode: String
    )
}