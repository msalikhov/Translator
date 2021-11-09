package com.msalikhov.dictionarysample.utils.extensions

import io.reactivex.Observable
import io.reactivex.Single

fun <T: Any> Single<T>.mapToResult() = map { Result.success(it) }.onErrorReturn { Result.failure(it) }
fun <T: Any> Observable<T>.mapToResult() = map { Result.success(it) }.onErrorReturn { Result.failure(it) }