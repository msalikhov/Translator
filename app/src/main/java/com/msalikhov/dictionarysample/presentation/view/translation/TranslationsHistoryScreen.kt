package com.msalikhov.dictionarysample.presentation.view.translation

import ru.terrakok.cicerone.android.support.FragmentParams
import ru.terrakok.cicerone.android.support.SupportAppScreen

class TranslationsHistoryScreen : SupportAppScreen() {
    override fun getFragmentParams() = FragmentParams(TranslationsHistoryFragment::class.java)
}