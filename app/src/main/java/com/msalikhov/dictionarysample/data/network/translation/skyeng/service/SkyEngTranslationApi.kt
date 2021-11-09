package com.msalikhov.dictionarysample.data.network.translation.skyeng.service

import com.msalikhov.dictionarysample.data.network.translation.skyeng.models.SkyEngWord
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SkyEngTranslationApi {
    @GET("words/search")
    fun translateWord(@Query("search") word: String): Single<List<SkyEngWord>>
}