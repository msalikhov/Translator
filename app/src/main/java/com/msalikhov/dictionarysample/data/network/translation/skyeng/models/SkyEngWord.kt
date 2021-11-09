package com.msalikhov.dictionarysample.data.network.translation.skyeng.models

import com.google.gson.annotations.SerializedName

data class SkyEngWord(
    @SerializedName("id") val id: Int,
    @SerializedName("text") val text: String,
    @SerializedName("meanings") val meanings: List<SkyEngMeaning>,
)