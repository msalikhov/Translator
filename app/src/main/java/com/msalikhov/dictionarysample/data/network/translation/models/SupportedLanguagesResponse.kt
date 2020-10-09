package com.msalikhov.dictionarysample.data.network.translation.models

import com.google.gson.annotations.SerializedName

data class SupportedLanguagesResponse(
    @SerializedName("languages")
    val languages: List<Language>
) {
    data class Language(
        @SerializedName("code")
        val code: String,
        @SerializedName("name")
        val name: String?
    )
}