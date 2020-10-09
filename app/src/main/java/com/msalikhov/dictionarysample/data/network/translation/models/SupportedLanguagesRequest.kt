package com.msalikhov.dictionarysample.data.network.translation.models

import com.google.gson.annotations.SerializedName

data class SupportedLanguagesRequest(
    @SerializedName("folder_id")
    val folderId: String
)