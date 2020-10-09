package com.msalikhov.dictionarysample.utils

import io.reactivex.Completable
import io.reactivex.Single

object CacheHelper {
    inline fun <DB, N> dbOrNetwork(
        dbSingleProvider: Single<DB>,
        networkSingleProvider: Single<N>,
        crossinline getFromNetworkCondition: (DB) -> Boolean,
        crossinline dbWriteProvider: (N) -> Completable
    ): Single<DB> = dbSingleProvider.flatMap { data ->
        if (getFromNetworkCondition(data)) {
            networkSingleProvider
                .flatMapCompletable { dbWriteProvider(it) }
                .andThen(dbSingleProvider)
        } else {
            Single.just(data)
        }
    }
}