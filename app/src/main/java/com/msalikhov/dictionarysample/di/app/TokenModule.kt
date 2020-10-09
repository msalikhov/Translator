package com.msalikhov.dictionarysample.di.app

import com.msalikhov.dictionarysample.data.repository.token.TokenRepository
import com.msalikhov.dictionarysample.data.repository.token.TokenRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface TokenModule {

    @Binds
    @Singleton
    fun TokenRepositoryImpl.bindTokenRepository(): TokenRepository
}