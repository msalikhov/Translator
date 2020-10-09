package com.msalikhov.dictionarysample.di.translation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.msalikhov.dictionarysample.data.repository.translation.TranslationRepository
import com.msalikhov.dictionarysample.data.repository.translation.TranslationRepositoryImpl
import com.msalikhov.dictionarysample.di.app.DaggerFragmentFactory
import com.msalikhov.dictionarysample.di.app.FragmentMapKey
import com.msalikhov.dictionarysample.presentation.view.translation.TranslationFragment
import com.msalikhov.dictionarysample.presentation.view.translation.TranslationsHistoryFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TranslationModule {

    @Binds
    @TranslationScope
    fun TranslationRepositoryImpl.bindTranslationRepository(): TranslationRepository

    @Binds
    fun DaggerFragmentFactory.bindFragmentFactory(): FragmentFactory

    @Binds
    @IntoMap
    @FragmentMapKey(TranslationFragment::class)
    fun TranslationFragment.bindTranslationFragment(): Fragment

    @Binds
    @IntoMap
    @FragmentMapKey(TranslationsHistoryFragment::class)
    fun TranslationsHistoryFragment.bindTranslationHistoryFragment(): Fragment
}