package com.msalikhov.dictionarysample.utils.lifecycle

import java.util.concurrent.atomic.AtomicBoolean

interface Consumable<out T: Any> {
    fun consume(): T?
}

class ConsumableImpl<out T : Any>(private val value: T): Consumable<T> {

    private val consumed = AtomicBoolean(false)

    override fun consume(): T? =
        if (consumed.compareAndSet(false, true)) value else null
}