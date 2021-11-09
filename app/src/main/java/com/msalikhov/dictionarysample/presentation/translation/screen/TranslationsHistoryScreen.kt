package com.msalikhov.dictionarysample.presentation.translation.screen

import com.msalikhov.dictionarysample.presentation.translation.view.TranslationsHistoryFragment
import ru.terrakok.cicerone.android.support.FragmentParams
import ru.terrakok.cicerone.android.support.SupportAppScreen

class TranslationsHistoryScreen : SupportAppScreen() {
    override fun getFragmentParams() = FragmentParams(TranslationsHistoryFragment::class.java)
}