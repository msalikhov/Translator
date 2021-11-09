package com.msalikhov.dictionarysample.data.network.translation.skyeng.models

import com.google.gson.annotations.SerializedName

data class SkyEngMeaning(
    @SerializedName("id") val id: Int,
    @SerializedName("partOfSpeechCode") val partOfSpeechCode: String,
    @SerializedName("previewUrl") val previewUrl: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("transcription") val transcription: String,
    @SerializedName("soundUrl") val soundUrl: String,
    @SerializedName("translation") val translation: SkyEngTranslation
)