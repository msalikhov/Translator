package com.msalikhov.dictionarysample.presentation

import ru.terrakok.cicerone.Cicerone

object CiceroneHolder {
    private val cicerone by lazy { Cicerone.create() }

    val navigationHolder get() = cicerone.navigatorHolder
    val router get() = cicerone.router
}