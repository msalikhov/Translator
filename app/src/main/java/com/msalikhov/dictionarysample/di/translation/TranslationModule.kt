package com.msalikhov.dictionarysample.di.translation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.msalikhov.dictionarysample.data.repository.translation.TranslationRepository
import com.msalikhov.dictionarysample.data.repository.translation.TranslationRepositoryImpl
import com.msalikhov.dictionarysample.di.fragment.DaggerFragmentFactory
import com.msalikhov.dictionarysample.di.fragment.FragmentMapKey
import com.msalikhov.dictionarysample.domain.translation.TranslationHistoryInterator
import com.msalikhov.dictionarysample.domain.translation.TranslationHistoryInteratorImpl
import com.msalikhov.dictionarysample.domain.translation.TranslationInteractor
import com.msalikhov.dictionarysample.domain.translation.TranslationInteractorImpl
import com.msalikhov.dictionarysample.presentation.view.translation.TranslationFragment
import com.msalikhov.dictionarysample.presentation.view.translation.TranslationsHistoryFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [SkyEngTranslationServiceModule::class])
interface TranslationModule {

    @Binds
    @TranslationScope
    fun TranslationRepositoryImpl.bindTranslationRepository(): TranslationRepository

    @Binds
    @TranslationScope
    fun DaggerFragmentFactory.bindFragmentFactory(): FragmentFactory

    @Binds
    @TranslationScope
    fun TranslationHistoryInteratorImpl.bindTranslationHistoryInteractor(): TranslationHistoryInterator

    @Binds
    @TranslationScope
    fun TranslationInteractorImpl.bindTranslationInteractor(): TranslationInteractor

    @Binds
    @IntoMap
    @FragmentMapKey(TranslationFragment::class)
    fun TranslationFragment.bindTranslationFragment(): Fragment

    @Binds
    @IntoMap
    @FragmentMapKey(TranslationsHistoryFragment::class)
    fun TranslationsHistoryFragment.bindTranslationHistoryFragment(): Fragment
}