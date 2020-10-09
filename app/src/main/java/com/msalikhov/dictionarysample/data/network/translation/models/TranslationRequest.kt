package com.msalikhov.dictionarysample.data.network.translation.models

import com.google.gson.annotations.SerializedName

@Suppress("ArrayInDataClass")
data class TranslationRequest(
    @SerializedName("folder_id")
    val folderId: String,
    @SerializedName("texts")
    val texts: Array<out String>,
    @SerializedName("targetLanguageCode")
    val targetLanguageCode: String
)