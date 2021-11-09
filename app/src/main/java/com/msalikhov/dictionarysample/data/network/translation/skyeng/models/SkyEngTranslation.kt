package com.msalikhov.dictionarysample.data.network.translation.skyeng.models

import com.google.gson.annotations.SerializedName

data class SkyEngTranslation(
    @SerializedName("text") val text: String
)
