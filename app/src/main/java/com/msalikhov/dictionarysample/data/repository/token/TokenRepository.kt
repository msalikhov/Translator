package com.msalikhov.dictionarysample.data.repository.token

import io.reactivex.Single

interface TokenRepository {
    val iamToken: Single<String>
}